package in.codifi.cache;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.scrips.model.response.GenericResponse;

@Path("/cache")
public class CacheController {

	@Inject
	CacheService service;

	/**
	 * 
	 * Method to reload cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Path("/reload")
	@GET
	public RestResponse<GenericResponse> reloadCache() {
		return service.reloadCache();
	}

	/**
	 * 
	 * Method to clear search data from cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Path("/clear/search/")
	@GET
	public RestResponse<GenericResponse> clearSearchData() {
		return service.clearSearchData();
	}
}
