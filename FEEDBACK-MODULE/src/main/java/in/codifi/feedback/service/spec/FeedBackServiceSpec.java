package in.codifi.feedback.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.feedback.entity.FeedbackEntity;
import in.codifi.feedback.model.request.FeedbackRequestModel;
import in.codifi.feedback.model.response.GenericResponse;

public interface FeedBackServiceSpec {
	/**
	 * method to send feedback message
	 * 
	 * @author LOKESH
	 * 
	 * @return
	 */

	RestResponse<GenericResponse> sendFeedbackMessge(FeedbackEntity entity);

	/**
	 * method to get feedback message
	 * 
	 * @author LOKESH
	 */
	RestResponse<GenericResponse> getFeedBackMessage(FeedbackRequestModel reqModel);

}
