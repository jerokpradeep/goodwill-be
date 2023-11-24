package in.codifi.common.service.spec;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.entity.FutureMonthMappingEntity;
import in.codifi.common.model.request.MapReqModel;
import in.codifi.common.model.response.GenericResponse;

public interface FutureMonthServiceSpec {

	/**
	 * Get future month data details
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	RestResponse<GenericResponse> getFutureMonthData();

	/**
	 * Method to insert future month map data.This is for Admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	RestResponse<GenericResponse> insertFutureMonthData();

	/**
	 * Method to add scrips in future month mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @param entities
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> addMappingScrips(List<FutureMonthMappingEntity> entities, ClinetInfoModel info);

	/**
	 * Method to delete futute month mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @param ids
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> deleteMappingScrips(MapReqModel request, ClinetInfoModel info);

	/**
	 * Method to get future month mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	RestResponse<GenericResponse> getMappingScrips();

}
