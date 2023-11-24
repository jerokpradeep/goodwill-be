package in.codifi.scrips.service.spec;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.scrips.model.request.GetContractInfoReqModel;
import in.codifi.scrips.model.request.SearchScripReqModel;
import in.codifi.scrips.model.request.SecurityInfoReqModel;
import in.codifi.scrips.model.response.GenericResponse;

@ApplicationScoped
public interface ScripsServiceSpecs {

	/**
	 * Method to get all scrips
	 * 
	 * @author Dinesh Kumar
	 * @param reqModel
	 * @return
	 */
	RestResponse<GenericResponse> getScrips(SearchScripReqModel reqModel);

	/**
	 * Method to get Contract Info
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqModel
	 * @return
	 */
	RestResponse<GenericResponse> getContractInfo(GetContractInfoReqModel reqModel);

	/*
	 * method to get security information
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	RestResponse<GenericResponse> getSecurityInfo(SecurityInfoReqModel model, ClinetInfoModel info);
}
