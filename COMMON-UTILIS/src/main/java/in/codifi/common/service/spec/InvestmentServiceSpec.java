package in.codifi.common.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.model.response.InvestmentDashBoardRespModel;

public interface InvestmentServiceSpec {

	RestResponse<InvestmentDashBoardRespModel> getInvestmentInfo(String userId);

}
