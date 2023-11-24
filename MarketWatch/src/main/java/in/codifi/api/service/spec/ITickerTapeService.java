package in.codifi.api.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.model.ReqModel;
import in.codifi.api.model.ResponseModel;

public interface ITickerTapeService {

	RestResponse<ResponseModel> getTicketTapeScrips(ReqModel model);

}
