package in.codifi.admin.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.request.MarketWatchReqModel;
import in.codifi.admin.model.request.PaymentRefReqModel;
import in.codifi.admin.model.response.BankDetailsResponseModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.model.response.HoldingsResponseModel;
import in.codifi.admin.model.response.PostionAvgPriceResponseModel;

public interface ReportServiceSpec {

	/**
	 * Method to get holdings data
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> getHoldingsData(HoldingsResponseModel holdingsModel);

	/**
	 * Method to get Position Avg User
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> getPositionAvgUser(PostionAvgPriceResponseModel postionResponseModel);

	/**
	 * Method to get the User Bank details for the given User Id
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> getUserBankDetails(BankDetailsResponseModel bankRespModel);

	/**
	 * Method to get the Position avg Price Count
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> getPosAvgCount();

	/**
	 * Method to get Payment reference details by date and status
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> getPaymentRefreneceDetailsByDate(PaymentRefReqModel paymentRefModel);

	/**
	 * Method to get Payment reference details by date and status
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> getPaymentOutDetailsByDate(PaymentRefReqModel paymentOutModel);

	/**
	 * Method to get market watch data by users
	 * 
	 * @author LOKESH
	 * @param requestContext
	 * @return
	 */
	RestResponse<GenericResponse> getMarketWatchdata(MarketWatchReqModel mwReqModel);
	
	/**
	 * Method to get the Holdings Count
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> getHolCount();
}
