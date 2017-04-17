// Licensed to Cloudera, Inc. under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  Cloudera, Inc. licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.cloudera.api;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

public class ClouderaManagerClientBuilder {

  public static final int DEFAULT_TCP_PORT = 7180;
  public static final long DEFAULT_CONNECTION_TIMEOUT = 0;
  public static final TimeUnit DEFAULT_CONNECTION_TIMEOUT_UNITS =
      TimeUnit.MILLISECONDS;
  public static final long DEFAULT_RECEIVE_TIMEOUT = 0;
  public static final TimeUnit DEFAULT_RECEIVE_TIMEOUT_UNITS =
      TimeUnit.MILLISECONDS;

  private URL baseUrl;
  private String hostname;
  private int port = DEFAULT_TCP_PORT;
  private boolean enableTLS;
  private boolean enableLogging;
  private String username;
  private String password;
  private long connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
  private TimeUnit connectionTimeoutUnits = DEFAULT_CONNECTION_TIMEOUT_UNITS;
  private long receiveTimeout = DEFAULT_RECEIVE_TIMEOUT;
  private TimeUnit receiveTimeoutUnits = DEFAULT_RECEIVE_TIMEOUT_UNITS;
  private boolean validateCerts = true;
  private boolean validateCn = true;
  private boolean threadSafe;
  private boolean maintainSessionAcrossRequests;
  private boolean streamAutoClosure;
  private TrustManager[] trustManagers;
  private String acceptLanguage;

  /**
   * Cache JAXRSClientFactoryBean per proxyType.
   *
   * We need a cache because CXF stores stubs
   * ({@link org.apache.cxf.jaxrs.model.ClassResourceInfo} objects) as a reference
   * inside JAXRSServiceFactoryBean, which is composed within instances of
   * {@link JAXRSClientFactoryBean}.
   *
   * This avoids:
   * - creating a lot of temporaries generated during the proxy creation for
   * each client
   *
   * - ensures that different proxies of the same type actually share the same
   * ClassResourceInfo, thus reducing aggregate usage
   *
   * Also, as a useful side effect, generates proxies with cached proxy types faster.
   */
  private static final LoadingCache<Class<?>, JAXRSClientFactoryBean>
    clientStaticResources =
      CacheBuilder.newBuilder()
      .softValues()
      .build(
        new CacheLoader<Class<?>, JAXRSClientFactoryBean>(){
          @Override
          public JAXRSClientFactoryBean load(Class<?> proxyType) throws Exception {
            JAXRSClientFactoryBean clientFactoryBean = new JAXRSClientFactoryBean();
            clientFactoryBean.setResourceClass(proxyType);
            clientFactoryBean.setProvider(new JacksonJsonProvider(new ApiObjectMapper()));
            return clientFactoryBean;
          }
        });

  public ClouderaManagerClientBuilder withAcceptLanguage(
      String acceptLaunguage) {
    this.acceptLanguage = acceptLaunguage;
    return this;
  }

  public ClouderaManagerClientBuilder withBaseURL(URL baseUrl) {
    this.baseUrl = baseUrl;
    return this;
  }

  public ClouderaManagerClientBuilder withHost(String hostname) {
    this.hostname = hostname;
    return this;
  }

  public ClouderaManagerClientBuilder withPort(int port) {
    this.port = port;
    return this;
  }

  public ClouderaManagerClientBuilder enableTLS() {
    this.enableTLS = true;
    return this;
  }

  public ClouderaManagerClientBuilder enableLogging() {
    this.enableLogging = true;
    return this;
  }

  /**
   * @param threadSafe Set if to create a thread safe client.
   * @return
   */
  public ClouderaManagerClientBuilder setThreadSafe(boolean threadSafe) {
    this.threadSafe = threadSafe;
    return this;
  }

  /**
   * @param maintainSessionAcrossRequests If set to true, created client will
   * maintain HTTP session across multiple requests. Setting this to true also
   * means that login attempt will be made for the 1st request for a new client
   * or when the previous session has time out.
   * @return
   */
  public ClouderaManagerClientBuilder setMaintainSessionAcrossRequests(
      boolean maintainSessionAcrossRequests) {
    this.maintainSessionAcrossRequests = maintainSessionAcrossRequests;
    return this;
  }

  public ClouderaManagerClientBuilder withUsernamePassword(String username,
                                                           String password) {
    this.username = username;
    this.password = password;
    return this;
  }

  public ClouderaManagerClientBuilder withConnectionTimeout(
      long connectionTimeout,
      TimeUnit connectionTimeoutUnits) {
    this.connectionTimeout = connectionTimeout;
    this.connectionTimeoutUnits = connectionTimeoutUnits;
    return this;
  }

  public ClouderaManagerClientBuilder withReceiveTimeout(
      long receiveTimeout,
      TimeUnit receiveTimeoutUnits) {
    this.receiveTimeout = receiveTimeout;
    this.receiveTimeoutUnits = receiveTimeoutUnits;
    return this;
  }

  public ClouderaManagerClientBuilder disableTlsCertValidation() {
    this.validateCerts = false;
    return this;
  }

  public ClouderaManagerClientBuilder disableTlsCnValidation() {
    this.validateCn = false;
    return this;
  }

