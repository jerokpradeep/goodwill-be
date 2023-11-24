package in.codifi.position.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.position.model.request.PositionConversionReq;
import in.codifi.position.model.response.GenericResponse;

public interface PositionServiceSpec {

	RestResponse<GenericResponse> getposition(ClinetInfoModel info);

	RestResponse<GenericResponse> positionConversion(PositionConversionReq positionConversionReq, ClinetInfoModel info);

}
