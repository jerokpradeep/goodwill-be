package in.codifi.orders.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.orders.controller.spec.OrderInfoControllerSpec;
import in.codifi.orders.model.request.OrderDetails;
import in.codifi.orders.model.request.OrderReqModel;
import in.codifi.orders.model.response.GenericResponse;
import in.codifi.orders.service.spec.OrderInfoServiceSpec;
import in.codifi.orders.utility.AppConstants;
import in.codifi.orders.utility.AppUtil;
import in.codifi.orders.utility.PrepareResponse;
import in.codifi.orders.utility.StringUtil;
import io.quarkus.logging.Log;

@Path("info")
public class OrderInfoController implements OrderInfoControllerSpec {

	@Inject
	OrderInfoServiceSpec orderInfoService;
	@Inject
	PrepareResponse prepareResponse;
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
	public RestResponse<GenericResponse> getOrderBookInfoByProduct(OrderDetails orderDetails) {

		if (orderDetails == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return orderInfoService.getOrderBookInfoByProduct(orderDetails, info);
	}
	
	/**
	 * Method to get order book details
	 * 
	 * @author Nesan
	 *
	 * @param orderReqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getOrderBookInfo() {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return orderInfoService.getOrderBookInfo(info);
	}

	/**
	 * Method to get trade book details
	 * 
	 * @author Nesan
	 * 
	 * @param orderReqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getTradeBookInfo() {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return orderInfoService.getTradeBookInfo(info);
	}

	/**
	 * 
	 * Method to Get Order history
	 * 
	 * @author SOWMIYA
	 *
	 * @param orderReqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getOrderHistory(OrderReqModel orderReqModel) {

		if (orderReqModel == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}

		return orderInfoService.getOrderHistory(orderReqModel, info);
	}

}
