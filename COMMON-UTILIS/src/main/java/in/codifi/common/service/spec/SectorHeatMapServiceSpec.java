package in.codifi.common.service.spec;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.entity.SectorHeatMappingEntity;
import in.codifi.common.model.request.MapReqModel;
import in.codifi.common.model.response.GenericResponse;

public interface SectorHeatMapServiceSpec {

	/**
	 * Get sector heat map details
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	RestResponse<GenericResponse> getSectorHeatMap();

	/**
	 * Method to insert sector heat map data.This is for Admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	RestResponse<GenericResponse> insertSectorHeatMapData();

	/**
	 * Method to add scrips in sector heat mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @param entities
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> addMappingScrips(List<SectorHeatMappingEntity> entities, ClinetInfoModel info);

	/**
	 * Method to delete sector heat mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @param ids
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> deleteMappingScrips(MapReqModel request, ClinetInfoModel info);

	/**
	 * Method to get sector heat mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	RestResponse<GenericResponse> getMappingScrips();

}
