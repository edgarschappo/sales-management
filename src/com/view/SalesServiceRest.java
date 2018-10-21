package com.view;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.controller.SalesController;

/**
 * @author edgar.luis
 */
@Path("/salesService")
public class SalesServiceRest {

	@POST
	@Path("/sendMessage")
	@Produces(MediaType.TEXT_HTML)
	public Response sendMessage(@QueryParam("message") Integer message, @QueryParam("product") String product,
			@QueryParam("quantity") Integer quantity, @QueryParam("value") Float value,
			@QueryParam("operation") OperationType operation) {

		Response response = null;
		try {

			//check if application is paused.
			boolean applicationIsPaused = SalesController.getInstance().applicationIsPaused();

			//if application is paused not process any message
			if (applicationIsPaused) {
				//define response return
				response = Response.status(Response.Status.SERVICE_UNAVAILABLE)
						.entity(ResponseMessages.APPLICATION_PAUSED.toString()).build();
			//if application is available process message
			} else {
				String returnMessage = SalesController.getInstance().processMessage(message, product, operation,
						quantity, value);
				//define response return
				response = Response.status(Response.Status.OK).entity(returnMessage).build();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		return response;
	}
}