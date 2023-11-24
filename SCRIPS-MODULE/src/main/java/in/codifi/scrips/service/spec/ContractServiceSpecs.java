package in.codifi.scrips.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.scrips.model.response.GenericResponse;

public interface ContractServiceSpecs {

	/**
	 * Method to load contract master
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	RestResponse<GenericResponse> loadContractMaster();

	/**
	 * Delete Expired contract manually
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	RestResponse<GenericResponse> deleteExpiredContract();

	/**
	 * 
	 * Method to Delete BSE Contract
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	RestResponse<GenericResponse> deleteBSEContract();

	/**
	 * Method to get reload contract master file from server
	 * 
	 * @author Nesan
	 *
	 * @return
	 */
	RestResponse<GenericResponse> reloadContractMasterFile();

	/**
	 * Method to load PNL Lot
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	RestResponse<GenericResponse> loadPnlLotSize();

}
