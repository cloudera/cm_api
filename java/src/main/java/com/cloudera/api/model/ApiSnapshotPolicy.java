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
import com.google.common.base.Preconditions;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A snapshot policy.
 * <p/>
 * Snapshot policies have service specific arguments. This object has methods
 * to retrieve arguments for all supported types of snapshots, but only one
 * argument type is allowed to be set; the backend will check that the provided
 * argument matches the type of the service with which the snapshot policy is
 * associated.
 */
@XmlRootElement(name = "snapshotPolicy")
public class ApiSnapshotPolicy {
  private String name;
  private String description;

  // Number of snapshots to be retained for each interval type.
  private long hourlySnapshots;
  private long dailySnapshots;
  private long weeklySnapshots;
  private long monthlySnapshots;
  private long yearlySnapshots;

  private byte minuteOfHour;
  private byte hourOfDay;
  private byte dayOfWeek = 1;
  private byte dayOfMonth = 1;
  private byte monthOfYear = 1;

  List<Byte> hoursForHourlySnapshots;

  private boolean alertOnStart;
  private boolean alertOnSuccess;
  private boolean alertOnFail;
  private boolean alertOnAbort;
  private Boolean paused;

  private ApiHBaseSnapshotPolicyArguments hbaseArguments;
  private ApiHdfsSnapshotPolicyArguments hdfsArguments;

  private ApiSnapshotCommand lastCommand;
  private ApiSnapshotCommand lastSuccessfulCommand;

  public ApiSnapshotPolicy() {
    // For JAX-B.
  }

  public ApiSnapshotPolicy(String name, String description,
      long hourlySnapshots, long dailySnapshots, long weeklySnapshots,
      long monthlySnapshots, long yearlySnapshots) {
    this.name = name;
    this.description = description;
    this.hourlySnapshots = hourlySnapshots;
    this.dailySnapshots = dailySnapshots;
    this.weeklySnapshots = weeklySnapshots;
    this.monthlySnapshots = monthlySnapshots;
    this.yearlySnapshots = yearlySnapshots;
  }

  /** Name of the snapshot policy. */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** Description of the snapshot policy. */
  @XmlElement
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /** Number of hourly snapshots to be retained. */
  @XmlElement
  public long getHourlySnapshots() {
    return hourlySnapshots;
  }

  public void setHourlySnapshots(long hourlySnapshots) {
    this.hourlySnapshots = hourlySnapshots;
  }

  /** Number of daily snapshots to be retained. */
  @XmlElement
  public long getDailySnapshots() {
    return dailySnapshots;
  }

  public void setDailySnapshots(long dailySnapshots) {
    this.dailySnapshots = dailySnapshots;
  }

  /** Number of weekly snapshots to be retained. */
  @XmlElement
  public long getWeeklySnapshots() {
    return weeklySnapshots;
  }

  public void setWeeklySnapshots(long weeklySnapshots) {
    this.weeklySnapshots = weeklySnapshots;
  }

  /** Number of monthly snapshots to be retained. */
  @XmlElement
  public long getMonthlySnapshots() {
    return monthlySnapshots;
  }

  public void setMonthlySnapshots(long monthlySnapshots) {
    this.monthlySnapshots = monthlySnapshots;
  }

  /** Number of yearly snapshots to be retained. */
  @XmlElement
  public long getYearlySnapshots() {
    return yearlySnapshots;
  }

  public void setYearlySnapshots(long yearlySnapshots) {
    this.yearlySnapshots = yearlySnapshots;
  }

  /**
   * Minute in the hour that hourly, daily, weekly, monthly and yearly
   * snapshots should be created. Valid values are 0 to 59. Default value is 0.
   */
  @XmlElement
  public byte getMinuteOfHour() {
    return minuteOfHour;
  }

  public void setMinuteOfHour(byte minuteOfHour) {
    Preconditions.checkArgument(minuteOfHour >= 0 && minuteOfHour <= 59,
        "Invalid minute of hour. Valid values are 0 to 59.");
    this.minuteOfHour = minuteOfHour;
  }

  /**
   * Hours of the day that hourly snapshots should be created. Valid values
   * are 0 to 23. If this list is null or empty, then hourly snapshots are
   * created for every hour.
   */
  @XmlElementWrapper
  public List<Byte> getHoursForHourlySnapshots() {
    return hoursForHourlySnapshots;
  }

