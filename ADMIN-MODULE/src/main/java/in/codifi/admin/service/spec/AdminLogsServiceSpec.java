package in.codifi.admin.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.response.AccesslogResponseModel;
import in.codifi.admin.model.response.GenericResponse;

public interface AdminLogsServiceSpec {

	/**
	 * Method to get the total logged in details for past days
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> userLogDetails();

	/**
	 * method to get the user based records from the data base (TOP 10 USER)
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> getUserbasedRecords(AccesslogResponseModel accessModel);

	/**
	 * method to get the user based records from the data base (TOP 10 USER)
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> getUrlBasedRecords();

	/**
	 * Method to get last 12 hour login count
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> getLast12hourLoginCount();

	/**
	 * Method to get distinct url for drop down
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> getDistinctUrl();

	/**
	 * method to get user logged in details
	 * 
	 * @author SowmiyaThangaraj
	 * @return
	 */
	RestResponse<GenericResponse> getUserLoggedInDetails();

}
