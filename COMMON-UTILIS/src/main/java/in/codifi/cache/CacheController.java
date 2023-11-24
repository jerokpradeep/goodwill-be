package in.codifi.cache;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.model.response.GenericResponse;

@Path("/cache")
public class CacheController {

	@Inject
	CacheService cacheService;

	/**
	 * Method to load cache data
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Path("/load")
	@GET
	public RestResponse<GenericResponse> loadCacheData() {
		return cacheService.loadCacheData();
	}

	/**
	 * Method to update latest data into cache by fetching data from mapping table
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Path("/update")
	@GET
	public RestResponse<GenericResponse> updateCacheData() {
		return cacheService.updateCacheData();
	}
}
