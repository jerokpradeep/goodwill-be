package in.codifi.notify.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.notify.controller.spec.PopUpNotificationControllerSpec;
import in.codifi.notify.model.request.PopUpGetRequest;
import in.codifi.notify.model.request.PopUpRequest;
import in.codifi.notify.model.response.GenericResponse;
import in.codifi.notify.service.spec.PopUpNotificationServiceSpec;

@Path("/notify")
public class PopUpNotificationController implements PopUpNotificationControllerSpec {
	@Inject
	PopUpNotificationServiceSpec service;

	/**
	 * method to insert pop-up notification user
	 * 
	 * @author LOKESH
	 */
	@Override
	public RestResponse<GenericResponse> insertPopUpNotification(PopUpRequest reqModel) {
		return service.insertPopUpNotification(reqModel);
	}

	/**
	 * method to get pop-up notification user
	 * 
	 * @author LOKESH
	 */
	@Override
	public RestResponse<GenericResponse> getPopUpNotification(PopUpGetRequest getReqModel) {
		return service.getPopUpNotification(getReqModel);
	}

}