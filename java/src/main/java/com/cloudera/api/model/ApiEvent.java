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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import java.util.List;
import java.util.Map;

// Note that the properties in this class are used for event query search.
// See EventStoreDaoImpl to update the search keywords if you add/edit the
// properties in the ApiEvent class.

/**
 * Events model noteworthy incidents in Cloudera Manager or the managed
 * Hadoop cluster. An event carries its event category, severity, and a
 * string content. They also have generic attributes, which are free-form
 * key value pairs. Important events may be promoted into alerts.
 */
@XmlRootElement(name = "event")
@XmlType(
    propOrder = {"id", "content", "timeOccurred", "timeReceived",
        "category", "severity", "alert", "attributes"})
public class ApiEvent {
  private String id;
  private Date timeOccurred;
  private Date timeReceived;
  private String content;
  private List<ApiEventAttribute> attributes;
  private ApiEventCategory category;
  private ApiEventSeverity severity;
  private boolean alert;

  public ApiEvent() {
    // For JAX-B
  }

  public ApiEvent(String id, Date timeOccurred, Date timeReceived,
                  ApiEventCategory category, ApiEventSeverity severity,
                  String content, boolean alert,
                  Map<String, List<String>> attrMap) {
    this.id = id;
    this.timeOccurred = timeOccurred;
    this.timeReceived = timeReceived;
    this.content = content;
    this.category = category;
    this.severity = severity;
    this.alert = alert;
    this.attributes = Lists.newArrayList();
    if (attrMap != null) {
      for (Map.Entry<String, List<String>> e : attrMap.entrySet()) {
        this.attributes.add(new ApiEventAttribute(e.getKey(), e.getValue()));
      }
    }
  }

  @Override
  public boolean equals(Object o) {
    ApiEvent that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(id, that.getId()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  /** A unique ID for this event. */
  @XmlElement
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /** The content payload of this event. */
  @XmlElement
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  /** When the event was generated. */
  @XmlElement
  public Date getTimeOccurred() {
    return timeOccurred;
  }

  public void setTimeOccurred(Date timeOccurred) {
    this.timeOccurred = timeOccurred;
  }

  /**
   * When the event was stored by Cloudera Manager. Events do not arrive in
   * the order that they are generated. If you are writing an event poller,
   * this is a useful field to query.
   */
  @XmlElement
  public Date getTimeReceived() {
    return timeReceived;
  }

  public void setTimeReceived(Date timeReceived) {
    this.timeReceived = timeReceived;
  }

  /**
   * The category of this event -- whether it is a health event, an audit
   * event, an activity event, etc.
   */
  @XmlElement
  public ApiEventCategory getCategory() {
    return category;
  }

  public void setCategory(ApiEventCategory category) {
    this.category = category;
  }

  /** The severity of the event. */
  @XmlElement
  public ApiEventSeverity getSeverity() {
    return severity;
  }

  public void setSeverity(ApiEventSeverity severity) {
    this.severity = severity;
  }

  /** Whether the event is promoted to an alert according to configuration. */
  @XmlElement
  public boolean isAlert() {
    return alert;
  }

  public void setAlert(boolean alert) {
    this.alert = alert;
  }

  /** A list of key-value attribute pairs. */
  @XmlElementWrapper(name = "attributes")
  public List<ApiEventAttribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<ApiEventAttribute> attributes) {
    this.attributes = attributes;
  }
}
