/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.stratio.meta.common.exceptions.ConnectionException;
import com.stratio.meta.common.exceptions.ExecutionException;
import com.stratio.meta.common.exceptions.ParsingException;
import com.stratio.meta.common.exceptions.UnsupportedException;
import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.rest.models.JsonQueryResult;
import com.stratio.meta.rest.utils.DriverHelper;
import com.stratio.meta.rest.utils.RestResultHandler;
import com.stratio.meta.rest.utils.RestServerUtils;

/**
 * Root resource (exposed at "" path)
 */
@Path("")
public class RestServer {
  private DriverHelper driver = DriverHelper.getInstance();
  private RestResultHandler callback = new RestResultHandler();


  @GET
  @Produces(MediaType.TEXT_HTML)
  public String getIt() {
    return "META REST Server up!";
  }

  /**
   * Method handling HTTP POST requests. The returned object will be sent to the client as
   * "text/plain" media type.
   * 
   * @return String that will be returned as a text/plain response.
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonGenerationException
   */
  @POST
  @Path("api")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_PLAIN)
  public String postQuery(@FormParam("catalog") String catalog, @FormParam("query") String query) {
    String queryId = "";



    try {
      System.out.println("[MetaRestServer] query: " + query + " catalog " + catalog);
      driver.executeAsyncQuery(query, catalog, callback);
    } catch (UnsupportedException | ParsingException | ValidationException | ExecutionException
        | ConnectionException e) {
      return e.getMessage();
    }
    queryId = driver.getAsyncResult();
    return queryId;
  }

  @GET
  @Path("api/query/{queryId}")
  @Produces(MediaType.APPLICATION_JSON)
  public String getQuery(@PathParam("queryId") String queryId) throws JsonGenerationException,
      JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    callback = (RestResultHandler) driver.getCallback(queryId);
    Result callbackResult = callback.getResult(queryId);
    if (callbackResult != null) {
      if (callbackResult instanceof QueryResult) {
        QueryResult qr = (QueryResult) callbackResult;
        JsonQueryResult jqr = RestServerUtils.toJsonQueryResult(qr);
        return mapper.writeValueAsString(jqr);
      } else {
        return mapper.writeValueAsString(callbackResult);
      }
    } else {
      if (callback.getErrorResult() != null) {
        return mapper.writeValueAsString(callback.getErrorResult());
      } else {
        return "OK";
      }
    }
  }
}
