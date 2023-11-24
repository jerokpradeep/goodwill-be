package in.codifi.common.service.spec;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.entity.EQSectorMappingEntity;
import in.codifi.common.model.request.MapReqModel;
import in.codifi.common.model.response.GenericResponse;

public interface EQSectorServiceSpec {

	/**
	 * Get EQSector details
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	RestResponse<GenericResponse> getEQSector();

	/**
	 * Method to insert EQSector.This is for Admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	RestResponse<GenericResponse> insertEQSectorData();

	/**
	 * Method to add scrips in EQSector mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @param entities
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> addMappingScrips(List<EQSectorMappingEntity> entities, ClinetInfoModel info);

	/**
	 * Method to delete EQSector mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @param ids
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> deleteMappingScrips(MapReqModel request, ClinetInfoModel info);

	/**
	 * Method to get EQSector mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	RestResponse<GenericResponse> getMappingScrips();

}
