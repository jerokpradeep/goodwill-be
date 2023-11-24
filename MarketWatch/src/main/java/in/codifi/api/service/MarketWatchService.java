package in.codifi.api.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.resteasy.reactive.RestResponse;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.codifi.api.cache.HazelCacheController;
import in.codifi.api.cache.MwCacheController;
import in.codifi.api.entity.primary.MarketWatchNameDTO;
import in.codifi.api.entity.primary.MarketWatchScripDetailsDTO;
import in.codifi.api.model.CacheMwAdvDetailsModel;
import in.codifi.api.model.CacheMwDetailsModel;
import in.codifi.api.model.MwRequestModel;
import in.codifi.api.model.MwScripModel;
import in.codifi.api.model.ResponseModel;
import in.codifi.api.repository.MarketWatchEntityManager;
import in.codifi.api.repository.MarketWatchNameRepository;
import in.codifi.api.repository.MarketWatchRepository;
import in.codifi.api.service.spec.IMarketWatchService;
import in.codifi.api.util.AppConstants;
import in.codifi.api.util.PrepareResponse;
import in.codifi.api.util.StringUtil;
import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.cache.model.ContractMasterModel;
import io.quarkus.logging.Log;

@SuppressWarnings("unchecked")
@Service
public class MarketWatchService implements IMarketWatchService {

	@Autowired
	MarketWatchRepository mwRespo;
	@Autowired
	MarketWatchNameRepository mwNameRepo;
	@Autowired
	MarketWatchRepository marketWatchRepo;
	@Autowired
	PrepareResponse prepareResponse;
	@Autowired
	MarketWatchEntityManager entityManager;

	/**
	 * Load the contract master from data base from Cache
	 * 
	 * @author Gowrisankar
	 */
//	@Override
//	public RestResponse<ResponseModel> loadContractMaster() {
//		ResponseModel response = new ResponseModel();
//		try {
//			HazleCacheController.getInstance().getAlternateSymbolCache().clear();
//			HazleCacheController.getInstance().getContractMaster().clear();
//			Iterable<ContractMasterDTO> contractList = contractRepo.findAll();
//			Iterator<ContractMasterDTO> itr = contractList.iterator();
//			while (itr.hasNext()) {
//				ContractMasterDTO tempDto = itr.next();
//				ContractMasterModel result = new ContractMasterModel();
//				String symbolName = tempDto.getSymbol();
//				String exch = tempDto.getEx();
//				String token = tempDto.getToken();
//				result.setSymbolName(symbolName);
//				result.setIsin(tempDto.getInsName());
//				result.setScripName(tempDto.getFormattedInsName());
//				result.setExch(exch);
//				result.setExchSeg(tempDto.getExSeg());
//				result.setToken(tempDto.getToken());
//				result.setSymbol(tempDto.getTradingSymbol());
//				result.setGroupName(tempDto.getGroupName());
//				result.setInsType(tempDto.getInsType());
//				result.setOptionType(tempDto.getOptionType());
//				result.setStrikePrice(tempDto.getStrikePrice());
//				result.setExpDt(tempDto.getExpiryDate());
//				result.setLotSiz(tempDto.getLotSize());
//				result.setTicSiz(tempDto.getTickSize());
//				result.setCompanyName(symbolName);
//				String key = exch + "_" + token;
//				HazleCacheController.getInstance().getContractMaster().put(key, result);
//				if (exch.equalsIgnoreCase("NSE") || exch.equalsIgnoreCase("BSE")) {
//					ContractMasterModel alternateResult = new ContractMasterModel();
//					alternateResult.setInOplotSiz(tempDto.getLotSize());
//					alternateResult.setInOpTicSiz(tempDto.getTickSize());
//					alternateResult.setInOpToken(token);
//					if (exch.equalsIgnoreCase("NSE")) {
//						alternateResult.setInOpsymbol(symbolName + "-" + tempDto.getGroupName());
//					} else {
//						alternateResult.setInOpsymbol(symbolName);
//					}
//					HazleCacheController.getInstance().getAlternateSymbolCache().put(symbolName + "_" + exch,
//							alternateResult);
//				}
//
//			}
//			System.out.println("Loaded SucessFully");
//			System.out.println("Alternate Sizw " + HazleCacheController.getInstance().getAlternateSymbolCache().size());
//			System.out.println("Full Size " + HazleCacheController.getInstance().getContractMaster().size());
//			return prepareResponse.prepareSuccessResponseObject(AppConstants.EMPTY_ARRAY);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
//	}

