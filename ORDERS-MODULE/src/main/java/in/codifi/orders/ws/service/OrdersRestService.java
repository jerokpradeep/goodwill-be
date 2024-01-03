package in.codifi.orders.ws.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.cache.model.ContractMasterModel;
import in.codifi.orders.config.RestServiceProperties;
import in.codifi.orders.entity.logs.RestAccessLogModel;
import in.codifi.orders.model.response.GenericResponse;
import in.codifi.orders.model.transformation.GenericOrderBookResp;
import in.codifi.orders.model.transformation.GenericOrderMariginRespModel;
import in.codifi.orders.model.transformation.GenericOrderResp;
import in.codifi.orders.model.transformation.GenericTradeBookResp;
import in.codifi.orders.model.transformation.OrderHisRespModel;
import in.codifi.orders.repository.AccessLogManager;
import in.codifi.orders.utility.AppConstants;
import in.codifi.orders.utility.AppUtil;
import in.codifi.orders.utility.CodifiUtil;
import in.codifi.orders.utility.PrepareResponse;
import in.codifi.orders.utility.StringUtil;
import in.codifi.orders.ws.model.CancelOrderRespModel;
import in.codifi.orders.ws.model.ExitSnoOrederRespModel;
import in.codifi.orders.ws.model.ModifyOrderRespModel;
import in.codifi.orders.ws.model.OrderBookFailModel;
import in.codifi.orders.ws.model.OrderBookSuccess;
import in.codifi.orders.ws.model.OrderHistoryFailModel;
import in.codifi.orders.ws.model.OrderHistorySuccessModel;
import in.codifi.orders.ws.model.OrderMariginRespModel;
import in.codifi.orders.ws.model.PlaceOrderRespModel;
import in.codifi.orders.ws.model.TradeBookFailModel;
import in.codifi.orders.ws.model.TradeBookSuccess;
import io.quarkus.logging.Log;

@ApplicationScoped
public class OrdersRestService {

	@Inject
	RestServiceProperties props;

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	AppUtil appUtil;

	@Inject
	AccessLogManager accessLogManager;

