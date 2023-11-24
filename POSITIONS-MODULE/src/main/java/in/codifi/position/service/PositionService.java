package in.codifi.position.service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.position.model.request.PositionConversionReq;
import in.codifi.position.model.response.GenericResponse;
import in.codifi.position.service.spec.PositionServiceSpec;
import in.codifi.position.utility.AppConstants;
import in.codifi.position.utility.AppUtil;
import in.codifi.position.utility.PrepareResponse;
import in.codifi.position.utility.StringUtil;
import in.codifi.position.ws.model.RestConversionReq;
import in.codifi.position.ws.model.RestPositionReq;
import in.codifi.position.ws.model.RestPositionSuccessResp;
import in.codifi.position.ws.service.PositionRestService;
import io.quarkus.logging.Log;

@ApplicationScoped
public class PositionService implements PositionServiceSpec {

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	PositionRestService positionRestService;

	@Inject
	AppUtil appUtil;

	/**
	 * Get position
	 * 
	 * @author Nesan
	 */
	@Override
	public RestResponse<GenericResponse> getposition(ClinetInfoModel info) {

		try {

			/** Get user session from cache **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** prepare request body */
			String req = preparePositionReq(info, userSession);
			if (StringUtil.isNullOrEmpty(req))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			return positionRestService.getPositionKambala(req, info.getUserId());

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	private String preparePositionReq(ClinetInfoModel info, String session) {

		String request = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			RestPositionReq reqModel = new RestPositionReq();
			reqModel.setUid(info.getUcc());
			reqModel.setActid(info.getUcc());
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
	 * position conversion
	 * 
	 * @author Nesan
	 */
//	@Override
//	public RestResponse<GenericResponse> positionConversion(PositionConversionReq model, ClinetInfoModel info) {
//		try {
//
//			/** Validate Request **/
//			if (!validatePositionConversionReq(model))
//				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
//
//			/** Get user session from cache **/
//			String userSession = AppUtil.getUserSession(info.getUserId());
//			if (StringUtil.isNullOrEmpty(userSession))
//				return prepareResponse.prepareUnauthorizedResponse();
//
//			/** prepare request body */
//			String req = preparePositionConversionReq(model, userSession, info);
//			if (StringUtil.isNullOrEmpty(req))
//				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
//
//			return positionRestService.positionConversionKambala(req, info.getUserId());
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.error(e.getMessage());
//
//		}
//
//		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
//	}
//	

	/**
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	private String preparePositionConversionReq(PositionConversionReq model, String session, ClinetInfoModel info) {

		String request = "";
		RestConversionReq reqModel = new RestConversionReq();
		try {
			ObjectMapper mapper = new ObjectMapper();
			reqModel.setExch(model.getExchange());
			reqModel.setTsym(URLEncoder.encode(model.getTradingSymbol(), AppConstants.UTF_8));
			reqModel.setQty(model.getQty());
			reqModel.setUid(info.getUcc());
			reqModel.setActid(info.getUcc());
			String product = appUtil.getRestProductType(model.getProduct());
			String prevProduct = appUtil.getRestProductType(model.getPrevProduct());
			if (StringUtil.isNullOrEmpty(product)) {
				Log.error("Product type is empty. Not able to map for place order request");
				return request;
			}
			if (StringUtil.isNullOrEmpty(prevProduct)) {
				Log.error("Product type is empty. Not able to map for place order request");
				return request;
			}
			reqModel.setPrd(product);
			reqModel.setPrevprd(prevProduct);
			reqModel.setTrantype(model.getTransType());
			reqModel.setPostype(model.getPosType());
			reqModel.setOrdersource(model.getOrderSource());

			String json = mapper.writeValueAsString(reqModel);
			request = AppConstants.JDATA + AppConstants.SYMBOL_EQUAL + json + AppConstants.SYMBOL_AND
					+ AppConstants.JKEY + AppConstants.SYMBOL_EQUAL + session;
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}

		return request;

	}

	@Override
	public RestResponse<GenericResponse> positionConversion(PositionConversionReq model, ClinetInfoModel info) {
		try {

			/** Validate Request **/
			if (!validatePositionConversionReq(model))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			/** Get user session from cache **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			String exch = model.getExchange();
			if ((exch.equalsIgnoreCase("NSE") || exch.equalsIgnoreCase("BSE")) && (model.getPrevProduct()
					.equalsIgnoreCase("MIS") && model.getProduct().equalsIgnoreCase("CNC")
					&& (model.getTransType().contentEquals("S") || model.getTransType().contentEquals("SELL")))) {

				Log.error("Can't convert MIS to CNC");
				return prepareResponse.prepareFailedResponse(AppConstants.CANNOT_CONVERT_CNC);
			}

			String getPopsReq = preparePositionReq(info, userSession);
			if (StringUtil.isNullOrEmpty(getPopsReq)) {
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
			}

			List<RestPositionSuccessResp> positionBook = positionRestService.getPositionForConversion(getPopsReq,
					info.getUserId());

			if (StringUtil.isListNullOrEmpty(positionBook))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_DATA);

			/** prepare request body */
			List<String> req = preparePositionConversionReqList(model, userSession, info, positionBook);

			List<String> responseList = convertPositionsSimultaneously(req, info.getUserId());

			boolean isUnauthorized = false;
			boolean isFailed = false;
			String error = "";
			if (StringUtil.isListNotNullOrEmpty(responseList) && responseList.size() > 0) {
				for (String resp : responseList) {
					if (resp.equalsIgnoreCase("Unauthorized")) {
						isUnauthorized = true;
						break;
					}
					if (resp.startsWith(AppConstants.REST_STATUS_NOT_OK)) {
						isFailed = true;
						error = resp;
						break;
					}
				}
				if (isUnauthorized) {
					return prepareResponse.prepareUnauthorizedResponse();
				} else if (isFailed) {
					return prepareResponse.prepareFailedResponse(error);
				} else {
					return prepareResponse.prepareSuccessResponseObject(AppConstants.EMPTY_ARRAY);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());

		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Convert positions Simultaneously
	 * 
	 * @author Dinesh Kumar
	 * @param req
	 * @param userId
	 * @return
	 */
	private List<String> convertPositionsSimultaneously(List<String> req, String userId) {

		List<Callable<String>> tasks = new ArrayList<>();
		for (String posConvReq : req) {
			Callable<String> task = () -> {
				String resp = positionRestService.positionConversion(posConvReq, userId);
				return resp;
			};
			tasks.add(task);
		}
		return tasks.stream().map(callable -> {
			try {
				return callable.call();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
	}

	/**
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	private List<String> preparePositionConversionReqList(PositionConversionReq model, String session,
			ClinetInfoModel info, List<RestPositionSuccessResp> positions) {
		List<String> request = new ArrayList<>();
		try {

			for (RestPositionSuccessResp posModel : positions) {
				String product = "";
				String orderType = "";
				if (StringUtil.isNotNullOrEmpty(posModel.getPrd())) {
					if (posModel.getPrd().equalsIgnoreCase(AppConstants.REST_BRACKET)
							|| posModel.getPrd().equalsIgnoreCase(AppConstants.REST_COVER)) {
						orderType = appUtil.getOrderType(posModel.getPrd());
						product = appUtil.getProductType(AppConstants.REST_PRODUCT_MIS);
					} else {
						orderType = appUtil.getOrderType(AppConstants.REST_PRODUCT_NRML);
						product = appUtil.getProductType(posModel.getPrd());
					}
				}

				if (posModel.getTsym().contentEquals(model.getTradingSymbol())
						&& product.equalsIgnoreCase(model.getPrevProduct())) {
					int netQty = 0;
					if (StringUtil.isNotNullOrEmpty(posModel.getNetqty())) {
						String netQt = posModel.getNetqty();
						netQt = netQt.replace("-", "");
						netQty = Integer.parseInt(netQt);
					}
					int conversionQty = Integer.parseInt(model.getQty());
					if (netQty < conversionQty) {
						Log.error("Qty should be less than net qty");
					} else {
						int dayQty = 0;
						int cfQty = 0;

						if (StringUtil.isNotNullOrEmpty(posModel.getCfsellqty())
								&& Integer.parseInt(posModel.getCfsellqty()) > 0) {
							cfQty = Integer.parseInt(posModel.getCfsellqty());
						} else if (StringUtil.isNotNullOrEmpty(posModel.getCfbuyqty())
								&& Integer.parseInt(posModel.getCfbuyqty()) > 0) {
							cfQty = Integer.parseInt(posModel.getCfbuyqty());
						}
//						String onQty = posModel.getOvernightQty();
//						onQty = onQty.replace("-", "");
//						cfQty = Integer.parseInt(onQty);

						if (StringUtil.isNotNullOrEmpty(posModel.getDaybuyqty())
								&& Integer.parseInt(posModel.getDaybuyqty()) > 0) {
							dayQty = Integer.parseInt(posModel.getDaybuyqty());
						} else if (StringUtil.isNotNullOrEmpty(posModel.getDaysellqty())
								&& Integer.parseInt(posModel.getDaysellqty()) > 0) {
							dayQty = Integer.parseInt(posModel.getDaysellqty());
						}

						if (dayQty >= conversionQty) {
							model.setPosType("Day");
							model.setQty(String.valueOf(conversionQty));
							String req = preparePositionConversionReq(model, session, info);
							System.out.println("Day request - " + req);
							request.add(req);
						} else if (dayQty > 0) {
							model.setPosType("Day");
							model.setQty(String.valueOf(dayQty));
							String req = preparePositionConversionReq(model, session, info);
							System.out.println("Day and CF request - " + req);
							request.add(req);
							model.setPosType("CF");
							model.setQty(String.valueOf(conversionQty - dayQty));
							String cfReq = preparePositionConversionReq(model, session, info);
							System.out.println("CF and Day request - " + cfReq);
							request.add(cfReq);
						} else if (cfQty >= conversionQty) {
							model.setPosType("CF");
							model.setQty(String.valueOf(conversionQty));
							String cfReq = preparePositionConversionReq(model, session, info);
							System.out.println("CF request - " + cfReq);
							request.add(cfReq);
						}
					}
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}

		return request;

	}

	/**
	 * 
	 * Method to validate position conversion request
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param model
	 * @return
	 */
	private boolean validatePositionConversionReq(PositionConversionReq model) {

		if (StringUtil.isNotNullOrEmpty(model.getExchange()) && StringUtil.isNotNullOrEmpty(model.getTradingSymbol())
				&& StringUtil.isNotNullOrEmpty(model.getQty()) && StringUtil.isNotNullOrEmpty(model.getProduct())
				&& StringUtil.isNotNullOrEmpty(model.getPrevProduct())
				&& StringUtil.isNotNullOrEmpty(model.getTransType()) && StringUtil.isNotNullOrEmpty(model.getPosType())
				&& StringUtil.isNotNullOrEmpty(model.getOrderSource())) {
			return true;
		}
		return false;
	}

}
