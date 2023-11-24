package in.codifi.admin.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;
import org.json.simple.JSONArray;

import in.codifi.admin.model.request.MarketWatchReqModel;
import in.codifi.admin.model.request.PaymentRefReqModel;
import in.codifi.admin.model.request.PostionAvgPriceReqModel;
import in.codifi.admin.model.response.BankDetailsResponseModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.model.response.HoldingCountResponseModel;
import in.codifi.admin.model.response.HoldingsResponseModel;
import in.codifi.admin.model.response.MarketWatchResponseModel;
import in.codifi.admin.model.response.PaymentOutResponseModel;
import in.codifi.admin.model.response.PaymentRefResponseModel;
import in.codifi.admin.model.response.PostionAvgPriceResponseModel;
import in.codifi.admin.repository.MarketWatchDAO;
import in.codifi.admin.repository.ReportDAO;
import in.codifi.admin.service.spec.ReportServiceSpec;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.utility.PrepareResponse;
import in.codifi.admin.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class ReportService implements ReportServiceSpec {

	/**
	 * Method to get holdings data
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	ReportDAO reportDAO;
	@Inject
	MarketWatchDAO marketWatchDAO;

	public RestResponse<GenericResponse> getHoldingsData(HoldingsResponseModel holdingsModel) {
		try {
			List<HoldingsResponseModel> result = reportDAO.getHoldingsData(holdingsModel);
			if (!result.isEmpty()) {
				return prepareResponse.prepareSuccessResponseObject(result);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get Position Avg User
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPositionAvgUser(PostionAvgPriceResponseModel postionResponseModel) {
		try {
			List<PostionAvgPriceResponseModel> result = reportDAO.getPositionAvgUser(postionResponseModel);
			if (!result.isEmpty()) {
				return prepareResponse.prepareSuccessResponseObject(result);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get the User Bank details for the given User Id
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getUserBankDetails(BankDetailsResponseModel bankRespModel) {
		try {
			if (StringUtil.isNotNullOrEmpty(bankRespModel.getClientId())) {
				JSONArray bankDetails = reportDAO.getUserBankDetails(bankRespModel.getClientId());
				if (bankDetails != null && bankDetails.size() > 0) {
					return prepareResponse.prepareSuccessResponseObject(bankDetails);
				} else {
					return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
				}
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get the Position avg Price Count
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPosAvgCount() {
		List<PostionAvgPriceReqModel> countResponse = new ArrayList<PostionAvgPriceReqModel>();
		try {
			countResponse = reportDAO.getPosAvgCount();
			if (countResponse != null && countResponse.size() > 0) {
				return prepareResponse.prepareSuccessResponseObject(countResponse);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get Payment reference details by date and status
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPaymentRefreneceDetailsByDate(PaymentRefReqModel paymentRefModel) {
		try {
			List<PaymentRefResponseModel> result = reportDAO.getPaymentRefreneceDetailsByDate(paymentRefModel);
			if (!result.isEmpty()) {
				return prepareResponse.prepareSuccessResponseObject(result);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get Payment reference details by date and status
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPaymentOutDetailsByDate(PaymentRefReqModel paymentOutModel) {
		try {
			List<PaymentOutResponseModel> result = reportDAO.getPaymentOutDetailsByDate(paymentOutModel);
			if (!result.isEmpty()) {
				return prepareResponse.prepareSuccessResponseObject(result);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get market watch data by users
	 * 
	 * @author LOKESH
	 * @param requestContext
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getMarketWatchdata(MarketWatchReqModel mwReqModel) {
		try {
			List<MarketWatchResponseModel> result = marketWatchDAO.getMarketWatchdata(mwReqModel);
			if (!result.isEmpty()) {
				return prepareResponse.prepareSuccessResponseObject(result);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get the Holdings Count
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getHolCount() {
		HoldingCountResponseModel countResponse = new HoldingCountResponseModel();
		try {
			countResponse = reportDAO.getHoldingsCount();
			if (countResponse != null) {
				return prepareResponse.prepareSuccessResponseObject(countResponse);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
