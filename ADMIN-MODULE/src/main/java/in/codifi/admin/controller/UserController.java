package in.codifi.admin.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.controller.spec.UserControllerSpec;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.service.spec.UserServiceSpec;

@Path("/user")
public class UserController implements UserControllerSpec {
	@Inject
	UserServiceSpec service;

	/**
	 * method to get users logged in details
	 * 
	 * @author LOKESH
	 */
	@Override
	public RestResponse<GenericResponse> getUserLoggedInDetails() {
		return service.getUserLoggedInDetails();
	}

	/**
	 * Method to Truncate user logged in details
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> truncateUserLoggedInDetails() {
		return service.truncateUserLoggedInDetails();
	}
}
