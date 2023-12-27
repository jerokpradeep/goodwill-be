package in.codifi.api.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.model.MWReqModel;
import in.codifi.api.model.MwRequestModel;
import in.codifi.api.model.ResponseModel;
import in.codifi.cache.model.ClinetInfoModel;

public interface AdvanceMWTestServiceSpec {

	/**
	 * method to get advance market watch
	 * 
	 * @author SowmiyaThangaraj
	 * @param reqModel
	 * @param info
	 * @return
	 */
	RestResponse<ResponseModel> advanceMW(MWReqModel reqModel, ClinetInfoModel info);

	/**
	 * method to add new scrip
	 * 
	 * @author SowmiyaThangaraj
	 * @param pDto
	 * @param info
	 * @return
	 */
	RestResponse<ResponseModel> addscrip(MwRequestModel pDto, ClinetInfoModel info);

	/**
	 * method to advance market watch scrips
	 * 
	 * @author SowmiyaThangaraj
	 * @param reqModel
	 * @param info
	 * @return
	 */
	RestResponse<ResponseModel> advanceMWScrips(MWReqModel reqModel, ClinetInfoModel info);

	/**
	 * method to delete scrip
	 * 
	 * @author SowmiyaThangaraj
	 * @param pDto
	 * @param info
	 * @return
	 */
	RestResponse<ResponseModel> deletescrip(MwRequestModel pDto, ClinetInfoModel info);

	/**
	 * method to sort market watch scrips
	 * 
	 * @author SowmiyaThangaraj
	 * @param pDto
	 * @param info
	 * @return
	 */
//	RestResponse<ResponseModel> sortMwScrips(MwRequestModel pDto, ClinetInfoModel info);
	RestResponse<ResponseModel> sortMwScrips(MwRequestModel pDto, String userid);

}
