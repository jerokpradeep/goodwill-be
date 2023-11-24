package in.codifi.api.controller;

import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;

import in.codifi.api.controller.spec.ICacheController;
import in.codifi.api.model.ResponseModel;
import in.codifi.api.service.spec.ICacheService;

@Path("/mwcache")
public class CacheController implements ICacheController {

	@Autowired
	ICacheService iCacheService;

	@Override
	public RestResponse<ResponseModel> loadUserMWData() {
		return iCacheService.loadUserMWData();
	}

}
