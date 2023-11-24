package in.codifi.sso.auth.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.sso.auth.entity.primary.VendorAppEntity;
import in.codifi.sso.auth.model.response.GenericResponse;

public interface VendorServiceSpec {

	RestResponse<GenericResponse> getVendorAppDetails(String userId);

	RestResponse<GenericResponse> createNewVendorApp(VendorAppEntity entity, String userId);

	RestResponse<GenericResponse> updateVendorApp(VendorAppEntity entity, String userId);

	RestResponse<GenericResponse> restAPISecret(long appId, String userId);

	RestResponse<GenericResponse> deleteVendor(long appId, String userId);

}
