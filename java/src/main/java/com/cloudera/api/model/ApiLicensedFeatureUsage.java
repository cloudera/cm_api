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

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Information about the number of nodes using which product features.
 * <p>
 * Usage information is provided for individual clusters, as well as totals
 * across all clusters.
 */
@XmlRootElement(name = "licensedFeatureUsage")
public class ApiLicensedFeatureUsage {

  private Map<String, Integer> totals;
  private Map<String, Map<String, Integer>> byCluster;

  public ApiLicensedFeatureUsage() {
  }

  public ApiLicensedFeatureUsage(Map<String, Integer> totals,
                         Map<String, Map<String, Integer>> byCluster) {
    this.totals = totals;
    this.byCluster = byCluster;
  }

  /**
   * Map from named features to the total number of nodes using those features.
   */
  @XmlElementWrapper(name = "totals")
  public Map<String, Integer> getTotals() {
    return totals;
  }

  public void setTotals(Map<String, Integer> totals) {
    this.totals = totals;
  }

  /**
   * Map from clusters to maps of named features to the number of nodes in the
   * cluster using those features.
   */
  @XmlElementWrapper(name = "clusters")
  public Map<String, Map<String, Integer>> getClusters() {
    return byCluster;
  }

  public void setCluster(Map<String, Map<String, Integer>> byCluster) {
    this.byCluster = byCluster;
  }
}
