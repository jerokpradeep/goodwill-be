package in.codifi.notify.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.notify.model.request.PopUpGetRequest;
import in.codifi.notify.model.request.PopUpRequest;
import in.codifi.notify.model.response.GenericResponse;

public interface PopUpNotificationServiceSpec {

	/**
	 * method to insert pop-up notification
	 * 
	 * @author LOKESH
	 */

	RestResponse<GenericResponse> insertPopUpNotification(PopUpRequest reqModel);

	/**
	 * method to get pop-up notification
	 * 
	 * @author LOKESH
	 */

	RestResponse<GenericResponse> getPopUpNotification(PopUpGetRequest getReqModel);

}
