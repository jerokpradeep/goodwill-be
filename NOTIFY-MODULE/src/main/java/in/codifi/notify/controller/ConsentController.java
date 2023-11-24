package in.codifi.notify.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.notify.controller.spec.ConsentControllerSpec;
import in.codifi.notify.model.request.ConsentRequest;
import in.codifi.notify.model.response.GenericResponse;
import in.codifi.notify.service.spec.ConsentServiceSpec;

@Path("/confirm")
public class ConsentController implements ConsentControllerSpec {
	@Inject
	ConsentServiceSpec service;

	/**
	 * method to send confirm consent
	 * 
	 * @author LOKESH
	 */
	public RestResponse<GenericResponse> sendConsentNotification(ConsentRequest request) {
		return service.sendConsentNotification(request);
	}

}
