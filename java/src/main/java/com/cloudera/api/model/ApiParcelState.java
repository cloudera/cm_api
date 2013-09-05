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
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * The ApiParcelState encapsulates the state of a parcel while it is in
 * transition and reports any errors that may have occurred..
 * <p>
 * The complete progress of a parcel is broken up into two different reporting
 * indicators - progress and count. Progress is the primary indicator that reports
 * the global state of transitions. For example, when downloading,
 * progress and totalProgress will show the current number of bytes downloaded
 * and the total number of bytes needed to be downloaded respectively.
 * </p>
 * <p>
 * The count and totalCount indicator is used when a state transition affects
 * multiple hosts. The count and totalCount show the current number of hosts
 * completed and the total number of hosts respectively. For example, during distribution,
 * the progress and totalProgress will show how many bytes have been transferred
 * to each host and the count will indicate how many hosts of of totalCount
 * have had parcels unpacked.
 * </p>
 * <p>
 * Along with the two progress indicators, the ApiParcelState shows both errors
 * and warnings that may have turned up during a state transition.
 * </p>
 */
public class ApiParcelState {

  private long progress;
  private long totalProgress;
  private long count;
  private long totalCount;
  private List<String> warnings;
  private List<String> errors;

  public ApiParcelState() {
    // For JAX-B
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("progress", progress)
                  .add("progressTotal", totalProgress)
                  .add("count", count)
                  .add("countTotal", totalCount)
                  .add("warnings", warnings)
                  .add("errors", errors)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiParcelState that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(progress, that.progress) &&
        Objects.equal(totalProgress, that.totalProgress) &&
        Objects.equal(count, that.count) &&
        Objects.equal(totalCount, that.totalCount) &&
        Objects.equal(warnings, that.warnings) &&
        Objects.equal(errors, that.errors));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(progress, totalProgress, count, totalCount, warnings, errors);
  }

  /**
   * The progress of the state transition.
   */
  @XmlElement
  public long getProgress() {
    return progress;
  }

  public void setProgress(long progress) {
    this.progress = progress;
  }

  /**
   * The total amount that {@link #getProgress()} needs to get to.
   */
  @XmlElement
  public long getTotalProgress() {
    return totalProgress;
  }

  public void setTotalProgress(long totalProgress) {
    this.totalProgress = totalProgress;
  }

  /**
   * The current hosts that have completed.
   */
  @XmlElement
  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  /**
   * The total amount that {@link #getCount()} needs to get to.
   */
  @XmlElement
  public long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(long totalCount) {
    this.totalCount = totalCount;
  }

  /**
   * The errors that exist for this parcel.
   */
  @XmlElementWrapper(name = "errors")
  public List<String> getErrors() {
    return errors;
  }

  public void setErrors(List<String> errors) {
    this.errors = errors;
  }

  /**
   * The warnings that exist for this parcel.
   */
  @XmlElementWrapper(name = "warnings")
  public List<String> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<String> warnings) {
    this.warnings = warnings;
  }
}
