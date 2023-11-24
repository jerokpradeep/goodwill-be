package in.codifi.api.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.entity.primary.PredefinedMwEntity;
import in.codifi.api.model.PreDefMwReqModel;
import in.codifi.api.model.ResponseModel;

public interface IPredefinedMwService {

	RestResponse<ResponseModel> getAllPreDedinedMwScrips();

	/**
	 * Method to add the script
	 * 
	 * @author SOWMIYA
	 */
	RestResponse<ResponseModel> addscrip(PredefinedMwEntity predefinedEntity);

	/**
	 * Method to delete the script
	 * 
	 * @author SOWMIYA
	 */
	RestResponse<ResponseModel> deletescrip(PredefinedMwEntity predefinedEntity);

	/**
	 * Method to sort the script
	 * 
	 * @author SOWMIYA
	 */
	RestResponse<ResponseModel> sortMwScrips(PredefinedMwEntity predefinedEntity);

	/**
	 * Method to provide all pre defined MW list
	 * 
	 * @author Dinesh
	 */
	RestResponse<ResponseModel> getPDMwScrips(PreDefMwReqModel pDto);

	/**
	 * Method to get pre defined market watch name
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	RestResponse<ResponseModel> getMwNameList();
	
	/**
	 * Method to get predefined market watch by source
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	RestResponse<ResponseModel> getPrefedinedMwBySource(String source, String userId);

}
