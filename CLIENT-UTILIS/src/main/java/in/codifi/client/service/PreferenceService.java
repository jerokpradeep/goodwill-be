package in.codifi.client.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.cache.model.PreferenceModel;
import in.codifi.client.config.HazelcastConfig;
import in.codifi.client.entity.primary.PreferenceEntity;
import in.codifi.client.entity.primary.PreferenceMappingEntity;
import in.codifi.client.model.request.PreferenceReqModel;
import in.codifi.client.model.response.GenericResponse;
import in.codifi.client.repository.PreferenceMappingRepository;
import in.codifi.client.repository.PreferenceRepository;
import in.codifi.client.service.spec.IPreferenceService;
import in.codifi.client.utilis.AppConstants;
import in.codifi.client.utilis.PrepareResponse;
import in.codifi.client.utilis.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class PreferenceService implements IPreferenceService {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	PreferenceRepository preferenceRepository;
	@Inject
	PreferenceMappingRepository mappingRepository;

	/**
	 * method to get User Preference
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPreferenceForWeb(ClinetInfoModel info) {
		try {

			/** to get the data into cache **/
			List<PreferenceModel> userPreferenceDetails = HazelcastConfig.getInstance().getPerference()
					.get(info.getUserId() + "_" + AppConstants.SOURCE_WEB);
			userPreferenceDetails = getPreferenceBySource(userPreferenceDetails, AppConstants.SOURCE_WEB, info);

			if (StringUtil.isListNotNullOrEmpty(userPreferenceDetails))
				return prepareResponse.prepareSuccessResponseObject(userPreferenceDetails);

			/** If preference does't exist for user, set default preference **/
			userPreferenceDetails = setClientPreferences(info.getUserId(), AppConstants.SOURCE_WEB);
			if (StringUtil.isListNotNullOrEmpty(userPreferenceDetails))
				return prepareResponse.prepareSuccessResponseObject(userPreferenceDetails);

		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get User Preference by source
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	private List<PreferenceModel> getPreferenceBySource(List<PreferenceModel> userPreferenceDetails, String source,
			ClinetInfoModel info) {
		List<PreferenceModel> clientPreference = new ArrayList<>();
		try {
			/** If exist in cache return **/
			if (StringUtil.isListNotNullOrEmpty(userPreferenceDetails))
				return userPreferenceDetails;
			/** If does not exist in cache load it from DB **/
			clientPreference = loadUserPreferences(info.getUserId(), source);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return clientPreference;
	}

	/**
	 * method to load User Preference by userId , source
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	private List<PreferenceModel> loadUserPreferences(String userId, String source) {
		List<PreferenceModel> preferenceModel = new ArrayList<>();
		try {
			List<PreferenceEntity> preferenceEntities = preferenceRepository.findByUserIdAndSource(userId, source);
			if (StringUtil.isListNotNullOrEmpty(preferenceEntities)) {
				preferenceModel = prepareClientPreference(preferenceEntities);
				HazelcastConfig.getInstance().getPerference().remove(userId + "_" + source);
				HazelcastConfig.getInstance().getPerference().put(userId + "_" + source, preferenceModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return preferenceModel;
	}

	/**
	 * method to prepare client preference
	 * 
	 * @author sowmiya
	 * @param clientPreference
	 * @return
	 */
	private List<PreferenceModel> prepareClientPreference(List<PreferenceEntity> clientPreference) {
		List<PreferenceModel> preferenceModel = new ArrayList<>();
		try {
			for (PreferenceEntity entity : clientPreference) {
				PreferenceModel model = new PreferenceModel();
				model.setSource(entity.getSource());
				model.setTag(entity.getTag());
				model.setUserId(entity.getUserId());
				model.setValue(entity.getValue());
				preferenceModel.add(model);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return preferenceModel;
	}

	/**
	 * 
	 * Method to load and save default preference into client preference cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param masterPreferenceEntities
	 * @param userId
	 * @param source
	 * @return
	 */
	private List<PreferenceModel> setClientPreferences(String userId, String source) {
		List<PreferenceModel> userPreferenceResponse = new ArrayList<>();
		try {
			List<PreferenceEntity> userPreference = new ArrayList<>();
			/** get default preferences **/
			List<PreferenceMappingEntity> masterPreferenceEntities = getMasterPreference(source);
			for (PreferenceMappingEntity entity : masterPreferenceEntities) {
				PreferenceEntity preference = new PreferenceEntity();
				preference.setTag(entity.getTag());
				preference.setValue(entity.getValue());
				preference.setSource(entity.getSource());
				preference.setUserId(userId);
				userPreference.add(preference);
			}

			if (StringUtil.isListNotNullOrEmpty(userPreference)) {
				userPreference = preferenceRepository.saveAll(userPreference);
				userPreferenceResponse = prepareClientPreference(userPreference);
				HazelcastConfig.getInstance().getPerference().remove(userId + "_" + source);
				HazelcastConfig.getInstance().getPerference().put(userId + "_" + source, userPreferenceResponse);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return userPreferenceResponse;
	}

	/**
	 * 
	 * Method to get master preference details
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param source
	 * @return
	 */
	private List<PreferenceMappingEntity> getMasterPreference(String source) {
		List<PreferenceMappingEntity> masterPreferenceEntities = new ArrayList<>();
		try {
			masterPreferenceEntities = HazelcastConfig.getInstance().getMasterPerference()
					.get(AppConstants.MASTER_PREFERENCES + source);
			if (StringUtil.isListNotNullOrEmpty(masterPreferenceEntities))
				return masterPreferenceEntities;

			masterPreferenceEntities = mappingRepository.findBySource(source);
			if (StringUtil.isListNotNullOrEmpty(masterPreferenceEntities)) {
				HazelcastConfig.getInstance().getMasterPerference().remove(AppConstants.MASTER_PREFERENCES + source);
				HazelcastConfig.getInstance().getMasterPerference().put(AppConstants.MASTER_PREFERENCES + source,
						masterPreferenceEntities);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return masterPreferenceEntities;
	}

	/**
	 * Method to update User Preference for web
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> updatePreferenceForWeb(PreferenceReqModel reqModel, ClinetInfoModel info) {
		try {
			if (StringUtil.isNullOrEmpty(reqModel.getValue()) || StringUtil.isNullOrEmpty(reqModel.getTag()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			preferenceRepository.updatePreferenceDetails(reqModel.getTag(), reqModel.getValue(), info.getUserId());
			preferenceRepository.flush();
			List<PreferenceModel> updatedPreferenceDetails = loadUserPreferences(info.getUserId(),
					AppConstants.SOURCE_WEB);
			return prepareResponse.prepareSuccessResponseObject(updatedPreferenceDetails);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to reset Preference foe web
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> resetPreferenceForWeb(ClinetInfoModel info) {
		List<PreferenceModel> userPreferenceDetails = new ArrayList<>();
		try {
			Long deleteOldPreference = preferenceRepository.deleteByUserIdAndSource(info.getUserId(),
					AppConstants.SOURCE_WEB);
			if (deleteOldPreference > 0) {
				userPreferenceDetails = setClientPreferences(info.getUserId(), AppConstants.SOURCE_WEB);
				return prepareResponse.prepareSuccessResponseObject(userPreferenceDetails);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get User Preference for mobile
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPreferenceForMobile(ClinetInfoModel info) {
		try {
			/** to get the data into cache **/
			List<PreferenceModel> userPreferenceDetails = HazelcastConfig.getInstance().getPerference()
					.get(info.getUserId() + "_" + AppConstants.SOURCE_MOB);

			/** To get preference by source **/
			userPreferenceDetails = getPreferenceBySource(userPreferenceDetails, AppConstants.SOURCE_MOB, info);

			if (StringUtil.isListNotNullOrEmpty(userPreferenceDetails))
				return prepareResponse.prepareSuccessResponseObject(userPreferenceDetails);

			/** If preference does't exist for user, set default preference **/
			userPreferenceDetails = setClientPreferences(info.getUserId(), AppConstants.SOURCE_MOB);
			if (StringUtil.isListNotNullOrEmpty(userPreferenceDetails))
				return prepareResponse.prepareSuccessResponseObject(userPreferenceDetails);

		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

	/**
	 * Method to update Preference for Mobile
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> updatePreferenceForMobile(PreferenceReqModel reqModel, ClinetInfoModel info) {
		try {
			if (StringUtil.isNullOrEmpty(reqModel.getValue()) || StringUtil.isNullOrEmpty(reqModel.getTag()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			preferenceRepository.updatePreferenceDetails(reqModel.getTag(), reqModel.getValue(), info.getUserId());
			preferenceRepository.flush();
			List<PreferenceModel> updatedPreferenceDetails = loadUserPreferences(info.getUserId(),
					AppConstants.SOURCE_MOB);
			return prepareResponse.prepareSuccessResponseObject(updatedPreferenceDetails);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to update multiple preference for mobile
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param model
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> updatePreferenceListForMobile(List<PreferenceReqModel> model,
			ClinetInfoModel info) {
		try {
			for (PreferenceReqModel reqModel : model) {
				if (StringUtil.isNullOrEmpty(reqModel.getValue()) || StringUtil.isNullOrEmpty(reqModel.getTag()))
					return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			}

			for (PreferenceReqModel reqModel : model) {
				preferenceRepository.updateMobilePreferenceDetails(reqModel.getTag(), reqModel.getValue(),
						info.getUserId(), AppConstants.SOURCE_MOB);
			}
			List<PreferenceModel> updatedPreferenceDetails = loadUserPreferences(info.getUserId(),
					AppConstants.SOURCE_MOB);
			return prepareResponse.prepareSuccessResponseObject(updatedPreferenceDetails);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to reset Preference for mob
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> resetPreferenceForMobile(ClinetInfoModel info) {
		List<PreferenceModel> userPreferenceDetails = new ArrayList<>();
		try {
			Long deleteOldPreference = preferenceRepository.deleteByUserIdAndSource(info.getUserId(),
					AppConstants.SOURCE_MOB);
			if (deleteOldPreference > 0) {
				userPreferenceDetails = setClientPreferences(info.getUserId(), AppConstants.SOURCE_MOB);
				return prepareResponse.prepareSuccessResponseObject(userPreferenceDetails);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to load user preference
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> loadUserPreference() {
		try {
			List<PreferenceEntity> clientPreference = new ArrayList<>();
			List<String> userIdList = preferenceRepository.getDistinctUserId();
			if (StringUtil.isListNotNullOrEmpty(userIdList)) {
				HazelcastConfig.getInstance().getPerference().clear();
				for (String userId : userIdList) {
					List<PreferenceModel> webList = new ArrayList<>();
					List<PreferenceModel> mobList = new ArrayList<>();
					clientPreference = preferenceRepository.findByUserId(userId);
					List<PreferenceModel> model = prepareClientPreference(clientPreference);
					for (PreferenceModel entity : model) {
						if (entity.getSource().equalsIgnoreCase(AppConstants.SOURCE_WEB)) {
							webList.add(entity);
						} else if (entity.getSource().equalsIgnoreCase(AppConstants.SOURCE_MOB)) {
							mobList.add(entity);
						}
					}
					if (StringUtil.isListNotNullOrEmpty(mobList)) {
						HazelcastConfig.getInstance().getPerference().remove(userId + "_" + AppConstants.SOURCE_MOB);
						HazelcastConfig.getInstance().getPerference().put(userId + "_" + AppConstants.SOURCE_MOB,
								mobList);
					}
					if (StringUtil.isListNotNullOrEmpty(webList)) {
						HazelcastConfig.getInstance().getPerference().remove(userId + "_" + AppConstants.SOURCE_WEB);
						HazelcastConfig.getInstance().getPerference().put(userId + "_" + AppConstants.SOURCE_WEB,
								webList);
					}
				}
				return prepareResponse.prepareSuccessResponseMessage(AppConstants.CACHE_LOAD_SUCCESS);
			}
			return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to load master preferences
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> loadMasterPreference() {
		List<PreferenceMappingEntity> masterPreferenceEntities = new ArrayList<>();
		try {
			List<String> sourceList = mappingRepository.getDistinctSource();

			if (StringUtil.isListNotNullOrEmpty(sourceList)) {
				for (String source : sourceList) {
					masterPreferenceEntities = mappingRepository.findBySource(source);
					if (StringUtil.isListNotNullOrEmpty(masterPreferenceEntities)) {
						HazelcastConfig.getInstance().getMasterPerference()
								.remove(AppConstants.MASTER_PREFERENCES + source);
						HazelcastConfig.getInstance().getMasterPerference()
								.put(AppConstants.MASTER_PREFERENCES + source, masterPreferenceEntities);
					}
				}
				return prepareResponse.prepareSuccessResponseMessage(AppConstants.CACHE_LOAD_SUCCESS);
			}
			return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
