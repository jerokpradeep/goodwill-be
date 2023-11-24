package in.codifi.api.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.model.MWReqModel;
import in.codifi.api.model.MwRequestModel;
import in.codifi.api.model.ResponseModel;
import in.codifi.cache.model.ClinetInfoModel;

public interface AdvanceMWServiceSpec {

	/**
	 * method to get advance market watch
	 * 
	 * @author sowmiya
	 * @param reqModel
	 * @param info
	 * @return
	 */
	public RestResponse<ResponseModel> advanceMW(MWReqModel reqModel, ClinetInfoModel info);

	/**
	 * method to get advance market watch scrips
	 * 
	 * @author sowmiya
	 * @param reqModel
	 * @param info
	 * @return
	 */
	public RestResponse<ResponseModel> advanceMWScrips(MWReqModel reqModel, ClinetInfoModel info);

	/**
	 * 
	 * Method to delete the scrips from the cache and market watch
	 * 
	 * @author Gowrisankar
	 * @param pDto
	 * @return
	 */
	public RestResponse<ResponseModel> deletescrip(MwRequestModel pDto, ClinetInfoModel info);

	/**
	 * Method to add the scrip into cache and data base
	 * 
	 * @author Gowrisankar
	 * @param pDto
	 * @return
	 */
	public RestResponse<ResponseModel> addscrip(MwRequestModel pDto, ClinetInfoModel info);

	/**
	 * 
	 * Method to Sort MW scrips
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param pDto
	 * @return
	 */
	public RestResponse<ResponseModel> sortMwScrips(MwRequestModel pDto, ClinetInfoModel info);

}
