package in.codifi.common.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.model.response.GenericResponse;

public interface ProductMasterServiceSpec {

	RestResponse<GenericResponse> loadProductMaster();

}
