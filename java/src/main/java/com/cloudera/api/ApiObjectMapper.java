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

package com.cloudera.api;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.SimpleTimeZone;

/**
 * This class is injected to give us control over the object mapper
 */
public class ApiObjectMapper extends ObjectMapper {

  public ApiObjectMapper() {
    // Print the JSON with indentation (ie. pretty print)
    configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

    // Set up a secondary annotation interceptor for JAXB annotations.
    AnnotationIntrospector pair = new AnnotationIntrospector.Pair(
        getSerializationConfig().getAnnotationIntrospector(),
        new JaxbAnnotationIntrospector());
    setSerializationConfig(
        getSerializationConfig().withAnnotationIntrospector(pair));

    pair = new AnnotationIntrospector.Pair(
        getDeserializationConfig().getAnnotationIntrospector(),
        new JaxbAnnotationIntrospector());
    setDeserializationConfig(
        getDeserializationConfig().withAnnotationIntrospector(pair));

    // Print all dates in ISO8601 format
    DateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
    iso8601.setCalendar(cal);
    setDateFormat(iso8601);
  }

}
