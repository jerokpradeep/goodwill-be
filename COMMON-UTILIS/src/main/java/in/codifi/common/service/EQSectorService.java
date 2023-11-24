package in.codifi.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.config.HazelcastConfig;
import in.codifi.common.entity.EQSectorDetailsEntity;
import in.codifi.common.entity.EQSectorEntity;
import in.codifi.common.entity.EQSectorMappingEntity;
import in.codifi.common.entity.EQSectorMasterMappingEntity;
import in.codifi.common.model.request.MapReqModel;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.repo.entitymanager.ContractEntityManger;
import in.codifi.common.reposirory.EQMappingRepository;
import in.codifi.common.reposirory.EQSectorMasterMappingRepository;
import in.codifi.common.reposirory.EQSectorRepository;
import in.codifi.common.service.spec.EQSectorServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class EQSectorService implements EQSectorServiceSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	EQSectorRepository eqSectorRepo;
	@Inject
	EQMappingRepository eqMappingRepo;
	@Inject
	ContractEntityManger entityManger;
	@Inject
	EQSectorMasterMappingRepository masterMappingRepository;

	/**
	 * Get EQ sector details
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getEQSector() {
		List<EQSectorEntity> EQSectorMasterEntity = new ArrayList<>();
		try {
			if (HazelcastConfig.getInstance().getEqSectorDetails().containsKey(AppConstants.HAZEL_KEY_EQSECTOR)) {
				EQSectorMasterEntity = HazelcastConfig.getInstance().getEqSectorDetails()
						.get(AppConstants.HAZEL_KEY_EQSECTOR);
			} else {
				EQSectorMasterEntity = loadEQSectorData();
			}
			if (StringUtil.isListNotNullOrEmpty(EQSectorMasterEntity)) {
				return prepareResponse.prepareSuccessResponseObject(EQSectorMasterEntity);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		}
	}

	/**
	 * 
	 * Method to get EQ Sector data
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public List<EQSectorEntity> getEqSector() {
		List<EQSectorEntity> eqSectorListModels = new ArrayList<EQSectorEntity>();
		try {
			if (StringUtil.isListNotNullOrEmpty(
					HazelcastConfig.getInstance().getEqSectorDetails().get(AppConstants.HAZEL_KEY_EQSECTOR))) {
				eqSectorListModels = HazelcastConfig.getInstance().getEqSectorDetails()
						.get(AppConstants.HAZEL_KEY_EQSECTOR);
			} else {
				eqSectorListModels = loadEQSectorData();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return eqSectorListModels;
	}

	/**
	 * method to load eq sector
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	public List<EQSectorEntity> loadEQSectorData() {
		List<EQSectorEntity> eqSectorMasterEntities = eqSectorRepo.findAll();
		try {
			if (StringUtil.isListNotNullOrEmpty(eqSectorMasterEntities)) {
				HazelcastConfig.getInstance().getEqSectorDetails().clear();
				HazelcastConfig.getInstance().getEqSectorDetails().put(AppConstants.HAZEL_KEY_EQSECTOR,
						eqSectorMasterEntities);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return eqSectorMasterEntities;
	}

	/**
	 * Method to EQSector data.This is for Admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> insertEQSectorData() {
		try {

			List<Integer> sectorIds = eqMappingRepo.findByDistinctSectorId();
			List<EQSectorMasterMappingEntity> masterMappingData = masterMappingRepository.findAll();
			List<EQSectorEntity> dataToInsert = new ArrayList<>();

			if (StringUtil.isListNullOrEmpty(sectorIds))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			for (Integer sectorId : sectorIds) {
				EQSectorEntity eqSectorEntity = new EQSectorEntity();
				List<EQSectorMappingEntity> mappingEntities = eqMappingRepo.findAllBySectorIdAndActiveStatus(sectorId,
						1);
				List<String> scrips = new ArrayList<>();
				for (EQSectorMappingEntity entity : mappingEntities) {
					scrips.add(entity.getScrips());
				}
				eqSectorEntity.setSectorName(mappingEntities.get(0).getSectorName());
				eqSectorEntity.setSectorList(sectorId);
				for (EQSectorMasterMappingEntity mappingEntity : masterMappingData) {
					if (mappingEntity.getSectorList() == sectorId) {
						eqSectorEntity.setOneYear(mappingEntity.getOneYear());
						eqSectorEntity.setSixMonths(mappingEntity.getSixMonths());
						eqSectorEntity.setThreeMonths(mappingEntity.getThreeMonths());
						eqSectorEntity.setImageUrl(mappingEntity.getImageUrl());
						break;
					}
				}

				List<EQSectorDetailsEntity> eqSectorEntities = entityManger.getEQSectorDetails(scrips);
				eqSectorEntity.setScrips(eqSectorEntities);
				dataToInsert.add(eqSectorEntity);
			}

			if (StringUtil.isListNotNullOrEmpty(dataToInsert)) {
				eqSectorRepo.deleteAll();
				eqSectorRepo.saveAll(dataToInsert);
				loadEQSectorData();
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
	 * @author SOWMIYA
	 *
	 * @param entities
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> addMappingScrips(List<EQSectorMappingEntity> entities, ClinetInfoModel info) {
		try {
			if (StringUtil.isListNullOrEmpty(entities))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			List<EQSectorMappingEntity> dataToSave = new ArrayList<>();
			for (EQSectorMappingEntity eQSectorMappingEntity : entities) {
				EQSectorMappingEntity dataFromDB = new EQSectorMappingEntity();
				dataFromDB = eqMappingRepo.findByScrips(eQSectorMappingEntity.getScrips());
				if (dataFromDB != null) {
					dataFromDB.setActiveStatus(1);
					dataFromDB.setUpdatedBy(info.getUserId());
					dataToSave.add(dataFromDB);
				} else {
					eQSectorMappingEntity.setCreatedBy(info.getUserId());
					dataToSave.add(eQSectorMappingEntity);
				}
			}
			List<EQSectorMappingEntity> eQSectorMappingEntities = eqMappingRepo.saveAll(dataToSave);
			return prepareResponse.prepareSuccessResponseObject(eQSectorMappingEntities);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to delete ETF mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
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
			eqMappingRepo.updateActiveStatus(request.getIds(), info.getUserId(), activeStatus);
			return prepareResponse.prepareSuccessMessage(AppConstants.DELETED);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get EQSector mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getMappingScrips() {
		try {
			List<EQSectorMappingEntity> entities = new ArrayList<>();
			entities = eqMappingRepo.findAllByActiveStatus(1);
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
