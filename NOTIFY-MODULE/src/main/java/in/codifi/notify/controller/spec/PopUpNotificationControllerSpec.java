package in.codifi.notify.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.notify.model.request.PopUpGetRequest;
import in.codifi.notify.model.request.PopUpRequest;
import in.codifi.notify.model.response.GenericResponse;

public interface PopUpNotificationControllerSpec {

	/**
	 * method to insert pop-up notification
	 * 
	 * @author LOKESH
	 */
	@Path("/insert/popup")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> insertPopUpNotification(PopUpRequest reqModel);

	/**
	 * method to get pop-up notification
	 * 
	 * @author LOKESH
	 */
	@Path("/get/popup")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getPopUpNotification(PopUpGetRequest getReqModel);

}
