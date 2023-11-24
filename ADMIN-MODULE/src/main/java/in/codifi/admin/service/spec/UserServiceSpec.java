package in.codifi.admin.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.response.GenericResponse;

public interface UserServiceSpec {

	/**
	 * method to get users logged in details
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> getUserLoggedInDetails();

	/**
	 * Method to Truncate user logged in details
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> truncateUserLoggedInDetails();
}
