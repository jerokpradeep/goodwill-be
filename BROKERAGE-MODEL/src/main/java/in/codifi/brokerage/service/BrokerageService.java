package in.codifi.brokerage.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.brokerage.config.HazelcastConfig;
import in.codifi.brokerage.entity.BrokerageEquityEntity;
import in.codifi.brokerage.model.request.BrokerageReqModel;
import in.codifi.brokerage.model.response.BrokerageRespModel;
import in.codifi.brokerage.model.response.GenericResponse;
import in.codifi.brokerage.repository.BrokerageRepository;
import in.codifi.brokerage.service.spec.BrokerageServiceSpec;
import in.codifi.brokerage.utility.AppConstants;
import in.codifi.brokerage.utility.PrepareResponse;
import in.codifi.brokerage.utility.StringUtil;

@ApplicationScoped
public class BrokerageService implements BrokerageServiceSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	BrokerageRepository brokerageRepo;

	/**
	 * Method to brokerage calculation
	 *
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> brokerageCalculation(BrokerageReqModel brokerageReqModel) {
		double calBrokerage = 0;
		double calTurnover = 0;
		double ipft = 0;
		try {
			List<String> errorMsg = validateMandatoryField(brokerageReqModel);
			if (StringUtil.isListNotNullOrEmpty(errorMsg))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);

			brokerageReqModel.setPlan("IBT");

			BrokerageEquityEntity dto = new BrokerageEquityEntity();
			String cacheKey = brokerageReqModel.getPlan().toUpperCase() + "_"
					+ brokerageReqModel.getSegment().toUpperCase() + "_"
					+ brokerageReqModel.getInstrumentType().toUpperCase() + "_"
					+ brokerageReqModel.getType().toUpperCase();
			if (HazelcastConfig.getInstance().getBrokerageCalc().get(cacheKey) != null) {
				dto = HazelcastConfig.getInstance().getBrokerageCalc().get(cacheKey);
			} else {
				dto = brokerageRepo.values(brokerageReqModel);
			}
			if (dto != null) {
				double brokerage = Double.parseDouble(dto.getLot());
				double stt = 0.0;
				double transaction_charge = 0.0;
				double sebi = 0.0;
				double stamp_charges = 0.0;
				calTurnover = 0.0;
				double cm_charges = Double.parseDouble(dto.getClearing_member());
				if (dto.getIpft() == null) {
					ipft = 0;
				} else {
					ipft = Double.parseDouble(dto.getIpft());
				}
				double gst = Double.parseDouble(dto.getGst());
				if (brokerageReqModel.getTransactionType().equalsIgnoreCase(AppConstants.BUY)) {
					stt = Double.parseDouble(dto.getSecuritiesTransactionTaxBuy());
					transaction_charge = Double.parseDouble(dto.getTransactionChargesNseBuy());
					sebi = Double.parseDouble(dto.getSebiChargesBuy());
					stamp_charges = Double.parseDouble(dto.getStampChargesBuy());
				} else {
					stt = Double.parseDouble(dto.getSecuritiesTransactionTaxSell());
					transaction_charge = Double.parseDouble(dto.getTransactionChargesNseSell());
					sebi = Double.parseDouble(dto.getSebiChargesSell());
					stamp_charges = Double.parseDouble(dto.getStampChargesSell());
				}
				if (brokerageReqModel.getPrice().matches("\\d+\\.\\d{1,2}")) {
					if (!Double.isInfinite(Double.parseDouble(brokerageReqModel.getPrice()))) {
						calTurnover = (Double.parseDouble(brokerageReqModel.getPrice())
								* Double.parseDouble(brokerageReqModel.getQty()));
						calBrokerage = 0.0;
					}
				} else {
					return prepareResponse.prepareFailedResponse(AppConstants.PRICE_INVALID);
				}
				if (StringUtil.isStrContainsWithEqIgnoreCase(AppConstants.CONST_LOT, dto.getBase())) {
					calBrokerage = brokerage * (Double.parseDouble(brokerageReqModel.getQty())
							/ Double.parseDouble(brokerageReqModel.getLotSize()));
				} else if (StringUtil.isStrContainsWithEqIgnoreCase(AppConstants.CONST_ORDER, dto.getBase())) {
					calBrokerage = brokerage;
				} else if (StringUtil.isStrContainsWithEqIgnoreCase(AppConstants.CONST_TURN_OVER, dto.getBase())) {
					calBrokerage = (brokerage * calTurnover) / 100;
					if (StringUtil.isNotNullOrEmpty(dto.getTurnOver())
							&& StringUtil.isNotNullOrEmpty(dto.getCompareCost())) {
						if (StringUtil.isStrContainsWithEqIgnoreCase(AppConstants.CONST_LOW, dto.getCompareCost())) {
							calBrokerage = calBrokerage > Double.parseDouble(dto.getTurnOver())
									? Double.parseDouble(dto.getTurnOver())
									: calBrokerage;
						} else {
							calBrokerage = calBrokerage < Double.parseDouble(dto.getTurnOver())
									? Double.parseDouble(dto.getTurnOver())
									: calBrokerage;
						}
					}
				}
				BigDecimal roundcalBrokerage = new BigDecimal(calBrokerage).setScale(2, RoundingMode.HALF_EVEN);
				BigDecimal roundTrunover = new BigDecimal(calTurnover).setScale(2, RoundingMode.HALF_EVEN);
				double calStt = (stt * calTurnover) / 100;
				BigDecimal roundcalStt = new BigDecimal(calStt).setScale(2, RoundingMode.HALF_EVEN);
				double calTransactionCharge = (transaction_charge * calTurnover) / 100;
				BigDecimal roundcalTransactionCharge = new BigDecimal(calTransactionCharge).setScale(2,
						RoundingMode.HALF_EVEN);
				double calSebi = (sebi * calTurnover) / 100;
				BigDecimal roundcalSebi = new BigDecimal(calSebi).setScale(2, RoundingMode.HALF_EVEN);
				double calStampCharge = (stamp_charges * calTurnover) / 100;
				BigDecimal roundcalStampCharge = new BigDecimal(calStampCharge).setScale(2, RoundingMode.HALF_EVEN);
				double calCmCharge = (cm_charges * calTurnover) / 100;
				BigDecimal roundcalCmCharge = new BigDecimal(calCmCharge).setScale(2, RoundingMode.HALF_EVEN);
				double calIpft = (ipft * calTurnover) / 100;
				String roundIpft = new DecimalFormat("#.0#####################").format(calIpft);
//				BigDecimal roundIpft = new BigDecimal(s).setScale(2, RoundingMode.HALF_EVEN);
				double calGst = ((calBrokerage + calTransactionCharge + calSebi + calCmCharge) * gst) / 100;
				BigDecimal roundGst = new BigDecimal(calGst).setScale(2, RoundingMode.HALF_EVEN);
				BrokerageRespModel calcResultDTO = new BrokerageRespModel();
				calcResultDTO.setTurnOver(String.valueOf(roundTrunover));
				calcResultDTO.setBrokerage(String.valueOf(roundcalBrokerage));
				calcResultDTO.setStt(String.valueOf(roundcalStt));
				calcResultDTO.setTransactionCharge(String.valueOf(roundcalTransactionCharge));
				calcResultDTO.setSebi(String.valueOf(roundcalSebi));
				calcResultDTO.setStampCharges(String.valueOf(roundcalStampCharge));
				calcResultDTO.setCmCharges(String.valueOf(roundcalCmCharge));
				calcResultDTO.setGst(String.valueOf(roundGst));
				calcResultDTO.setIpft(String.valueOf(roundIpft));
				double total = calBrokerage + calStt + calTransactionCharge + calSebi + calStampCharge + calCmCharge
						+ calGst + calIpft;
				BigDecimal roundtotal = new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
				calcResultDTO.setTotal(String.valueOf(roundtotal));
				return prepareResponse.prepareSuccessResponseObject(calcResultDTO);

			}

		} catch (

		Exception e) {
			e.printStackTrace();

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to validate the request param
	 * 
	 * @author SOWMIYA
	 * 
	 * @param calcRequestDTO
	 * @return
	 */
	private List<String> validateMandatoryField(BrokerageReqModel calcRequestDTO) {
		List<String> errorMsg = new ArrayList<>();
		if (calcRequestDTO == null) {
			errorMsg.add(AppConstants.DTO_NULL);
		} else {
			if (StringUtil.isNullOrEmpty(calcRequestDTO.getUser())) {
				errorMsg.add(AppConstants.USER_NULL);
			}
			if (StringUtil.isNullOrEmpty(calcRequestDTO.getPlan())) {
				errorMsg.add(AppConstants.PLAN_NULL);
			}
			if (StringUtil.isNullOrEmpty(calcRequestDTO.getSegment())) {
				errorMsg.add(AppConstants.SEGMENT_NULL);
			}
			if (StringUtil.isNullOrEmpty(calcRequestDTO.getInstrumentType())) {
				errorMsg.add(AppConstants.INSTRUMENT_TYPE_NULL);
			}
			if (StringUtil.isNullOrEmpty(calcRequestDTO.getType())) {
				errorMsg.add(AppConstants.TYPE_NULL);
			}
			if (StringUtil.isNullOrEmpty(calcRequestDTO.getTransactionType())) {
				errorMsg.add(AppConstants.TRANSACTION_TYPE_NULL);
			}
			if (StringUtil.isNullOrEmpty(calcRequestDTO.getPrice())) {
				errorMsg.add(AppConstants.PRICE_NULL);
			}
			if (StringUtil.isNullOrEmpty(calcRequestDTO.getLotSize())) {
				errorMsg.add(AppConstants.LOTSIZE_NULL);
			}
			if (StringUtil.isNullOrEmpty(calcRequestDTO.getQty())) {
				errorMsg.add(AppConstants.QUANTITY_NULL);
			}
			if (StringUtil.isNullOrEmpty(calcRequestDTO.getToken())) {
				errorMsg.add(AppConstants.TOKEN_NULL);
			}
		}
		return errorMsg;
	}

}
