package in.codifi.client.service.spec;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.client.model.request.PreferenceReqModel;
import in.codifi.client.model.response.GenericResponse;

public interface IPreferenceService {

	/**
	 * Method to get User Preference
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	RestResponse<GenericResponse> getPreferenceForWeb(ClinetInfoModel info);

	/**
	 * Method to update User Preference for web
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	RestResponse<GenericResponse> updatePreferenceForWeb(PreferenceReqModel reqModel, ClinetInfoModel info);

	/**
	 * Method to reset Preference foe web
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	RestResponse<GenericResponse> resetPreferenceForWeb(ClinetInfoModel info);

	/**
	 * Method to get User Preference for mobile
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	RestResponse<GenericResponse> getPreferenceForMobile(ClinetInfoModel info);

	/**
	 * Method to update Preference for Mobile
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	RestResponse<GenericResponse> updatePreferenceForMobile(PreferenceReqModel reqModel, ClinetInfoModel info);

	/**
	 * method to reset Preference for mob
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	RestResponse<GenericResponse> resetPreferenceForMobile(ClinetInfoModel info);

	/**
	 * Method to load user preference
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	RestResponse<GenericResponse> loadUserPreference();

	/**
	 * Method to load master preferences
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	RestResponse<GenericResponse> loadMasterPreference();

	/**
	 * Method to update multiple preference for mobile
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param model
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> updatePreferenceListForMobile(List<PreferenceReqModel> model, ClinetInfoModel info);

}
