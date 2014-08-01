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
import com.stratio.meta.common.result.Result;
import com.stratio.meta.rest.utils.DriverHelper;
import com.stratio.meta.rest.utils.RestResultHandler;

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
  public String postQuery(@FormParam("query") String query, @FormParam("catalog") String catalog)
      throws IOException {
    String queryId = "";

    // SYNCHRONOUS WAY
    // ObjectMapper mapper = new ObjectMapper();
    // try {
    // driver.executeSyncQuery(query, catalog);
    // } catch (UnsupportedException | ParsingException | ValidationException | ExecutionException
    // | ConnectionException e) {
    // return mapper.writeValueAsString(e.getMessage());
    // }
    // return mapper.writeValueAsString(driver.getResult());

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
      return mapper.writeValueAsString(callbackResult);
    } else {
      if (callback.getErrorResult() != null) {
        return mapper.writeValueAsString(callback.getErrorResult());
      } else {
        return mapper.writeValueAsString(callback.getStatus());
      }
    }
  }
}