  public void setHoursForHourlySnapshots(List<Byte> hours) {
    if (hours != null) {
      for (Byte hour : hours) {
        Preconditions.checkArgument(hour != null && hour >= 0 && hour <= 23,
            "Invalid hour of day (%s). Valid values are 0 to 23.", hour);
      }
    }
    this.hoursForHourlySnapshots = hours;
  }

  /**
   * Hour in the day that daily, weekly, monthly and yearly snapshots should be
   * created. Valid values are 0 to 23. Default value is 0.
   */
  @XmlElement
  public byte getHourOfDay() {
    return hourOfDay;
  }

  public void setHourOfDay(byte hourOfDay) {
    Preconditions.checkArgument(hourOfDay >= 0 && hourOfDay <= 23,
        "Invalid hour of day. Valid values are 0 to 23.");
    this.hourOfDay = hourOfDay;
  }

  /**
   * Day of the week that weekly snapshots should be created. Valid values are
   * 1 to 7, 1 representing Sunday. Default value is 1.
   */
  @XmlElement
  public byte getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(byte dayOfWeek) {
    Preconditions.checkArgument(dayOfWeek >= 1 && dayOfWeek <= 7,
        "Invalid day of week. Valid values are 1 to 7.");
    this.dayOfWeek = dayOfWeek;
  }

  /**
   * Day of the month that monthly and yearly snapshots should be created.
   * Values from 1 to 31 are allowed. Additionally 0 to -30 can be used to
   * specify offsets from the last day of the month. Default value is 1.
   * <p/>
   * If this value is invalid for any month for which snapshots are required,
   * the backend will throw an exception.
   */
  @XmlElement
  public byte getDayOfMonth() {
    return dayOfMonth;
  }

  public void setDayOfMonth(byte dayOfMonth) {
    Preconditions.checkArgument(dayOfMonth >= -30 && dayOfMonth <= 31,
        "Invalid day of month. Value values are 1 to 31, or 0 to -30 to " +
            "specify offset from last day of month.");
    this.dayOfMonth = dayOfMonth;
  }

  /**
   * Month of the year that yearly snapshots should be created. Valid values
   * are 1 to 12, 1 representing January. Default value is 1.
   */
  @XmlElement
  public byte getMonthOfYear() {
    return monthOfYear;
  }

  public void setMonthOfYear(byte monthOfYear) {
    Preconditions.checkArgument(monthOfYear >= 1 && monthOfYear <= 12,
        "Invalid month. Valid values are 1 to 12.");
    this.monthOfYear = monthOfYear;
  }

  /** Whether to alert on start of snapshot creation/deletion activity. */
  @XmlElement
  public boolean getAlertOnStart() {
    return alertOnStart;
  }

  public void setAlertOnStart(boolean alertOnStart) {
    this.alertOnStart = alertOnStart;
  }

  /**
   * Whether to alert on successful completion of snapshot creation/deletion
   * activity.
   */
  @XmlElement
  public boolean getAlertOnSuccess() {
    return alertOnSuccess;
  }

  public void setAlertOnSuccess(boolean alertOnSuccess) {
    this.alertOnSuccess = alertOnSuccess;
  }

  /** Whether to alert on failure of snapshot creation/deletion activity. */
  @XmlElement
  public boolean getAlertOnFail() {
    return alertOnFail;
  }

  public void setAlertOnFail(boolean alertOnFail) {
    this.alertOnFail = alertOnFail;
  }

  /** Whether to alert on abort of snapshot creation/deletion activity. */
  @XmlElement
  public boolean getAlertOnAbort() {
    return alertOnAbort;
  }

  public void setAlertOnAbort(boolean alertOnAbort) {
    this.alertOnAbort = alertOnAbort;
  }

  /** Arguments specific to HBase snapshot policies. */
  @XmlElement
  public ApiHBaseSnapshotPolicyArguments getHBaseArguments() {
    return hbaseArguments;
  }

  public void setHBaseArguments(
      ApiHBaseSnapshotPolicyArguments hbaseArguments) {
    this.hbaseArguments = hbaseArguments;
  }

