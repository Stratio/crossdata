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
import com.stratio.meta.common.result.QueryStatus;

/**
 * Root resource (exposed at "/api" path)
 */
@Path("api")
public class MetaApi {
  private DriverHelper driver = DriverHelper.getInstance();
  private RestResultHandler callback = new RestResultHandler();

  // private ArrayList<String> queryIds = new ArrayList<String>();

  /**
   * Method handling HTTP POST requests. The returned object will be sent to the client as
   * "text/plain" media type.
   * 
   * @return String that will be returned as a text/plain response.ººº
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonGenerationException
   */
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_PLAIN)
  public String postQuery(@FormParam("query") String query, @FormParam("catalog") String catalog)
      throws JsonGenerationException, JsonMappingException, IOException {
    // ObjectMapper mapper = new ObjectMapper();
    String queryId = "";
    // try {
    // driver.executeSyncQuery(query, catalog);
    // } catch (UnsupportedException | ParsingException | ValidationException | ExecutionException
    // | ConnectionException e) {
    // return mapper.writeValueAsString(e.getMessage());
    // }
    // return mapper.writeValueAsString(driver.getResult());

    try {
      driver.executeAsyncQuery(query, catalog, callback);
    } catch (UnsupportedException | ParsingException | ValidationException | ExecutionException
        | ConnectionException e) {
      return e.getMessage();
    }
    queryId = driver.getAsyncResult();
    // queryIds.add(queryId);
    return queryId;
  }

  @GET
  @Path("/{queryId}")
  @Produces(MediaType.APPLICATION_JSON)
  public String getQuery(@PathParam("queryId") String queryId) throws JsonGenerationException,
      JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    callback = (RestResultHandler) driver.getCallback(queryId);
    if (callback.getResult() != null)
      return mapper.writeValueAsString(callback.getResult());
    else
      return mapper.writeValueAsString(callback.getStatus());
  }
}
