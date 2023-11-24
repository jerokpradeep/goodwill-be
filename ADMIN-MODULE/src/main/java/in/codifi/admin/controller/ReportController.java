package in.codifi.admin.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.controller.spec.ReportControllerSpec;
import in.codifi.admin.model.request.MarketWatchReqModel;
import in.codifi.admin.model.request.PaymentRefReqModel;
import in.codifi.admin.model.response.BankDetailsResponseModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.model.response.HoldingsResponseModel;
import in.codifi.admin.model.response.PostionAvgPriceResponseModel;
import in.codifi.admin.service.spec.ReportServiceSpec;

@Path("/report")
public class ReportController implements ReportControllerSpec {

	@Inject
	ReportServiceSpec service;

	/**
	 * Method to get holdings data
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getHoldingsData(HoldingsResponseModel holdingsModel) {
		return service.getHoldingsData(holdingsModel);
	}

	/**
	 * Method to get Position Avg User
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPositionAvgUser(PostionAvgPriceResponseModel postionResponseModel) {
		return service.getPositionAvgUser(postionResponseModel);
	}

	/**
	 * Method to get the User Bank details for the given User Id
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getUserBankDetails(BankDetailsResponseModel bankRespModel) {

		return service.getUserBankDetails(bankRespModel);
	}

	/**
	 * Method to get the Position avg Price Count
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPosAvgCount() {
		return service.getPosAvgCount();
	}

	/**
	 * Method to get Payment reference details by date and status
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPaymentRefreneceDetailsByDate(PaymentRefReqModel paymentRefModel) {
		return service.getPaymentRefreneceDetailsByDate(paymentRefModel);
	}

	/**
	 * Method to get Payment reference details by date and status
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPaymentOutDetailsByDate(PaymentRefReqModel paymentOutModel) {
		return service.getPaymentOutDetailsByDate(paymentOutModel);
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
		return service.getMarketWatchdata(mwReqModel);
	}
	
	/**
	 * Method to get the Holdings Count
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getHolCount() {
		return service.getHolCount();
	}
}
