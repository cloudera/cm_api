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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * ApiRootResourceInvocationHandler allows delegation of
 * call on API proxy from {@link ApiRootResource} interface to {@link ApiRootResourceExternal}.
 *
 * Generating stubs using {@link ApiRootResourceExternal} is more memory efficient and hence
 * the need for delegation which allows us to redirect call without requiring
 * changes for consumers of {@link ClouderaManagerClientBuilder}.
 */
class ApiRootResourceInvocationHandler implements InvocationHandler {
  final private ApiRootResourceExternal delegateRootResource;

  ApiRootResourceInvocationHandler(ApiRootResourceExternal rootResource) {
    delegateRootResource = rootResource;
  }

  ApiRootResourceExternal getDelegateRootResource() {
    return delegateRootResource;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Method delegateMethod = ApiRootResourceExternal.class.getMethod(method.getName(),
      method.getParameterTypes());
    return delegateMethod.invoke(delegateRootResource, args);
  }
}