  /** Arguments specific to Hdfs snapshot policies. */
  @XmlElement
  public ApiHdfsSnapshotPolicyArguments getHdfsArguments() {
    return hdfsArguments;
  }

  public void setHdfsArguments(
      ApiHdfsSnapshotPolicyArguments hdfsArguments) {
    this.hdfsArguments = hdfsArguments;
  }

  /**
   * Latest command of this policy.
   * The command might still be active.
   */
  public ApiSnapshotCommand getLastCommand() {
    return lastCommand;
  }

  public void setLastCommand(ApiSnapshotCommand lastCommand) {
    this.lastCommand = lastCommand;
  }

  /**
   * Last successful command of this policy.
   * Returns null if there has been no successful command.
   */
  public ApiSnapshotCommand getLastSuccessfulCommand() {
    return lastSuccessfulCommand;
  }

  public void setLastSuccessfulCommand(ApiSnapshotCommand lastSuccessfulCommand) {
    this.lastSuccessfulCommand = lastSuccessfulCommand;
  }

  /** Whether to pause a snapshot policy, available since V11. */
  @XmlElement
  public Boolean getPaused() {
    return paused;
  }

  public void setPaused(Boolean paused) {
    this.paused = paused;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("name", name)
        .add("description", description)
        .add("hourlySnapshots", hourlySnapshots)
        .add("dailySnapshots", dailySnapshots)
        .add("weeklySnapshots", weeklySnapshots)
        .add("monthlySnapshots", monthlySnapshots)
        .add("yearlySnapshots", yearlySnapshots)
        .add("minuteOfHour", minuteOfHour)
        .add("hourOfDay", hourOfDay)
        .add("dayOfWeek", dayOfWeek)
        .add("dayOfMonth", dayOfMonth)
        .add("monthOfYear", monthOfYear)
        .add("hoursForHourlySnapshots", hoursForHourlySnapshots)
        .add("alertOnStart", alertOnStart)
        .add("alertOnSuccess", alertOnSuccess)
        .add("alertOnFail", alertOnFail)
        .add("alertOnAbort", alertOnAbort)
        .add("hbaseArguments", hbaseArguments)
        .add("hdfsArguments", hdfsArguments)
        .add("lastCommand", lastCommand)
        .add("lastSuccessfulCommand", lastSuccessfulCommand)
        .add("paused", paused)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiSnapshotPolicy other = ApiUtils.baseEquals(this, o);
    return other != null &&
        Objects.equal(name, other.getName()) &&
        Objects.equal(description, other.getDescription()) &&
        hourlySnapshots == other.getHourlySnapshots() &&
        dailySnapshots == other.getDailySnapshots() &&
        weeklySnapshots == other.getWeeklySnapshots() &&
        monthlySnapshots == other.getMonthlySnapshots() &&
        yearlySnapshots == other.getYearlySnapshots() &&
        minuteOfHour == other.getMinuteOfHour() &&
        hourOfDay == other.getHourOfDay() &&
        dayOfWeek == other.getDayOfWeek() &&
        dayOfMonth == other.getDayOfMonth() &&
        monthOfYear == other.getMonthOfYear() &&
        Objects.equal(hoursForHourlySnapshots,
            other.getHoursForHourlySnapshots()) &&
        alertOnStart == other.getAlertOnStart() &&
        alertOnSuccess == other.getAlertOnSuccess() &&
        alertOnFail == other.getAlertOnFail() &&
        alertOnAbort == other.getAlertOnAbort() &&
        Objects.equal(hbaseArguments, other.getHBaseArguments()) &&
        Objects.equal(hdfsArguments, other.getHdfsArguments()) &&
        Objects.equal(lastCommand, other.lastCommand) &&
        Objects.equal(lastSuccessfulCommand, other.lastSuccessfulCommand) &&
        Objects.equal(paused, other.paused);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, description, hourlySnapshots, dailySnapshots,
        weeklySnapshots, monthlySnapshots, yearlySnapshots, minuteOfHour,
        hourOfDay, dayOfWeek, dayOfMonth, monthOfYear, hoursForHourlySnapshots,
        alertOnStart, alertOnSuccess, alertOnFail, alertOnAbort,
        hbaseArguments, hdfsArguments, lastCommand, lastSuccessfulCommand,
        paused);
  }
}
