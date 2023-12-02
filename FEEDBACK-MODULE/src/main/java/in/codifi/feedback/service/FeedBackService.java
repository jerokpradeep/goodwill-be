package in.codifi.feedback.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.feedback.entity.FeedbackEntity;
import in.codifi.feedback.model.request.FeedbackRequestModel;
import in.codifi.feedback.model.response.GenericResponse;
import in.codifi.feedback.repository.FeedBackRepository;
import in.codifi.feedback.service.spec.FeedBackServiceSpec;
import in.codifi.feedback.utils.AppConstants;
import in.codifi.feedback.utils.PrepareResponse;
import in.codifi.feedback.utils.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class FeedBackService implements FeedBackServiceSpec {
	@Inject
	FeedBackRepository feedbackrepository;
	@Inject
	PrepareResponse prepareResponse;

	/**
	 * method to send feedback message
	 * 
	 * @author LOKESH
	 * @return
	 */

	public RestResponse<GenericResponse> sendFeedbackMessge(FeedbackEntity entity) {
		try {
			if (StringUtil.isNotNullOrEmpty(entity.getComments())) {
				FeedbackEntity feedbackEntity = feedbackrepository.save(entity);
				if (feedbackEntity != null) {
					return prepareResponse.prepareSuccessResponseObject(feedbackEntity);
				}
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);

			}
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

	/**
	 * method to get feedback message
	 * 
	 * @author LOKESH
	 */
	@Override
	public RestResponse<GenericResponse> getFeedBackMessage(FeedbackRequestModel reqModel) {
		try {
			if (reqModel.getFromDate() != null && reqModel.getToDate() != null) {
				List<FeedbackEntity> getmessage = feedbackrepository.getFeedBackMessageWithDate(reqModel.getFromDate(),
						reqModel.getToDate());
				if (StringUtil.isListNotNullOrEmpty(getmessage)) {
					return prepareResponse.prepareSuccessResponseObject(getmessage);
				} else {
					return prepareResponse.prepareSuccessResponseObject(AppConstants.NO_RECORDS_FOUND);
				}
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
