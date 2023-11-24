package in.codifi.funds.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.funds.model.response.GenericResponse;

public interface FundsServiceSpec {

	RestResponse<GenericResponse> getLimits(ClinetInfoModel info);

	RestResponse<GenericResponse> getCommodityLimits(ClinetInfoModel info);

}
