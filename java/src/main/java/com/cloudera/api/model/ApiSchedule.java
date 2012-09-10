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

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

/**
 * Base class for commands that can be scheduled in Cloudera Manager.
 */
public abstract class ApiSchedule<T extends ApiCommand> {
  private Long id;
  private Date startTime;
  private Date endTime;
  private long interval;
  private ApiScheduleInterval intervalUnit;
  private boolean paused;
  private Date nextRun;
  private List<T> history;
  private boolean alertOnStart;
  private boolean alertOnSuccess;
  private boolean alertOnFail;
  private boolean alertOnAbort;

  protected ApiSchedule() {
    this(null, null, null, 1, ApiScheduleInterval.DAY, false);
  }

  protected ApiSchedule(Long id, Date startTime, Date endTime, long interval,
      ApiScheduleInterval intervalUnit, boolean paused) {
    this.id = id;
    this.startTime = startTime;
    this.endTime = endTime;
    this.interval = interval;
    this.intervalUnit = intervalUnit;
    this.paused = paused;
  }

  /** The schedule id. */
  @XmlElement
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * The time at which the scheduled activity is triggered for the first
   * time.
   */
  @XmlElement
  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  /**
   * The time after which the scheduled activity will no longer be triggered.
   */
  @XmlElement
  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  /** The duration between consecutive triggers of a scheduled activity. */
  @XmlElement
  public long getInterval() {
    return interval;
  }

  public void setInterval(long interval) {
    this.interval = interval;
  }

  /** The unit for the repeat interval. */
  @XmlElement
  public ApiScheduleInterval getIntervalUnit() {
    return intervalUnit;
  }

  public void setIntervalUnit(ApiScheduleInterval intervalUnit) {
    this.intervalUnit = intervalUnit;
  }

  /** Readonly. The time the scheduled command will run next. */
  public Date getNextRun() {
    return nextRun;
  }

  public void setNextRun(Date nextRun) {
    this.nextRun = nextRun;
  }

  /**
   * History of executions of this schedule, sorted in reverse chronological
   * order.
   */
  public List<T> getHistory() {
    return history;
  }

  public void setHistory(List<T> history) {
    this.history = history;
  }

  /**
   * The paused state for the schedule. The scheduled activity will not be
   * triggered as long as the scheduled is paused.
   */
  @XmlElement
  public boolean isPaused() {
    return paused;
  }

  public void setPaused(boolean paused) {
    this.paused = paused;
  }

  /** Whether to alert on start of the scheduled activity. */
  @XmlElement
  public boolean getAlertOnStart() {
    return alertOnStart;
  }

  public void setAlertOnStart(boolean alertOnStart) {
    this.alertOnStart = alertOnStart;
  }

  /** Whether to alert on successful completion of the scheduled activity. */
  @XmlElement
  public boolean getAlertOnSuccess() {
    return alertOnSuccess;
  }

  public void setAlertOnSuccess(boolean alertOnSuccess) {
    this.alertOnSuccess = alertOnSuccess;
  }

  /** Whether to alert on failure of the scheduled activity. */
  @XmlElement
  public boolean getAlertOnFail() {
    return alertOnFail;
  }

  public void setAlertOnFail(boolean alertOnFail) {
    this.alertOnFail = alertOnFail;
  }

  /** Whether to alert on abort of the scheduled activity. */
  @XmlElement
  public boolean getAlertOnAbort() {
    return alertOnAbort;
  }

  public void setAlertOnAbort(boolean alertOnAbort) {
    this.alertOnAbort = alertOnAbort;
  }

  @Override
  public String toString() {
      return toStringHelper().toString();
  }

  protected Objects.ToStringHelper toStringHelper() {
    return Objects.toStringHelper(this)
        .add("id", id)
        .add("startTime", startTime)
        .add("endTime", endTime)
        .add("interval", interval)
        .add("intervalUnit", intervalUnit)
        .add("paused", paused)
        .add("alertOnStart", alertOnStart)
        .add("alertOnSuccess", alertOnSuccess)
        .add("alertOnFail", alertOnFail)
        .add("alertOnAbort", alertOnAbort);
  }

  @Override
  public boolean equals(Object o) {
    ApiSchedule other = ApiUtils.baseEquals(this, o);
    return other != null &&
        Objects.equal(id, other.getId()) &&
        Objects.equal(startTime, other.getStartTime()) &&
        Objects.equal(endTime, other.getEndTime()) &&
        Objects.equal(interval, other.getInterval()) &&
        Objects.equal(intervalUnit, other.getIntervalUnit()) &&
        Objects.equal(paused, other.isPaused()) &&
        alertOnStart == other.getAlertOnStart() &&
        alertOnSuccess == other.getAlertOnSuccess() &&
        alertOnFail == other.getAlertOnFail() &&
        alertOnAbort == other.getAlertOnAbort();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, startTime, endTime, interval,
        intervalUnit, paused, alertOnStart, alertOnSuccess, alertOnFail,
        alertOnAbort);
  }
}
