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

import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ClouderaManagerClientBuilderTest {

  /**
   * Replacement for WebClient.getConfig() on delegated proxy.
   *
   * Because we use dynamic proxy mechanism to delegate call from {@link ApiRootResource}
   * to {@link ApiRootResourceExternal}, instanceof operator doesn't work since they don't
   * share any common interface. Hence need for this method.
   */
  private ClientConfiguration getClientConfigFromProxy(ApiRootResource proxy) {
    InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);
    assertTrue(invocationHandler instanceof ApiRootResourceInvocationHandler);
    return WebClient.getConfig(
      ((ApiRootResourceInvocationHandler)invocationHandler).getDelegateRootResource());
  }

  @Test
  public void testURLGeneration() throws MalformedURLException {
    assertEquals("Wrong address generated from base url",
                 "http://localhost:7180/api/",
                 new ClouderaManagerClientBuilder()
                     .withBaseURL(new URL("http://localhost:7180"))
                     .generateAddress());

    new ClouderaManagerClientBuilder()
        .withHost("1.1.1.1")
        .build();

    new ClouderaManagerClientBuilder()
        .withHost("myhostname")
        .withPort(12345)
        .build();

    new ClouderaManagerClientBuilder()
        .withHost("myhostname")
        .withPort(12345)
        .enableTLS()
        .build();

    try {
      new ClouderaManagerClientBuilder().build();
      fail("Client was built without a hostname");
    } catch (IllegalArgumentException e) {
      // expected
    }

    try {
      new ClouderaManagerClientBuilder()
          .withHost("/////&&&&:::::````````").
          build();
      fail("Client was built with an invalid hostname");
    } catch (IllegalArgumentException e) {
      // expected
    }

    try {
      new ClouderaManagerClientBuilder()
          .withHost("myhostname")
          .withPort(-1)
          .build();
      fail("Client was built with an invalid port");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void testResourceConfig() {
    ClouderaManagerClientBuilder builder = new ClouderaManagerClientBuilder();
    ApiRootResource a = newProxy(builder);
    assertNotNull(a);
    ApiRootResource b = newProxy(builder);
    assertNotNull(b);

    ClouderaManagerClientBuilder.clearCachedResources();
    // test after clear
    assertNotNull(newProxy(builder));
  }

  public ApiRootResource newProxy(ClouderaManagerClientBuilder builder) {
    return builder.withHost("localhost")
      .withPort(1)
      .enableLogging()
      .build();
  }

  @Test
  public void testMaintainSessionConfig() {
    ClouderaManagerClientBuilder builder = new ClouderaManagerClientBuilder();
    ApiRootResource proxy = builder.withHost("localhost")
        .withPort(1)
        .enableLogging()
        .build();
    ClientConfiguration cfg = getClientConfigFromProxy(proxy);
    assertNotNull(cfg);
    Boolean maintainSession = (Boolean)cfg.getRequestContext().get(Message.MAINTAIN_SESSION);
    assertNull(maintainSession);

    proxy = builder.setMaintainSessionAcrossRequests(true).build();
    cfg = getClientConfigFromProxy(proxy);
    assertNotNull(cfg);
    maintainSession = (Boolean)cfg.getRequestContext().get(Message.MAINTAIN_SESSION);
    assertTrue(maintainSession);
  }

  @Test
  public void testPassingLocale() {
    ClouderaManagerClientBuilder builder = new ClouderaManagerClientBuilder();
    ApiRootResource proxy = builder.withHost("localhost")
        .withPort(1)
        .enableLogging()
        .build();
    ClientConfiguration cfg = getClientConfigFromProxy(proxy);
    HTTPConduit conduit = (HTTPConduit) cfg.getConduit();
    HTTPClientPolicy clientPolicy = conduit.getClient();

    assertNotNull(clientPolicy);
    String acceptLanguage = clientPolicy.getAcceptLanguage();
    assertNull(acceptLanguage);

    proxy = builder.withAcceptLanguage("some-string").build();
    cfg = getClientConfigFromProxy(proxy);
    conduit = (HTTPConduit) cfg.getConduit();
    clientPolicy = conduit.getClient();
    assertEquals("some-string", clientPolicy.getAcceptLanguage());
  }

  @Test
  public void testStreamAutoClosureConfig() {
    ClouderaManagerClientBuilder builder = new ClouderaManagerClientBuilder();
    ApiRootResource proxy = builder.withHost("localhost")
        .withPort(1)
        .enableLogging()
        .build();
    ClientConfiguration cfg = getClientConfigFromProxy(proxy);
    assertNotNull(cfg);
    Boolean autoClosure = (Boolean)cfg.getRequestContext().get("response.stream.auto.close");
    assertNull(autoClosure);

    proxy = builder.enableStreamAutoClosure().build();
    cfg = getClientConfigFromProxy(proxy);
    assertNotNull(cfg);
    autoClosure = (Boolean)cfg.getRequestContext().get("response.stream.auto.close");
    assertTrue(autoClosure);
  }
}
