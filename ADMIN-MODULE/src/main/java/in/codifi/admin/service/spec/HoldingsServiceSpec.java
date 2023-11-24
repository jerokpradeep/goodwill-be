package in.codifi.admin.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.request.FormDataModel;
import in.codifi.admin.model.request.HoldingsReqModel;
import in.codifi.admin.model.response.GenericResponse;

public interface HoldingsServiceSpec {

	/**
	 * Method to upload holdings file
	 *
	 * @author SOWMIYA
	 *
	 * @param file
	 * @param holdingsType
	 * @return
	 */
	RestResponse<GenericResponse> uploadHoldingsFile(FormDataModel file, String holdingsType);

	/**
	 * Method to insert holdings file
	 *
	 * @author SOWMIYA
	 *
	 * @return
	 */
	RestResponse<GenericResponse> insertHoldingsFile();

//	/**
//	 * Method to load holdings data
//	 *
//	 * @author SOWMIYA
//	 *
//	 * @return
//	 */
//	RestResponse<GenericResponse> loadHoldingsData();

	/**
	 * method to get holdings data from database
	 * 
	 * @author SOWMIYA
	 * @param reqModel
	 * @return
	 */
	RestResponse<GenericResponse> getHoldingsData(HoldingsReqModel reqModel);

	/**
	 * method to get holdings Count
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	RestResponse<GenericResponse> getHoldingsCount();

}
