package in.codifi.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.config.HazelcastConfig;
import in.codifi.common.entity.IndicesEntity;
import in.codifi.common.entity.IndicesMappingEntity;
import in.codifi.common.model.request.MapReqModel;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.repo.entitymanager.ContractEntityManger;
import in.codifi.common.reposirory.IndiciesMappingRepository;
import in.codifi.common.reposirory.IndiciesRepository;
import in.codifi.common.service.spec.IndicesServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class IndicesService implements IndicesServiceSpec {

	@Inject
	IndiciesRepository indiciesRepository;

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	IndiciesMappingRepository mappingRepository;

	@Inject
	ContractEntityManger entityManger;

	/**
	 * Method to get indices details
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getIndices(ClinetInfoModel info) {

		try {
			List<IndicesEntity> indicesDetailsEntity = new ArrayList<IndicesEntity>();
			if (StringUtil.isListNotNullOrEmpty(
					HazelcastConfig.getInstance().getIndicesDetails().get(AppConstants.HAZEL_KEY_INDICES))) {
				indicesDetailsEntity = HazelcastConfig.getInstance().getIndicesDetails()
						.get(AppConstants.HAZEL_KEY_INDICES);
			} else {
				indicesDetailsEntity = loadIndicesDetailsData();
			}
			if (StringUtil.isListNullOrEmpty(indicesDetailsEntity))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			return prepareResponse.prepareSuccessResponseObject(indicesDetailsEntity);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to get indices details
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public List<IndicesEntity> getIndicesDetails() {
		List<IndicesEntity> indicesDetailsEntity = new ArrayList<IndicesEntity>();
		try {

			if (StringUtil.isListNotNullOrEmpty(
					HazelcastConfig.getInstance().getIndicesDetails().get(AppConstants.HAZEL_KEY_INDICES))) {
				indicesDetailsEntity = HazelcastConfig.getInstance().getIndicesDetails()
						.get(AppConstants.HAZEL_KEY_INDICES);
			} else {
				indicesDetailsEntity = loadIndicesDetailsData();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return indicesDetailsEntity;
	}

	/*
	 * method to indices details data
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	public List<IndicesEntity> loadIndicesDetailsData() {
		List<IndicesEntity> indicesDetailsEntity = indiciesRepository.findAll();
		if (StringUtil.isListNotNullOrEmpty(indicesDetailsEntity)) {
			HazelcastConfig.getInstance().getIndicesDetails().clear();
			HazelcastConfig.getInstance().getIndicesDetails().put(AppConstants.HAZEL_KEY_INDICES, indicesDetailsEntity);
		}
		return indicesDetailsEntity;

	}

	/**
	 * Method to insert indices data
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> insertIndicesData() {
		try {

			List<String> exchangeList = mappingRepository.findDistinctByExchange();
			List<IndicesEntity> dataToInsert = new ArrayList<>();

			if (StringUtil.isListNullOrEmpty(exchangeList))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			for (String exchange : exchangeList) {
				List<IndicesMappingEntity> mappingEntities = mappingRepository.findAllByExchange(exchange);
				List<String> scrips = new ArrayList<>();
				for (IndicesMappingEntity entity : mappingEntities) {
					scrips.add(entity.getScrips());
				}
				List<IndicesEntity> indicesEntities = entityManger.getIndicesDetails(scrips, exchange);
				dataToInsert.addAll(indicesEntities);
			}

			if (StringUtil.isListNotNullOrEmpty(dataToInsert)) {
				indiciesRepository.deleteAll();
				indiciesRepository.saveAll(dataToInsert);
				loadIndicesDetailsData();
				return prepareResponse.prepareSuccessResponseObject(AppConstants.INSERTED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to add Indices mapping scrips. This is for admin
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param entities
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> addMappingScrips(List<IndicesMappingEntity> entities, ClinetInfoModel info) {
		try {

			if (StringUtil.isListNullOrEmpty(entities))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			List<IndicesMappingEntity> dataToSave = new ArrayList<>();
			for (IndicesMappingEntity indicesMappingEntity : entities) {
				IndicesMappingEntity dataFromDB = new IndicesMappingEntity();
				dataFromDB = mappingRepository.findByScripsAndExchange(indicesMappingEntity.getScrips(),
						indicesMappingEntity.getExchange());
				if (dataFromDB != null) {
					dataFromDB.setActiveStatus(1);
					dataFromDB.setUpdatedBy(info.getUserId());
					dataToSave.add(dataFromDB);
				} else {
					indicesMappingEntity.setCreatedBy(info.getUserId());
					dataToSave.add(indicesMappingEntity);
				}
			}

			List<IndicesMappingEntity> indicesMappingEntities = mappingRepository.saveAll(dataToSave);
			return prepareResponse.prepareSuccessResponseObject(indicesMappingEntities);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to delete Indices mapping scrips. This is for admin
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
	 * Method to get Indices mapping scrips. This is for admin
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getMappingScrips() {
		try {
			List<IndicesMappingEntity> entities = new ArrayList<>();
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
