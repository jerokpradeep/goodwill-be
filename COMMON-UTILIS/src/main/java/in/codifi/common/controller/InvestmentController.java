package in.codifi.common.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.controller.spec.InvestmentControllerSpec;
import in.codifi.common.model.response.InvestmentDashBoardRespModel;
import in.codifi.common.service.spec.InvestmentServiceSpec;
import in.codifi.common.utility.AppUtil;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@Path("/dashboard/investment")
public class InvestmentController implements InvestmentControllerSpec {

	@Inject
	InvestmentServiceSpec investmentServiceSpec;
	@Inject
	AppUtil appUtil;

	/**
	 * 
	 * Method to get Investment dash board info for mobile
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Override
	public RestResponse<InvestmentDashBoardRespModel> getInvestmentInfo() {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return investmentServiceSpec.getInvestmentInfo("");
		} else {
			return investmentServiceSpec.getInvestmentInfo(info.getUserId());
		}

	}
}
