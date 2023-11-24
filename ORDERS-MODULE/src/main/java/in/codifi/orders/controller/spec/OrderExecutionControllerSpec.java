package in.codifi.orders.controller.spec;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.orders.model.request.MarginReqModel;
import in.codifi.orders.model.request.OrderDetails;
import in.codifi.orders.model.response.GenericResponse;

public interface OrderExecutionControllerSpec {

	/**
	 * Method to execute place orders
	 * 
	 * @author Dinesh
	 *
	 * @param orderDetails
	 * @return
	 */
	@Path("/execute")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<List<GenericResponse>> placeOrder(List<OrderDetails> orderDetails);

	/**
	 * Method to modify order
	 * 
	 * @author Nesan
	 *
	 * @param orderDetails
	 * @return
	 */
	@Path("/modify")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> modifyOrder(OrderDetails orderDetails);

	/**
	 * Method to cancel order
	 * 
	 * @author Dinesh
	 *
	 * @param orderDetails
	 * @return
	 */
	@Path("/cancel")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<List<GenericResponse>> cancelOrder(List<OrderDetails> orderDetails);

	/**
	 * 
	 * Method to Get Order Margin
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param marginReqModel
	 * @return
	 */
	@Path("getmargin")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getOrderMargin(MarginReqModel marginReqModel);

	/**
	 * Method to square off positions
	 * 
	 * @author Dinesh
	 *
	 * @param orderDetails
	 * @return
	 */
	@Path("/positions/sqroff")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<List<GenericResponse>> positionSquareOff(List<OrderDetails> orderDetails);

	/**
	 * Method to execute basket orders
	 * 
	 * @author Dinesh
	 *
	 * @param orderDetails
	 * @return
	 */
	@Path("/execute/basket")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	List<GenericResponse> executeBasketOrder(List<OrderDetails> orderDetails);

	/**
	 * 
	 * Method to exit BO/CO orders
	 * 
	 * @author SOWMIYA
	 *
	 * @param orderDetails
	 * @return
	 */
	@Path("/exit/sno")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<List<GenericResponse>> exitSnoOrder(List<OrderDetails> orderDetails);

}
