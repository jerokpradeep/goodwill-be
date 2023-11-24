package in.codifi.orders.service.spec;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.orders.model.request.MarginReqModel;
import in.codifi.orders.model.request.OrderDetails;
import in.codifi.orders.model.response.GenericResponse;

public interface OrderExecutionServiceSpec {

	RestResponse<List<GenericResponse>> placeOrder(List<OrderDetails> orderDetails, ClinetInfoModel info);

	RestResponse<GenericResponse> modifyOrder(OrderDetails orderDetails, ClinetInfoModel info);

	RestResponse<List<GenericResponse>> cancelOrder(List<OrderDetails> orderDetails, ClinetInfoModel info);

	RestResponse<GenericResponse> getOrderMargin(MarginReqModel marginReqModel, ClinetInfoModel info);

	RestResponse<List<GenericResponse>> positionSquareOff(List<OrderDetails> orderDetails, ClinetInfoModel info);

	List<GenericResponse> executeBasketOrder(List<OrderDetails> orderDetails, ClinetInfoModel info);

	/**
	 * method to exit BO/CO order
	 * 
	 * @author sowmiya
	 * 
	 * @param orderDetails
	 * @param info
	 * @return
	 */
	RestResponse<List<GenericResponse>> exitSnoOrder(List<OrderDetails> orderDetails, ClinetInfoModel info);

}
