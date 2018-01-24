//  Copyright (c) 2016 Cloudera, Inc. All rights reserved.
package com.cloudera.api.v14;

import com.cloudera.api.DataView;
import com.cloudera.api.Parameters;
import com.cloudera.api.model.ApiExternalAccount;
import com.cloudera.api.model.ApiExternalAccountCategoryList;
import com.cloudera.api.model.ApiExternalAccountList;
import com.cloudera.api.model.ApiExternalAccountTypeList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Manage external accounts used by various Cloudera Manager features, for performing external
 * tasks.
 */
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface ExternalAccountsResourceV14 {

  /**
   * List of external account categories supported by this Cloudera Manager.
   * @return external account categories
   */
  @GET
  @Path("/supportedCategories")
  ApiExternalAccountCategoryList getSupportedCategories();

  /**
   * List of external account types supported by this Cloudera Manager by category.
   */
  @GET
  @Path("/supportedTypes/{" + Parameters.EXTERNAL_ACCOUNT_CATEGORY_NAME + "}")
  ApiExternalAccountTypeList getSupportedTypes(
      @PathParam(Parameters.EXTERNAL_ACCOUNT_CATEGORY_NAME) String categoryName);

  /**
   * Get a list of external accounts for a specific account type.
   */
  @GET
  @Path("/type/{" + Parameters.EXTERNAL_ACCOUNT_TYPE_NAME + "}")
  ApiExternalAccountList readAccounts(
      @PathParam(Parameters.EXTERNAL_ACCOUNT_TYPE_NAME) String typeName,
      @QueryParam(Parameters.DATA_VIEW) DataView view);

  /**
   * Get a single external account by account name.
   */
  @GET
  @Path("/account/{" + Parameters.EXTERNAL_ACCOUNT_NAME + "}")
  ApiExternalAccount readAccount(@PathParam(Parameters.EXTERNAL_ACCOUNT_NAME) String name,
                                 @QueryParam(Parameters.DATA_VIEW) DataView view);

  /**
   * Get a single external account by display name.
   */
  @GET
  @Path("/accountByDisplayName/{" + Parameters.DISPLAY_NAME + "}")
  ApiExternalAccount readAccountByDisplayName(
      @PathParam(Parameters.DISPLAY_NAME) String displayName,
      @QueryParam(Parameters.DATA_VIEW) DataView view);

  /**
   * Create a new external account.
   * Account names and display names must be unique, i.e. they must not share names or display
   * names with an existing account.
   * Server generates an account ID for the requested account.
   */
  @POST
  @Path("/create")
  ApiExternalAccount createAccount(ApiExternalAccount account);

  /**
   * Update an external account.
   */
  @PUT
  @Path("/update")
  ApiExternalAccount updateAccount(ApiExternalAccount account);

  /**
   * Delete an external account, specifying its name.
   */
  @DELETE
  @Path("/delete/{" + Parameters.EXTERNAL_ACCOUNT_NAME + "}")
  ApiExternalAccount deleteAccount(@PathParam(Parameters.EXTERNAL_ACCOUNT_NAME) String name);
}
