package in.codifi.scrips.controller.spec;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.scrips.model.response.GenericResponse;

public interface ContractControllerSpec {

	@Path("/reloadcache")
	@POST
	RestResponse<GenericResponse> loadContractMaster();

	/**
	 * 
	 * Method to Delete expired contract
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Path("/delete/expired")
	@POST
	RestResponse<GenericResponse> deleteExpiredContract();

	/**
	 * Delete Delete BSE contract
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Path("/delete/bse")
	@GET
	RestResponse<GenericResponse> deleteBSEContract();

	/**
	 * Method to get reload - contract master file from server
	 * 
	 * @author Nesan
	 *
	 * @return
	 */
	@Path("/reload/contractmaster")
	@GET
	RestResponse<GenericResponse> reloadContractMasterFile();

}
