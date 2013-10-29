// Copyright (c) 2014 Cloudera, Inc. All rights reserved.
package com.cloudera.api.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A list of command metadata.
 */
@XmlRootElement(name = "commandMetadataList")
public class ApiCommandMetadataList extends ApiListBase<ApiCommandMetadata> {

  public ApiCommandMetadataList() {}

  public ApiCommandMetadataList(List<ApiCommandMetadata> commandMetadata) {
    super(commandMetadata);
  }

  /**
   * The list of command metadata objects.
   */
  @XmlElementWrapper(name = ApiListBase.ITEMS_ATTR)
  public List<ApiCommandMetadata> getCommandMetadataItems() { return values; }

  public void setCommandMetadataItems(List<ApiCommandMetadata> items) { this.values = items; }
}
