package in.codifi.client.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.cache.model.ContractMasterModel;
import in.codifi.client.config.HazelcastConfig;
import in.codifi.client.entity.primary.PinStartBarEntity;
import in.codifi.client.entity.primary.PinStartBarMappingEntity;
import in.codifi.client.model.request.PinToStartbarModel;
import in.codifi.client.model.response.GenericResponse;
import in.codifi.client.repository.PinStartBarMappingRepository;
import in.codifi.client.repository.PinStartBarRepository;
import in.codifi.client.service.spec.IPinStartBarService;
import in.codifi.client.utilis.AppConstants;
import in.codifi.client.utilis.PrepareResponse;
import in.codifi.client.utilis.StringUtil;

@ApplicationScoped
public class PinStartBarService implements IPinStartBarService {
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	PinStartBarRepository pinStartBarRepository;
	@Inject
	PinStartBarMappingRepository mappingRepository;

	/**
	 * Method to get pin to start bar details
	 *
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPinToStartBar(ClinetInfoModel info) {
		try {
			List<PinStartBarEntity> pintostartbar = HazelcastConfig.getInstance().getPinTostartbar()
					.get(info.getUserId() + "_" + AppConstants.SOURCE_WEB);

			if (StringUtil.isListNotNullOrEmpty(pintostartbar))
				return prepareResponse.prepareSuccessResponseObject(pintostartbar);

			pintostartbar = pinStartBarRepository.findByUserIdAndSource(info.getUserId(), AppConstants.SOURCE_WEB);
			if (StringUtil.isListNotNullOrEmpty(pintostartbar)) {
				HazelcastConfig.getInstance().getPinTostartbar().put(info.getUserId() + "_" + AppConstants.SOURCE_WEB,
						pintostartbar);
			} else {
				/** to load default value **/
				pintostartbar = loadDeafultStartBar(info.getUserId(), AppConstants.SOURCE_WEB);
			}
			return prepareResponse.prepareSuccessResponseObject(pintostartbar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to load pin start bar data into cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
//	public RestResponse<GenericResponse> loadPinToStartBarIntoCache() {
//		try {
//			List<String> userIdList = pinStartBarRepository.getDistinctUserId();
//			HazelcastConfig.getInstance().getPinTostartbar().clear();
//			for (String userId : userIdList) {
//				List<PinStartBarEntity> pintostartbarEntity = pinStartBarRepository.findByUserId(userId);
//				HazelcastConfig.getInstance().getPinTostartbar().put(userId, pintostartbarEntity);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
//
//	}

	/**
	 * method to load pin to start bar
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public RestResponse<GenericResponse> loadPinToStartBar() {
		try {
			List<String> userIdList = pinStartBarRepository.getDistinctUserId();
			if (userIdList != null && userIdList.size() > 0) {
				HazelcastConfig.getInstance().getPinTostartbar().clear();
				for (String userId : userIdList) {
					List<PinStartBarEntity> webList = new ArrayList<>();
					List<PinStartBarEntity> mobList = new ArrayList<>();
					webList = pinStartBarRepository.findByUserIdAndSource(userId, AppConstants.SOURCE_WEB);
					mobList = pinStartBarRepository.findByUserIdAndSource(userId, AppConstants.SOURCE_MOB);

					if (StringUtil.isListNotNullOrEmpty(mobList))
						HazelcastConfig.getInstance().getPinTostartbar().put(userId + "_" + AppConstants.SOURCE_MOB,
								mobList);
					if (StringUtil.isListNotNullOrEmpty(webList))
						HazelcastConfig.getInstance().getPinTostartbar().put(userId + "_" + AppConstants.SOURCE_WEB,
								webList);

				}
				return prepareResponse.prepareSuccessResponseMessage(AppConstants.CACHE_LOAD_SUCCESS);
			}
			return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get pin to start bar for mobile
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public RestResponse<GenericResponse> getPinToStartBarForMob(ClinetInfoModel info) {
		try {
			List<PinStartBarEntity> pintostartbar = HazelcastConfig.getInstance().getPinTostartbar()
					.get(info.getUserId() + "_" + AppConstants.SOURCE_MOB);

			if (StringUtil.isListNotNullOrEmpty(pintostartbar))
				return prepareResponse.prepareSuccessResponseObject(pintostartbar);

			pintostartbar = pinStartBarRepository.findByUserIdAndSource(info.getUserId(), AppConstants.SOURCE_MOB);
			if (StringUtil.isListNotNullOrEmpty(pintostartbar)) {
				HazelcastConfig.getInstance().getPinTostartbar().put(info.getUserId() + "_" + AppConstants.SOURCE_MOB,
						pintostartbar);
			} else {
				/** to load default value **/
				pintostartbar = loadDeafultStartBar(info.getUserId(), AppConstants.SOURCE_MOB);
			}
			return prepareResponse.prepareSuccessResponseObject(pintostartbar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

	/**
	 * Method to load for user
	 * 
	 * @author Dinesh Kumar
	 * @param userId
	 * @return
	 */
	public RestResponse<GenericResponse> loadPinToStartBarIntoCacheForUser(String userId, String source) {
		try {

			List<PinStartBarEntity> entities = new ArrayList<>();
			entities = pinStartBarRepository.findByUserIdAndSource(userId, source);
			HazelcastConfig.getInstance().getPinTostartbar().remove(userId + "_" + source);
			if (StringUtil.isListNotNullOrEmpty(entities))
				HazelcastConfig.getInstance().getPinTostartbar().put(userId + "_" + source, entities);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

	/**
	 * Method to load default value
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @return
	 */
	public List<PinStartBarEntity> loadDeafultStartBar(String userId, String source) {

		List<PinStartBarMappingEntity> mappingEntities = mappingRepository.findAll();
		List<PinStartBarEntity> pinStartBarEntities = new ArrayList<>();
		for (PinStartBarMappingEntity masterData : mappingEntities) {
			PinStartBarEntity pinStartBarEntity = new PinStartBarEntity();
			pinStartBarEntity.setToken(masterData.getToken());
			pinStartBarEntity.setExchange(masterData.getExchange());
			pinStartBarEntity.setSymbol(masterData.getSymbol());
			pinStartBarEntity.setFormattedInsName(masterData.getSymbol());
			pinStartBarEntity.setTradingSymbol(masterData.getSymbol());
			pinStartBarEntity.setSortOrder(masterData.getSortOrder());
			pinStartBarEntity.setUserId(userId);
			pinStartBarEntity.setSegment(masterData.getSegment());
			pinStartBarEntity.setSource(source);
			pinStartBarEntities.add(pinStartBarEntity);
		}
		if (StringUtil.isListNotNullOrEmpty(pinStartBarEntities)) {
			pinStartBarRepository.saveAll(pinStartBarEntities);
			HazelcastConfig.getInstance().getPinTostartbar().put(userId + "_" + source, pinStartBarEntities);
		}

		return pinStartBarEntities;
	}

	/**
	 * Method to add pin bar to start bar details
	 *
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> addPinToStartBar(PinToStartbarModel model, ClinetInfoModel info) {
		try {
			PinStartBarEntity pinStartBarEntity = new PinStartBarEntity();

			if (!validateReq(model))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			List<PinStartBarEntity> dbData = pinStartBarRepository.findByUserIdAndSortOrderAndSource(info.getUserId(),
					model.getSortOrder(), AppConstants.SOURCE_WEB);
			if (StringUtil.isListNullOrEmpty(dbData))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_DATA);

			ContractMasterModel masterData = HazelcastConfig.getInstance().getContractMaster()
					.get(model.getExchange() + "_" + model.getToken());
			if (masterData == null)
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
			pinStartBarEntity.setId(dbData.get(0).getId());
			pinStartBarEntity.setToken(masterData.getToken());
			pinStartBarEntity.setExchange(masterData.getExch());
			pinStartBarEntity.setSymbol(masterData.getSymbol());
			pinStartBarEntity.setSegment(masterData.getSegment());
			pinStartBarEntity.setPdc(masterData.getPdc());
			pinStartBarEntity.setFormattedInsName(masterData.getFormattedInsName());
			pinStartBarEntity.setTradingSymbol(masterData.getTradingSymbol());
			pinStartBarEntity.setExpiry(masterData.getExpiry());
			pinStartBarEntity.setSortOrder(model.getSortOrder());
			pinStartBarEntity.setUserId(info.getUserId());
			pinStartBarEntity.setSource(AppConstants.SOURCE_WEB);
			PinStartBarEntity pinStartBarEntityNew = pinStartBarRepository.saveAndFlush(pinStartBarEntity);
			if (pinStartBarEntityNew != null) {
				HazelcastConfig.getInstance().getPinTostartbar()
						.remove(info.getUserId() + "_" + AppConstants.SOURCE_WEB);
//				loadPinToStartBarIntoCacheForUser(info.getUserId(), AppConstants.SOURCE_WEB);
				return prepareResponse.prepareSuccessResponseObject(AppConstants.ADDED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to validate addPinToStartBar request
	 * 
	 * @author Dinesh Kumar
	 * @param model
	 * @return
	 */
	private boolean validateReq(PinToStartbarModel model) {
		if (StringUtil.isNotNullOrEmpty(model.getExchange()) && StringUtil.isNotNullOrEmpty(model.getToken())
				&& model.getSortOrder() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Method to add pin bar to start bar details
	 *
	 * @author Dinesh Kumar
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> addPinToStartBarForMob(PinToStartbarModel model, ClinetInfoModel info) {
		try {
			PinStartBarEntity pinStartBarEntity = new PinStartBarEntity();

			if (!validateReq(model))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			List<PinStartBarEntity> dbData = pinStartBarRepository.findByUserIdAndSortOrderAndSource(info.getUserId(),
					model.getSortOrder(), AppConstants.SOURCE_MOB);
			if (StringUtil.isListNullOrEmpty(dbData))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_DATA);

			ContractMasterModel masterData = HazelcastConfig.getInstance().getContractMaster()
					.get(model.getExchange() + "_" + model.getToken());
			if (masterData == null)
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
			pinStartBarEntity.setId(dbData.get(0).getId());
			pinStartBarEntity.setToken(masterData.getToken());
			pinStartBarEntity.setExchange(masterData.getExch());
			pinStartBarEntity.setSymbol(masterData.getSymbol());
			pinStartBarEntity.setSegment(masterData.getSegment());
			pinStartBarEntity.setPdc(masterData.getPdc());
			pinStartBarEntity.setFormattedInsName(masterData.getFormattedInsName());
			pinStartBarEntity.setTradingSymbol(masterData.getTradingSymbol());
			pinStartBarEntity.setExpiry(masterData.getExpiry());
			pinStartBarEntity.setSortOrder(model.getSortOrder());
			pinStartBarEntity.setUserId(info.getUserId());
			pinStartBarEntity.setSource(AppConstants.SOURCE_MOB);
			PinStartBarEntity pinStartBarEntityNew = pinStartBarRepository.saveAndFlush(pinStartBarEntity);
			if (pinStartBarEntityNew != null) {
				HazelcastConfig.getInstance().getPinTostartbar()
						.remove(info.getUserId() + "_" + AppConstants.SOURCE_MOB);
//				loadPinToStartBarIntoCacheForUser(info.getUserId(), AppConstants.SOURCE_MOB);
				return prepareResponse.prepareSuccessResponseObject(AppConstants.ADDED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
