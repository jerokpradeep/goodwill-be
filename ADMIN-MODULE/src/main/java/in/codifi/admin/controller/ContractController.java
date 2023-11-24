package in.codifi.admin.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.controller.spec.ContractControllerSpec;
import in.codifi.admin.model.response.ExchangeResponseModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.req.model.ContractMasterReqModel;
import in.codifi.admin.service.spec.ContractServiceSpec;

@Path("/contract")
public class ContractController implements ContractControllerSpec {
	@Inject
	ContractServiceSpec service;

	/**
	 * method to get contract master list
	 * 
	 * @author LOKESH
	 * 
	 */
	public RestResponse<GenericResponse> getContractMasterList(ContractMasterReqModel reqModel) {
		return service.getContractMasterList(reqModel);
	}

	/**
	 * method to get newly added list symbol
	 * 
	 * @author LOKESH
	 */
	@Override
	public RestResponse<GenericResponse> getNewlyAddedList() {
		return service.getNewlyAddedList();
	}

	/**
	 * method to get deactivated list
	 * 
	 * @author LOKESH
	 */
	@Override
	public RestResponse<GenericResponse> getDeactivedList() {
		return service.getDeactivatedList();
	}

	/**
	 * Method to add the contract into the master details
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> addContractMaster(ExchangeResponseModel exchangeModel) {
		return service.addContractMaster(exchangeModel);
	}

	/**
	 * Method to get the duplicate symbols list from contract master
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getDuplicateList() {
		return service.getDuplicateList();
	}
}
