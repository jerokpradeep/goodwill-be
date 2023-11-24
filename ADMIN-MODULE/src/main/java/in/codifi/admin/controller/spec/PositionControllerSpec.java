package in.codifi.admin.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.request.FormDataModel;
import in.codifi.admin.model.request.PositionReqModel;
import in.codifi.admin.model.response.GenericResponse;

public interface PositionControllerSpec {
	/**
	 * 
	 * Method to upload position file
	 *
	 * @author SOWMIYA
	 *
	 * @param file
	 * @return
	 */
	@Path("/upload")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public RestResponse<GenericResponse> uploadPositionFile(@MultipartForm FormDataModel file);

	/**
	 * 
	 * Method to insert position file
	 *
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Path("/insert")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> insertPositionFile();

	/**
	 * method to get position avg price
	 * 
	 * @author SOWMIYA
	 * @param model
	 * @return
	 */
	@Path("/get")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getPositionAvgUser(PositionReqModel model);

	/**
	 * method to get position count by exchange
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Path("/count")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getPositionCountByExch();

}
