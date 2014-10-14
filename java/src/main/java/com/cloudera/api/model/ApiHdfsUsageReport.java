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

import com.cloudera.api.ApiUtils;
import com.cloudera.api.CsvElementWrapper;
import com.google.common.base.Objects;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "hdfsUsageReport")
public class ApiHdfsUsageReport extends ApiListBase<ApiHdfsUsageReportRow> {
  private Date lastUpdateTime;

  public ApiHdfsUsageReport() {
  }

  public ApiHdfsUsageReport(Date lastUpdateTime,
                            List<ApiHdfsUsageReportRow> rows) {
    super(rows);
    this.lastUpdateTime = lastUpdateTime;
  }

  /**
   * The time when HDFS usage info was last collected. No information
   * beyond this time can be provided.
   */
  @XmlElement
  public Date getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(Date lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  /**
   * A list of per-user usage information at the requested
   * time granularity.
   */
  @XmlElementWrapper(name = ApiListBase.ITEMS_ATTR)
  @CsvElementWrapper(rowtype = ApiHdfsUsageReportRow.class)
  public List<ApiHdfsUsageReportRow> getReportRows() {
    return values;
  }

  public void setReportRows(List<ApiHdfsUsageReportRow> rows) {
    this.values = rows;
  }

  @Override
  public boolean equals(Object o) {
    ApiHdfsUsageReport that = ApiUtils.baseEquals(this, 0);
    return this == that ||
        (that != null &&
            super.equals(o) &&
            Objects.equal(lastUpdateTime, that.lastUpdateTime));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(lastUpdateTime, super.hashCode());
  }
}
