package in.codifi.api.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.model.MwRequestModel;
import in.codifi.api.model.ResponseModel;
import in.codifi.cache.model.ClinetInfoModel;

public interface IMarketWatchService {

	/**
	 * Load the contract master from data base from Cache
	 * 
	 * @author Gowrisankar
	 * @return
	 */
//	public RestResponse<ResponseModel> loadContractMaster();

	/**
	 * To get scrip details for given user id
	 * 
	 * @author Gowrisankar
	 * @param pDto
	 * @return
	 */
	public RestResponse<ResponseModel> getAllMwScrips(String pUserId);

	/**
	 * Method to get the Scrip for given user id and market watch Id
	 * 
	 * @author Gowrisankar
	 * @param pDto
	 * @param info
	 * @return
	 */
	public RestResponse<ResponseModel> getMWScrips(MwRequestModel pDto, ClinetInfoModel info);

	/**
	 * Method to create the new marketWatch
	 * 
	 * @author dinesh Kumar
	 */
	public RestResponse<ResponseModel> createMW(String pUserId);

	/**
	 * 
	 * Method to Delete expired contract in MW
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	RestResponse<ResponseModel> deleteExpiredContract();

	/**
	 * Method to add the scrip into cache and data baseF
	 * 
	 * @author Dinesh Kumar
	 */
	RestResponse<ResponseModel> addscrip(MwRequestModel parmDto, ClinetInfoModel info);

	/**
	 * Method to delete the scrips from the cache and market watch
	 * 
	 * @author Gowrisankar
	 * @param pDto
	 * @return
	 */
	RestResponse<ResponseModel> deletescrip(MwRequestModel pDto, ClinetInfoModel info);

	/**
	 * 
	 * Method to Sort MW scrips
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param pDto
	 * @return
	 */
	RestResponse<ResponseModel> sortMwScrips(MwRequestModel pDto, ClinetInfoModel info);

	
	public RestResponse<ResponseModel> renameMarketWatch(MwRequestModel pDto, ClinetInfoModel info);

}
