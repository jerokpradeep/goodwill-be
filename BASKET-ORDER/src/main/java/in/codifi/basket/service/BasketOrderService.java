package in.codifi.basket.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.jboss.resteasy.reactive.RestResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.basket.entity.primary.BasketNameEntity;
import in.codifi.basket.entity.primary.BasketScripEntity;
import in.codifi.basket.model.request.BasketMarginRequest;
import in.codifi.basket.model.request.BasketOrderReq;
import in.codifi.basket.model.request.ExecuteBasketOrderReq;
import in.codifi.basket.model.request.RetrieveBasketModel;
import in.codifi.basket.model.request.ScripRequestModel;
import in.codifi.basket.model.request.SpanMarginReq;
import in.codifi.basket.model.response.BasketMarginRespModel;
import in.codifi.basket.model.response.GenericResponse;
import in.codifi.basket.model.response.SpanMarginResp;
import in.codifi.basket.repository.BasketOrderEntityManager;
import in.codifi.basket.repository.BasketOrderRepository;
import in.codifi.basket.repository.BasketScripRepository;
import in.codifi.basket.service.spec.IBasketOrderService;
import in.codifi.basket.utils.AppConstants;
import in.codifi.basket.utils.AppUtil;
import in.codifi.basket.utils.PrepareResponse;
import in.codifi.basket.utils.StringUtil;
import in.codifi.basket.ws.model.BasketListModel;
import in.codifi.basket.ws.model.BasketMarginRestReqModel;
import in.codifi.basket.ws.model.BasketMarginRestRespModel;
import in.codifi.basket.ws.model.OrderDetails;
import in.codifi.basket.ws.model.SpanMarginPos;
import in.codifi.basket.ws.model.SpanMarginRestReq;
import in.codifi.basket.ws.service.InternalRestService;
import in.codifi.basket.ws.service.SpanMarginRestService;
import in.codifi.cache.CacheService;
import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.cache.model.ContractMasterModel;
import io.quarkus.logging.Log;

@ApplicationScoped
public class BasketOrderService implements IBasketOrderService {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	BasketOrderRepository basketOrderRepository;
	@Inject
	BasketScripRepository basketScripRepository;
	@Inject
	BasketOrderEntityManager basketOrderEntityManager;
	@Inject
	CacheService cacheService;
	@Inject
	InternalRestService internalRestService;
	@Inject
	AppUtil appUtil;
	@Inject
	SpanMarginRestService spanMarginRestService;

	/**
	 * 
	 * Method to get only basket names by userId
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<BasketNameEntity> getBasketNameByUserId(String userId) {
		List<BasketNameEntity> basketNameEntity = new ArrayList<>();
		try {
			basketNameEntity = basketOrderRepository.findAllByUserId(userId);
			if (StringUtil.isListNotNullOrEmpty(basketNameEntity)) {
				basketNameEntity.stream().forEach(e -> e.getBasketScrip().clear());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return basketNameEntity;
	}

	/**
	 * method to get basket scrips
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	private List<BasketScripEntity> getBasketScrips(long basketId) {
		List<BasketScripEntity> basketScripEntity = new ArrayList<>();
		try {
			basketScripEntity = basketScripRepository.findByBasketId(basketId);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return basketScripEntity;
	}

	/**
	 * method to Create basket order
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public BasketNameEntity createBasket(BasketNameEntity basketOrder) {
		BasketNameEntity saveBasketName = new BasketNameEntity();
		try {
			ExecutorService pool = Executors.newSingleThreadExecutor();
			pool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						basketOrderRepository.save(basketOrder);
					} catch (Exception ex) {
						ex.printStackTrace();
						Log.info(ex.getMessage());
					}
				}
			});
			pool.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return saveBasketName;
	}

	/**
	 * method to get Basket without scrip
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<BasketNameEntity> getBasketName(List<BasketNameEntity> list) {
		try {
			list.stream().forEach(e -> e.getBasketScrip().clear());
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return list;
	}

	/**
	 * method to get basket id
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public long getMaxBasketId() {
		long bId = 1;
		try {
			List<BasketNameEntity> basketNameEntity = basketOrderRepository.findAll();
			if (StringUtil.isListNotNullOrEmpty(basketNameEntity)) {
				Optional<BasketNameEntity> getBasketId = basketNameEntity.stream()
						.max(Comparator.comparing(BasketNameEntity::getBasketId));
				BasketNameEntity maxBasketId = getBasketId.get();
				bId = maxBasketId.getBasketId() + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return bId;
	}

	/**
	 * method to get return response
	 * 
	 * @author Gowthaman M
	 * @return
	 */
//	public List<BasketNameEntity> returnResponse(String userId) {
//		List<BasketNameEntity> responseEntity = new ArrayList<>();
//		List<BasketNameEntity> basketEntity = new ArrayList<>();
//		try {
//			List<BasketNameEntity> getCacheData = HazleCacheController.getInstance().getBasketOrderDto().get(userId);
//			for (int i = 0; i < getCacheData.size(); i++) {
//				if (getCacheData.get(i).getBasketScrip() != null) {
//					responseEntity.add(getCacheData.get(i));
//				} else {
//					basketEntity.add(getCacheData.get(i));
//				}
//			}
//			responseEntity = getBasketName(responseEntity);
//			responseEntity.addAll(basketEntity);
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.error(e.getMessage());
//		}
//		return responseEntity;
//	}

