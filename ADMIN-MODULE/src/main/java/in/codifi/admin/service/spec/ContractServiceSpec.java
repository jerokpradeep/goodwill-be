package in.codifi.admin.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.response.ExchangeResponseModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.req.model.ContractMasterReqModel;

public interface ContractServiceSpec {

	/**
	 * method to get contract master list
	 * 
	 * @author LOKESH
	 * @param reqModel
	 * @return
	 */
	RestResponse<GenericResponse> getContractMasterList(ContractMasterReqModel reqModel);

	/**
	 * method to get newly added symbol list
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> getNewlyAddedList();

	/**
	 * method to get deactivated list
	 * 
	 * @author LOKESH
	 */
	RestResponse<GenericResponse> getDeactivatedList();

	/**
	 * Method to add the contract into the master details
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> addContractMaster(ExchangeResponseModel exchangeModel);

	/**
	 * Method to get the duplicate symbols list from contract master
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> getDuplicateList();
}
