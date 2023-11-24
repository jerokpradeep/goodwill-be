package in.codifi.holdings.controller.spec;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.holdings.model.request.EdisReqModel;
import in.codifi.holdings.model.response.GenericResponse;

public interface EDISControllerSpec {

	/**
	 * method to initialize edis request
	 * 
	 * @author SOWMIYA
	 */

	@Path("/initialize")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> initializeEdisRequest(List<EdisReqModel> model);

	/**
	 * method to get hs token
	 * 
	 * @author SOWMIYA
	 */

	@Path("/get/hstoken")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getHSToken();
}

//	/**
//	 * method to get revocation returns
//	 * 
//	 * @author SowmiyaThangaraj
//	 * @param req
//	 * @param userId
//	 * @return
//	 */
//	@Path("/revocationreturn")
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	public Response revocationReturn(String req, @QueryParam("userId") String userId);
//}