	/**
	 * method to validate the Basket Id and userId are match
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public boolean getValiedBasket(long basketId, String userId) {
		BasketNameEntity basketNameEntity = new BasketNameEntity();
		try {
			basketNameEntity = basketOrderRepository.findValiedBasketId(basketId, userId);
			if (basketNameEntity != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return false;
	}

	/**
	 * Method to basketName is exist or not
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public BasketNameEntity getBasketNameAndUserId(String basketName, String userId) {
		BasketNameEntity basketNameEntity = new BasketNameEntity();
		try {
			basketNameEntity = basketOrderRepository.findByBasketName(basketName, userId);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
			basketNameEntity = null;
		}
		return basketNameEntity;
	}

	/**
	 * method to Create Basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> createBasketName(BasketOrderReq basketOrderReq, ClinetInfoModel info) {
		try {
			if (StringUtil.isNullOrEmpty(basketOrderReq.getBasketName()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			/** validate basket name is exit or not **/
			if (getBasketNameAndUserId(basketOrderReq.getBasketName(), info.getUserId()) != null)
				return prepareResponse.prepareFailedResponse(AppConstants.BASKET_NAME_ALREADY_EXIST);

			/** Get basketId to insert **/
			BasketNameEntity entity = new BasketNameEntity();
			entity.setBasketId(getMaxBasketId());
			entity.setUserId(info.getUserId());
			entity.setCreatedBy(info.getUserId());
			entity.setBasketName(basketOrderReq.getBasketName());
			/** Method to create basket **/
			BasketNameEntity basketNameEntity = basketOrderRepository.save(entity);

