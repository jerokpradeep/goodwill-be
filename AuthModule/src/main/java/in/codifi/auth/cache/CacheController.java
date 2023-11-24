package in.codifi.auth.cache;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.model.response.GenericResponse;

@Path("/cache")
public class CacheController {

	@Inject
	CacheService cacheService;

	
	@Path("/twofact/reload")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> reload2FACache() {
		return cacheService.reload2FACache();
	}
}
