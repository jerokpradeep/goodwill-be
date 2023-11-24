package in.codifi.admin.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;
import org.json.simple.JSONObject;

import in.codifi.admin.model.response.ContractMasterRespModel;
import in.codifi.admin.model.response.ContractSymbolRespModel;
import in.codifi.admin.model.response.ExchangeResponseModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.repository.ContractEntityManager;
import in.codifi.admin.req.model.ContractMasterReqModel;
import in.codifi.admin.service.spec.ContractServiceSpec;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.utility.PrepareResponse;
import in.codifi.admin.utility.StringUtil;

@ApplicationScoped
public class ContractService implements ContractServiceSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	ContractEntityManager contractEntityManager;

	/**
	 * method to get contract master list
	 * 
	 * @author LOKESH
	 * 
	 */
	@Override
	public RestResponse<GenericResponse> getContractMasterList(ContractMasterReqModel reqModel) {
		try {
			if (!validateContractMasterParams(reqModel))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			String symbol = reqModel.getSymbol();
			if (reqModel.getExch().equalsIgnoreCase("nse_fo") || reqModel.getExch().equalsIgnoreCase("nse_cm")
					|| reqModel.getExch().equalsIgnoreCase("bse_cm") || reqModel.getExch().equalsIgnoreCase("cde_fo")
					|| reqModel.getExch().equalsIgnoreCase("nse_idx")
					|| reqModel.getExch().equalsIgnoreCase("bse_idx")) {
				if (!symbol.isEmpty() || !symbol.equalsIgnoreCase("") && !reqModel.getExch().isEmpty()
						|| reqModel.getExch().equalsIgnoreCase("")) {
					symbol = symbol.replace(" ", "%");
					ContractMasterRespModel contractModel = contractEntityManager.getContractMasterList(
							reqModel.getExch(), reqModel.getExpiry(), reqModel.getGroup(), reqModel.getSymbol());
					if (contractModel != null) {
						return prepareResponse.prepareSuccessResponseObject(contractModel);
					}
				}
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_EXCH);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to validate contract master params
	 * 
	 * @author LOKESH
	 * @param reqModel
	 * @return
	 */
	private boolean validateContractMasterParams(ContractMasterReqModel reqModel) {
		if (StringUtil.isNotNullOrEmpty(reqModel.getExch()) && StringUtil.isNotNullOrEmpty(reqModel.getSymbol())) {
			return true;
		}
		return false;
	}

	/**
	 * method to get newly added list
	 * 
	 * @author LOKESH
	 * @return
	 */
	public RestResponse<GenericResponse> getNewlyAddedList() {
		List<ContractSymbolRespModel> symbolResponseModel = new ArrayList<>();
		try {
			List<String> exchSeg = contractEntityManager.getDistinctExchSeg();
			if (exchSeg.size() > 0) {
				for (String exchangeSeg : exchSeg) {
					List<String> exchSegDetails = contractEntityManager.getNewlyAddedListSymbol(exchangeSeg);
					if (exchSegDetails.size() > 0) {
						for (String exchSegment : exchSegDetails) {
							String[] a2 = exchSegment.split("-");
							String exSegment = a2[0];
							if (exSegment.equalsIgnoreCase("bcs_fo")) {
								exSegment = "BSE Currency";
							} else if (exSegment.equalsIgnoreCase("bse_com")) {
								exSegment = "BSE Commodities ";
							} else if (exSegment.equalsIgnoreCase("bse_cm")) {
								exSegment = "BSE Cash ";
							} else if (exSegment.equalsIgnoreCase("bse_fo")) {
								exSegment = "BSE FNO";
							} else if (exSegment.equalsIgnoreCase("cde_fo")) {
								exSegment = "NSE Cash";
							} else if (exSegment.equalsIgnoreCase("mcx_fo")) {
								exSegment = "MCX FNO";
							} else if (exSegment.equalsIgnoreCase("nse_cm")) {
								exSegment = "NSE Cash";
							} else if (exSegment.equalsIgnoreCase("nse_com")) {
								exSegment = "NSE Commodities";
							} else if (exSegment.equalsIgnoreCase("nse_fo")) {
								exSegment = "NSE FNO";
							}
							String groupName = a2[1];
							if (groupName.equalsIgnoreCase("null")) {
								groupName = "-";
							}
							String sym = a2[2];
							String insName = a2[3];
							String token = a2[4];
							ContractSymbolRespModel response = new ContractSymbolRespModel();
							response.setExchSegment(exSegment);
							response.setGroupName(groupName);
							response.setInstrumentName(insName);
							response.setSymbol(sym);
							response.setToken(token);
							symbolResponseModel.add(response);
						}
					}
				}

			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
			return prepareResponse.prepareSuccessResponseObject(symbolResponseModel);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get deactivaed symbol
	 * 
	 * @author LOKESH
	 * @return
	 */
	public RestResponse<GenericResponse> getDeactivatedList() {
		List<ContractSymbolRespModel> symbolResponseModel = new ArrayList<>();
		try {
			List<String> exchSeg = contractEntityManager.getDistinctExchSeg();
			if (exchSeg.size() > 0) {
				for (String exchangeSeg : exchSeg) {
					List<String> exchSegDetails = contractEntityManager.getDeactivatedSymbol(exchangeSeg);
					if (exchSegDetails.size() > 0) {
						for (String exchSegment : exchSegDetails) {
							String[] a2 = exchSegment.split(",");
							String exSegment = a2[0];
							if (exSegment.equalsIgnoreCase("bcs_fo")) {
								exSegment = "BSE Currency";
							} else if (exSegment.equalsIgnoreCase("bse_com")) {
								exSegment = "BSE Commodities ";
							} else if (exSegment.equalsIgnoreCase("bse_cm")) {
								exSegment = "BSE Cash ";
							} else if (exSegment.equalsIgnoreCase("bse_fo")) {
								exSegment = "BSE FNO";
							} else if (exSegment.equalsIgnoreCase("cde_fo")) {
								exSegment = "NSE Cash";
							} else if (exSegment.equalsIgnoreCase("mcx_fo")) {
								exSegment = "MCX FNO";
							} else if (exSegment.equalsIgnoreCase("nse_cm")) {
								exSegment = "NSE Cash";
							} else if (exSegment.equalsIgnoreCase("nse_com")) {
								exSegment = "NSE Commodities";
							} else if (exSegment.equalsIgnoreCase("nse_fo")) {
								exSegment = "NSE FNO";
							}
							String groupName = a2[1];
							if (groupName.equalsIgnoreCase("null")) {
								groupName = "-";
							}
							String sym = a2[2];
							String insName = a2[3];
							String token = a2[4];
							ContractSymbolRespModel response = new ContractSymbolRespModel();
							response.setExchSegment(exSegment);
							response.setGroupName(groupName);
							response.setInstrumentName(insName);
							response.setSymbol(sym);
							response.setToken(token);
							symbolResponseModel.add(response);
						}
					} else {
						return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
					}
				}
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}

			return prepareResponse.prepareSuccessResponseObject(symbolResponseModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to add the contract into the master details
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> addContractMaster(ExchangeResponseModel exchangeModel) {
		if (exchangeModel != null && exchangeModel.getExch() != null && exchangeModel.getExchange_segment() != null
				&& exchangeModel.getToken() != null && exchangeModel.getSymbol() != null) {
			boolean isInserted = contractEntityManager.addNewContractInMaster(exchangeModel,
					exchangeModel.getSort_order_1(), exchangeModel.getSort_order_2(), exchangeModel.getSort_order_3());
			if (!isInserted) {
				return prepareResponse.prepareSuccessResponseObject(AppConstants.INSERTED);
			}
		} else {
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_REQUEST_ADMIN);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get the duplicate symbols list from contract master
	 * 
	 * @author LOKESH
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RestResponse<GenericResponse> getDuplicateList() {
		List<JSONObject> result = new ArrayList<JSONObject>();
		List<String> duplicateSymbols = contractEntityManager.getDuplicateSymbols();
		for (int i = 0; i < duplicateSymbols.size(); i++) {
			String a = duplicateSymbols.get(i);
			String[] a2 = a.split("-");
			String duplicateSegmentSymbol = a2[0];
			if (duplicateSegmentSymbol.equalsIgnoreCase("bse_cm")) {
				duplicateSegmentSymbol = "BSE Cash";
			} else if (duplicateSegmentSymbol.equalsIgnoreCase("nse_cm")) {
				duplicateSegmentSymbol = "NSE Cash";
			}
			String duplicateSymbol = a2[1];
			String duplicateSymbolCount = a2[2];
			JSONObject tempJson = new JSONObject();
			tempJson.put("exch_segment", duplicateSegmentSymbol);
			tempJson.put("duplicate_symbols", duplicateSymbol);
			tempJson.put("count", duplicateSymbolCount);
			result.add(tempJson);
		}
		if (result != null && result.size() > 0) {
			return prepareResponse.prepareSuccessResponseObject(result);
		} else {
			return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
		}
	}
}
