package in.codifi.admin.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.request.MarketWatchReqModel;
import in.codifi.admin.model.request.PaymentRefReqModel;
import in.codifi.admin.model.response.BankDetailsResponseModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.model.response.HoldingsResponseModel;
import in.codifi.admin.model.response.PostionAvgPriceResponseModel;

public interface ReportControllerSpec {

	/**
	 * Method to get holdings data
	 * 
	 * @author LOKESH
	 * @param requestContext
	 * @return
	 */
	@Path("/getHoldingsData")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getHoldingsData(HoldingsResponseModel holdingsModel);

	/**
	 * Method to get Position Avg User
	 * 
	 * @author LOKESH
	 * @param requestContext
	 * @return
	 */
	@Path("/getPositionAvgUser")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getPositionAvgUser(PostionAvgPriceResponseModel postionResponseModel);

	/**
	 * Method to get the User Bank details for the given User Id
	 * 
	 * @author LOKESH
	 * @param requestContext
	 * @return
	 */
	@Path("/getUserBankDetails")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getUserBankDetails(BankDetailsResponseModel bankRespModel);

	/**
	 * Method to get the Position avg Price Count
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Path("/getPosAvgCount")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getPosAvgCount();

	/**
	 * Method to get Payment reference details by date and status
	 * 
	 * @author LOKESH
	 * @param requestContext
	 * @return
	 */
	@Path("/getPaymentRefreneceDetailsByDate")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getPaymentRefreneceDetailsByDate(PaymentRefReqModel paymentRefModel);

	/**
	 * Method to get Payment out details by date and status
	 * 
	 * @author LOKESH
	 * @param requestContext
	 * @return
	 */
	@Path("/getPaymentOutDetailsByDate")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getPaymentOutDetailsByDate(PaymentRefReqModel paymentOutModel);

	/**
	 * Method to get market watch data by users
	 * 
	 * @author LOKESH
	 * @param requestContext
	 * @return
	 */
	@Path("/getMarketWatchdata")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getMarketWatchdata(MarketWatchReqModel mwReqModel);
	
	/**
	 * Method to get the Holdings Count
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Path("/getHolCount")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getHolCount();
}
