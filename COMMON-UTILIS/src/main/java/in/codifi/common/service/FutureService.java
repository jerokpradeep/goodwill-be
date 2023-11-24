package in.codifi.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.config.HazelcastConfig;
import in.codifi.common.entity.FutureDetailsEntity;
import in.codifi.common.entity.FutureEntity;
import in.codifi.common.entity.FutureMappingEntity;
import in.codifi.common.model.request.MapReqModel;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.repo.entitymanager.ContractEntityManger;
import in.codifi.common.reposirory.FutureMappingRepository;
import in.codifi.common.reposirory.FutureRepository;
import in.codifi.common.service.spec.FutureServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class FutureService implements FutureServiceSpec {
	@Inject
	FutureRepository futureMasterRepo;
	@Inject
	FutureMappingRepository futureMappingRepo;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	ContractEntityManger entityManger;

	/**
	 * Get future data
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getFutureData() {
		List<FutureEntity> futureEntity = new ArrayList<>();
		try {
			if (HazelcastConfig.getInstance().getFutureDetails().containsKey(AppConstants.HAZEL_KEY_FUTURES)) {
				futureEntity = HazelcastConfig.getInstance().getFutureDetails().get(AppConstants.HAZEL_KEY_FUTURES);
			} else {
				futureEntity = loadFutureDetails();
			}
			if (StringUtil.isListNotNullOrEmpty(futureEntity)) {
				return prepareResponse.prepareSuccessResponseObject(futureEntity);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		}
	}

	/**
	 * method to load future details
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	public List<FutureEntity> loadFutureDetails() {
		List<FutureEntity> futureEntity = new ArrayList<>();
		try {
			futureEntity = futureMasterRepo.findAll();
			if (StringUtil.isListNotNullOrEmpty(futureEntity)) {
				HazelcastConfig.getInstance().getFutureDetails().clear();
				System.out.println(futureEntity.get(0).getScrips() != null ? futureEntity.get(0).getScrips()
						: AppConstants.NO_SCRIP);
				HazelcastConfig.getInstance().getFutureDetails().put(AppConstants.HAZEL_KEY_FUTURES, futureEntity);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return futureEntity;
	}

	/**
	 * Method to insert future details.This is for Admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> insertFutureData() {
		List<FutureMappingEntity> mappingEntities = new ArrayList<>();
		try {

			List<Integer> distinctFuturesId = futureMappingRepo.findDistinctByFutureId();
			List<FutureEntity> dataToInsert = new ArrayList<>();
			for (Integer futureId : distinctFuturesId) {
				FutureEntity futureEntity = new FutureEntity();
				mappingEntities = futureMappingRepo.findAllByFutureId(futureId);
				if (StringUtil.isListNotNullOrEmpty(mappingEntities)) {
					List<FutureDetailsEntity> futureDetailsEntities = new ArrayList<>();
					for (FutureMappingEntity entity : mappingEntities) {
						String exch = entity.getExch();
						String symbol = entity.getSymbol();
						String insType = entity.getInsType();
						List<FutureDetailsEntity> futureDetails = entityManger.getFutureDetails(exch, symbol, insType);
						futureDetailsEntities.addAll(futureDetails);
					}
					futureEntity.setFuturesList(mappingEntities.get(0).getFutureId());
					futureEntity.setFuturesName(mappingEntities.get(0).getFutureName());
					futureEntity.setScrips(futureDetailsEntities);
					dataToInsert.add(futureEntity);
				}
			}
			if (StringUtil.isListNotNullOrEmpty(dataToInsert)) {
				futureMasterRepo.deleteAll();
				futureMasterRepo.saveAll(dataToInsert);
				loadFutureDetails();
				return prepareResponse.prepareSuccessResponseObject(AppConstants.INSERTED);

			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to add scrips in future mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @param entities
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> addMappingScrips(List<FutureMappingEntity> entities, ClinetInfoModel info) {
		try {

			if (StringUtil.isListNullOrEmpty(entities))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			List<FutureMappingEntity> dataToSave = new ArrayList<>();
			for (FutureMappingEntity futureMappingEntity : entities) {
				FutureMappingEntity dataFromDB = new FutureMappingEntity();
				dataFromDB = futureMappingRepo.findByExchAndSymbolAndInsType(futureMappingEntity.getSymbol(),
						futureMappingEntity.getExch(), futureMappingEntity.getInsType());
				if (dataFromDB != null) {
					dataFromDB.setActiveStatus(1);
					dataFromDB.setUpdatedBy(info.getUserId());
					dataToSave.add(dataFromDB);
				} else {
					futureMappingEntity.setCreatedBy(info.getUserId());
					dataToSave.add(futureMappingEntity);
				}
			}
			dataToSave = futureMappingRepo.saveAll(dataToSave);
			return prepareResponse.prepareSuccessResponseObject(dataToSave);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to delete future mapping scrips. This is for admin
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
			futureMappingRepo.updateActiveStatus(request.getIds(), info.getUserId(), activeStatus);
			return prepareResponse.prepareSuccessMessage(AppConstants.DELETED);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get future mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getMappingScrips() {
		try {
			List<FutureMappingEntity> entities = new ArrayList<>();
			entities = futureMappingRepo.findAllByActiveStatus(1);
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
