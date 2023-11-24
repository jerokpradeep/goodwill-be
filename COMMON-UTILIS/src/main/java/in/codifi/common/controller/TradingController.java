package in.codifi.common.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.controller.spec.TradingControllerSpec;
import in.codifi.common.model.response.TradingDashBoardRespModel;
import in.codifi.common.service.spec.TradingServiceSpec;
import in.codifi.common.utility.AppUtil;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@Path("/dashboard/trading")
public class TradingController implements TradingControllerSpec {

	@Inject
	TradingServiceSpec tradingServiceSpec;
	@Inject
	AppUtil appUtil;

	/**
	 * 
	 * Method to get trading dash board info for mobile
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Override
	public RestResponse<TradingDashBoardRespModel> getTradingInfo() {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return tradingServiceSpec.getTradingInfo("");
		} else {
			return tradingServiceSpec.getTradingInfo(info.getUserId());
		}
	}
}
