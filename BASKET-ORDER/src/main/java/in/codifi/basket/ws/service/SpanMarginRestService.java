package in.codifi.basket.ws.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.basket.config.RestServiceProperties;
import in.codifi.basket.entity.logs.RestAccessLogModel;
import in.codifi.basket.repository.AccessLogManager;
import in.codifi.basket.utils.AppConstants;
import in.codifi.basket.utils.CodifiUtil;
import in.codifi.basket.utils.PrepareResponse;
import in.codifi.basket.utils.StringUtil;
import in.codifi.basket.ws.model.BasketMarginRestRespModel;
import in.codifi.basket.ws.model.SpanMarginRestResp;
import io.quarkus.logging.Log;

@ApplicationScoped
public class SpanMarginRestService {
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	RestServiceProperties props;
	@Inject
	AccessLogManager accessLogManager;

	/**
	 * Method to connect the API
	 * 
	 * @author SOWMIYA
	 * @param baseUrl
	 * @return
	 */
	public String getSpanMargin(String request, String userId) {
		Log.info("span margin Request" + request);
		String response = AppConstants.FAILED_STATUS;
		try {
			RestAccessLogModel accessLogModel = new RestAccessLogModel();
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));
			accessLogModel.setMethod("executeOrders");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setResBody(response.toString());
			URL url = new URL(props.getSpanMarginUrl());
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
				insertRestAccessLogs(accessLogModel);
				Log.error("Unauthorized error in span margin api");
			} else if (responseCode == 200) {
				insertRestAccessLogs(accessLogModel);
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				Log.info("span margin response" + output);
				if (StringUtil.isNotNullOrEmpty(output)) {

					ObjectMapper mapper = new ObjectMapper();
					SpanMarginRestResp spanResponseModel = mapper.readValue(output, SpanMarginRestResp.class);
					if (StringUtil.isNotNullOrEmpty(spanResponseModel.getStat())
							&& spanResponseModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {

						double spanTrade = 0;
						double expoTrade = 0;
						double totalSpan = 0;
						if (StringUtil.isNotNullOrEmpty(spanResponseModel.getSpan_trade())) {
							spanTrade = Double.valueOf(spanResponseModel.getSpan_trade());
						}
						if (StringUtil.isNotNullOrEmpty(spanResponseModel.getExpo_trade())) {
							expoTrade = Double.valueOf(spanResponseModel.getExpo_trade());
						}
						totalSpan = spanTrade + expoTrade;
						return String.valueOf(totalSpan);
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return response;

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
	 * method to get basket margin
	 * 
	 * @author SowmiyaThangaraj
	 * @param request
	 * @return
	 */
	public BasketMarginRestRespModel getBasketMarginList(String request, String userId) {
		Log.info("basket margin Request" + request);
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		ObjectMapper mapper = new ObjectMapper();
		BasketMarginRestRespModel basketResponseModel = new BasketMarginRestRespModel();
		try {
			accessLogModel.setMethod("getBasketMargin");
			accessLogModel.setModule(AppConstants.MODULE_BASKET);
			accessLogModel.setUrl(props.getBasketMarginUrl());
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(userId);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));

			CodifiUtil.trustedManagement();
			URL url = new URL(props.getBasketMarginUrl());
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
				Log.error("Unauthorized error in basket margin api");
				accessLogModel.setResBody("Unauthorized");
				insertRestAccessLogs(accessLogModel);
				basketResponseModel.setEmsg("401 - Unauthorized");
				return basketResponseModel;
			} else if (responseCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				output = bufferedReader.readLine();
				if (StringUtil.isNotNullOrEmpty(output)) {
					accessLogModel.setResBody(output);
					insertRestAccessLogs(accessLogModel);
					basketResponseModel = mapper.readValue(output, BasketMarginRestRespModel.class);
					if (StringUtil.isNotNullOrEmpty(basketResponseModel.getStat())
							&& basketResponseModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						return basketResponseModel;
					} else {
						return basketResponseModel;
					}

				}
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				output = bufferedReader.readLine();
				accessLogModel.setResBody(output);
				insertRestAccessLogs(accessLogModel);
				if (StringUtil.isNotNullOrEmpty(output)) {
					basketResponseModel = mapper.readValue(output, BasketMarginRestRespModel.class);
					if (StringUtil.isNotNullOrEmpty(basketResponseModel.getEmsg()))
						System.out.println("Error Connection in basket margin api. Response code -"
								+ basketResponseModel.getEmsg());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return basketResponseModel;
	}

}
