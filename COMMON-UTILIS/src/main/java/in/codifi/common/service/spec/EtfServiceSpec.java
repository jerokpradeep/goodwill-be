package in.codifi.common.service.spec;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.entity.EtfMappingEntity;
import in.codifi.common.model.request.MapReqModel;
import in.codifi.common.model.response.GenericResponse;

public interface EtfServiceSpec {

	/**
	 * Method to get ETF data
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	RestResponse<GenericResponse> getEtf();

	/**
	 * Method to ETF indices data. This is for Admin
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	RestResponse<GenericResponse> insertEtfData();

	/**
	 * Method to add Indices mapping scrips. This is for admin
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param entities
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> addMappingScrips(List<EtfMappingEntity> entities, ClinetInfoModel info);

	/**
	 * Method to delete ETF mapping scrips. This is for admin
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param ids
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> deleteMappingScrips(MapReqModel request, ClinetInfoModel info);

	/**
	 * Method to get ETF mapping scrips. This is for admin
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	RestResponse<GenericResponse> getMappingScrips();

}
