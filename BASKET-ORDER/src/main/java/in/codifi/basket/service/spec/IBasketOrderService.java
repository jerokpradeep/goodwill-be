package in.codifi.basket.service.spec;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.basket.model.request.BasketMarginRequest;
import in.codifi.basket.model.request.BasketOrderReq;
import in.codifi.basket.model.request.ExecuteBasketOrderReq;
import in.codifi.basket.model.request.SpanMarginReq;
import in.codifi.basket.model.response.GenericResponse;
import in.codifi.cache.model.ClinetInfoModel;

public interface IBasketOrderService {

	/**
	 * method to Create Basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> createBasketName(BasketOrderReq req, ClinetInfoModel info);

	/**
	 * method to Retrieve Basket Details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> basketdetails(ClinetInfoModel info);

	/**
	 * method to Add Scrips to Basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> addBasketScrip(BasketOrderReq req, ClinetInfoModel info);

	/**
	 * method to Delete Scrips in Basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> deleteBasketScrip(BasketOrderReq req, ClinetInfoModel info);

	/**
	 * method to Delete the whole Basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> deleteBasket(int basketId, ClinetInfoModel info);

	/**
	 * method to Rename Basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> updateBasketName(BasketOrderReq req, ClinetInfoModel info);

	/**
	 * method to Retrieve Scrips
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> retrieveScrips(int basketId, ClinetInfoModel info);

	/**
	 * Method to execute basket orders
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param executeBasketOrderReq
	 * @param info
	 * @return
	 */
	RestResponse<List<GenericResponse>> excuteBasketOrder(ExecuteBasketOrderReq executeBasketOrderReq,
			ClinetInfoModel info);

	/**
	 * Method to reset execution status
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param basketOrderReq
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> resetExecutionStatus(int basketId, ClinetInfoModel info);

	/**
	 * Method to get span margin
	 * 
	 * @author SOWMIYA
	 *
	 * @param spanMarginReq
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> getSpanMargin(List<SpanMarginReq> spanMarginReq, ClinetInfoModel info);

	/**
	 * 
	 * Method to update basket scrips
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param basketOrderReq
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> updateBasketScrip(BasketOrderReq basketOrderReq, ClinetInfoModel info);

	/**
	 * method to add basket scrip list
	 * 
	 * @author sowmiya
	 * @param basketOrderReq
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> addBasketScripList(BasketOrderReq basketOrderReq, ClinetInfoModel info);

	/**
	 * method to get basket margin list
	 * 
	 * @author sowmiya
	 * @param reqModel
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> getBasketMarginList(List<BasketMarginRequest> reqModel, ClinetInfoModel info);

}
