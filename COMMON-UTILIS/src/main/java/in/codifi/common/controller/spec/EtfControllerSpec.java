package in.codifi.common.controller.spec;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.entity.EtfMappingEntity;
import in.codifi.common.model.request.MapReqModel;
import in.codifi.common.model.response.GenericResponse;

public interface EtfControllerSpec {

	/**
	 * Method to get ETF data
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Path("/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getEtf();

	/**
	 * Method to insert ETF data
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Path("/load")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> insertEtfData();

	/**
	 * Method to add mapping scrips
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Path("/map/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> addMappingScrips(List<EtfMappingEntity> entities);

	/**
	 * Method to delete mapping scrips
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Path("/map/delete")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> deleteMappingScrips(MapReqModel request);

	/**
	 * Method to get mapping scrips data
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Path("/map/scrips")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getMappingScrips();

}
