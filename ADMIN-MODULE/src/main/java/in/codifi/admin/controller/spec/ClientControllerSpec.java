package in.codifi.admin.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.req.model.FormDataModel;

public interface ClientControllerSpec {

	@Path("/insert")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> createUsers();

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
	public RestResponse<GenericResponse> uploadClientDetails(@MultipartForm FormDataModel file);

}
