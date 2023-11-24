package in.codifi.notify.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.notify.model.request.ConsentRequest;
import in.codifi.notify.model.response.GenericResponse;

public interface ConsentControllerSpec {

	/**
	 * method to send confirm consent
	 * 
	 * @author LOKESH
	 */
	@Path("/send/consent")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> sendConsentNotification(ConsentRequest request);

}
