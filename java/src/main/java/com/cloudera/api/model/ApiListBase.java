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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Iterator;
import java.util.List;

/**
 * A generic list.
 */
abstract class ApiListBase<T> implements Iterable<T> {

  public static final String ITEMS_ATTR = "items";

  protected List<T> values;

  public ApiListBase() {
    values = Lists.newArrayList();
  }

  public ApiListBase(List<T> values) {
    this.values = values;
  }

  public boolean add(T item) {
    return values.add(item);
  }

  public Iterator<T> iterator() {
    return values.iterator();
  }

  public int size() {
    return values.size();
  }

  public T get(int index) {
    return values.get(index);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("values", values)
                  .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(values);
  }

  @Override
  public boolean equals(Object o) {
    ApiListBase that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(values, that.values));
  }
}
