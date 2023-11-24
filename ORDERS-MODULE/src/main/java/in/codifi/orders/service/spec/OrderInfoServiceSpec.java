package in.codifi.orders.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.orders.model.request.OrderDetails;
import in.codifi.orders.model.request.OrderReqModel;
import in.codifi.orders.model.response.GenericResponse;

public interface OrderInfoServiceSpec {

	RestResponse<GenericResponse> getOrderBookInfo(ClinetInfoModel info);

	RestResponse<GenericResponse> getTradeBookInfo(ClinetInfoModel info);

	RestResponse<GenericResponse> getOrderHistory(OrderReqModel orderReqModel, ClinetInfoModel info);

	RestResponse<GenericResponse> getOrderBookInfoByProduct(OrderDetails orderDetails, ClinetInfoModel info);

}
