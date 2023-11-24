package in.codifi.common.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.config.HazelcastConfig;
import in.codifi.common.entity.SectorHeatMapDetailsEntity;
import in.codifi.common.entity.SectorHeatMapEntity;
import in.codifi.common.entity.SectorHeatMappingEntity;
import in.codifi.common.model.request.MapReqModel;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.model.response.SectorHeatMapModel;
import in.codifi.common.repo.entitymanager.ContractEntityManger;
import in.codifi.common.reposirory.SectorHeatMapRepository;
import in.codifi.common.reposirory.SectorMappingRepository;
import in.codifi.common.service.spec.SectorHeatMapServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class SectorHeatMapService implements SectorHeatMapServiceSpec {
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	ContractEntityManger entityManger;
	@Inject
	SectorMappingRepository sectorMappingRepo;
	@Inject
	SectorHeatMapRepository sectorHeatmapRepo;

	/**
	 * Get sector heat map details
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getSectorHeatMap() {
		List<SectorHeatMapEntity> sectorHeatMapEntities = new ArrayList<>();
		try {
			if (HazelcastConfig.getInstance().getSectorHeatMap().containsKey(AppConstants.HAZEL_KEY_SECTOR_HEATMAP)) {
				sectorHeatMapEntities = HazelcastConfig.getInstance().getSectorHeatMap()
						.get(AppConstants.HAZEL_KEY_SECTOR_HEATMAP);
			} else {
				sectorHeatMapEntities = loadHeatMapSectorMasterData();
			}
			if (StringUtil.isListNotNullOrEmpty(sectorHeatMapEntities)) {
				return prepareResponse.prepareSuccessResponseObject(sectorHeatMapEntities);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	public RestResponse<GenericResponse> getSectorList() {
		try {
			List<SectorHeatMapModel> sectorMasterModel = new ArrayList<>();
			List<SectorHeatMapEntity> sectorHeatMapEntities = new ArrayList<>();
			if (HazelcastConfig.getInstance().getSectorHeatMap().containsKey(AppConstants.HAZEL_KEY_SECTOR_HEATMAP)) {
				sectorHeatMapEntities = HazelcastConfig.getInstance().getSectorHeatMap()
						.get(AppConstants.HAZEL_KEY_SECTOR_HEATMAP);
			} else {
				sectorHeatMapEntities = loadHeatMapSectorMasterData();
			}
			if (StringUtil.isListNotNullOrEmpty(sectorHeatMapEntities)) {
				for (SectorHeatMapEntity entity : sectorHeatMapEntities) {
					SectorHeatMapModel sectorMaster = new SectorHeatMapModel();
					sectorMaster.setSectorId(entity.getSectorId());
					sectorMaster.setSectorName(entity.getSectorName());
					sectorMaster.setOneDay(entity.getOneDay());
					sectorMasterModel.add(sectorMaster);
				}
				return prepareResponse.prepareSuccessResponseObject(sectorMasterModel);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to get heat map sector list
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public List<SectorHeatMapModel> getHeatMapSectors() {
		List<SectorHeatMapModel> sectorMasterModel = new ArrayList<SectorHeatMapModel>();
		try {
			List<SectorHeatMapEntity> sectorHeatMapEntities = new ArrayList<>();
			if (HazelcastConfig.getInstance().getSectorHeatMap().containsKey(AppConstants.HAZEL_KEY_SECTOR_HEATMAP)) {
				sectorHeatMapEntities = HazelcastConfig.getInstance().getSectorHeatMap()
						.get(AppConstants.HAZEL_KEY_SECTOR_HEATMAP);
			} else {
				sectorHeatMapEntities = loadHeatMapSectorMasterData();
			}
			if (StringUtil.isListNotNullOrEmpty(sectorHeatMapEntities)) {
				for (SectorHeatMapEntity entity : sectorHeatMapEntities) {
					SectorHeatMapModel sectorMaster = new SectorHeatMapModel();
					sectorMaster.setSectorId(entity.getSectorId());
					sectorMaster.setSectorName(entity.getSectorName());
					sectorMaster.setOneDay(entity.getOneDay());
					sectorMasterModel.add(sectorMaster);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return sectorMasterModel;
	}

	/**
	 * load sector heat map details
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	public List<SectorHeatMapEntity> loadHeatMapSectorMasterData() {
		List<SectorHeatMapEntity> heatmapMaster = new ArrayList<>();
		try {
			heatmapMaster = sectorHeatmapRepo.findAll();
			if (StringUtil.isListNotNullOrEmpty(heatmapMaster)) {
				HazelcastConfig.getInstance().getSectorHeatMap().clear();
				HazelcastConfig.getInstance().getSectorHeatMap().put(AppConstants.HAZEL_KEY_SECTOR_HEATMAP,
						heatmapMaster);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return heatmapMaster;
	}

	/**
	 * Method to insert sector heat map data.This is for Admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> insertSectorHeatMapData() {
		try {
			List<Integer> sectorIds = sectorMappingRepo.findDistinctBySectorId();
			List<SectorHeatMapEntity> dataToInsert = new ArrayList<>();

			if (StringUtil.isListNullOrEmpty(sectorIds))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			for (Integer sectorId : sectorIds) {
				SectorHeatMapEntity sectorHeatMapEntity = new SectorHeatMapEntity();
				List<SectorHeatMappingEntity> mappingEntities = sectorMappingRepo
						.findAllBySectorIdAndActiveStatus(sectorId, 1);
				List<String> scrips = new ArrayList<>();
				for (SectorHeatMappingEntity entity : mappingEntities) {
					scrips.add(entity.getScrips());
				}
				sectorHeatMapEntity.setSectorName(mappingEntities.get(0).getSectorName());
				sectorHeatMapEntity.setSectorId(sectorId);
				/** method to get last date of current month **/
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				int lastDateOfMonth = cal.getActualMaximum(Calendar.DATE);

				cal.set(Calendar.DATE, lastDateOfMonth);

				Date lastDateOfMonthDate = cal.getTime();
				String lastDayOfMonthDate = df.format(lastDateOfMonthDate);
				List<SectorHeatMapDetailsEntity> eqSectorEntities = entityManger.getSectorHeatMapDetails(scrips,
						lastDayOfMonthDate);
				if (StringUtil.isListNullOrEmpty(eqSectorEntities)) {
					/** method to get next month last date **/
					String nextMonthLastDate = nextMonthLastDate();
					eqSectorEntities = entityManger.getSectorHeatMapDetails(scrips, nextMonthLastDate);

				}
				sectorHeatMapEntity.setOneDay("+11.5");
				sectorHeatMapEntity.setScrips(eqSectorEntities);
				dataToInsert.add(sectorHeatMapEntity);
			}

			if (StringUtil.isListNotNullOrEmpty(dataToInsert)) {
				sectorHeatmapRepo.deleteAll();
				sectorHeatmapRepo.saveAll(dataToInsert);
				loadHeatMapSectorMasterData();
				return prepareResponse.prepareSuccessResponseObject(AppConstants.INSERTED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/*
	 * method to get the next month last date
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	private String nextMonthLastDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date nextDate = cal.getTime();
		String nextMonthLastDate = df.format(nextDate);
		return nextMonthLastDate;
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
	public RestResponse<GenericResponse> addMappingScrips(List<SectorHeatMappingEntity> entities,
			ClinetInfoModel info) {
		try {

			if (StringUtil.isListNullOrEmpty(entities))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			List<SectorHeatMappingEntity> dataToSave = new ArrayList<>();
			for (SectorHeatMappingEntity sectorHeatMappingEntity : entities) {
				SectorHeatMappingEntity dataFromDB = new SectorHeatMappingEntity();
				dataFromDB = sectorMappingRepo.findByScrips(sectorHeatMappingEntity.getScrips());
				if (dataFromDB != null) {
					dataFromDB.setActiveStatus(1);
					dataFromDB.setUpdatedBy(info.getUserId());
					dataToSave.add(dataFromDB);
				} else {
					sectorHeatMappingEntity.setCreatedBy(info.getUserId());
					dataToSave.add(sectorHeatMappingEntity);
				}
			}
			List<SectorHeatMappingEntity> eQSectorMappingEntities = sectorMappingRepo.saveAll(dataToSave);
			return prepareResponse.prepareSuccessResponseObject(eQSectorMappingEntities);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to delete sector heat mapping scrips. This is for admin
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
			sectorMappingRepo.updateActiveStatus(request.getIds(), info.getUserId(), activeStatus);
			return prepareResponse.prepareSuccessMessage(AppConstants.DELETED);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get sector heat mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getMappingScrips() {
		try {
			List<SectorHeatMappingEntity> entities = new ArrayList<>();
			entities = sectorMappingRepo.findAllByActiveStatus(1);
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
