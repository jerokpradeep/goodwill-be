package in.codifi.admin.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.controller.spec.ClientControllerSpec;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.req.model.FormDataModel;
import in.codifi.admin.service.spec.ClientServiceSpec;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.utility.PrepareResponse;

@Path("/client")
public class ClientController implements ClientControllerSpec {

	@Inject
	ClientServiceSpec clientServiceSpec;
	@Inject
	PrepareResponse prepareResponse;

	/**
	 * Method to create existing user in keycloak
	 * 
	 * @author Dinesh Kumar
	 * @param file
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> createUsers() {
		return clientServiceSpec.createUsers();
	}

	/**
	 * method to upload a file
	 * 
	 * @author SOWMIYA
	 */
	public RestResponse<GenericResponse> uploadClientDetails(FormDataModel file) {
		if (file == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
		return clientServiceSpec.uploadClientDetails(file);

	}

}
