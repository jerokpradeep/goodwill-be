package in.codifi.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.config.HazelcastConfig;
import in.codifi.common.entity.EtfDetailsEntity;
import in.codifi.common.entity.EtfEntity;
import in.codifi.common.entity.EtfMappingEntity;
import in.codifi.common.model.request.MapReqModel;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.repo.entitymanager.ContractEntityManger;
import in.codifi.common.reposirory.EtfMappingRepository;
import in.codifi.common.reposirory.EtfRepository;
import in.codifi.common.service.spec.EtfServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class EtfService implements EtfServiceSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	EtfRepository etfRepository;
	@Inject
	EtfMappingRepository mappingRepository;
	@Inject
	ContractEntityManger entityManger;

	/**
	 * Get ETF details
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getEtf() {
		List<EtfEntity> etfMasterEntity = new ArrayList<>();
		try {
			if (HazelcastConfig.getInstance().getEtfDetails().containsKey(AppConstants.HAZEL_KEY_ETF)) {
				etfMasterEntity = HazelcastConfig.getInstance().getEtfDetails().get(AppConstants.HAZEL_KEY_ETF);
			} else {
				etfMasterEntity = loadEtfData();
			}
			if (StringUtil.isListNotNullOrEmpty(etfMasterEntity)) {
				return prepareResponse.prepareSuccessResponseObject(etfMasterEntity);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		}
	}

	/*
	 * method to load EtfData
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	public List<EtfEntity> loadEtfData() {
		List<EtfEntity> etfMasterEntity = etfRepository.findAll();
		if (StringUtil.isListNotNullOrEmpty(etfMasterEntity)) {
			HazelcastConfig.getInstance().getEtfDetails().clear();
			HazelcastConfig.getInstance().getEtfDetails().put(AppConstants.HAZEL_KEY_ETF, etfMasterEntity);
		}
		return etfMasterEntity;
	}

	/**
	 * Method to ETF indices data.This is for Admin
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> insertEtfData() {
		try {

			List<Integer> etfIds = mappingRepository.findDistinctByEtfId();
			List<EtfEntity> dataToInsert = new ArrayList<>();

			if (StringUtil.isListNullOrEmpty(etfIds))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			for (Integer etfId : etfIds) {
				EtfEntity etfEntity = new EtfEntity();
				List<EtfMappingEntity> mappingEntities = mappingRepository.findAllByEtfIdAndActiveStatus(etfId, 1);
				List<String> scrips = new ArrayList<>();
				for (EtfMappingEntity entity : mappingEntities) {
					scrips.add(entity.getScrips());
				}
				etfEntity.setEtfName(mappingEntities.get(0).getEtfName());
				etfEntity.setEtfList(etfId);
				List<EtfDetailsEntity> indicesEntities = entityManger.getEtfDetails(scrips);
				etfEntity.setScrips(indicesEntities);
				dataToInsert.add(etfEntity);
			}

			if (StringUtil.isListNotNullOrEmpty(dataToInsert)) {
				etfRepository.deleteAll();
				etfRepository.saveAll(dataToInsert);
				loadEtfData();
				return prepareResponse.prepareSuccessResponseObject(AppConstants.INSERTED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to add scrips in ETF mapping scrips. This is for admin
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param entities
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> addMappingScrips(List<EtfMappingEntity> entities, ClinetInfoModel info) {
		try {

			if (StringUtil.isListNullOrEmpty(entities))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			List<EtfMappingEntity> dataToSave = new ArrayList<>();
			for (EtfMappingEntity etfMappingEntity : entities) {
				EtfMappingEntity dataFromDB = new EtfMappingEntity();
				dataFromDB = mappingRepository.findByScrips(etfMappingEntity.getScrips());
				if (dataFromDB != null) {
					dataFromDB.setActiveStatus(1);
					dataFromDB.setUpdatedBy(info.getUserId());
					dataToSave.add(dataFromDB);
				} else {
					etfMappingEntity.setCreatedBy(info.getUserId());
					dataToSave.add(etfMappingEntity);
				}
			}
			List<EtfMappingEntity> indicesMappingEntities = mappingRepository.saveAll(dataToSave);
			return prepareResponse.prepareSuccessResponseObject(indicesMappingEntities);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to delete ETF mapping scrips. This is for admin
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param ids
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> deleteMappingScrips(MapReqModel request, ClinetInfoModel info) {
		try {

			if (request != null && StringUtil.isListNullOrEmpty(request.getIds()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			int activeStatus = 0;
			mappingRepository.updateActiveStatus(request.getIds(), info.getUserId(), activeStatus);
			return prepareResponse.prepareSuccessMessage(AppConstants.DELETED);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get ETF mapping scrips. This is for admin
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getMappingScrips() {
		try {
			List<EtfMappingEntity> entities = new ArrayList<>();
			entities = mappingRepository.findAllByActiveStatus(1);
			if (StringUtil.isListNullOrEmpty(entities))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			return prepareResponse.prepareSuccessResponseObject(entities);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
