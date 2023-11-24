package in.codifi.common.controller.spec;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.model.response.GenericResponse;

public interface ProductMasterControllerSpec {

	/**
	 * Method to load product master into cache
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Path("/load")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> loadProductMaster();

}
