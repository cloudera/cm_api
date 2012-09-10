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

import com.google.common.base.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

// Note that the properties in this class are used for activity query search.
// See ActivityMonitorDaoImpl to update the search keywords if you add/edit the
// properties in the ApiActivity class.

/**
 * Represents a user activity, such as a MapReduce job, a Hive query, an Oozie
 * workflow, etc.
 */
@XmlRootElement(name="activity")
public class ApiActivity {
  private String id;
  private String name;
  private ApiActivityType type;
  private ApiActivityStatus status;
  private String parent;
  private Date startTime;
  private Date finishTime;
  private String user;
  private String group;
  private String inputDir;
  private String outputDir;
  private String mapper;
  private String combiner;
  private String reducer;
  private String queueName;
  private String schedulerPriority;

  public ApiActivity() {
    // For JAX-B
  }

  /**
   * Default view
   * @param id The id of the activity
   * @param name The name of the activity
   * @param type The activity type
   * @param status The activity status
   * @param parent The parent of the activity
   * @param startTime The time the activity started
   * @param finishTime The time the activity finished
   * @param user The user running the activity
   * @param group The group running the activity
   */
  public ApiActivity(String id, String name, ApiActivityType type,
                     ApiActivityStatus status, String parent,
                     Date startTime, Date finishTime,
                     String user, String group) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.parent = parent;
    this.startTime = startTime;
    this.finishTime = finishTime;
    this.status = status;
    this.user = user;
    this.group = group;
    // These are for the full view
    this.inputDir = null;
    this.outputDir = null;
    this.mapper = null;
    this.combiner = null;
    this.reducer = null;
    this.queueName = null;
    this.schedulerPriority = null;
  }

  /**
   * Full view of an activity
   * @param id The activity id
   * @param name The activity name
   * @param type The type of activity
   * @param status The status of the activity
   * @param parent The parent of the activity
   * @param startTime The time the activity started
   * @param finishTime The time the activity finished
   * @param user The user who started the activity
   * @param group The group of the running activity
   * @param inputDir The input directory
   * @param outputDir The output directory
   * @param mapper The mapper class for the activity
   * @param combiner The combiner class for the activity
   * @param reducer The reducer class for the activity
   * @param queueName The name of the scheduler queue
   * @param schedulerPriority The scheduler priority
   */
  public ApiActivity(String id, String name, ApiActivityType type,
                     ApiActivityStatus status, String parent,
                     Date startTime, Date finishTime,
                     String user, String group,
                     String inputDir, String outputDir,
                     String mapper, String combiner, String reducer,
                     String queueName, String schedulerPriority)  {
    this.id = id;
    this.name = name;
    this.type = type;
    this.parent = parent;
    this.startTime = startTime;
    this.finishTime = finishTime;
    this.status = status;
    this.user = user;
    this.group = group;
    this.inputDir = inputDir;
    this.outputDir = outputDir;
    this.mapper = mapper;
    this.combiner = combiner;
    this.reducer = reducer;
    this.queueName = queueName;
    this.schedulerPriority = schedulerPriority;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("id", id)
                  .add("name", name)
                  .add("type", type)
                  .add("parent", parent)
                  .add("startTime", startTime)
                  .add("finishTime", finishTime)
                  .add("status", status)
                  .toString();
  }

  /** Activity name. */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** Activity type. Whether it's an MR job, a Pig job, a Hive query, etc. */
  @XmlElement
  public ApiActivityType getType() {
    return type;
  }

  public void setType(ApiActivityType type) {
    this.type = type;
  }

  /** The name of the parent activity. */
  @XmlElement
  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  /** The start time of this activity. */
  @XmlElement
  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  /** The finish time of this activity. */
  @XmlElement
  public Date getFinishTime() {
    return finishTime;
  }

  public void setFinishTime(Date finishTime) {
    this.finishTime = finishTime;
  }

  /** Activity id, which is unique within a MapReduce service. */
  @XmlElement
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /** Activity status. */
  @XmlElement
  public ApiActivityStatus getStatus() {
    return status;
  }

  public void setStatus(ApiActivityStatus status) {
    this.status = status;
  }

  /** The user who submitted this activity. */
  @XmlElement
  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  /** The user-group of this activity. */
  @XmlElement
  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  /** The input data directory of the activity. An HDFS url. */
  @XmlElement
  public String getInputDir() {
    return inputDir;
  }

  public void setInputDir(String inputDir) {
    this.inputDir = inputDir;
  }

  /** The output result directory of the activity. An HDFS url. */
  @XmlElement
  public String getOutputDir() {
    return outputDir;
  }

  public void setOutputDir(String outputDir) {
    this.outputDir = outputDir;
  }

  /** The mapper class. */
  @XmlElement
  public String getMapper() {
    return mapper;
  }

  public void setMapper(String mapper) {
    this.mapper = mapper;
  }

  /** The combiner class. */
  @XmlElement
  public String getCombiner() {
    return combiner;
  }

  public void setCombiner(String combiner) {
    this.combiner = combiner;
  }

  /** The reducer class. */
  @XmlElement
  public String getReducer() {
    return reducer;
  }

  public void setReducer(String reducer) {
    this.reducer = reducer;
  }

  /** The scheduler queue this activity is in. */
  @XmlElement
  public String getQueueName() {
    return queueName;
  }

  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }

  /** The scheduler priority of this activity. */
  @XmlElement
  public String getSchedulerPriority() {
    return schedulerPriority;
  }

  public void setSchedulerPriority(String schedulerPriority) {
    this.schedulerPriority = schedulerPriority;
  }
}
