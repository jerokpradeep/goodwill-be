package in.codifi.common.service.spec;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.entity.FutureMappingEntity;
import in.codifi.common.model.request.MapReqModel;
import in.codifi.common.model.response.GenericResponse;

public interface FutureServiceSpec {

	/**
	 * Get future data
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	RestResponse<GenericResponse> getFutureData();

	/**
	 * Method to insert future details.This is for Admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	RestResponse<GenericResponse> insertFutureData();

	/**
	 * Method to add scrips in future mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @param entities
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> addMappingScrips(List<FutureMappingEntity> entities, ClinetInfoModel info);

	/**
	 * Method to delete future mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @param ids
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> deleteMappingScrips(MapReqModel request, ClinetInfoModel info);

	/**
	 * Method to get future mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	RestResponse<GenericResponse> getMappingScrips();

}
