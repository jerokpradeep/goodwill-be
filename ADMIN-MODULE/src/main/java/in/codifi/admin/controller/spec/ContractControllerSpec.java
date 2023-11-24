package in.codifi.admin.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.response.ExchangeResponseModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.req.model.ContractMasterReqModel;

public interface ContractControllerSpec {
	/**
	 * method to get contract master list
	 * 
	 * @author LOKESH
	 * @param reqModel
	 * @return
	 */
	@Path("/get")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getContractMasterList(ContractMasterReqModel reqModel);

	/**
	 * method to get newly added symbol list
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Path("/get/newlyadd")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getNewlyAddedList();

	/**
	 * method to get deactivated symbol list
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Path("/get/deactivated")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getDeactivedList();

	/**
	 * Method to add the contract into the master details
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Path("/addContractMaster")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> addContractMaster(ExchangeResponseModel exchangeModel);

	/**
	 * Method to get the duplicate symbols list from contract master
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Path("/getDuplicateList")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getDuplicateList();
}
