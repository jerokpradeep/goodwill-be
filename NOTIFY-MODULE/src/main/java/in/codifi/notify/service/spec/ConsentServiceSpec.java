package in.codifi.notify.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.notify.model.request.ConsentRequest;
import in.codifi.notify.model.response.GenericResponse;

public interface ConsentServiceSpec {

	/**
	 * method to send confirm consent
	 * 
	 * @author LOKESH
	 */

	RestResponse<GenericResponse> sendConsentNotification(ConsentRequest request);

}
