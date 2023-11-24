package in.codifi.basket.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.basket.model.response.GenericResponse;

public interface ICacheController {
//
//	/**
//	 * method to Load Cache
//	 * 
//	 * @author Gowthaman M
//	 * @return
//	 */
//	@Path("/load")
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	RestResponse<CommonResponseModel> loadCache(RequestModel requestModel);
//
//	/**
//	 * method to Clear Cache
//	 * 
//	 * @author Gowthaman M
//	 * @return
//	 */
//	@Path("/clear")
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	RestResponse<CommonResponseModel> clearCache(RequestModel requestModel);
//
//	/**
//	 * method to Load Cache By UserId
//	 * 
//	 * @author Gowthaman M
//	 * @return
//	 */
//	@Path("/load/byUserId")
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	RestResponse<CommonResponseModel> cacheLoadByUserId(RequestModel requestModel);
//
//	/**
//	 * method to clear Cache By UserId
//	 * 
//	 * @author Gowthaman M
//	 * @return
//	 */
//	@Path("/clear/cacheByUserId")
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	RestResponse<CommonResponseModel> clearCacheByUserId(RequestModel requestModel);

	/**
	 * method to delete expiry scrips
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/delete/expiry")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> deleteExpiry();

}