  /**
   * By default, ClouderaManagerClientBuilder disables auto-closure of response
   * streams when generated client are making requests. This method enables
   * this. If this is not enabled, caller of the client is responsible for
   * closing the response streams.
   * @return ClouderaManagerClientBuilder
   */
  public ClouderaManagerClientBuilder enableStreamAutoClosure() {
    this.streamAutoClosure = true;
    return this;
  }

  @VisibleForTesting
  String generateAddress() {
    final String apiRootPath = "api/";

    if (baseUrl != null) {
      // Short-circuit and use the base URL to generate the full URL
      return String.format("%s/%s", baseUrl.toExternalForm(),
                           apiRootPath);
    }

    if (hostname == null) {
      throw new IllegalArgumentException("hostname or full url must be set");
    }

    if (port <= 0) {
      throw new IllegalArgumentException(
          String.format("'%d' is not a valid port number", port));
    }
    String urlString = String.format("%s://%s:%d/%s",
                                     enableTLS ? "https" : "http",
                                     hostname, port, apiRootPath);
    try {
      // Check the syntax of the generated URL string
      new URI(urlString);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(
          String.format("'%s' is not a valid hostname", hostname), e);
    }
    return urlString;
  }

  public void setTrustManagers(TrustManager[] managers) {
    trustManagers = managers;
  }

  /**
   * Build an ApiRootResource proxy object for communicating with the remote server.
   * @return an ApiRootResource proxy object
   */
  public ApiRootResource build() {
    // Generating stubs on ApiRootResource consumes more memory.
    // Hence we generate stubs using ApiRootResourceExternal and then use dynamic proxy mechanism
    // to delegate call on ApiRootResource to ApiRootResourceExternal generated stub.
    ApiRootResourceExternal apiRootResourceExternal = build(ApiRootResourceExternal.class);

    return (ApiRootResource) Proxy.newProxyInstance(
      ClouderaManagerClientBuilder.class.getClassLoader(),
      new Class[]{ApiRootResource.class},
      new ApiRootResourceInvocationHandler(apiRootResourceExternal));
  }

  /**
   * Build a client proxy, for a specific proxy type.
   * @param proxyType proxy type class
   * @return client proxy stub
   */
  protected <T> T build(Class<T> proxyType) {
    String address = generateAddress();
    T rootResource;
    // Synchronized on the class to correlate with the scope of clientStaticResources
    // We want to ensure that the shared bean isn't set concurrently in multiple callers
    synchronized (ClouderaManagerClientBuilder.class) {
      JAXRSClientFactoryBean bean =
          cleanFactory(clientStaticResources.getUnchecked(proxyType));
      bean.setAddress(address);
      if (username != null) {
        bean.setUsername(username);
        bean.setPassword(password);
      }

      if (enableLogging) {
        bean.setFeatures(Arrays.<AbstractFeature>asList(new LoggingFeature()));
      }
      bean.setThreadSafe(threadSafe);
      rootResource = bean.create(proxyType);
    }

    boolean isTlsEnabled = address.startsWith("https://");
    ClientConfiguration config = WebClient.getConfig(rootResource);
    if (maintainSessionAcrossRequests) {
      config.getRequestContext().put(Message.MAINTAIN_SESSION,
                                     Boolean.TRUE);
    }
    if (streamAutoClosure) {
      config.getRequestContext().put("response.stream.auto.close",
                                     Boolean.TRUE);
    }

    HTTPConduit conduit = (HTTPConduit) config.getConduit();
    if (isTlsEnabled) {
      TLSClientParameters tlsParams = new TLSClientParameters();
      if (!validateCerts) {
        tlsParams.setTrustManagers(new TrustManager[] { new AcceptAllTrustManager() });
      }
      else if (trustManagers != null) {
        tlsParams.setTrustManagers(trustManagers);
      }
      tlsParams.setDisableCNCheck(!validateCn);
      conduit.setTlsClientParameters(tlsParams);
    }

    HTTPClientPolicy policy = conduit.getClient();
    if (acceptLanguage != null) {
      policy.setAcceptLanguage(acceptLanguage);
    }
    policy.setConnectionTimeout(
        connectionTimeoutUnits.toMillis(connectionTimeout));
    policy.setReceiveTimeout(
        receiveTimeoutUnits.toMillis(receiveTimeout));
    return rootResource;
  }

  private static JAXRSClientFactoryBean cleanFactory(JAXRSClientFactoryBean bean) {
    bean.setUsername(null);
    bean.setPassword(null);
    bean.setInitialState(null);
    bean.setFeatures(Arrays.<AbstractFeature>asList());
    return bean;
  }

  /**
   * Releases the internal state and configuration associated with this client.
   * Reopening a new connection requires creating a new client object using the
   * build() method in this builder.
   *
   * @param root The resource returned by the build() method of this
   *             builder class
   */
  public static void closeClient(Object root) {
    Client client = WebClient.client(root);
    if (client != null) {
      client.close();
    }
  }

  /**
   * Clears any cached resources shared during build operations
   * across instances of this class.
   *
   * This includes shared proxy/stub information, that will be automatically
   * garbage collected when used heap memory in the JVM
   * nears max heap memory in the JVM.
   *
   * In general, it is unnecessary to invoke this method, unless you are
   * concerned with reducing used heap memory in the JVM.
   */
  public static void clearCachedResources() {
    clientStaticResources.invalidateAll();
  }

  /** A trust manager that will accept all certificates. */
  private static class AcceptAllTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
      // no op.
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
      // no op.
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }
  }
}
