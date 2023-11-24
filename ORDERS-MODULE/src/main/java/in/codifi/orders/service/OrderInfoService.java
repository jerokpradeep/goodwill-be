package in.codifi.orders.service;

import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.orders.model.request.OrderDetails;
import in.codifi.orders.model.request.OrderReqModel;
import in.codifi.orders.model.response.GenericResponse;
import in.codifi.orders.service.spec.OrderInfoServiceSpec;
import in.codifi.orders.utility.AppConstants;
import in.codifi.orders.utility.AppUtil;
import in.codifi.orders.utility.PrepareResponse;
import in.codifi.orders.utility.StringUtil;
import in.codifi.orders.ws.model.OrderBookReqModel;
import in.codifi.orders.ws.model.OrderHistoryReqModel;
import in.codifi.orders.ws.model.TradeBookReqModel;
import in.codifi.orders.ws.service.OrdersRestService;
import io.quarkus.logging.Log;

@Service
public class OrderInfoService implements OrderInfoServiceSpec {

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	OrdersRestService restService;

	@Inject
	AppUtil appUtil;

	/**
	 * Method to get order book details
	 * 
	 * @author Nesan
	 *
	 * @param orderReqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getOrderBookInfo(ClinetInfoModel info) {
		try {

			/** Verify session **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** prepare request body */

			String req = prepareOrderBookReq(null, userSession, info);
			if (StringUtil.isNullOrEmpty(req))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			/** Get Order Book **/
			return restService.getOrderBookInfo(req, info.getUserId());

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get order book details by product
	 * 
	 * @author Nesan
	 *
	 * @param orderReqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getOrderBookInfoByProduct(OrderDetails orderDetails, ClinetInfoModel info) {
		try {

			/** Verify session **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** Validate Request **/
			if (!validateGetOrderBookReq(orderDetails))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			/** prepare request body */
			String req = prepareOrderBookReq(orderDetails, userSession, info);
			if (StringUtil.isNullOrEmpty(req))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			/** Get Order Book **/
			return restService.getOrderBookInfo(req, info.getUserId());

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to validate place order request
	 * 
	 * @author Dinesh
	 *
	 * @param orderDetails
	 * @return
	 */
	private boolean validateGetOrderBookReq(OrderDetails details) {
		if (StringUtil.isNotNullOrEmpty(details.getProduct()) && StringUtil.isNotNullOrEmpty(details.getOrderType())) {
			return true;

		}
		return false;
	}

	/**
	 * Method to get order book request
	 * 
	 * @author Nesan
	 *
	 * @param orderReqModel
	 * @return
	 */
	private String prepareOrderBookReq(OrderDetails orderDetails, String session, ClinetInfoModel info) {

		String request = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			OrderBookReqModel reqModel = new OrderBookReqModel();
			reqModel.setUid(info.getUserId());

			/** If product exist **/
			if (orderDetails != null && StringUtil.isNotNullOrEmpty(orderDetails.getProduct())) {
				String productType = appUtil.getRestProductType(orderDetails.getProduct());
				if (StringUtil.isNullOrEmpty(productType)) {
					Log.error("Product type is empty. Not able to map for get order book request");
					return request;
				}
				String restOrderType = appUtil.getRestOrderType(orderDetails.getOrderType());
				if (StringUtil.isNullOrEmpty(restOrderType)) {
					Log.error("Order type is empty to map for get order book request");
					return request;
				}
				String orderType = orderDetails.getOrderType().trim();
				if (orderType.equalsIgnoreCase(AppConstants.AMO) || orderType.equalsIgnoreCase(AppConstants.REGULAR)) {
					reqModel.setPrd(productType);
				} else if (orderType.equalsIgnoreCase(AppConstants.BRACKET)
						|| orderType.equalsIgnoreCase(AppConstants.COVER)) {
					reqModel.setPrd(restOrderType);
				} else {
					Log.error("Invalid order type. Not able to map for get order book request");
					return request;
				}
				reqModel.setPrd(orderDetails.getProduct());
			}

			String json = mapper.writeValueAsString(reqModel);
			request = AppConstants.JDATA + AppConstants.SYMBOL_EQUAL + json + AppConstants.SYMBOL_AND
					+ AppConstants.JKEY + AppConstants.SYMBOL_EQUAL + session;
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return request;
	}

	/**
	 * Method to get Trade book details
	 * 
	 * @author Nesan
	 * @param orderReqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getTradeBookInfo(ClinetInfoModel info) {
		try {

			/** Verify session **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** prepare request body */
			String req = prepareTradeBookReq(info, userSession);
			if (StringUtil.isNullOrEmpty(req))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			/** Get Trade book **/
			return restService.getTradeBookInfo(req, info.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to prepare trade book request
	 * 
	 * @param orderReqModel
	 * @param session
	 * @return
	 */
	private String prepareTradeBookReq(ClinetInfoModel info, String session) {

		String request = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			TradeBookReqModel reqModel = new TradeBookReqModel();
			reqModel.setUid(info.getUserId());
			reqModel.setActid(info.getUserId());

			String json = mapper.writeValueAsString(reqModel);
			request = AppConstants.JDATA + AppConstants.SYMBOL_EQUAL + json + AppConstants.SYMBOL_AND
					+ AppConstants.JKEY + AppConstants.SYMBOL_EQUAL + session;
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return request;
	}

	/**
	 * 
	 * Method to get order history
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param model
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getOrderHistory(OrderReqModel model, ClinetInfoModel info) {
		try {
			if (model == null || StringUtil.isNullOrEmpty(model.getOrderNo()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			/** Verify session **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** prepare order history Request **/
			String request = prepareOrderHisRequest(model, userSession, info);
			if (StringUtil.isNullOrEmpty(request))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			/** connect to order history request **/
			return restService.orderHistory(request, info.getUserId());

		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to prepare order history
	 * 
	 * @author sowmiya
	 * 
	 */
	private String prepareOrderHisRequest(OrderReqModel model, String userSession, ClinetInfoModel info) {
		ObjectMapper mapper = new ObjectMapper();
		String request = "";
		try {
			OrderHistoryReqModel reqModel = new OrderHistoryReqModel();
			reqModel.setUid(info.getUserId());
			reqModel.setNorenordno(model.getOrderNo());
			String json = mapper.writeValueAsString(reqModel);
			request = AppConstants.JDATA + AppConstants.SYMBOL_EQUAL + json + AppConstants.SYMBOL_AND
					+ AppConstants.JKEY + AppConstants.SYMBOL_EQUAL + userSession;

		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return request;
	}

}
