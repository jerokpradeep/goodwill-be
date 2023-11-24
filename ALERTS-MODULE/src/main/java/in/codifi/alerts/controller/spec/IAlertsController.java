package in.codifi.alerts.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.alerts.entity.primary.AlertsEntity;
import in.codifi.alerts.model.request.RequestModel;
import in.codifi.alerts.model.response.GenericResponse;

public interface IAlertsController {

	/**
	 * method to create Alert
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/create")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> createAlert(RequestModel reqModel);

	/**
	 * method to get alert Details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getAlertDetails();

	/**
	 * method to update Alert
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/update")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> updateAlert(AlertsEntity alertsEntity);

	/**
	 * method to delete Alert
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/delete/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> deleteAlert(@PathParam("id") int id);

	/**
	 * method to update Trigger Status
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/updateTriggerStatus")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> updateTriggerStatus(@QueryParam("alertId") int alertId);
}