	/**
	 * 
	 * Method to execute place order
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param request
	 * @return
	 */
	public RestResponse<GenericResponse> placeOrder(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		PlaceOrderRespModel placeOrderRespModel = new PlaceOrderRespModel();
		Log.info("Place Order Request - " + request);
		try {
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("placeOrder");
			accessLogModel.setModule(AppConstants.MODULE_ORDERS);
			accessLogModel.setUrl(props.getPlaceOrderUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getPlaceOrderUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));

			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 401) {
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				accessLogModel.setResBody("Unauthorized");

			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					placeOrderRespModel = mapper.readValue(output, PlaceOrderRespModel.class);
					/** Bind the response to generic response **/
					if (placeOrderRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						GenericOrderResp extract = bindPlaceOrderData(placeOrderRespModel);
						return prepareResponse.prepareSuccessResponseObject(extract);
					} else if (placeOrderRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_NOT_OK)) {
						return prepareResponse.prepareFailedResponseForRestService(placeOrderRespModel.getEmsg());
					}
				}
			} else {
				System.out.println("Error Connection in place Order api. Rsponse code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					placeOrderRespModel = mapper.readValue(output, PlaceOrderRespModel.class);
					if (StringUtil.isNotNullOrEmpty(placeOrderRespModel.getEmsg()))
						return prepareResponse.prepareFailedResponseForRestService(placeOrderRespModel.getEmsg());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to execute place order
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param request
	 * @return
	 */
	public GenericResponse executePlaceOrder(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		PlaceOrderRespModel placeOrderRespModel = new PlaceOrderRespModel();
		Log.info("Place Order Request - " + request);
		try {
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("executePlaceOrder");
			accessLogModel.setModule(AppConstants.MODULE_ORDERS);
			accessLogModel.setUrl(props.getPlaceOrderUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getPlaceOrderUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 401) {
				Log.error("Unauthorized error in execute order");
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponseBody();

			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					placeOrderRespModel = mapper.readValue(output, PlaceOrderRespModel.class);
					/** Bind the response to generic response **/
					if (placeOrderRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						GenericOrderResp extract = bindPlaceOrderData(placeOrderRespModel);
						return prepareResponse.prepareSuccessResponseBody(extract);
					} else if (placeOrderRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_NOT_OK)) {
						return prepareResponse.prepareFailedResponseBody(placeOrderRespModel.getEmsg());
					}
				}
			} else {
				System.out.println("Error Connection in place Order api. Response code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					placeOrderRespModel = mapper.readValue(output, PlaceOrderRespModel.class);
					if (StringUtil.isNotNullOrEmpty(placeOrderRespModel.getEmsg()))
						return prepareResponse.prepareFailedResponseBody(placeOrderRespModel.getEmsg());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponseBody(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to execute modify order
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param request
	 * @return
	 */
	public RestResponse<GenericResponse> modifyOrder(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		ModifyOrderRespModel modifyOrderRespModel = new ModifyOrderRespModel();
		Log.info("Modify Order Request - " + request);
		try {
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("modifyOrder");
			accessLogModel.setModule(AppConstants.MODULE_ORDERS);
			accessLogModel.setUrl(props.getModifyOrderurl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getModifyOrderurl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));

			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 401) {
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();

			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					modifyOrderRespModel = mapper.readValue(output, ModifyOrderRespModel.class);
					/** Bind the response to generic response **/
					if (modifyOrderRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						GenericOrderResp extract = bindModifyOrderData(modifyOrderRespModel);
						return prepareResponse.prepareSuccessResponseObject(extract);
					} else if (modifyOrderRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_NOT_OK)) {
						return prepareResponse.prepareFailedResponseForRestService(modifyOrderRespModel.getEmsg());
					}
				}

			} else {
				System.out.println("Error Connection in modify Order api. Response code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					modifyOrderRespModel = mapper.readValue(output, ModifyOrderRespModel.class);
					if (StringUtil.isNotNullOrEmpty(modifyOrderRespModel.getEmsg()))
						return prepareResponse.prepareFailedResponseForRestService(modifyOrderRespModel.getEmsg());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to execute cancel order
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param request
	 * @return
	 */
	public RestResponse<GenericResponse> cancelOrder(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		CancelOrderRespModel cancelOrderRespModel = new CancelOrderRespModel();
		Log.info("Cancel Order Request - " + request);
		try {
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("cancelOrder");
			accessLogModel.setModule(AppConstants.MODULE_ORDERS);
			accessLogModel.setUrl(props.getCancelOrderUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getCancelOrderUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 401) {
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();

			} else if (responseCode == 200) {

				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					cancelOrderRespModel = mapper.readValue(output, CancelOrderRespModel.class);
					/** Bind the response to generic response **/
					if (cancelOrderRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						GenericOrderResp extract = bindCancelOrderData(cancelOrderRespModel);
						return prepareResponse.prepareSuccessResponseObject(extract);
					} else if (cancelOrderRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_NOT_OK)) {
						return prepareResponse.prepareFailedResponseForRestService(cancelOrderRespModel.getEmsg());
					}
				}

			} else {
				System.out.println("Error Connection in cancel Order api. Rsponse code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					cancelOrderRespModel = mapper.readValue(output, CancelOrderRespModel.class);
					if (StringUtil.isNotNullOrEmpty(cancelOrderRespModel.getEmsg()))
						return prepareResponse.prepareFailedResponseForRestService(cancelOrderRespModel.getEmsg());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to execute cancel order
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param request
	 * @return
	 */
	public GenericResponse executeCancelOrder(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		CancelOrderRespModel cancelOrderRespModel = new CancelOrderRespModel();
		Log.info("Cancel Order Request - " + request);
		try {
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("executeCancelOrder");
			accessLogModel.setModule(AppConstants.MODULE_ORDERS);
			accessLogModel.setUrl(props.getCancelOrderUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getCancelOrderUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 401) {
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponseBody();
			} else if (responseCode == 200) {

				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					cancelOrderRespModel = mapper.readValue(output, CancelOrderRespModel.class);
					/** Bind the response to generic response **/
					if (cancelOrderRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						GenericOrderResp extract = bindCancelOrderData(cancelOrderRespModel);
						return prepareResponse.prepareSuccessResponseBody(extract);
					} else if (cancelOrderRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_NOT_OK)) {
						return prepareResponse.prepareFailedResponseBody(cancelOrderRespModel.getEmsg());
					}
				}

			} else {
				System.out.println("Error Connection in cancel Order api. Rsponse code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				if (StringUtil.isNotNullOrEmpty(output)) {
					cancelOrderRespModel = mapper.readValue(output, CancelOrderRespModel.class);
					if (StringUtil.isNotNullOrEmpty(cancelOrderRespModel.getEmsg()))
						return prepareResponse.prepareFailedResponseBody(cancelOrderRespModel.getEmsg());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponseBody(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to bind modify order response to generic model
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param model
	 * @return
	 */
	private GenericOrderResp bindModifyOrderData(ModifyOrderRespModel model) {
		GenericOrderResp response = new GenericOrderResp();
		response.setOrderNo(model.getResult());
		response.setRequestTime(model.getRequestTime());
		return response;
	}

	/**
	 * 
	 * Method to bind place order response to generic model
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param model
	 * @return
	 */
	private GenericOrderResp bindPlaceOrderData(PlaceOrderRespModel model) {
		GenericOrderResp response = new GenericOrderResp();
		response.setOrderNo(model.getNorenordno());
		response.setRequestTime(model.getRequestTime());
		return response;
	}

	/**
	 * 
	 * Method to bind cancel order response to generic model
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param model
	 * @return
	 */
	private GenericOrderResp bindCancelOrderData(CancelOrderRespModel model) {
		GenericOrderResp response = new GenericOrderResp();
		response.setOrderNo(model.getResult());
		response.setRequestTime(model.getRequestTime());
		return response;
	}

	/**
	 * Method to get order book
	 * 
	 * @author Nesan
	 * @param req
	 * @return
	 **/
	public RestResponse<GenericResponse> getOrderBookInfo(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		List<OrderBookSuccess> success = null;
		OrderBookFailModel fail = null;
		Log.info("Order book req-" + request);
		try {
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("getOrderBookInfo");
			accessLogModel.setModule(AppConstants.MODULE_ORDERS);
			accessLogModel.setUrl(props.getOrderBookUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getOrderBookUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));

			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 401) {
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();
			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					if (output.startsWith("[{")) {
						success = mapper.readValue(output,
								mapper.getTypeFactory().constructCollectionType(List.class, OrderBookSuccess.class));
						List<GenericOrderBookResp> extract = bindOrderBookData(success);
						return prepareResponse.prepareSuccessResponseObject(extract);
					} else {
						fail = mapper.readValue(output, OrderBookFailModel.class);
						if (StringUtil.isNotNullOrEmpty(fail.getEmsg()))
							return prepareResponse.prepareFailedResponseForRestService(fail.getEmsg());
					}
				}
			} else {
				System.out.println("Error Connection in Order book api");
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					fail = mapper.readValue(output, OrderBookFailModel.class);
					if (StringUtil.isNotNullOrEmpty(fail.getEmsg()))
						return prepareResponse.prepareFailedResponseForRestService(fail.getEmsg());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get Trade book
	 * 
	 * @author Nesan
	 * @param req
	 * @return
	 */
	public RestResponse<GenericResponse> getTradeBookInfo(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		List<TradeBookSuccess> success = null;
		TradeBookFailModel fail = null;
		Log.info("Trade book req-" + request);
		try {
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("getTradeBookInfo");
			accessLogModel.setModule(AppConstants.MODULE_ORDERS);
			accessLogModel.setUrl(props.getTradeBookUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getTradeBookUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));

			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 401) {
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();
			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					if (output.startsWith("[{")) {
						success = mapper.readValue(output,
								mapper.getTypeFactory().constructCollectionType(List.class, TradeBookSuccess.class));
						List<GenericTradeBookResp> extract = bindTradeBookData(success);
						return prepareResponse.prepareSuccessResponseObject(extract);
					} else {
						fail = mapper.readValue(output, TradeBookFailModel.class);
						if (StringUtil.isNotNullOrEmpty(fail.getEmsg()))
							return prepareResponse.prepareFailedResponseForRestService(fail.getEmsg());
					}
				}
			} else {
				System.out.println("Error Connection in Order book api");
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					fail = mapper.readValue(output, TradeBookFailModel.class);
					if (StringUtil.isNotNullOrEmpty(fail.getEmsg()))
						return prepareResponse.prepareFailedResponseForRestService(fail.getEmsg());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to map Generic TradeBookResp layer with kamabala core API values api
	 * response
	 * 
	 * @author Nesan
	 * @param success
	 * @return
	 */
	private List<GenericTradeBookResp> bindTradeBookData(List<TradeBookSuccess> success) {
		List<GenericTradeBookResp> responseList = new ArrayList<>();
		try {
			for (TradeBookSuccess model : success) {
				GenericTradeBookResp extract = new GenericTradeBookResp();
				String exch = model.getExch();
				String token = model.getToken();
				ContractMasterModel coModel = AppUtil.getContractMaster(exch, token);
				if (coModel != null) {
					String scripName = StringUtil.isNotNullOrEmpty(coModel.getFormattedInsName())
							? coModel.getFormattedInsName()
							: "";
					extract.setFormattedInsName(scripName);
				}

				extract.setOrderNo(model.getNorenordno());
				extract.setUserId(model.getUid());
				extract.setActId(model.getActid());
				extract.setExchange(model.getExch());
				extract.setRet(model.getRet());
				extract.setFillId(model.getFlid());
				extract.setFillTime(model.getFltm());
				extract.setTransType(model.getTrantype());
				extract.setTradingSymbol(model.getTsym());
				extract.setQty(model.getQty());
				extract.setToken(model.getToken());
//				extract.setFillshares(model.getFillshares());
				extract.setFillshares(
						StringUtil.isNotNullOrEmpty(model.getFillshares()) ? model.getFillshares() : String.valueOf(0));
				extract.setFillqty(model.getFlqty());
				extract.setPricePrecision(model.getPp());
				extract.setLotSize(model.getLs());
				extract.setTickSize(model.getTi());
				extract.setPrice(model.getPrc());
				extract.setPrcftr(model.getPrcftr());
				extract.setFillprc(model.getFlprc());
				extract.setExchUpdateTime(model.getExchTm());
				extract.setExchOrderId(model.getExchordid());
				extract.setOrderTime(model.getNorentm());
				extract.setPriceType(appUtil.getPriceType(model.getPrctyp()));
				if (StringUtil.isNotNullOrEmpty(model.getPrd())
						&& model.getPrd().equalsIgnoreCase(AppConstants.REST_BRACKET)) {
					extract.setOrderType(AppConstants.BRACKET);
					extract.setProduct(appUtil.getProductType(AppConstants.REST_PRODUCT_MIS));
				} else if (StringUtil.isNotNullOrEmpty(model.getPrd())
						&& model.getPrd().equalsIgnoreCase(AppConstants.REST_COVER)) {
					extract.setOrderType(AppConstants.COVER);
					extract.setProduct(appUtil.getProductType(AppConstants.REST_PRODUCT_MIS));
				} else {
					extract.setOrderType(AppConstants.REGULAR);
					extract.setProduct(appUtil.getProductType(model.getPrd()));
				}

				responseList.add(extract);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return responseList;
	}

	/**
	 * Method to map Generic OrderBook Resp with kamabala core API values response
	 * 
	 * @author Nesan
	 * @param success
	 * @return
	 */
	private List<GenericOrderBookResp> bindOrderBookData(List<OrderBookSuccess> success) {
		List<GenericOrderBookResp> responseList = new ArrayList<>();
		try {
			for (OrderBookSuccess model : success) {
				GenericOrderBookResp extract = new GenericOrderBookResp();
				String exch = model.getExch();
				String token = model.getToken();
				ContractMasterModel coModel = AppUtil.getContractMaster(exch, token);
				if (coModel != null) {
					String scripName = StringUtil.isNotNullOrEmpty(coModel.getFormattedInsName())
							? coModel.getFormattedInsName()
							: "";
					extract.setFormattedInsName(scripName);
				}

				extract.setOrderNo(model.getNorenordno());
				extract.setUserId(model.getUid());
				extract.setActId(model.getActid());
				extract.setExchange(model.getExch());
				extract.setTradingSymbol(model.getTsym());
				extract.setQty(model.getQty());
				extract.setSnoOrderNumber(model.getSnoNum());
				extract.setSnoFillid(model.getSnoFillid());

//				if (StringUtil.isNotNullOrEmpty(model.getOrdenttm())) {
//					String orderTime = DateUtil.milliSecToDate(Long.parseLong(model.getOrdenttm()));
//					extract.setOrderEntryTime(orderTime);
//				}

				extract.setTransType(model.getTrantype());
				extract.setRet(model.getRet());
				extract.setToken(model.getToken());
				extract.setMultiplier(model.getMult());
				extract.setLotSize(model.getLs());
				extract.setTickSize(model.getTi());
				extract.setPrice(model.getPrc());
				extract.setAvgTradePrice(model.getAvgprc());
				extract.setDisclosedQty(model.getDscqty());
				extract.setOrderStatus(model.getStatus());
				extract.setFillShares(
						StringUtil.isNotNullOrEmpty(model.getFillshares()) ? model.getFillshares() : String.valueOf(0));
				extract.setExchUpdateTime(model.getExchTm());
				extract.setExchOrderId(model.getExchordid());
				extract.setRPrice(model.getRprc());
				extract.setRQty(model.getRqty());
				extract.setRejectedReason(model.getRejreason());
				extract.setTriggerPrice(model.getTrgPrc());
				extract.setMktProtection(model.getMktProtection());
				extract.setTarget(model.getBpPrc());
				extract.setStopLoss(model.getBlPrc());
				extract.setTrailingPrice(model.getTrailPrc());
				extract.setOrderTime(model.getNorentm());

				extract.setPriceType(appUtil.getPriceType(model.getPrctyp()));
				if (StringUtil.isNotNullOrEmpty(model.getAmo()) && model.getAmo().equalsIgnoreCase(AppConstants.YES)) {
					extract.setOrderType(AppConstants.AMO);
					extract.setProduct(appUtil.getProductType(model.getPrd()));
				} else if (StringUtil.isNotNullOrEmpty(model.getPrd())
						&& model.getPrd().equalsIgnoreCase(AppConstants.REST_BRACKET)) {
					extract.setOrderType(AppConstants.BRACKET);
					extract.setProduct(appUtil.getProductType(AppConstants.REST_PRODUCT_MIS));
				} else if (StringUtil.isNotNullOrEmpty(model.getPrd())
						&& model.getPrd().equalsIgnoreCase(AppConstants.REST_COVER)) {
					extract.setOrderType(AppConstants.COVER);
					extract.setProduct(appUtil.getProductType(AppConstants.REST_PRODUCT_MIS));
				} else {
					extract.setOrderType(AppConstants.REGULAR);
					extract.setProduct(appUtil.getProductType(model.getPrd()));
				}

				responseList.add(extract);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return responseList;
	}

	/**
	 * 
	 * Method to get Order margin
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param request
	 * @return
	 */
	public RestResponse<GenericResponse> getOrderMargin(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		OrderMariginRespModel mariginRespModel = new OrderMariginRespModel();
		Log.info("Get Order Margin Request - " + request);
		try {
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("getOrderMargin");
			accessLogModel.setModule(AppConstants.MODULE_ORDERS);
			accessLogModel.setUrl(props.getOrderMarginUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getOrderMarginUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			int responseCode = conn.getResponseCode();
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 401) {
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponse();

			} else if (responseCode == 200) {

				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				while ((output = bufferedReader.readLine()) != null) {
					Log.info("Order Margin resp -" + output);
					accessLogModel.setResBody(output);
					insertRestAccessLogs(accessLogModel);
					mariginRespModel = mapper.readValue(output, OrderMariginRespModel.class);
				}
				/** Bind the response to generic response **/
				if (mariginRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
					GenericOrderMariginRespModel extract = bindOrderMarginData(mariginRespModel);
					return prepareResponse.prepareSuccessResponseObject(extract);
				} else if (mariginRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_NOT_OK)) {
					return prepareResponse.prepareFailedResponseForRestService(mariginRespModel.getEmsg());
				}

			} else {
				System.out.println("Error Connection in Get Order Margin api. Rsponse code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				while ((output = bufferedReader.readLine()) != null) {
					accessLogModel.setResBody(output);
					insertRestAccessLogs(accessLogModel);
					mariginRespModel = mapper.readValue(output, OrderMariginRespModel.class);
				}
				return prepareResponse.prepareFailedResponseForRestService(mariginRespModel.getEmsg());
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to bind order margin response to generic response
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param mariginRespModel
	 * @return
	 */
	private GenericOrderMariginRespModel bindOrderMarginData(OrderMariginRespModel mariginRespModel) {
		GenericOrderMariginRespModel respModel = new GenericOrderMariginRespModel();

		/** Change request by Raghuram - 18-05-23 **/
//		respModel.setCash(mariginRespModel.getCash());
//		respModel.setMarginUsed(mariginRespModel.getMarginUsed());
//		respModel.setMarginUsedPrev(mariginRespModel.getMarginUsedPrev());
//		respModel.setOrderMargin(mariginRespModel.getOrderMargin());

		float openingBalance = StringUtil.isNotNullOrEmpty(mariginRespModel.getCash())
				? Float.parseFloat(mariginRespModel.getCash())
				: 0;
		float marginUsed = StringUtil.isNotNullOrEmpty(mariginRespModel.getMarginUsedPrev())
				? Float.parseFloat(mariginRespModel.getMarginUsedPrev())
				: 0;
		float requiredMargin = StringUtil.isNotNullOrEmpty(mariginRespModel.getOrderMargin())
				? Float.parseFloat(mariginRespModel.getOrderMargin())
				: 0;
		float marginShortfall = 0;
		if (StringUtil.isNotNullOrEmpty(mariginRespModel.getRemarks())
				&& mariginRespModel.getRemarks().equalsIgnoreCase("Insufficient Balance")) {
			marginShortfall = StringUtil.isNotNullOrEmpty(mariginRespModel.getMarginUsed())
					? Float.parseFloat(mariginRespModel.getMarginUsed())
					: 0;
		}
		respModel.setOpeningBalance(openingBalance);
		respModel.setMarginUsed(marginUsed);
		respModel.setRequiredMargin(requiredMargin);
		respModel.setMarginShortfall(marginShortfall);
		respModel.setAvailableMargin(openingBalance - marginUsed);
		respModel.setRemarks(mariginRespModel.getRemarks());
		return respModel;
	}

	/**
	 * 
	 * Method to get Order history
	 * 
	 * @author sowmiya
	 *
	 * @param request
	 * @return
	 */
	public RestResponse<GenericResponse> orderHistory(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		Log.info("order history request" + request);
		try {
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("orderHistory");
			accessLogModel.setModule(AppConstants.MODULE_ORDERS);
			accessLogModel.setUrl(props.getOrderHistoryUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			String baseUrl = props.getOrderHistoryUrl();
			URL url = new URL(baseUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;

			if (responseCode == 401) {
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				Log.error("Unauthorized error in order history");
				return prepareResponse.prepareUnauthorizedResponse();
			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				while ((output = bufferedReader.readLine()) != null) {
					Log.info("order history response" + output);
					accessLogModel.setResBody(output);
					insertRestAccessLogs(accessLogModel);
					if (output.startsWith("[{")) {
						List<OrderHistorySuccessModel> repModel = mapper.readValue(output, mapper.getTypeFactory()
								.constructCollectionType(List.class, OrderHistorySuccessModel.class));
						List<OrderHisRespModel> response = bindOrderHistData(repModel);
						return prepareResponse.prepareSuccessResponseObject(response);
					} else if (output.startsWith("[]")) {
						return prepareResponse.prepareFailedResponse(AppConstants.REST_NO_DATA);
					} else {
						OrderHistoryFailModel fail = mapper.readValue(output, OrderHistoryFailModel.class);
						if (StringUtil.isNotNullOrEmpty(fail.getEmsg()))
							return prepareResponse.prepareFailedResponseForRestService(fail.getEmsg());
					}
				}
			} else {
				Log.info("Error Connection in exchange message. Response Code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					OrderHistoryFailModel RespFailModel = mapper.readValue(output, OrderHistoryFailModel.class);
					if (StringUtil.isNotNullOrEmpty(RespFailModel.getEmsg()))
						return prepareResponse.prepareFailedResponseForRestService(RespFailModel.getEmsg());
				}
			}
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/*
	 * method to bind order history data
	 * 
	 * @Author SOWMIYA
	 */

	public List<OrderHisRespModel> bindOrderHistData(List<OrderHistorySuccessModel> repModel) {
		List<OrderHisRespModel> responseModel = new ArrayList<>();
		try {

			for (OrderHistorySuccessModel successModel : repModel) {
				OrderHisRespModel response = new OrderHisRespModel();
				response.setUserId(successModel.getUid());
				response.setActId(successModel.getActid());
				response.setOrderNo(successModel.getNorenordno());
				response.setExchange(successModel.getExch());
				response.setExchOrderNo(successModel.getExchordid());
				response.setExchTime(successModel.getExchTm());
				response.setAvgPrice(successModel.getAvgprc());
				response.setDisclosedQty(successModel.getDscqty());
//				response.setFillshares(successModel.getFillshares());
				response.setFillshares(
						StringUtil.isNotNullOrEmpty(successModel.getFillshares()) ? successModel.getFillshares()
								: String.valueOf(0));
				response.setLotSize(successModel.getLs());
				response.setTickSize(successModel.getTi());
				response.setPrice(successModel.getPrc());
				response.setPricePrecision(successModel.getPp());
				response.setToken(successModel.getToken());
				response.setTransType(successModel.getTrantype());
				response.setTime(successModel.getNorentm());
				response.setTradingSymbol(successModel.getTsym());
				response.setStatus(successModel.getStatus());
				response.setQuantity(successModel.getQty());
				response.setRemarks(successModel.getRemarks());
				response.setReport(successModel.getRpt());
				response.setRet(successModel.getRet());

				response.setPriceType(successModel.getPrctyp());
				response.setProduct(successModel.getPrd());

				response.setPriceType(appUtil.getPriceType(successModel.getPrctyp()));
				if (StringUtil.isNotNullOrEmpty(successModel.getAmo())
						&& successModel.getAmo().equalsIgnoreCase(AppConstants.YES)) {
					response.setOrderType(AppConstants.AMO);
					response.setProduct(appUtil.getProductType(successModel.getPrd()));
				} else if (StringUtil.isNotNullOrEmpty(successModel.getPrd())
						&& successModel.getPrd().equalsIgnoreCase(AppConstants.REST_BRACKET)) {
					response.setOrderType(AppConstants.BRACKET);
					response.setProduct(appUtil.getProductType(AppConstants.REST_PRODUCT_MIS));
				} else if (StringUtil.isNotNullOrEmpty(successModel.getPrd())
						&& successModel.getPrd().equalsIgnoreCase(AppConstants.REST_COVER)) {
					response.setOrderType(AppConstants.COVER);
					response.setProduct(appUtil.getProductType(AppConstants.REST_PRODUCT_MIS));
				} else {
					response.setOrderType(AppConstants.REGULAR);
					response.setProduct(appUtil.getProductType(successModel.getPrd()));
				}
				responseModel.add(response);
			}

		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return responseModel;
	}

	/**
	 * 
	 * Method to insert rest service access logs
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param accessLogModel
	 */
	public void insertRestAccessLogs(RestAccessLogModel accessLogModel) {

		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					accessLogManager.insert24RestAccessLog(accessLogModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		pool.shutdown();
	}

	/**
	 * method to exit bo orders
	 * 
	 * @author sowmiya
	 * @param req
	 * @param userId
	 * @return
	 */
	public GenericResponse executeExitSnoOrder(String request, String userId) {
		ObjectMapper mapper = new ObjectMapper();
		ExitSnoOrederRespModel exitSnoOrederRespModel = new ExitSnoOrederRespModel();
		Log.info("Exit Sno Order Request - " + request);
		try {

			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setMethod("executeExitSnoOrder");
			accessLogModel.setModule(AppConstants.MODULE_ORDERS);
			accessLogModel.setUrl(props.getExitSnoOrderUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getExitSnoOrderUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.POST_METHOD);
			conn.setRequestProperty(AppConstants.ACCEPT, AppConstants.TEXT_PLAIN);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = request.getBytes(AppConstants.UTF_8);
				os.write(input, 0, input.length);
			}
			int responseCode = conn.getResponseCode();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			BufferedReader bufferedReader;
			String output = null;
			if (responseCode == 401) {
				Log.error("Unauthorized error in kambala position api");
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				return prepareResponse.prepareUnauthorizedResponseBody();

			} else if (responseCode == 200) {

				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					exitSnoOrederRespModel = mapper.readValue(output, ExitSnoOrederRespModel.class);
					/** Bind the response to generic response **/
					if (exitSnoOrederRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						return prepareResponse.prepareSuccessResponseBody(exitSnoOrederRespModel.getDmsg());
					} else if (exitSnoOrederRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_NOT_OK)) {
						return prepareResponse.prepareFailedResponseBody(exitSnoOrederRespModel.getEmsg());
					}
				}

			} else {
				System.out.println("Error Connection in Exit Sno Order api. Response code -" + responseCode);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					exitSnoOrederRespModel = mapper.readValue(output, ExitSnoOrederRespModel.class);
					if (StringUtil.isNotNullOrEmpty(exitSnoOrederRespModel.getEmsg()))
						return prepareResponse.prepareFailedResponseBody(exitSnoOrederRespModel.getEmsg());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponseBody(AppConstants.FAILED_STATUS);

	}
}