	/**
	 * Mwthod to provide the User scrips details from the data base or cache
	 * 
	 * @author Gowrisankar
	 */
	@Override
	public RestResponse<ResponseModel> getAllMwScrips(String pUserId) {
		try {
			/*
			 * Check the user has the scrips in cache or not
			 */
			List<JSONObject> result = HazelCacheController.getInstance().getMwListByUserId().get(pUserId);
			if (result != null && result.size() > 0) {
				/*
				 * if cache is there return from then return from cache
				 */

				return prepareResponse.prepareSuccessResponseObject(result);
			} else {
				/*
				 * take the scrip details from the Data base for the user
				 */
//				List<IMwTblResponse> scripDetails = mwNameRepo.getUserScripDetails(pUserId);
				List<CacheMwDetailsModel> scripDetails = entityManager.getMarketWatchByUserId(pUserId);
				if (scripDetails != null && scripDetails.size() > 0) {
					/*
					 * Populate the filed for Marketwatch as per the requirement
					 */
					List<JSONObject> tempResult = populateFields(scripDetails, pUserId);
					if (tempResult != null && tempResult.size() > 0) {
						return prepareResponse.prepareSuccessResponseObject(tempResult);
					}
				} else {

					/**
					 * Create New market watch if does not exist
					 */
					List<JSONObject> resp = create(pUserId);
					if (resp != null && resp.size() > 0) {
						return prepareResponse.prepareSuccessResponseObject(resp);
					} else {
						return prepareResponse.prepareFailedResponse(AppConstants.NO_MW);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to create new market watch
	 * 
	 * @author Dinesh Kumar
	 * @param pDto
	 * @return
	 */
	private List<JSONObject> create(String pUserId) {
		List<JSONObject> response = new ArrayList<>();
		try {
			if (StringUtil.isNotNullOrEmpty(pUserId)) {
				/* Check user has how many market watch */
				List<MarketWatchNameDTO> mwList = mwNameRepo.findAllByUserId(pUserId);
				/* If null or size is lesser than 5 create a new Market Watch */
				if (mwList == null || mwList.size() == 0) {
					/* Create the new Market Watch */
					List<MarketWatchNameDTO> newMwList = new ArrayList<MarketWatchNameDTO>();
					for (int i = 0; i < AppConstants.MW_SIZE; i++) {
						MarketWatchNameDTO newDto = new MarketWatchNameDTO();
						newDto.setUserId(pUserId);
						newDto.setMwId(i + 1);
						newDto.setMwName(AppConstants.MARKET_WATCH_LIST_NAME + (i + 1));
						newDto.setPosition(Long.valueOf(i));
						newMwList.add(newDto);
					}
					mwNameRepo.saveAll(newMwList);
//					List<IMwTblResponse> scripDetails = mwNameRepo.getUserScripDetails(pUserId);
					List<CacheMwDetailsModel> scripDetails = entityManager.getMarketWatchByUserId(pUserId);
					if (scripDetails != null && scripDetails.size() > 0) {
						response = populateFields(scripDetails, pUserId);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return response;
	}

	/**
	 * Method to get the Scrip for given user id and market watch Id
	 * 
	 * @author Gowrisankar
	 */
	@Override
	public RestResponse<ResponseModel> getMWScrips(MwRequestModel pDto, ClinetInfoModel info) {
		try {
			List<JSONObject> res = HazelCacheController.getInstance().getMwListByUserId().get(info.getUserId());
			String marketWatchId = String.valueOf(pDto.getMwId());
			JSONObject result = null;
			if (res != null && res.size() > 0) {
				for (int itr = 0; itr < res.size(); itr++) {
					result = new JSONObject();
					result = res.get(itr);
					String mwId = (String) result.get("mwId");
					if (marketWatchId.equalsIgnoreCase(mwId)) {
						break;
					}
				}
				if (result != null && !result.isEmpty()) {
					List<CacheMwDetailsModel> scripDetails = (List<CacheMwDetailsModel>) result.get("scrips");
					if (scripDetails != null && scripDetails.size() > 0) {
						return prepareResponse.prepareSuccessResponseObject(scripDetails);
					} else {
						return prepareResponse.prepareFailedResponse(AppConstants.NO_MW);
					}
				} else {
					return prepareResponse.prepareFailedResponse(AppConstants.NO_MW);
				}
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_MW);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to populate the fields for the user scrips details for given user
	 * 
	 * @author Gowrisankar
	 * @param userScripDetails
	 * @param pUserId
	 * @return
	 */
//	private List<JSONObject> populateFields(List<IMwTblResponse> userScripDetails, String pUserId) {
	private List<JSONObject> populateFields(List<CacheMwDetailsModel> cacheMwDetailsModels, String pUserId) {

		List<JSONObject> response = new ArrayList<>();
		try {
//			ObjectMapper mapper = new ObjectMapper();
//			List<CacheMwDetailsModel> cacheMwDetailsModels = new ArrayList<>();
//			for (IMwTblResponse iMwTblResponse : userScripDetails) {
//				CacheMwDetailsModel model = new CacheMwDetailsModel();
//				model = mapper.readValue((mapper.writeValueAsString(iMwTblResponse)), CacheMwDetailsModel.class);
//				cacheMwDetailsModels.add(model);
//			}
			JSONObject tempResponse = new JSONObject();
			for (CacheMwDetailsModel tempModel : cacheMwDetailsModels) {
				String mwName = tempModel.getMwName();
				String mwId = String.valueOf(tempModel.getMwId());
				String tempMwID = pUserId + "_" + mwId + "_" + mwName;
				String scripName = tempModel.getFormattedInsName();
				if (scripName != null && !scripName.isEmpty()) {
					if (tempResponse.containsKey(tempMwID)) {
						List<CacheMwDetailsModel> tempList = new ArrayList<>();
						if (tempResponse.get(tempMwID) != null) {
							tempList = (List<CacheMwDetailsModel>) tempResponse.get(tempMwID);
						}
						tempList.add(tempModel);
						tempResponse.put(tempMwID, tempList);
					} else {
						List<CacheMwDetailsModel> tempList = new ArrayList<>();
						tempList.add(tempModel);
						tempResponse.put(tempMwID, tempList);
					}
				} else if (tempResponse.get(tempMwID) == null) {
					tempResponse.put(tempMwID, null);
				}
			}
			if (tempResponse != null) {
				response = getCacheListForScrips(tempResponse);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return response;
	}

	/**
	 * method to save the scrip details of user in cache
	 * 
	 * @author Gowrisankar
	 * @param mwResponse
	 * @return
	 */
	public List<JSONObject> getCacheListForScrips(JSONObject mwResponse) {
		List<JSONObject> response = new ArrayList<JSONObject>();
		try {
			Iterator<String> itr = mwResponse.keySet().iterator();
			itr = sortedIterator(itr);
			while (itr.hasNext()) {
				String tempStr = itr.next();
				String[] tempStrArr = tempStr.split("_");
				String user = tempStrArr[0];
				String mwId = tempStrArr[1];
				String mwName = tempStrArr[2];
				JSONObject result = new JSONObject();
				List<CacheMwAdvDetailsModel> tempJsonObject = new ArrayList<CacheMwAdvDetailsModel>();
				tempJsonObject = (List<CacheMwAdvDetailsModel>) mwResponse.get(tempStr);
				result.put("mwId", mwId);
				result.put("mwName", mwName);
				if (tempJsonObject != null && tempJsonObject.size() > 0) {
					result.put("scrips", tempJsonObject);
				} else {
					result.put("scrips", null);
				}

				response = MwCacheController.getAdvanceMWListByUserId().get(user);
				if (response != null) {
					response = MwCacheController.getAdvanceMWListByUserId().get(user);
					response.add(result);
					MwCacheController.getAdvanceMWListByUserId().put(user, response);
				} else {
					response = new ArrayList<JSONObject>();
					response.add(result);
					MwCacheController.getAdvanceMWListByUserId().put(user, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * Sorting ITR
	 * 
	 * @param it
	 * @return
	 */
	public Iterator<String> sortedIterator(Iterator<String> it) {
		List<String> list = new ArrayList<>();
		while (it.hasNext()) {
			list.add((String) it.next());
		}
		Collections.sort(list);
		return list.iterator();
	}

	/**
	 * Method to create new market watch
	 * 
	 * @author Gowrisankar
	 * @param pDto
	 * @return
	 */
	public RestResponse<ResponseModel> createMW(String pUserId) {
		try {
			if (StringUtil.isNotNullOrEmpty(pUserId)) {
				/* Check user has how many market watch */
				List<MarketWatchNameDTO> mwList = mwNameRepo.findAllByUserId(pUserId);
				/* If null or size is lesser than 5 create a new Market Watch */
				if (mwList == null || mwList.size() == 0) {
					/* Create the new Market Watch */
					List<MarketWatchNameDTO> newMwList = new ArrayList<MarketWatchNameDTO>();
					// TODO change hot code value
//					int mwListSize = Integer.parseInt(CSEnvVariables.getMethodNames(AppConstants.MW_LIST_SIZE));
					for (int i = 0; i < 5; i++) {
						MarketWatchNameDTO newDto = new MarketWatchNameDTO();
						newDto.setUserId(pUserId);
						newDto.setMwId(i + 1);
						newDto.setMwName(AppConstants.MARKET_WATCH_LIST_NAME + (i + 1));
						newDto.setPosition(Long.valueOf(i));
						newMwList.add(newDto);
					}
					mwNameRepo.saveAll(newMwList);
//					List<IMwTblResponse> scripDetails = mwNameRepo.getUserScripDetails(pUserId);
					List<CacheMwDetailsModel> scripDetails = entityManager.getMarketWatchByUserId(pUserId);
					if (scripDetails != null && scripDetails.size() > 0) {
						List<JSONObject> tempResult = populateFields(scripDetails, pUserId);
						if (tempResult != null && tempResult.size() > 0) {
							prepareResponse.prepareSuccessResponseWithMessage(tempResult,
									AppConstants.MARKET_WATCH_CREATED);
						}
					}
				} else { /* Else send the error response */
					return prepareResponse.prepareFailedResponse(AppConstants.LIMIT_REACHED_MW);
				}
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to add the scrip into cache and data baseF
	 * 
	 * @author Dinesh Kumar
	 */
	@Override
	public RestResponse<ResponseModel> addscrip(MwRequestModel parmDto, ClinetInfoModel info) {
		try {
			/*
			 * Check the list not null or empty
			 */
			if (StringUtil.isListNotNullOrEmpty(parmDto.getScripData()) && StringUtil.isNotNullOrEmpty(info.getUserId())
					&& parmDto.getMwId() > 0) {

				int curentSortOrder = getExistingSortOrder(info.getUserId(), parmDto.getMwId());
				List<MwScripModel> mwScripModels = new ArrayList<>();
				for (MwScripModel model : parmDto.getScripData()) {
					curentSortOrder = curentSortOrder + 1;
					model.setSortingOrder(curentSortOrder);
					mwScripModels.add(model);
				}
				List<CacheMwDetailsModel> scripDetails = getScripMW(mwScripModels);
				if (scripDetails != null && scripDetails.size() > 0) {
					List<CacheMwDetailsModel> newScripDetails = addNewScipsForMwIntoCache(scripDetails,
							info.getUserId(), parmDto.getMwId());
					if (newScripDetails != null && newScripDetails.size() > 0) {
						insertNewScipsForMwIntoDataBase(newScripDetails, info.getUserId(), parmDto.getMwId());
					}
					return prepareResponse.prepareSuccessResponseObject(scripDetails);
				} else {
					return prepareResponse.prepareFailedResponse(AppConstants.NOT_ABLE_TO_ADD_CONTRACT);
				}
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get the sorting order from the cache
	 * 
	 * @author Gowrisankar
	 * @param pUserId
	 * @param i
	 * @return
	 */
	private int getExistingSortOrder(String pUserId, int mwid) {
		int sortingOrder = 0;
		List<JSONObject> res = HazelCacheController.getInstance().getMwListByUserId().get(pUserId);
		JSONObject result = null;
		String marketWatchId = String.valueOf(mwid);
		if (res != null && res.size() > 0) {
			for (int itr = 0; itr < res.size(); itr++) {
				result = new JSONObject();
				result = res.get(itr);
				String tempMwId = (String) result.get("mwId");
				if (tempMwId.equalsIgnoreCase(marketWatchId)) {
					List<CacheMwDetailsModel> scripDetails = (List<CacheMwDetailsModel>) result.get("scrips");
					if (scripDetails != null && scripDetails.size() > 0) {
						Optional<CacheMwDetailsModel> maxByOrder = scripDetails.stream()
								.max(Comparator.comparing(CacheMwDetailsModel::getSortOrder));
						CacheMwDetailsModel model = maxByOrder.get();
						if (model != null && model.getSortOrder() > 0) {
							sortingOrder = model.getSortOrder();
						}
					}
					break;
				}
			}
		}
		return sortingOrder;
	}

	/**
	 * Method to get the scrip from the cache for Market watch
	 * 
	 * @author Dinesh Kumar
	 * @param pDto
	 * @return
	 */
	public List<CacheMwDetailsModel> getScripMW(List<MwScripModel> pDto) {
		List<CacheMwDetailsModel> response = new ArrayList<>();
		try {
			for (int itr = 0; itr < pDto.size(); itr++) {
				MwScripModel result = new MwScripModel();
				result = pDto.get(itr);
				String exch = result.getExch();
				String token = result.getToken();
				if (HazelCacheController.getInstance().getContractMaster().get(exch + "_" + token) != null) {
					CacheMwDetailsModel fResult = new CacheMwDetailsModel();
					ContractMasterModel masterData = HazelCacheController.getInstance().getContractMaster()
							.get(exch + "_" + token);
					fResult.setSymbol(masterData.getSymbol());
					fResult.setTradingSymbol(masterData.getTradingSymbol());
					fResult.setFormattedInsName(masterData.getFormattedInsName());
					fResult.setToken(masterData.getToken());
					fResult.setExchange(masterData.getExch());
					fResult.setSegment(masterData.getSegment());
					fResult.setExpiry(masterData.getExpiry());
					fResult.setSortOrder(result.getSortingOrder());
					fResult.setPdc(masterData.getPdc());
					fResult.setWeekTag(masterData.getWeekTag());
					response.add(fResult);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * Method to insert into data base in thread
	 * 
	 * @author Dinesh Kumar
	 * @param parmDto
	 */
	private void insertNewScipsForMwIntoDataBase(List<CacheMwDetailsModel> scripDetails, String userId, int mwId) {
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
//					int sortingOrder = getSortingOrder(userId, String.valueOf(mwId));
//					List<MarketWatchScripDetailsDTO> marketWatchNameDto = prepareMarketWatchEntity(parmDto,
//							sortingOrder + 1);
					List<MarketWatchScripDetailsDTO> marketWatchNameDto = prepareMarketWatchEntity(scripDetails, userId,
							mwId);

					/*
					 * Insert the scrip details into the data base
					 */
					if (marketWatchNameDto != null && marketWatchNameDto.size() > 0) {
						mwRespo.saveAll(marketWatchNameDto);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		pool.shutdown();
	}

	/**
	 * Method to add the New Scrips in Market Watch New
	 * 
	 * @author Gowrisankar
	 * @param newScripDetails
	 * @param pUserId
	 * @param userMwId
	 */
	public List<CacheMwDetailsModel> addNewScipsForMwIntoCache(List<CacheMwDetailsModel> newScripDetails,
			String pUserId, int userMwId) {
		List<CacheMwDetailsModel> responseModel = new ArrayList<>();
		responseModel.addAll(newScripDetails);
		List<JSONObject> res = HazelCacheController.getInstance().getMwListByUserId().get(pUserId);
		String marketWatchId = String.valueOf(userMwId);
		JSONObject result = null;
		int indexOfRes = 0;
		if (res != null && res.size() > 0) {
			for (int itr = 0; itr < res.size(); itr++) {
				result = new JSONObject();
				result = res.get(itr);
				String mwId = (String) result.get("mwId").toString();
				if (marketWatchId.equalsIgnoreCase(mwId)) {
					indexOfRes = itr;
					break;
				}
			}
			if (result != null && !result.isEmpty()) {
				List<CacheMwDetailsModel> scripDetails = (List<CacheMwDetailsModel>) result.get("scrips");
				List<CacheMwDetailsModel> latestScripDetails = new ArrayList<>();
				if (scripDetails != null && scripDetails.size() > 0) {
					latestScripDetails.addAll(scripDetails);
					for (int i = 0; i < newScripDetails.size(); i++) {
						CacheMwDetailsModel tempNewScrip = newScripDetails.get(i);
						String tempNewToken = tempNewScrip.getToken();
						String tempNewExch = tempNewScrip.getExchange();
						int alreadyAdded = 0;
						for (int j = 0; j < scripDetails.size(); j++) {
							CacheMwDetailsModel scrip = scripDetails.get(j);
							String token = scrip.getToken();
							String exch = scrip.getExchange();
							if (tempNewToken.equalsIgnoreCase(token) && tempNewExch.equalsIgnoreCase(exch)) {
								alreadyAdded = 1;
								break;
							}
						}
						if (alreadyAdded == 0) {
							latestScripDetails.add(tempNewScrip);
						} else {
							// If already exist remove it from list to avoid duplicate insert on DB
							responseModel.remove(i);
						}
					}
				} else {
					latestScripDetails.addAll(newScripDetails);
				}
				result.remove("scrips");
				result.put("scrips", latestScripDetails);
				res.remove(indexOfRes);
				res.add(indexOfRes, result);
				HazelCacheController.getInstance().getMwListByUserId().remove(pUserId);
				HazelCacheController.getInstance().getMwListByUserId().put(pUserId, res);
			}
		}
		return responseModel;
	}

	private List<MarketWatchScripDetailsDTO> prepareMarketWatchEntity(List<CacheMwDetailsModel> scripDetails,
			String userId, int mwId) {

		List<MarketWatchScripDetailsDTO> marketWatchScripDetailsDTOs = new ArrayList();
		for (int i = 0; i < scripDetails.size(); i++) {
			CacheMwDetailsModel model = scripDetails.get(i);
			MarketWatchScripDetailsDTO resultDto = new MarketWatchScripDetailsDTO();
			String exch = model.getExchange();
			String token = model.getToken();
			if (HazelCacheController.getInstance().getContractMaster().get(exch + "_" + token) != null) {
				ContractMasterModel masterData = HazelCacheController.getInstance().getContractMaster()
						.get(exch + "_" + token);
				resultDto.setUserId(userId);
				resultDto.setMwId(mwId);
				resultDto.setEx(exch);
				resultDto.setToken(token);
				resultDto.setTradingSymbol(masterData.getTradingSymbol());
				resultDto.setEx(masterData.getExch());
				resultDto.setExSeg(masterData.getSegment());
				resultDto.setToken(masterData.getToken());
				resultDto.setSymbol(masterData.getSymbol());
				resultDto.setGroupName(masterData.getGroupName());
				resultDto.setInstrumentType(masterData.getInsType());
				resultDto.setOptionType(masterData.getOptionType());
				resultDto.setStrikePrice(masterData.getStrikePrice());
				resultDto.setExpDt(masterData.getExpiry());
				resultDto.setLotSize(masterData.getLotSize());
				resultDto.setTickSize(masterData.getTickSize());
				resultDto.setFormattedName(masterData.getFormattedInsName());
				resultDto.setPdc(masterData.getPdc());
				resultDto.setAlterToken(masterData.getAlterToken());
				resultDto.setSortingOrder(model.getSortOrder());
				resultDto.setWeekTag(masterData.getWeekTag());
				marketWatchScripDetailsDTOs.add(resultDto);
			}

		}

		return marketWatchScripDetailsDTOs;
	}

	/**
	 * method to check wheather the token and exch in given market watch
	 * 
	 * @author Gowrisankar
	 * @param pUserId
	 * @param mwId
	 * @param token
	 * @param exch
	 * @return
	 */
	public int checkTokenInMw(String pUserId, String mwId, String token, String exch) {
		int isPresent = 0;
		try {
			List<JSONObject> res = HazelCacheController.getInstance().getMwListByUserId().get(pUserId);
			JSONObject result = null;
			if (res != null && res.size() > 0) {
				for (int itr = 0; itr < res.size(); itr++) {
					result = new JSONObject();
					result = res.get(itr);
					String tempMwId = (String) result.get("mwId");
					if (tempMwId.equalsIgnoreCase(mwId)) {
						break;
					}
				}
				if (result != null && !result.isEmpty()) {
					List<CacheMwDetailsModel> scripDetails = (List<CacheMwDetailsModel>) result.get("scrips");
					if (scripDetails != null && scripDetails.size() > 0) {
						for (CacheMwDetailsModel tempDto : scripDetails) {
							String tempExch = tempDto.getExchange();
							String tempToken = tempDto.getToken();
							if (tempExch.equalsIgnoreCase(exch) && token.equalsIgnoreCase(tempToken)) {
								isPresent = 1;
								return isPresent;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isPresent;
	}

	/**
	 * Method to delete the scrips from the cache and market watch
	 * 
	 * @author Gowrisankar
	 * @param pDto
	 * @return
	 */
	@Override
	public RestResponse<ResponseModel> deletescrip(MwRequestModel pDto, ClinetInfoModel info) {
		try {
			int mwId = pDto.getMwId();
			String useriD = info.getUserId();
			List<MwScripModel> dataToDelete = pDto.getScripData();
			if (StringUtil.isNotNullOrEmpty(useriD) && StringUtil.isListNotNullOrEmpty(dataToDelete) && mwId > 0) {
				deleteFromCache(dataToDelete, useriD, mwId);
				deleteFromDB(dataToDelete, useriD, mwId);
				return prepareResponse.prepareSuccessResponseObject(AppConstants.EMPTY_ARRAY);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to delete the scrips from the cache
	 * 
	 * @author Gowrisankar
	 * @param newScripDetails
	 * @param pUserId
	 * @param userMwId
	 */
	public void deleteFromCache(List<MwScripModel> dataToDelete, String pUserId, int userMwId) {
		if (dataToDelete != null && dataToDelete.size() > 0) {
			List<JSONObject> res = HazelCacheController.getInstance().getMwListByUserId().get(pUserId);
			String marketWatchId = String.valueOf(userMwId);
			JSONObject result = null;
			int indexOfRes = 0;
			if (res != null && res.size() > 0) {
				for (int itr = 0; itr < res.size(); itr++) {
					result = new JSONObject();
					result = res.get(itr);
					String mwId = (String) result.get("mwId").toString();
					if (marketWatchId.equalsIgnoreCase(mwId)) {
						indexOfRes = itr;
						break;
					}
				}
				if (result != null && !result.isEmpty()) {
					List<CacheMwDetailsModel> scripDetails = (List<CacheMwDetailsModel>) result.get("scrips");
					if (scripDetails != null && scripDetails.size() > 0) {
						for (int i = 0; i < dataToDelete.size(); i++) {
							MwScripModel tempDTO = dataToDelete.get(i);
							String token = tempDTO.getToken();
							String exch = tempDTO.getExch();
							for (int j = 0; j < scripDetails.size(); j++) {
								CacheMwDetailsModel tempScripDTO = scripDetails.get(j);
								String scripToken = tempScripDTO.getToken();
								String scripExch = tempScripDTO.getExchange();
								if (scripToken.equalsIgnoreCase(token) && scripExch.equalsIgnoreCase(exch)) {
									scripDetails.remove(j);
								}
							}
						}
						result.remove("scrips");
						result.put("scrips", scripDetails);
						res.remove(indexOfRes);
						res.add(indexOfRes, result);
						HazelCacheController.getInstance().getMwListByUserId().remove(pUserId);
						HazelCacheController.getInstance().getMwListByUserId().put(pUserId, res);
					}
				}
			}
		}
	}

	/**
	 * Method to delete the scrips from the cache
	 * 
	 * @author Gowrisankar
	 * @param newScripDetails
	 * @param pUserId
	 * @param userMwId
	 */
	public void deleteFromDB(List<MwScripModel> dataToDelete, String pUserId, int userMwId) {
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					if (dataToDelete != null && dataToDelete.size() > 0) {
						for (int i = 0; i < dataToDelete.size(); i++) {
							MwScripModel tempDTO = dataToDelete.get(i);
							String token = tempDTO.getToken();
							String exch = tempDTO.getExch();
							mwRespo.deleteScripFomDataBase(pUserId, exch, token, userMwId);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		pool.shutdown();
	}

	/**
	 * method to populate the fields for all users
	 *
	 * @author Gowri Sankar
	 * @param userScripDetails
	 * @return
	 */
//	public List<JSONObject> populateFieldsMWForAll(List<IMwTblResponse> userScripDetails) {
	public List<JSONObject> populateFieldsMWForAll(List<CacheMwAdvDetailsModel> cacheMwDetailsModels) {
		List<JSONObject> response = new ArrayList<JSONObject>();
		try {
//			ObjectMapper mapper = new ObjectMapper();
//			List<CacheMwDetailsModel> cacheMwDetailsModels = new ArrayList<>();
//			for (IMwTblResponse iMwTblResponse : userScripDetails) {
//				CacheMwDetailsModel model = new CacheMwDetailsModel();
//				model = mapper.readValue((mapper.writeValueAsString(iMwTblResponse)), CacheMwDetailsModel.class);
//				cacheMwDetailsModels.add(model);
//			}
			JSONObject tempResponse = new JSONObject();
			for (CacheMwAdvDetailsModel tempModel : cacheMwDetailsModels) {
				String mwName = tempModel.getMwName();
				String mwId = String.valueOf(tempModel.getMwId());
				String pUserId = tempModel.getUserId();
				String tempMwID = pUserId + "_" + mwId + "_" + mwName;
				String scripName = tempModel.getFormattedInsName();
				if (scripName != null && !scripName.isEmpty()) {
					if (tempResponse.containsKey(tempMwID)) {
						List<CacheMwAdvDetailsModel> tempList = new ArrayList<>();
						if (tempResponse.get(tempMwID) != null) {
							tempList = (List<CacheMwAdvDetailsModel>) tempResponse.get(tempMwID);
						}
						tempList.add(tempModel);
						tempResponse.put(tempMwID, tempList);
					} else {
						List<CacheMwAdvDetailsModel> tempList = new ArrayList<>();
						tempList.add(tempModel);
						tempResponse.put(tempMwID, tempList);
					}
				} else if (tempResponse.get(tempMwID) == null) {
					tempResponse.put(tempMwID, null);
				}
			}
			if (tempResponse != null) {
				response = getCacheListForScrips(tempResponse);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 
	 * Method to Rename Market Watch
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param pDto
	 * @return
	 */
	public RestResponse<ResponseModel> renameMarketWatch(MwRequestModel pDto, ClinetInfoModel info) {
		try {
			if (pDto != null && StringUtil.isNotNullOrEmpty(pDto.getMwName())
					&& StringUtil.isNotNullOrEmpty(info.getUserId()) && pDto.getMwId() != 0) {

				renameMwInCache(pDto.getMwName(), pDto.getMwId(), info.getUserId());
				updateMwNamw(pDto.getMwName(), pDto.getMwId(), info.getUserId());
				return prepareResponse.prepareSuccessResponseObject(AppConstants.EMPTY_ARRAY);

			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	private void updateMwNamw(String mwName, int mwId, String userId) {
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					mwNameRepo.updateMWName(mwName, mwId, userId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		pool.shutdown();

	}

	/** rename MW in cache **/
	private void renameMwInCache(String newWwName, int mwId, String userId) {

		List<JSONObject> res = MwCacheController.getAdvanceMWListByUserId().get(userId);
		String marketWatchId = String.valueOf(mwId);
		JSONObject result = null;
		if (res != null && res.size() > 0) {
			for (int itr = 0; itr < res.size(); itr++) {
				result = new JSONObject();
				result = res.get(itr);
				String mw = (String) result.get("mwId").toString();
				if (marketWatchId.equalsIgnoreCase(mw)) {
					result.remove("mwName");
					result.put("mwName", newWwName);
					res.remove(itr);
					res.add(itr, result);
					MwCacheController.getAdvanceMWListByUserId().remove(userId);
					MwCacheController.getAdvanceMWListByUserId().put(userId, res);
					break;
				}
			}
		}

	}

	/**
	 * 
	 * Method to Sort MW scrips
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param pDto
	 * @return
	 */
	@Override
	public RestResponse<ResponseModel> sortMwScrips(MwRequestModel pDto, ClinetInfoModel info) {
		try {
			if (StringUtil.isNotNullOrEmpty(info.getUserId()) && StringUtil.isListNotNullOrEmpty(pDto.getScripData())
					&& pDto.getMwId() > 0) {
				sortFromCache(pDto.getScripData(), info.getUserId(), pDto.getMwId());
				sortScripInDataBase(pDto.getScripData(), info.getUserId(), pDto.getMwId());
				return prepareResponse.prepareSuccessResponseObject(AppConstants.EMPTY_ARRAY);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	private void sortScripInDataBase(List<MwScripModel> scripDataToSort, String userId, int mwId) {

		if (scripDataToSort != null && scripDataToSort.size() > 0) {
			MarketWatchNameDTO mwList = mwNameRepo.findAllByUserIdAndMwId(userId, mwId);
			List<MarketWatchScripDetailsDTO> newScripDetails = new ArrayList<>();
			for (int i = 0; i < scripDataToSort.size(); i++) {
				MwScripModel model = new MwScripModel();
				model = scripDataToSort.get(i);
				for (int j = 0; j < mwList.getMwDetailsDTO().size(); j++) {
					MarketWatchScripDetailsDTO dbData = new MarketWatchScripDetailsDTO();
					dbData = mwList.getMwDetailsDTO().get(j);
					if (dbData.getToken().equalsIgnoreCase(model.getToken())
							&& dbData.getEx().equalsIgnoreCase(model.getExch())) {
						dbData.setSortingOrder(model.getSortingOrder());
						newScripDetails.add(dbData);
					}
				}
			}
			if (newScripDetails != null && newScripDetails.size() > 0) {
				marketWatchRepo.saveAll(newScripDetails);
			}
		}
	}

	public void sortFromCache(List<MwScripModel> dataToSort, String pUserId, int userMwId) {
		if (dataToSort != null && dataToSort.size() > 0) {
			List<JSONObject> res = HazelCacheController.getInstance().getMwListByUserId().get(pUserId);
			String marketWatchId = String.valueOf(userMwId);
			JSONObject result = null;
			int indexOfRes = 0;
			if (res != null && res.size() > 0) {
				for (int itr = 0; itr < res.size(); itr++) {
					result = new JSONObject();
					result = res.get(itr);
					String mwId = (String) result.get("mwId").toString();
					if (marketWatchId.equalsIgnoreCase(mwId)) {
						indexOfRes = itr;
						break;
					}
				}
				if (result != null && !result.isEmpty()) {
					List<CacheMwDetailsModel> scripDetails = (List<CacheMwDetailsModel>) result.get("scrips");
					if (scripDetails != null && scripDetails.size() > 0) {
						for (int i = 0; i < dataToSort.size(); i++) {
							MwScripModel tempDTO = dataToSort.get(i);
							String token = tempDTO.getToken();
							String exch = tempDTO.getExch();
							int sortOrder = tempDTO.getSortingOrder();
							for (int j = 0; j < scripDetails.size(); j++) {
								CacheMwDetailsModel tempScripDTO = scripDetails.get(j);
								String scripToken = tempScripDTO.getToken();
								String scripExch = tempScripDTO.getExchange();
								if (scripToken.equalsIgnoreCase(token) && scripExch.equalsIgnoreCase(exch)) {
									tempScripDTO.setSortOrder(sortOrder);
									scripDetails.remove(j);
									scripDetails.add(tempScripDTO);
								}
							}
						}
						result.remove("scrips");
						result.put("scrips", scripDetails);
						res.remove(indexOfRes);
						res.add(indexOfRes, result);
						HazelCacheController.getInstance().getMwListByUserId().remove(pUserId);
						HazelCacheController.getInstance().getMwListByUserId().put(pUserId, res);
					}
				}
			}
		}
	}

	/**
	 * 
	 * Method to Delete expired contract in MW
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Override
	public RestResponse<ResponseModel> deleteExpiredContract() {

		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String todayDate = format.format(date);
		return entityManager.deleteExpiredContract(todayDate);
	}

}
