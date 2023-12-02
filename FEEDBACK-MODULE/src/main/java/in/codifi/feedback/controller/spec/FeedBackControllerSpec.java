package in.codifi.feedback.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.feedback.entity.FeedbackEntity;
import in.codifi.feedback.model.request.FeedbackRequestModel;
import in.codifi.feedback.model.response.GenericResponse;

public interface FeedBackControllerSpec {
	/**
	 * method to send feedback message
	 * 
	 * @author LOKESH
	 * 
	 * @return
	 */
	@Path("/sendFeedback")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> sendFeedbackMessage(FeedbackEntity entity);

	/**
	 * method to get feedback message
	 * 
	 * @author LOKESH
	 * 
	 * @return
	 */
	@Path("/getFeedback")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getFeedBackMessage(FeedbackRequestModel reqModel);

}
