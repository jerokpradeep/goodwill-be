package in.codifi.common.controller.spec;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.entity.SectorHeatMappingEntity;
import in.codifi.common.model.request.MapReqModel;
import in.codifi.common.model.response.GenericResponse;

public interface SectorHeatMapControllerSpec {

	/**
	 * Method to get Sector heat map data
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Path("/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getSectorHeatMap();

	/**
	 * Method to insert sector heat map data.This is for Admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Path("/load")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
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
	@Path("/map/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> addMappingScrips(List<SectorHeatMappingEntity> entities);

	/**
	 * Method to delete sector heat mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @param ids
	 * @param info
	 * @return
	 */
	@Path("/map/delete")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> deleteMappingScrips(MapReqModel request);

	/**
	 * Method to get sector heat mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Path("/map/scrips")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getMappingScrips();

}
