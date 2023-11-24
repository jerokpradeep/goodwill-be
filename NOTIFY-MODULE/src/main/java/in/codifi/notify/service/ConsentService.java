package in.codifi.notify.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.notify.entity.ConsentEntity;
import in.codifi.notify.model.request.ConsentRequest;
import in.codifi.notify.model.response.GenericResponse;
import in.codifi.notify.repository.ConsentRepository;
import in.codifi.notify.service.spec.ConsentServiceSpec;
import in.codifi.notify.utils.AppConstants;
import in.codifi.notify.utils.PrepareResponse;
import io.quarkus.logging.Log;

@ApplicationScoped
public class ConsentService implements ConsentServiceSpec {

	@Inject
	ConsentRepository consentRepository;
	@Inject
	PrepareResponse prepareResponse;

	/**
	 * method to send confirm consent
	 * 
	 * @author LOKESH
	 * @return
	 */

	public RestResponse<GenericResponse> sendConsentNotification(ConsentRequest request) {
		try {
			if (request.getConsent_yes() == 1) {
				ConsentEntity entity = new ConsentEntity();
				entity.setUserId(request.getUserId());
				entity.setMessageId(request.getMessageId());
				entity.setMessageTitle(request.getMessageTitle());
				entity.setSource(request.getSource());
				entity.setDate(request.getDate());
				entity.setDeviceId(request.getDeviceId());
				entity.setIpAddress(request.getIpAddress());
				consentRepository.save(entity);
				return prepareResponse.prepareSuccessResponseObject(entity);
			}
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}
}
