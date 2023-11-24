package in.codifi.orders.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.orders.model.request.OrderDetails;
import in.codifi.orders.model.request.OrderReqModel;
import in.codifi.orders.model.response.GenericResponse;

public interface OrderInfoControllerSpec {

	/**
	 * Method to get order book details by product
	 * 
	 * @author Nesan
	 *
	 * @param orderReqModel
	 * @return
	 */
	@Path("/orderbook/product")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Get order book ")
	public RestResponse<GenericResponse> getOrderBookInfoByProduct(OrderDetails orderDetails);

	/**
	 * Method to get order book
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Path("/orderbook")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Get order book ")
	public RestResponse<GenericResponse> getOrderBookInfo();

	/**
	 * Method to get order book details
	 * 
	 * @author Nesan
	 *
	 * @param orderReqModel
	 * @return
	 */
	@Path("/tradebook")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Get trade book")
	public RestResponse<GenericResponse> getTradeBookInfo();

	/**
	 * 
	 * Method to Get Order history
	 * 
	 * @author SOWMIYA
	 *
	 * @param orderReqModel
	 * @return
	 */
	@Path("/history")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getOrderHistory(OrderReqModel orderReqModel);
}
