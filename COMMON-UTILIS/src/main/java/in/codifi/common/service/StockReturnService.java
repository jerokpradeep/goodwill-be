package in.codifi.common.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.StockReturnModel;
import in.codifi.common.config.HazelcastConfig;
import in.codifi.common.model.request.StockReturnReqModel;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.model.response.StockReturnRespModel;
import in.codifi.common.service.spec.StockReturnServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class StockReturnService implements StockReturnServiceSpec {
	@Inject
	PrepareResponse prepareResponse;

	/**
	 * method to get stock return
	 * 
	 * @author sowmiya
	 */
	@Override
	public RestResponse<GenericResponse> getStockReturn(StockReturnReqModel pReqModel) {
		try {
			if (StringUtil.isNullOrEmpty(pReqModel.getExch()) && StringUtil.isNullOrEmpty(pReqModel.getToken()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);

			String stockKey = pReqModel.getExch() + "_" + pReqModel.getToken();
			List<StockReturnModel> stockReturnModel = HazelcastConfig.getInstance().getStockReturnDetails()
					.get(stockKey);

			if (stockReturnModel != null && !stockReturnModel.isEmpty()) {
				StockReturnRespModel stockRespModel = new StockReturnRespModel();

				for (StockReturnModel model : stockReturnModel) {
					String tagOfPeriod = model.getTagOfPeriod();
					String changePerc = model.getChangePerc();

					switch (tagOfPeriod) {
					case "W1":
						stockRespModel.setWeek(String.valueOf(changePerc));
						break;
					case "M1":
						stockRespModel.setMonth(String.valueOf(changePerc));
						break;
					case "M3":
						stockRespModel.setThreeMonth(String.valueOf(changePerc));
						break;
					case "M6":
						stockRespModel.setSixMonth(String.valueOf(changePerc));
						break;
					case "M9":
						stockRespModel.setNineMonth(String.valueOf(changePerc));
						break;
					case "Y1":
						stockRespModel.setOneYear(String.valueOf(changePerc));
						break;
					case "Y2":
						stockRespModel.setTwoYear(String.valueOf(changePerc));
						break;
					case "Y3":
						stockRespModel.setThreeYear(String.valueOf(changePerc));
						break;
					case "Y4":
						stockRespModel.setFourYear(String.valueOf(changePerc));
						break;
					case "Y5":
						stockRespModel.setFiveYear(String.valueOf(changePerc));
						break;
					default:
						stockRespModel.setWeek("NA");
						stockRespModel.setMonth("NA");
						stockRespModel.setThreeMonth("NA");
						stockRespModel.setSixMonth("NA");
						stockRespModel.setNineMonth("NA");
						stockRespModel.setOneYear("NA");
						stockRespModel.setTwoYear("NA");
						stockRespModel.setThreeYear("NA");
						stockRespModel.setFourYear("NA");
						stockRespModel.setFiveYear("NA");
						break;
					}
				}
				return prepareResponse.prepareSuccessResponseObject(stockRespModel);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORD_FOUND);
			}
		} catch (Exception e) {
			Log.error("common - getSst", e);
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
