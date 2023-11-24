package in.codifi.holdings.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.holdings.model.request.HoldingsReqModel;
import in.codifi.holdings.model.response.GenericResponse;

public interface HoldingsServiceSpec {

	RestResponse<GenericResponse> getCNCHoldings(ClinetInfoModel info);

	RestResponse<GenericResponse> getMTFHoldings(ClinetInfoModel info);

	RestResponse<GenericResponse> getHoldingsByProduct(HoldingsReqModel reqModel, ClinetInfoModel info);

	RestResponse<GenericResponse> getCNCNonPOAHoldings(ClinetInfoModel info);

	RestResponse<GenericResponse> getMTFNonPOAHoldings(ClinetInfoModel info);

}
