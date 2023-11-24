package in.codifi.scrips.ws.remodeling;

import javax.enterprise.context.ApplicationScoped;

import in.codifi.scrips.model.transformation.SecurityInfoRespModel;
import in.codifi.scrips.ws.model.SecurityInfoRestSuccRespModel;
import io.quarkus.logging.Log;

@ApplicationScoped
public class SecurityInfoRemodeling {

	/*
	 * method to bind get security information
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	public SecurityInfoRespModel bindSecurityInfoData(SecurityInfoRestSuccRespModel respModel) {
		SecurityInfoRespModel response = new SecurityInfoRespModel();
		try {
			response.setCompanyName(respModel.getCname());
			response.setDeliveryUnits(respModel.getDelunt());
			response.setExchange(respModel.getExch());
			response.setInstrumentName(respModel.getInstname());
			response.setIsin(respModel.getIsin());
			response.setLotSize(respModel.getLs());
			response.setMultiplier(respModel.getMult());
			response.setPrcftr_d(respModel.getPrcftr_d());
			response.setPricePrecision(respModel.getPp());
			response.setSegment(respModel.getSeg());
			response.setSymbolName(respModel.getSymname());
			response.setTickSize(respModel.getTi());
			response.setToken(respModel.getToken());
			response.setTradeUnits(respModel.getTrdunt());
			response.setTradingSymbol(respModel.getTsym());
			response.setVarMargin(respModel.getVarmrg());
			response.setAdditionalLongMargin(respModel.getAddbmrg());
			response.setAdditionalShortMargin(respModel.getAddsmrg());
			response.setDeliveryMargin(respModel.getDelmrg());
			response.setDname(respModel.getDname());
			response.setDeliveryUnits(respModel.getDelunt());
			response.setElmBuyMargin(respModel.getElmbmrg());
			response.setElmSellMargin(respModel.getElmsmrg());
			response.setElmMargin(respModel.getElmmrg());
			response.setExerciseEndDate(respModel.getExeendd());
			response.setExerciseStartDate(respModel.getExestrd());
			response.setExposureMargin(respModel.getExpmrg());
			response.setFreezeQty(respModel.getFrzqty());
			response.setGp_nd(respModel.getGp_nd());
			response.setIssuedate(respModel.getIssue_d());
			response.setLastTradingDate(respModel.getLast_trd_d());
			response.setListingDate(respModel.getListing_d());
			response.setMarkettype(respModel.getMkt_t());
			response.setNontradableinstruments(respModel.getNontrd());
			response.setOptionType(respModel.getOptt());
			response.setTenderStartDate(respModel.getTenstrd());
			response.setTenderEndEate(respModel.getTenendd());
			response.setTenderMargin(respModel.getTenmrg());
			response.setStrikePrice(respModel.getStrprc());
			response.setSpecialLongMargin(respModel.getSplbmrg());
			response.setSpecialShortMargin(respModel.getSplsmrg());
			response.setExpiry(respModel.getExd());
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}

		return response;
	}

}
