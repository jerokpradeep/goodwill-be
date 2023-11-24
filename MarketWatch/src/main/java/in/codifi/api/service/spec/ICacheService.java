package in.codifi.api.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.model.ResponseModel;

public interface ICacheService {

	public RestResponse<ResponseModel> loadUserMWData();

}
