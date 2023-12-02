package in.codifi.feedback.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.feedback.controller.spec.FeedBackControllerSpec;
import in.codifi.feedback.entity.FeedbackEntity;
import in.codifi.feedback.model.request.FeedbackRequestModel;
import in.codifi.feedback.model.response.GenericResponse;
import in.codifi.feedback.service.spec.FeedBackServiceSpec;

@Path("/feedback")
public class FeedBackController implements FeedBackControllerSpec {

	@Inject
	FeedBackServiceSpec service;

	/**
	 * method to send feedback message
	 * 
	 * @author LOKESH
	 * 
	 * @return
	 */
	public RestResponse<GenericResponse> sendFeedbackMessage(FeedbackEntity entity) {
		return service.sendFeedbackMessge(entity);
	}

	/**
	 * method to get feedback message
	 * 
	 * @author LOKESH
	 * 
	 * @return
	 */
	public RestResponse<GenericResponse> getFeedBackMessage(FeedbackRequestModel reqModel) {
		return service.getFeedBackMessage(reqModel);
	}

}
