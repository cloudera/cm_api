// Copyright (c) 2017 Cloudera, Inc. All rights reserved.

package com.cloudera.api.model;

import com.google.common.base.Joiner;

public enum ApiHostRecommissionType {
  RECOMMISSION,
  RECOMMISSION_WITH_START;

  private static final String SUPPORTED_TYPES =
      Joiner.on(", ").join(values()).toLowerCase();

  public static ApiHostRecommissionType fromString(String s) {
    if (s == null || s.isEmpty()) {
      return RECOMMISSION;
    }
    try {
      return valueOf(s.toUpperCase());
    } catch (IllegalArgumentException t) {
      throw new IllegalArgumentException(
          String.format("Illegal type='%s'. Supported types: %s",
              SUPPORTED_TYPES));
    }
  }
}
