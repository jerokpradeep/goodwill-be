package in.codifi.admin.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.req.model.FormDataModel;

public interface ClientServiceSpec {

	/**
	 * Method to create existing user in keycloak
	 * 
	 * @author SOWMIYA
	 * @param file
	 * @return
	 */
	RestResponse<GenericResponse> createUsers();

	/**
	 * method to upload client details
	 * 
	 * @author SOWMIYA
	 * @param file
	 * @return
	 */
	RestResponse<GenericResponse> uploadClientDetails(FormDataModel file);

}
