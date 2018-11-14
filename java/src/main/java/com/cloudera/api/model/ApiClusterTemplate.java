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

package com.cloudera.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Details of cluster template
 */

@XmlRootElement(name = "clusterTemplate")
@JsonInclude(Include.NON_EMPTY)
public class ApiClusterTemplate {

  /**
   * CDH version
   */
  private String cdhVersion;
  /**
   * Cluster display name
   */
  private String displayName;
  /**
   * CM version for which the template
   */
  private String cmVersion;
  /**
   * List of all repositories registered with CM
   */
  @JsonInclude(Include.NON_EMPTY)
  private List<String> repositories = Lists.newArrayList();
  /**
   * All the parcels that needs to be deployed and activated
   */
  private List<ApiProductVersion> products = Lists.newArrayList();
  /**
   * All the services that needs to be deployed
   */
  @JsonInclude(Include.NON_EMPTY)
  private List<ApiClusterTemplateService> services = Lists.newArrayList();
  /**
   * All host templates
   */
  @JsonInclude(Include.NON_EMPTY)
  private List<ApiClusterTemplateHostTemplate> hostTemplates = Lists.newArrayList();

  /**
   * A constructor listing all the variables and references that needs to be
   * resolved for this template
   */
  private ApiClusterTemplateInstantiator instantiator;

  public String getCdhVersion() {
    return this.cdhVersion;
  }

  public void setCdhVersion(String cdhVersion) {
    this.cdhVersion = cdhVersion;
  }

  public List<ApiProductVersion> getProducts() {
    return this.products;
  }

  public void setProducts(List<ApiProductVersion> products) {
    this.products = products;
  }

  public List<ApiClusterTemplateService> getServices() {
    return this.services;
  }

  public void setServices(List<ApiClusterTemplateService> services) {
    this.services = services;
  }

  public List<ApiClusterTemplateHostTemplate> getHostTemplates() {
    return this.hostTemplates;
  }

  public void setHostTemplates(List<ApiClusterTemplateHostTemplate> hostTemplates) {
    this.hostTemplates = hostTemplates;
  }

  public String getDisplayName() {
    return this.displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getCmVersion() {
    return this.cmVersion;
  }

  public void setCmVersion(String cmVersion) {
    this.cmVersion = cmVersion;
  }

  public ApiClusterTemplateInstantiator getInstantiator() {
    return this.instantiator;
  }

  public void setInstantiator(ApiClusterTemplateInstantiator instantiator) {
    this.instantiator = instantiator;
  }

  public List<String> getRepositories() {
    return this.repositories;
  }

  public void setRepositories(List<String> repositories) {
    this.repositories = repositories;
  }

}
