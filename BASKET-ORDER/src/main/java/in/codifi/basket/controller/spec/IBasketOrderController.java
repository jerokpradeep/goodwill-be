package in.codifi.basket.controller.spec;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.basket.model.request.BasketMarginRequest;
import in.codifi.basket.model.request.BasketOrderReq;
import in.codifi.basket.model.request.ExecuteBasketOrderReq;
import in.codifi.basket.model.request.SpanMarginReq;
import in.codifi.basket.model.response.GenericResponse;

public interface IBasketOrderController {

	/**
	 * method to Create Basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/create")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> createBasketName(BasketOrderReq req);

	/**
	 * method to Get All Basket Names
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getBasketNames();

	/**
	 * method to Rename Basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/rename")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> updateBasketName(BasketOrderReq req);

	/**
	 * method to Delete the whole Basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/delete/{basketId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> deleteBasket(@PathParam("basketId") int basketId);

	/**
	 * method to Add Scrips to Basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/add/scrips")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> addBasketScrip(BasketOrderReq basketOrderReq);

	/**
	 * method to Delete Scrips in Basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/delete/scrips")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> deleteBasketScrip(BasketOrderReq basketOrderReq);

	/**
	 * method to Retrieve Scrips
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/get/scrips/{basketId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> retrieveScrips(@PathParam("basketId") int basketId);

	/**
	 * Method to execute basket orders
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param executeBasketOrderReq
	 * @return
	 */
	@Path("/execute")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<List<GenericResponse>> excuteBasketOrder(ExecuteBasketOrderReq executeBasketOrderReq);

	/**
	 * Method to reset execution status
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param basketOrderReq
	 * @return
	 */
	@Path("/reset/{basketId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> resetExecutionStatus(@PathParam("basketId") int basketId);

	/**
	 * Method toget span margin
	 * 
	 * @author SOWMIYA
	 *
	 * @param spanMarginEntity
	 * @return
	 */
	@Path("/spanmargin")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getSpanMargin(List<SpanMarginReq> spanMarginReq);

	/**
	 * 
	 * Method to update basket scrips
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param basketOrderReq
	 * @return
	 */
	@Path("/update/scrips")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> updateBasketScrip(BasketOrderReq basketOrderReq);

	/**
	 * method to Add list of Scrips to Basket
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Path("/add/scrips/list")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> addBasketScripList(BasketOrderReq basketOrderReq);

	/**
	 * Method to get basket margin list
	 * 
	 * @author SOWMIYA
	 *
	 * @param reqModel
	 * @return
	 */
	@Path("/getmargin")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getBasketMarginList(List<BasketMarginRequest> reqModel);


}
