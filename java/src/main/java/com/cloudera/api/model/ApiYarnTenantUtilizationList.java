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

import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/** A list of yarn tenant utilization reports. */
@XmlRootElement(name = "yarnTenantUtilizationList")
public class ApiYarnTenantUtilizationList extends ApiListBase<ApiYarnTenantUtilization> {

  public ApiYarnTenantUtilizationList() {
    // For JAX-B.
  }

  public ApiYarnTenantUtilizationList(List<ApiYarnTenantUtilization> yarnTenantUtilizations) {
    super(yarnTenantUtilizations);
  }

  @XmlElementWrapper(name = ApiListBase.ITEMS_ATTR)
  public List<ApiYarnTenantUtilization> getYarnTenantUtilizations() {
    return values;
  }

  public void setYarnTenantUtilizations(List<ApiYarnTenantUtilization> yarnTenantUtilizations) {
    values = yarnTenantUtilizations;
  }
}
