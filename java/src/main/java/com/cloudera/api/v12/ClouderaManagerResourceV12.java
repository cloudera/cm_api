// Copyright (c) 2015 Cloudera, Inc. All rights reserved.
package com.cloudera.api.v12;

import com.cloudera.api.model.ApiClusterTemplate;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.v11.ClouderaManagerResourceV11;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClouderaManagerResourceV12 extends ClouderaManagerResourceV11 {

  /**
   * Create cluster as per the given cluster template
   *
   * @param apiClusterTemplate cluster template
   * @param addRepositories if true the parcels repositories in the cluster
   *          template will be added.
   * @return The command performing import task
   */
  @POST
  @Path("importClusterTemplate")
  public ApiCommand importClusterTemplate(
      ApiClusterTemplate apiClusterTemplate,
      @QueryParam("addRepositories") @DefaultValue("false") boolean addRepositories);

}