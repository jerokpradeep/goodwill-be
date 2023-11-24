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
import in.codifi.common.entity.FutureMonthEntity;
import in.codifi.common.entity.FutureMonthMappingEntity;
import in.codifi.common.model.request.MapReqModel;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.repo.entitymanager.ContractEntityManger;
import in.codifi.common.reposirory.FutureMonthMappingRepository;
import in.codifi.common.reposirory.FutureMonthRepository;
import in.codifi.common.service.spec.FutureMonthServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class FutureMonthService implements FutureMonthServiceSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	FutureMonthRepository futureMonthRepo;
	@Inject
	FutureMonthMappingRepository futureMappingRepo;
	@Inject
	ContractEntityManger entityManger;

	/**
	 * Get future month data details
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getFutureMonthData() {
		List<FutureMonthEntity> futureMonthEntity = new ArrayList<>();
		try {
			if (HazelcastConfig.getInstance().getFutureMonth().containsKey(AppConstants.HAZEL_KEY_FUTURE_MONTH)) {
				futureMonthEntity = HazelcastConfig.getInstance().getFutureMonth()
						.get(AppConstants.HAZEL_KEY_FUTURE_MONTH);
			} else {
				futureMonthEntity = loadFutureMonth();
			}
			if (StringUtil.isListNotNullOrEmpty(futureMonthEntity)) {
				return prepareResponse.prepareSuccessResponseObject(futureMonthEntity);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		}
	}

	/**
	 * method to load future month details
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public List<FutureMonthEntity> loadFutureMonth() {
		List<FutureMonthEntity> futureMonthEntity = new ArrayList<>();
		try {
			futureMonthEntity = futureMonthRepo.findAll();
			if (StringUtil.isListNotNullOrEmpty(futureMonthEntity)) {
				HazelcastConfig.getInstance().getFutureMonth().clear();
				HazelcastConfig.getInstance().getFutureMonth().put(AppConstants.HAZEL_KEY_FUTURE_MONTH,
						futureMonthEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return futureMonthEntity;
	}

	/**
	 * Method to insert future month map data.This is for Admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> insertFutureMonthData() {
		try {
			List<FutureMonthEntity> dataToInsert = new ArrayList<>();
			FutureMonthEntity futureEntity = new FutureMonthEntity();
			List<FutureMonthMappingEntity> mappingEntities = futureMappingRepo.findAllByActiveStatus(1);
			if (StringUtil.isListNullOrEmpty(mappingEntities))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			for (FutureMonthMappingEntity enttiy : mappingEntities) {
				String exch = enttiy.getExch();
				String symbol = enttiy.getSymbol();
				String insType = enttiy.getInsType();

				/** method to get last date of current month **/
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				int lastDateOfMonth = cal.getActualMaximum(Calendar.DATE);

				cal.set(Calendar.DATE, lastDateOfMonth);

				Date lastDateOfMonthDate = cal.getTime();
				String lastDayOfMonthDate = df.format(lastDateOfMonthDate);
				futureEntity = entityManger.getFutureMonthDetails(exch, symbol, insType);
				dataToInsert.add(futureEntity);
			}

			if (StringUtil.isListNotNullOrEmpty(dataToInsert)) {
				futureMonthRepo.deleteAll();
				futureMonthRepo.saveAll(dataToInsert);
				loadFutureMonth();
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
	public RestResponse<GenericResponse> addMappingScrips(List<FutureMonthMappingEntity> entities,
			ClinetInfoModel info) {
		try {

			if (StringUtil.isListNullOrEmpty(entities))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			List<FutureMonthMappingEntity> dataToSave = new ArrayList<>();
			for (FutureMonthMappingEntity futureMonthMappingEntity : entities) {
				FutureMonthMappingEntity dataFromDB = new FutureMonthMappingEntity();
				dataFromDB = futureMappingRepo.findByExchAndSymbolAndInsType(futureMonthMappingEntity.getSymbol(),
						futureMonthMappingEntity.getExch(), futureMonthMappingEntity.getInsType());
				if (dataFromDB != null) {
					dataFromDB.setActiveStatus(1);
					dataFromDB.setUpdatedBy(info.getUserId());
					dataToSave.add(dataFromDB);
				} else {
					futureMonthMappingEntity.setCreatedBy(info.getUserId());
					dataToSave.add(futureMonthMappingEntity);
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
			futureMappingRepo.updateActiveStatus(request.getIds(), info.getUserId(), activeStatus);
			return prepareResponse.prepareSuccessMessage(AppConstants.DELETED);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get future month mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getMappingScrips() {
		try {
			List<FutureMonthMappingEntity> entities = new ArrayList<>();
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