			/** Method to get return latest data **/
			if (basketNameEntity != null) {
				return prepareResponse
						.prepareSuccessResponseObject(basketOrderEntityManager.getBasketReports(info.getUserId()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to Add Scrips to Basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> addBasketScrip(BasketOrderReq basketOrderReq, ClinetInfoModel info) {
		try {
			/** Validate the request **/
			if (!validateAddBasketReq(basketOrderReq))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			/** Method to validate basketId **/
			if (!getValiedBasket(basketOrderReq.getBasketId(), info.getUserId()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_BASKET);
			/** Prepare scrip entity **/
			BasketScripEntity basketScripEntity = prepareBasketScripEntity(basketOrderReq, info);
			BasketScripEntity saveBasketScrip = basketScripRepository.saveAndFlush(basketScripEntity);
			if (saveBasketScrip != null)
				/** Return latest data **/
				return prepareResponse.prepareSuccessResponseObject(getBasketScrips(basketOrderReq.getBasketId()));
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to validate Basket Scrip
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public boolean validateAddBasketReq(BasketOrderReq basketOrder) {
		try {
			if (basketOrder.getBasketId() > 0 && StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getExchange())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getToken())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getQty())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getPrice())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getProduct())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getTransType())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getPriceType())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getOrderType())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getRet())) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return false;
	}

	/**
	 * method to Map Basket Scrips
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public BasketScripEntity prepareBasketScripEntity(BasketOrderReq basketOrderReq, ClinetInfoModel info) {
		BasketScripEntity basketScripEntity = new BasketScripEntity();

		try {

			ContractMasterModel contractMasterModel = appUtil.getContractInfo(basketOrderReq.getScrips().getExchange(),
					basketOrderReq.getScrips().getToken());
			if (contractMasterModel != null) {
				basketScripEntity.setExchange(contractMasterModel.getExch());
				basketScripEntity.setExpiry(contractMasterModel.getExpiry());
				basketScripEntity.setToken(contractMasterModel.getToken());
				basketScripEntity.setTradingSymbol(contractMasterModel.getTradingSymbol());
				basketScripEntity.setFormattedInsName(contractMasterModel.getFormattedInsName());
				basketScripEntity.setWeekTag(contractMasterModel.getWeekTag());

				basketScripEntity.setBasketId(basketOrderReq.getBasketId());
				basketScripEntity.setCreatedBy(info.getUserId());
				basketScripEntity.setDisClosedQty(basketOrderReq.getScrips().getDisClosedQty());
				basketScripEntity.setMktProtection(basketOrderReq.getScrips().getMktProtection());
				basketScripEntity.setOrderType(basketOrderReq.getScrips().getOrderType());
				basketScripEntity.setPrice(basketOrderReq.getScrips().getPrice());
				basketScripEntity.setPriceType(basketOrderReq.getScrips().getPriceType());
				basketScripEntity.setProduct(basketOrderReq.getScrips().getProduct());
				basketScripEntity.setQty(basketOrderReq.getScrips().getQty());
				basketScripEntity.setRet(basketOrderReq.getScrips().getRet());
				basketScripEntity.setSortOrder(basketOrderReq.getScrips().getSortOrder());
				basketScripEntity.setStopLoss(basketOrderReq.getScrips().getStopLoss());
				basketScripEntity.setTarget(basketOrderReq.getScrips().getTarget());
				basketScripEntity.setTrailingStopLoss(basketOrderReq.getScrips().getTrailingStopLoss());
				basketScripEntity.setTransType(basketOrderReq.getScrips().getTransType());
				basketScripEntity.setTriggerPrice(basketOrderReq.getScrips().getTriggerPrice());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return basketScripEntity;
	}

	/**
	 * method to Delete Scrips in Basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> deleteBasketScrip(BasketOrderReq req, ClinetInfoModel info) {
		try {
			if (StringUtil.isListNullOrEmpty(req.getScripsId()) || req.getBasketId() < 1)
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			/** Validate the basket Id **/
			BasketNameEntity basketNameEntity = basketOrderRepository.findByBasketId(req.getBasketId());
			if (basketNameEntity == null)
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_BASKET);
			long deleteBasketScrip = basketOrderEntityManager.deleteBasketScrip(req.getBasketId(), req.getScripsId());
			if (deleteBasketScrip <= 0)
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			/** Return latest data **/
			return prepareResponse.prepareSuccessResponseObject(getBasketScrips(req.getBasketId()));
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to Delete the Basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> deleteBasket(int basketId, ClinetInfoModel info) {
		try {
			if (basketId < 1)
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			/** Method to validate basketId **/
			if (!getValiedBasket(basketId, info.getUserId()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_BASKET);
			long deleteBasketOrder = basketOrderRepository.deleteByBasketId(basketId);
			if (deleteBasketOrder <= 0)
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			/** Return latest data **/
			return prepareResponse
					.prepareSuccessResponseObject(basketOrderEntityManager.getBasketReports(info.getUserId()));
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to Rename Basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> updateBasketName(BasketOrderReq basketOrder, ClinetInfoModel info) {
		try {
			if (!validateUpdateBasketNameReq(basketOrder))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			/** Method to basketName is exist or not **/
			if (getBasketNameAndUserId(basketOrder.getBasketName(), info.getUserId()) != null)
				return prepareResponse.prepareFailedResponse(AppConstants.BASKET_NAME_ALREADY_EXIST);
			int renameBasketName = basketOrderRepository.updateBasketName(basketOrder.getBasketName(), info.getUserId(),
					info.getUserId(), basketOrder.getBasketId());
			if (renameBasketName > 0) {
				/** Return latest data **/
				return prepareResponse
						.prepareSuccessResponseObject(basketOrderEntityManager.getBasketReports(info.getUserId()));
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_BASKET);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to validate Basket Name
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public boolean validateUpdateBasketNameReq(BasketOrderReq basketOrder) {
		try {
			if (StringUtil.isNotNullOrEmpty(basketOrder.getBasketName()) && basketOrder.getBasketId() > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return false;
	}

	/**
	 * method to Retrieve Basket Details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> basketdetails(ClinetInfoModel info) {
		try {
			List<RetrieveBasketModel> scripReport = basketOrderEntityManager.getBasketReports(info.getUserId());
			if (StringUtil.isListNullOrEmpty(scripReport))
				return prepareResponse.prepareSuccessMessage(AppConstants.NO_RECORDS_FOUND);
			return prepareResponse.prepareSuccessResponseObject(scripReport);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to Retrieve Scrips
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> retrieveScrips(int basketId, ClinetInfoModel info) {
		try {
			if (basketId < 1)
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			if (!getValiedBasket(basketId, info.getUserId()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_BASKET);

			List<BasketScripEntity> scrips = basketScripRepository.findByBasketId(basketId);
			if (StringUtil.isListNullOrEmpty(scrips))
				return prepareResponse.prepareSuccessMessage(AppConstants.NO_RECORDS_FOUND);
			return prepareResponse.prepareSuccessResponseObject(scrips);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to execute basket order
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param executeBasketOrderReq
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<List<GenericResponse>> excuteBasketOrder(ExecuteBasketOrderReq executeBasketOrderReq,
			ClinetInfoModel info) {
		String orderResp = "";
		try {
			if (executeBasketOrderReq.getBasketId() < 1 || executeBasketOrderReq.getScrips().size() <= 0)
				return prepareResponse.prepareFailedResponseForList(AppConstants.INVALID_PARAMETER);

			for (ScripRequestModel model : executeBasketOrderReq.getScrips()) {
				if (!validateExecuteBasketReq(model))
					return prepareResponse.prepareFailedResponseForList(AppConstants.INVALID_PARAMETER);
			}

			/** Get access **/
			String token = appUtil.getAccessToken();
			if (StringUtil.isNullOrEmpty(token))
				return prepareResponse.prepareUnauthorizedResponseForList();

			if (!getValiedBasket(executeBasketOrderReq.getBasketId(), info.getUserId()))
				return prepareResponse.prepareFailedResponseForList(AppConstants.INVALID_BASKET);
			List<OrderDetails> request = prepareExecuteOrderReq(executeBasketOrderReq.getScrips());

			if (request == null || request.size() <= 0)
				return prepareResponse.prepareFailedResponseForList(AppConstants.FAILED_STATUS);

			List<GenericResponse> res = internalRestService.executeOrders(request, token);
			try {
				ObjectMapper mapper = new ObjectMapper();
				orderResp = mapper.writeValueAsString(res);
				Log.info("executeOrderResponse" + orderResp);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (res != null) {
				Log.info("updateExecutionStatus" + orderResp);
				int isUpdate = basketOrderEntityManager.updateExecuteOrdersIntoDB(info.getUserId(),
						executeBasketOrderReq.getBasketId());
				Log.info("isUpdate" + isUpdate);
				if (isUpdate > 0)
					return prepareResponse.prepareSuccessMessageForList(AppConstants.BASKET_EXECUTED);
			}

		} catch (ClientWebApplicationException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			int statusCode = e.getResponse().getStatus();
			if (statusCode == 401) {
				return prepareResponse.prepareUnauthorizedResponseForList();
			}
		}
		return prepareResponse.prepareFailedResponseForList(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to validate execute basket
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public boolean validateExecuteBasketReq(ScripRequestModel basketOrder) {
		try {
			if (StringUtil.isNotNullOrEmpty(basketOrder.getExchange())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getToken())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getTradingSymbol())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getQty())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getPrice())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getProduct())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getTransType())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getPriceType())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getOrderType())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getRet())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getSource())) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return false;
	}

	/**
	 * Methos to preapare request to execute basket order
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param scrips
	 * @return
	 */
	private List<OrderDetails> prepareExecuteOrderReq(List<ScripRequestModel> scrips) {
		List<OrderDetails> request = new ArrayList<>();
		for (ScripRequestModel model : scrips) {
			OrderDetails details = new OrderDetails();
			details.setExchange(model.getExchange());
			details.setTradingSymbol(model.getTradingSymbol());
			details.setQty(model.getQty());
			details.setPrice(model.getPrice());
			details.setOrderType(model.getOrderType());
			details.setProduct(model.getProduct());
			details.setPriceType(model.getPriceType());
			details.setTransType(model.getTransType());
			details.setRet(model.getRet());
			details.setTriggerPrice(model.getTriggerPrice());
			details.setDisclosedQty(model.getDisClosedQty());
			details.setMktProtection(model.getMktProtection());
			details.setTarget(model.getTarget());
			details.setStopLoss(model.getStopLoss());
			details.setTrailingStopLoss(model.getTrailingStopLoss());
			details.setSource(model.getSource());
			request.add(details);
		}
		return request;

	}

	/**
	 * Method to reset execution status
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param basketOrderReq
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> resetExecutionStatus(int basketId, ClinetInfoModel info) {
		try {

			/** Method to validate basketId **/
			if (!getValiedBasket(basketId, info.getUserId()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_BASKET);
			int update = basketOrderRepository.updateExecutionStatus("0", info.getUserId(), info.getUserId(), basketId);
			if (update > 0) {
				/** Return latest data **/
				return prepareResponse
						.prepareSuccessResponseObject(basketOrderEntityManager.getBasketReports(info.getUserId()));
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_BASKET);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get span margin
	 * 
	 * @author SOWMIYA
	 *
	 * @param spanMarginEntity
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getSpanMargin(List<SpanMarginReq> spanMarginReq, ClinetInfoModel info) {
		try {

//			/** Get user session from cache **/
//			String userSession = AppUtil.getUserSession(info.getUserId());
//			if (StringUtil.isNullOrEmpty(userSession))
//				return prepareResponse.prepareUnauthorizedResponse();

			for (SpanMarginReq model : spanMarginReq) {
				if (!validateSpanMarginReq(model))
					return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			}

			return getSpanMargin(spanMarginReq, info.getUserId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to validate span margin request
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param model
	 * @return
	 */
	private boolean validateSpanMarginReq(SpanMarginReq model) {
		try {
			if (StringUtil.isNotNullOrEmpty(model.getExchange()) && StringUtil.isNotNullOrEmpty(model.getToken())
					&& StringUtil.isNotNullOrEmpty(model.getQty()) && StringUtil.isNotNullOrEmpty(model.getPrice())
					&& StringUtil.isNotNullOrEmpty(model.getTransType())) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return false;
	}

	/**
	 * 
	 * Method to prepare span margin request
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param spanMarginReq
	 * @param userSession
	 * @return
	 */
	private RestResponse<GenericResponse> getSpanMargin(List<SpanMarginReq> spanMarginReq, String userId) {
		try {

			ObjectMapper mapper = new ObjectMapper();
			double totalSpan = 0;
			List<SpanMarginPos> marginPosList = new ArrayList<>();
			for (SpanMarginReq entity : spanMarginReq) {
				SpanMarginPos restReqModel = new SpanMarginPos();
				String exch = entity.getExchange();
				String token = entity.getToken();
				int qty = Integer.valueOf(entity.getQty());
				double price = Double.valueOf(entity.getPrice());
				String transType = entity.getTransType();
				if (exch.equalsIgnoreCase("NSE") || exch.equalsIgnoreCase("BSE")) {
					double tempPrice = qty * price;
					tempPrice = tempPrice / 5;
					totalSpan = totalSpan + tempPrice;
//				} else {
//					ContractMasterModel contractMasterModel = HazelcastConfig.getInstance().getContractMaster()
//							.get(exch + "_" + token);
//					if (contractMasterModel != null) {
//						if (exch.equalsIgnoreCase("MCX")) {
//							qty = qty * Integer.parseInt(contractMasterModel.getLotSize());
//						}
//						restReqModel.setPrd("H");
//						restReqModel.setExch(exch);
//						String exDate = "";
//						if (contractMasterModel.getExpiry() != null) {
//							SimpleDateFormat formater = new SimpleDateFormat("dd-MMM-yyyy");
//							exDate = formater.format(contractMasterModel.getExpiry());
//							restReqModel.setExd(exDate.toUpperCase());
//						}
//						restReqModel.setInstname(contractMasterModel.getInsType());
//						restReqModel.setLs(contractMasterModel.getLotSize());
//						if(StringUtil.isNotNullOrEmpty(contractMasterModel.getStrikePrice())) {
//							restReqModel.setStrprc(contractMasterModel.getStrikePrice());
//						}
//
//						if (contractMasterModel.getInsType().startsWith("OPT")
//								|| contractMasterModel.getInsType().endsWith("OPT")) {
//							if (contractMasterModel.getOptionType().equalsIgnoreCase("PE")) {
//								restReqModel.setOptt("PE");
//							} else if (contractMasterModel.getOptionType().equalsIgnoreCase("CE")) {
//								restReqModel.setOptt("CE");
//							}
//						} else {
//							restReqModel.setOptt("XX");
//						}
//
//						restReqModel.setSymname(contractMasterModel.getSymbol());
//						restReqModel.setToken(contractMasterModel.getToken());
//						restReqModel.setTsym(contractMasterModel.getTradingSymbol());
////						restReqModel.setSpan("0.00");
////						restReqModel.setExpo("0.00");
//						if (transType.equalsIgnoreCase("buy") || transType.equalsIgnoreCase("b")) {
//							restReqModel.setTtype("buy");
//							restReqModel.setNetqty(String.valueOf(qty));
//						} else if (transType.equalsIgnoreCase("sell") || transType.equalsIgnoreCase("s")) {
//							restReqModel.setTtype("sell");
//							restReqModel.setNetqty("-"+String.valueOf(qty));
//						}
//						marginPosList.add(restReqModel);
//					} else {
//						Log.info("ContractMasterModel is empty");
//					}
//				}
//			}
				} else {
					ContractMasterModel contractMasterModel = appUtil.getContractInfo(exch, token);
					if (contractMasterModel != null) {
						if (exch.equalsIgnoreCase("MCX")) {
							qty = qty * Integer.parseInt(contractMasterModel.getLotSize());
						}
						restReqModel.setPrd("H");
						restReqModel.setExch(exch);
						String exDate = "";
						if (contractMasterModel.getExpiry() != null) {
							SimpleDateFormat formater = new SimpleDateFormat("dd-MMM-yyyy");
							exDate = formater.format(contractMasterModel.getExpiry());
							restReqModel.setExd(exDate.toUpperCase());
						}

						if (StringUtil.isNotNullOrEmpty(contractMasterModel.getStrikePrice())) {
							restReqModel.setStrprc(contractMasterModel.getStrikePrice());
						}

						restReqModel.setInstname(contractMasterModel.getInsType());

						if (contractMasterModel.getInsType().startsWith("OPT")
								|| contractMasterModel.getInsType().endsWith("OPT")) {
							if (contractMasterModel.getOptionType().equalsIgnoreCase("PE")) {
								restReqModel.setOptt("PE");
							} else if (contractMasterModel.getOptionType().equalsIgnoreCase("CE")) {
								restReqModel.setOptt("CE");
							}
						} else {
							restReqModel.setOptt("XX");
						}
						restReqModel.setSymname(contractMasterModel.getSymbol());

						if (transType.equalsIgnoreCase("buy") || transType.equalsIgnoreCase("b")) {
							restReqModel.setBuyqty(String.valueOf(qty));
						} else if (transType.equalsIgnoreCase("sell") || transType.equalsIgnoreCase("s")) {
							restReqModel.setSellqty(String.valueOf(qty));
						}
						marginPosList.add(restReqModel);
					} else {
						Log.info("ContractMasterModel is empty");
					}
				}
			}

			if (StringUtil.isListNotNullOrEmpty(marginPosList)) {
				SpanMarginRestReq spanMarginRestReq = new SpanMarginRestReq();
				spanMarginRestReq.setActid("HI");
				spanMarginRestReq.setPos(marginPosList);
				String request = mapper.writeValueAsString(spanMarginRestReq);
				String requestParam = "jData=" + request;
				if (request != null) {
					String span = spanMarginRestService.getSpanMargin(requestParam, userId);
					if (StringUtil.isNotNullOrEmpty(span) && !span.equalsIgnoreCase(AppConstants.FAILED_STATUS)) {
						totalSpan = totalSpan + Double.valueOf(span);
					}
				}
			}

			SpanMarginResp resp = new SpanMarginResp();
			BigDecimal span = new BigDecimal(totalSpan).setScale(2, RoundingMode.HALF_EVEN);
			resp.setSpan(String.valueOf(span));
			return prepareResponse.prepareSuccessResponseObject(resp);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

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
	@Override
	public RestResponse<GenericResponse> updateBasketScrip(BasketOrderReq basketOrderReq, ClinetInfoModel info) {
		try {
			/** Validate the request **/
			if (!validateUpdateBasketReq(basketOrderReq))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			/** Method to validate basketId **/
			if (!getValiedBasket(basketOrderReq.getBasketId(), info.getUserId()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_BASKET);

			Optional<BasketScripEntity> optional = basketScripRepository.findById(basketOrderReq.getScrips().getId());
			if (optional == null || optional.isEmpty())
				return prepareResponse.prepareSuccessMessage(AppConstants.INVALID_BASKET);
			BasketScripEntity dbScrips = optional.get();
			prepareBasketScripEntityForUpdate(basketOrderReq, info, dbScrips);
			basketScripRepository.save(dbScrips);
			return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	public boolean validateUpdateBasketReq(BasketOrderReq basketOrder) {
		try {
			if (basketOrder.getBasketId() > 0 && basketOrder.getScrips().getId() > 0
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getExchange())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getToken())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getQty())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getPrice())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getProduct())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getTransType())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getPriceType())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getOrderType())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getRet())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getScrips().getSource())) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return false;
	}

	public BasketScripEntity prepareBasketScripEntityForUpdate(BasketOrderReq basketOrderReq, ClinetInfoModel info,
			BasketScripEntity basketScripEntity) {
		try {

			ContractMasterModel contractMasterModel = appUtil.getContractInfo(basketOrderReq.getScrips().getExchange(),
					basketOrderReq.getScrips().getToken());
			if (contractMasterModel != null) {
				basketScripEntity.setExchange(contractMasterModel.getExch());
				basketScripEntity.setExpiry(contractMasterModel.getExpiry());
				basketScripEntity.setToken(contractMasterModel.getToken());
				basketScripEntity.setTradingSymbol(contractMasterModel.getTradingSymbol());
				basketScripEntity.setFormattedInsName(contractMasterModel.getFormattedInsName());
				basketScripEntity.setWeekTag(contractMasterModel.getWeekTag());

				basketScripEntity.setUpdatedBy(info.getUserId());
				basketScripEntity.setDisClosedQty(basketOrderReq.getScrips().getDisClosedQty());
				basketScripEntity.setMktProtection(basketOrderReq.getScrips().getMktProtection());
				basketScripEntity.setOrderType(basketOrderReq.getScrips().getOrderType());
				basketScripEntity.setPrice(basketOrderReq.getScrips().getPrice());
				basketScripEntity.setPriceType(basketOrderReq.getScrips().getPriceType());
				basketScripEntity.setProduct(basketOrderReq.getScrips().getProduct());
				basketScripEntity.setQty(basketOrderReq.getScrips().getQty());
				basketScripEntity.setRet(basketOrderReq.getScrips().getRet());
				basketScripEntity.setSortOrder(basketOrderReq.getScrips().getSortOrder());
				basketScripEntity.setStopLoss(basketOrderReq.getScrips().getStopLoss());
				basketScripEntity.setTarget(basketOrderReq.getScrips().getTarget());
				basketScripEntity.setTrailingStopLoss(basketOrderReq.getScrips().getTrailingStopLoss());
				basketScripEntity.setTransType(basketOrderReq.getScrips().getTransType());
				basketScripEntity.setTriggerPrice(basketOrderReq.getScrips().getTriggerPrice());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return basketScripEntity;
	}

	/**
	 * method to add scrips to basket
	 * 
	 * @author SOWMIYA
	 */
	@Override
	public RestResponse<GenericResponse> addBasketScripList(BasketOrderReq basketOrderReq, ClinetInfoModel info) {
		try {
			List<BasketScripEntity> basketScripEntity = new ArrayList<>();
			/** Method to validate Request **/
			if (basketOrderReq == null || basketOrderReq.getScripsList().size() <= 0
					|| !valiedBasketList(basketOrderReq.getScripsList()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			/** Method to validate basketId **/
			if (!getValiedBasket(basketOrderReq.getBasketId(), info.getUserId()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_BASKET);

			basketScripEntity = prepareBasketScripsList(basketOrderReq.getScripsList(), info,
					basketOrderReq.getBasketId());
			if (StringUtil.isListNotNullOrEmpty(basketScripEntity)) {
				basketScripRepository.saveAll(basketScripEntity);
				return prepareResponse.prepareSuccessResponseObject(basketScripEntity);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to valied basket list
	 * 
	 * @author SOWMIYA
	 * @param basketOrderReq
	 * @param userId
	 * @return
	 */
	private boolean valiedBasketList(List<ScripRequestModel> model) {
		for (ScripRequestModel basketOrder : model) {
			if (StringUtil.isNotNullOrEmpty(basketOrder.getExchange())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getToken())
//					&& StringUtil.isNotNullOrEmpty(basketOrder.getTradingSymbol())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getQty())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getPrice())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getProduct())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getTransType())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getPriceType())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getOrderType())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getRet())
					&& StringUtil.isNotNullOrEmpty(basketOrder.getSource())) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * method to prepare basket scrips list
	 * 
	 * @author SOWMIYA
	 * @param basketOrderReq
	 * @param info
	 * @return
	 */
	private List<BasketScripEntity> prepareBasketScripsList(List<ScripRequestModel> basketOrderReq,
			ClinetInfoModel info, int basketId) {
		List<BasketScripEntity> entity = new ArrayList<>();
		try {
			for (ScripRequestModel req : basketOrderReq) {
				BasketScripEntity basketScripEntity = new BasketScripEntity();
				ContractMasterModel contractMasterModel = appUtil.getContractInfo(req.getExchange(), req.getToken());
				if (contractMasterModel != null) {
					basketScripEntity.setExchange(contractMasterModel.getExch());
					basketScripEntity.setExpiry(contractMasterModel.getExpiry());
					basketScripEntity.setToken(contractMasterModel.getToken());
					basketScripEntity.setTradingSymbol(contractMasterModel.getTradingSymbol());
					basketScripEntity.setFormattedInsName(contractMasterModel.getFormattedInsName());
					basketScripEntity.setWeekTag(contractMasterModel.getWeekTag());

					basketScripEntity.setBasketId(basketId);
					basketScripEntity.setCreatedBy(info.getUserId());
					basketScripEntity.setDisClosedQty(req.getDisClosedQty());
					basketScripEntity.setMktProtection(req.getMktProtection());
					basketScripEntity.setOrderType(req.getOrderType());
					basketScripEntity.setPrice(req.getPrice());
					basketScripEntity.setPriceType(req.getPriceType());
					basketScripEntity.setProduct(req.getProduct());
					basketScripEntity.setQty(req.getQty());
					basketScripEntity.setRet(req.getRet());
					basketScripEntity.setSortOrder(req.getSortOrder());
					basketScripEntity.setStopLoss(req.getStopLoss());
					basketScripEntity.setTarget(req.getTarget());
					basketScripEntity.setTrailingStopLoss(req.getTrailingStopLoss());
					basketScripEntity.setTransType(req.getTransType());
					basketScripEntity.setTriggerPrice(req.getTriggerPrice());
					entity.add(basketScripEntity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}

	/**
	 * method to get basket margin list
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> getBasketMarginList(List<BasketMarginRequest> reqModel, ClinetInfoModel info) {
		try {

			/** Validate the request **/
			if (!validategetBasketMarginReq(reqModel))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			/** Get user session from cache **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** method to get distinct exchange **/
			List<String> distinctExchanges = reqModel.stream().map(BasketMarginRequest::getExchange).distinct()
					.collect(Collectors.toList());

			/** method to get distinct exchange with values **/
			Map<String, List<BasketMarginRequest>> separatedByExchange = distinctExchanges.stream()
					.collect(Collectors.toMap(exchange -> exchange, exchange -> reqModel.stream()
							.filter(item -> item.getExchange().equals(exchange)).collect(Collectors.toList())));

			/** prepare basket margin request **/
			List<BasketMarginRestReqModel> basketReqList = prepareBasketMarginListRequest(separatedByExchange, info);
			if (basketReqList != null && basketReqList.size() > 0) {
				BigDecimal marginUsed = new BigDecimal(0);
				BigDecimal marginUsedTrade = new BigDecimal(0);
				for (int i = 0; basketReqList.size() > i; i++) {
					String request = prepareRequest(basketReqList.get(i), userSession);
					/** connect to get kambala **/
					BasketMarginRestRespModel responseModel = spanMarginRestService.getBasketMarginList(request,
							info.getUserId());
					if (responseModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						if (StringUtil.isNotNullOrEmpty(responseModel.getMarginUsed())) {

							marginUsed = marginUsed.add(new BigDecimal(responseModel.getMarginUsed()));
						}
						if (StringUtil.isNotNullOrEmpty(responseModel.getMarginUsedTrade())) {
							marginUsedTrade = marginUsedTrade.add(new BigDecimal(responseModel.getMarginUsedTrade()));
						}
					} else {
						return prepareResponse.prepareSuccessResponseObject(responseModel);
					}
				}
				BasketMarginRespModel totalMargin = new BasketMarginRespModel();
				totalMargin.setMarginUsed(String.valueOf(marginUsed));
				totalMargin.setMarginUsedTrade(String.valueOf(marginUsedTrade));
				return prepareResponse.prepareSuccessResponseObject(totalMargin);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
			}
		} catch (Exception e) {
			Log.error("getBasketMargin", e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to validate basket margin req
	 * 
	 * @author dinesh
	 * @param reqModel
	 * @return
	 */
	private boolean validategetBasketMarginReq(List<BasketMarginRequest> reqModel) {
		boolean rep = false;
		for (BasketMarginRequest model : reqModel) {
			if (StringUtil.isNotNullOrEmpty(model.getExchange()) && StringUtil.isNotNullOrEmpty(model.getQty())
					&& StringUtil.isNotNullOrEmpty(model.getPrice()) && StringUtil.isNotNullOrEmpty(model.getProduct())
					&& StringUtil.isNotNullOrEmpty(model.getTransType())
					&& StringUtil.isNotNullOrEmpty(model.getTradingSymbol())
					&& StringUtil.isNotNullOrEmpty(model.getPriceType())) {
				rep = true;
			} else {
				rep = false;
				break;
			}
		}
		return rep;
	}

	/**
	 * method to prepare basket margin
	 * 
	 * @author SowmiyaThangaraj
	 * @param separatedByExchange
	 * @param info
	 * @return
	 */
	private List<BasketMarginRestReqModel> prepareBasketMarginListRequest(
			Map<String, List<BasketMarginRequest>> separatedByExchange, ClinetInfoModel info) {
		List<BasketMarginRestReqModel> basketMarginRestReqModel = new ArrayList<>();
		try {
			for (String exchange : separatedByExchange.keySet()) {
				System.out.println("Exchange: " + exchange);
				List<BasketListModel> basketList = new ArrayList<>();
				List<BasketMarginRequest> exchangeList = new ArrayList<>();
				exchangeList = separatedByExchange.get(exchange);
				if (exchangeList != null) {
					BasketMarginRestReqModel basketMarginReqModel = new BasketMarginRestReqModel();
					basketMarginReqModel.setUid(info.getUserId());
					basketMarginReqModel.setActid(info.getUserId());
					if (exchangeList != null && !exchangeList.isEmpty() && exchangeList.size() > 0) {
						BasketMarginRequest tempBasketListsModel = exchangeList.get(0);
						if (tempBasketListsModel != null) {
							basketMarginReqModel.setExch(tempBasketListsModel.getExchange());
							basketMarginReqModel.setPrc(tempBasketListsModel.getPrice());
							String priceType = appUtil.getRestPriceType(tempBasketListsModel.getPriceType());
							if (StringUtil.isNullOrEmpty(priceType)) {
								Log.error("Price type is empty. Not able to map for place order request");
								return null;
							} else {
								basketMarginReqModel.setPrctyp(priceType);
							}

							String productType = appUtil.getRestProductType(tempBasketListsModel.getProduct());
							if (StringUtil.isNullOrEmpty(productType)) {
								Log.error("Product type is empty. Not able to map for place order request");
								return null;
							} else {
								basketMarginReqModel.setPrd(productType);
							}
							basketMarginReqModel.setQty(tempBasketListsModel.getQty());
							basketMarginReqModel.setTrantype(tempBasketListsModel.getTransType());
							basketMarginReqModel.setTsym(
									URLEncoder.encode(tempBasketListsModel.getTradingSymbol(), AppConstants.UTF_8));
							// non - mandatory filed
							if (StringUtil.isNotNullOrEmpty(tempBasketListsModel.getTriggerPrice())) {
								basketMarginReqModel.setTrgprc(tempBasketListsModel.getTriggerPrice());
							}
						}
					}

					if (exchangeList != null && exchangeList.size() > 1) {
						for (int i = 1; exchangeList.size() > i; i++) {
							BasketMarginRequest tempBasketListsModel = exchangeList.get(i);
							BasketListModel listModel = new BasketListModel();
							listModel.setExch(tempBasketListsModel.getExchange());
							listModel.setPrc(tempBasketListsModel.getPrice());
							String priceType = appUtil.getRestPriceType(tempBasketListsModel.getPriceType());
							if (StringUtil.isNullOrEmpty(priceType)) {
								Log.error("Price type is empty. Not able to map for place order request");
								return null;
							} else {
								listModel.setPrctyp(priceType);
							}

							String productType = appUtil.getRestProductType(tempBasketListsModel.getProduct());
							if (StringUtil.isNullOrEmpty(productType)) {
								Log.error("Product type is empty. Not able to map for place order request");
								return null;
							} else {
								listModel.setPrd(productType);
							}
							listModel.setQty(tempBasketListsModel.getQty());
							listModel.setTrantype(tempBasketListsModel.getTransType());
							listModel.setTsym(
									URLEncoder.encode(tempBasketListsModel.getTradingSymbol(), AppConstants.UTF_8));
							// non - mandatory filed
							if (StringUtil.isNotNullOrEmpty(tempBasketListsModel.getTriggerPrice())) {
								listModel.setTrgprc(tempBasketListsModel.getTriggerPrice());
							}
							basketList.add(listModel);

						}
						basketMarginReqModel.setBasketlists(basketList);
					}

					basketMarginRestReqModel.add(basketMarginReqModel);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return basketMarginRestReqModel;

	}

	/**
	 * method to prepare request
	 * 
	 * @author SowmiyaThangaraj
	 * @param basketListModel
	 * @param userSession
	 * @return
	 */
	private String prepareRequest(BasketMarginRestReqModel basketMarginReqModel, String userSession) {
		String request = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(basketMarginReqModel);
			request = "jData=" + json + "&jKey=" + userSession;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			Log.error("prepareRequest", e);
		}
		return request;
	}

	/**
	 * method to update testing
	 * 
	 * @author SowmiyaThangaraj
	 * 
	 * @return
	 */
	public RestResponse<GenericResponse> updateTesting() {
		long isUpdate = basketOrderRepository.updateExecutionStatus("1", "GC110180", "GC110180", 11);
		if (isUpdate > 0) {
			return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}
}
