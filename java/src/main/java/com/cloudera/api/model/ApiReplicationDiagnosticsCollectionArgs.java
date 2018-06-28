//  Copyright (c) 2015 Cloudera, Inc. All rights reserved.
package com.cloudera.api.model;

import com.google.common.base.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Optional arguments for diagnostics collection.
 */
@XmlRootElement(name = "replicationDiagnosticsCollectionArgs")
public class ApiReplicationDiagnosticsCollectionArgs {
  private ApiCommandList commands;
  private String ticketNumber;
  private String comments;
  private Boolean phoneHome;

  // For jackson/JAXB.
  public ApiReplicationDiagnosticsCollectionArgs() {
  }

  /**
   * Commands to limit diagnostics to.
   * By default, the most recent 10 commands on the schedule will be used.
   */
  @XmlElement
  public ApiCommandList getCommands() {
    return commands;
  }

  public void setCommands(ApiCommandList commands) {
    this.commands = commands;
  }

  /**
   * Ticket number to which this bundle must be associated with.
   */
  @XmlElement
  public String getTicketNumber() {
    return ticketNumber;
  }

  public void setTicketNumber(String ticketNumber) {
    this.ticketNumber = ticketNumber;
  }

  /**
   * Additional comments for the bundle.
   */
  @XmlElement
  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  /**
   * Whether the diagnostics bundle must be uploaded to Cloudera.
   */
  @XmlElement
  public Boolean getPhoneHome() {
    return phoneHome;
  }

  public void setPhoneHome(Boolean phoneHome) {
    this.phoneHome = phoneHome;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ApiReplicationDiagnosticsCollectionArgs that = (ApiReplicationDiagnosticsCollectionArgs) o;
    return Objects.equal(this.commands, that.commands) &&
           Objects.equal(this.phoneHome, that.phoneHome) &&
           Objects.equal(this.comments, that.comments) &&
           Objects.equal(this.ticketNumber, that.ticketNumber);
  }

  @Override
  public int hashCode() {
    return 31 * Objects.hashCode(
        this.commands, this.phoneHome, this.comments, this.ticketNumber);
  }
}
