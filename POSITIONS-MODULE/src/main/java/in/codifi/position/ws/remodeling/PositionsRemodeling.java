package in.codifi.position.ws.remodeling;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import in.codifi.cache.model.ContractMasterModel;
import in.codifi.position.config.HazelcastConfig;
import in.codifi.position.entity.primary.PositionAvgPriceEntity;
import in.codifi.position.model.transformation.PositionsRespModel;
import in.codifi.position.repository.PositionsRepository;
import in.codifi.position.utility.AppConstants;
import in.codifi.position.utility.AppUtil;
import in.codifi.position.utility.StringUtil;
import in.codifi.position.ws.model.RestPositionSuccessResp;
import io.quarkus.logging.Log;

@ApplicationScoped
public class PositionsRemodeling {

	@Inject
	AppUtil appUtil;

	@Inject
	PositionsRepository repository;

	public List<PositionsRespModel> preparePostitionResp(List<RestPositionSuccessResp> success, String userId) {
		List<PositionsRespModel> responseList = new ArrayList<>();
		try {
			List<PositionAvgPriceEntity> dbAvgPriceEntities = getUploadedData(userId);
			for (RestPositionSuccessResp model : success) {
				PositionsRespModel extract = new PositionsRespModel();
				String token = model.getToken();
				String exch = model.getExch();
				String pdc = "0";
				String segment = "";
				String formattedInsName = "";
				float buyPrice = 0f;
				float sellPrice = 0f;
				float mtmBuyPrice = 0f;
				float mtmSellPrice = 0f;

				int pnlLotSize = 0;
				ContractMasterModel contractMasterModel = AppUtil.getContractMaster(exch, token);
				if (contractMasterModel != null) {
					pdc = StringUtil.isNotNullOrEmpty(contractMasterModel.getPdc()) ? contractMasterModel.getPdc()
							: "0";
					segment = StringUtil.isNotNullOrEmpty(contractMasterModel.getSegment())
							? contractMasterModel.getSegment()
							: "";
					formattedInsName = StringUtil.isNotNullOrEmpty(contractMasterModel.getFormattedInsName())
							? contractMasterModel.getFormattedInsName()
							: "";

					/** For GoldM need to set lot as 10 **/
//					if (exch.equalsIgnoreCase("MCX") && StringUtil.isNotNullOrEmpty(contractMasterModel.getSymbol())
//							&& contractMasterModel.getSymbol().equalsIgnoreCase("GOLDM")) {
//						pnlLotSize = 10;
//					}
					/** get PNL Lot from DB. If not available get it from contract master **/
					String dbPnlLot = AppUtil.getPnlLotSize(exch, contractMasterModel.getSymbol());
					if (StringUtil.isNotNullOrEmpty(dbPnlLot)) {
						pnlLotSize = Integer.parseInt(dbPnlLot);
					} else {
						pnlLotSize = StringUtil.isNotNullOrEmpty(contractMasterModel.getLotSize())
								? Integer.parseInt(contractMasterModel.getLotSize())
								: 0;
					}
				}

				int netQty = StringUtil.isNotNullOrEmpty(model.getNetqty()) ? Integer.parseInt(model.getNetqty()) : 0;

				int lotSize = StringUtil.isNotNullOrEmpty(model.getLs()) ? Integer.parseInt(model.getLs()) : 0;

				float netAvgPrice = StringUtil.isNotNullOrEmpty(model.getNetavgprc())
						? Float.parseFloat(model.getNetavgprc())
						: 0;
				float buyAvgPrice = StringUtil.isNotNullOrEmpty(model.getTotbuyavgprc())
						? Float.parseFloat(model.getTotbuyavgprc())
						: 0;
				float sellAvgPrice = StringUtil.isNotNullOrEmpty(model.getTotsellavgprc())
						? Float.parseFloat(model.getTotsellavgprc())
						: 0;
				float realizedpnl = StringUtil.isNotNullOrEmpty(model.getRpnl()) ? Float.parseFloat(model.getRpnl())
						: 0;
				int multiplier = StringUtil.isNotNullOrEmpty(model.getMult()) ? Integer.parseInt(model.getMult()) : 0;
				float breakevenPrice = StringUtil.isNotNullOrEmpty(model.getBep()) ? Float.parseFloat(model.getBep())
						: 0;
				float netUploadedPrice = StringUtil.isNotNullOrEmpty(model.getNetupldprc())
						? Float.parseFloat(model.getNetupldprc())
						: 0;
				float uploadedPrice = StringUtil.isNotNullOrEmpty(model.getUpldprc())
						? Float.parseFloat(model.getUpldprc())
						: 0;
				/** Day Data **/
				int dayBuyQty = StringUtil.isNotNullOrEmpty(model.getDaybuyqty())
						? Integer.parseInt(model.getDaybuyqty())
						: 0;
				float dayBuyAmount = StringUtil.isNotNullOrEmpty(model.getDaybuyamt())
						? Float.parseFloat(model.getDaybuyamt())
						: 0;
				float dayBuyAvgPrice = StringUtil.isNotNullOrEmpty(model.getDaybuyavgprc())
						? Float.parseFloat(model.getDaybuyavgprc())
						: 0;
				int daySellQty = StringUtil.isNotNullOrEmpty(model.getDaysellqty())
						? Integer.parseInt(model.getDaysellqty())
						: 0;
				float daySellAmount = StringUtil.isNotNullOrEmpty(model.getDaysellamt())
						? Float.parseFloat(model.getDaysellamt())
						: 0;

				float daySellAvgPrice = StringUtil.isNotNullOrEmpty(model.getDaysellavgprc())
						? Float.parseFloat(model.getDaysellavgprc())
						: 0;
				/** Carry forward Data **/
				int cfBuyQty = StringUtil.isNotNullOrEmpty(model.getCfbuyqty()) ? Integer.parseInt(model.getCfbuyqty())
						: 0;
				float cfBuyAmount = StringUtil.isNotNullOrEmpty(model.getCfbuyamt())
						? Float.parseFloat(model.getCfbuyamt())
						: 0;
				float cfBuyAvgPrice = StringUtil.isNotNullOrEmpty(model.getCfbuyavgprc())
						? Float.parseFloat(model.getCfbuyavgprc())
						: 0;
				int cfSellQty = StringUtil.isNotNullOrEmpty(model.getCfsellqty())
						? Integer.parseInt(model.getCfsellqty())
						: 0;
				float cfSellAvgPrice = StringUtil.isNotNullOrEmpty(model.getCfsellavgprc())
						? Float.parseFloat(model.getCfsellavgprc())
						: 0;
				float cfSellAmount = StringUtil.isNotNullOrEmpty(model.getCfsellamt())
						? Float.parseFloat(model.getCfsellamt())
						: 0;
				extract.setDisplayName(formattedInsName);
				extract.setTradingsymbol(model.getTsym());
				extract.setExchange(exch);
				if (StringUtil.isNotNullOrEmpty(model.getPrd())) {
					if (model.getPrd().equalsIgnoreCase(AppConstants.REST_BRACKET)
							|| model.getPrd().equalsIgnoreCase(AppConstants.REST_COVER)) {
						extract.setOrderType(appUtil.getOrderType(model.getPrd()));
						extract.setProduct(appUtil.getProductType(AppConstants.REST_PRODUCT_MIS));
					} else {
						extract.setOrderType(appUtil.getOrderType(AppConstants.REST_PRODUCT_NRML));
						extract.setProduct(appUtil.getProductType(model.getPrd()));
					}
				}
				if (dbAvgPriceEntities != null && dbAvgPriceEntities.size() > 0) {
					for (PositionAvgPriceEntity avgPriceEntity : dbAvgPriceEntities) {
						if (avgPriceEntity.getToken() != null && token.equals(avgPriceEntity.getToken())
								&& (cfSellAvgPrice > 0 || cfBuyAvgPrice > 0)) {
							if (StringUtil.isNotNullOrEmpty(avgPriceEntity.getNetQty())
									&& StringUtil.isNotNullOrEmpty(avgPriceEntity.getNetRate())) {
								String dbQty = avgPriceEntity.getNetQty();
								String dbAvgPrice = avgPriceEntity.getNetRate();
								uploadedPrice = Float.parseFloat(dbAvgPrice);
								if (!exch.equalsIgnoreCase("MCX")) {
									if (dbQty.contains("-")) {
										dbQty = dbQty.replace("-", "");
										cfSellQty = Integer.parseInt(dbQty);
									} else {
										cfBuyQty = Integer.parseInt(dbQty);
									}
								}
							}
						}
					}
				}
				extract.setNetAvgPrice(String.valueOf(netUploadedPrice));
				extract.setNetQty(String.valueOf(netQty));
				extract.setOvernightQty(cfBuyQty > 0 ? String.valueOf(cfBuyQty) : String.valueOf(-cfSellQty));
				extract.setOvernightPrice(String.valueOf(uploadedPrice));
				extract.setBuyQty(String.valueOf(dayBuyQty + cfBuyQty));
				extract.setSellQty(String.valueOf(daySellQty + cfSellQty));
				extract.setRealizedPnl(String.valueOf(realizedpnl));
				extract.setUnrealizedPnl(model.getUrmtom());
				extract.setMultiplier(String.valueOf(multiplier));
				extract.setLotsize(model.getLs());
				extract.setTicksize(model.getTi());
				extract.setPdc(pdc);
				extract.setLtp(model.getLp());
				extract.setToken(token);
				extract.setBreakevenPrice(String.valueOf(breakevenPrice));
				extract.setPnlLotsize(String.valueOf(pnlLotSize));
				/** Buy/sell price calculation **/
				/** If exch is MCX divide the Qty by lot size **/
				if (exch.equalsIgnoreCase("MCX")) {
					cfSellQty = cfSellQty > 0 ? (cfSellQty / lotSize) : cfSellQty;
					cfBuyQty = cfBuyQty > 0 ? (cfBuyQty / lotSize) : cfBuyQty;
					dayBuyQty = dayBuyQty > 0 ? (dayBuyQty / lotSize) : dayBuyQty;
					daySellQty = daySellQty > 0 ? (daySellQty / lotSize) : daySellQty;
				}
				if (dayBuyQty > 0 || cfBuyQty > 0) {
					buyPrice = ((cfBuyQty * uploadedPrice) + (dayBuyQty * dayBuyAvgPrice)) / (cfBuyQty + dayBuyQty);
					mtmBuyPrice = ((cfBuyQty * cfBuyAvgPrice) + (dayBuyQty * dayBuyAvgPrice)) / (cfBuyQty + dayBuyQty);
				}
				if (daySellQty > 0 || cfSellQty > 0) {
					sellPrice = ((cfSellQty * uploadedPrice) + (daySellQty * daySellAvgPrice))
							/ (cfSellQty + daySellQty);
					mtmSellPrice = ((cfSellQty * cfSellAvgPrice) + (daySellQty * daySellAvgPrice))
							/ (cfSellQty + daySellQty);
				}
				extract.setBuyPrice(String.valueOf(buyPrice));
				extract.setSellPrice(String.valueOf(sellPrice));
				extract.setMtmBuyPrice(String.valueOf(mtmBuyPrice));
				extract.setMtmSellprice(String.valueOf(mtmSellPrice));
				responseList.add(extract);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return responseList;
	}

	/**
	 * Method to remodeling position response
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param success
	 * @return
	 */
	public List<PositionsRespModel> preparePostitionRespTest(List<RestPositionSuccessResp> success, String userId) {
		List<PositionsRespModel> responseList = new ArrayList<>();
		try {
			List<PositionAvgPriceEntity> dbAvgPriceEntities = getUploadedData(userId);
			for (RestPositionSuccessResp model : success) {
				PositionsRespModel extract = new PositionsRespModel();

				String token = model.getToken();
				String exch = model.getExch();
				String pdc = "0";
				String segment = "";
				String formattedInsName = "";
				float buyPrice = 0f;
				float sellPrice = 0f;
				float mtmBuyPrice = 0f;
				float mtmSellPrice = 0f;
				int lotSize = 0;
				int restLotSize = StringUtil.isNotNullOrEmpty(model.getLs()) ? Integer.parseInt(model.getLs()) : 0;

				ContractMasterModel contractMasterModel = AppUtil.getContractMaster(exch, token);
				if (contractMasterModel != null) {
					pdc = StringUtil.isNotNullOrEmpty(contractMasterModel.getPdc()) ? contractMasterModel.getPdc()
							: "0";
					segment = StringUtil.isNotNullOrEmpty(contractMasterModel.getSegment())
							? contractMasterModel.getSegment()
							: "";
					formattedInsName = StringUtil.isNotNullOrEmpty(contractMasterModel.getFormattedInsName())
							? contractMasterModel.getFormattedInsName()
							: "";
					lotSize = StringUtil.isNotNullOrEmpty(contractMasterModel.getLotSize())
							? Integer.parseInt(contractMasterModel.getLotSize())
							: 0;
					/** For GoldM need to set lot as 10 **/
					if (exch.equalsIgnoreCase("MCX") && StringUtil.isNotNullOrEmpty(contractMasterModel.getSymbol())
							&& contractMasterModel.getSymbol().equalsIgnoreCase("GOLDM")) {
						lotSize = 10;
					}
				}

				int netQty = StringUtil.isNotNullOrEmpty(model.getNetqty()) ? Integer.parseInt(model.getNetqty()) : 0;

				float netAvgPrice = StringUtil.isNotNullOrEmpty(model.getNetavgprc())
						? Float.parseFloat(model.getNetavgprc())
						: 0;
				float buyAvgPrice = StringUtil.isNotNullOrEmpty(model.getTotbuyavgprc())
						? Float.parseFloat(model.getTotbuyavgprc())
						: 0;
				float sellAvgPrice = StringUtil.isNotNullOrEmpty(model.getTotsellavgprc())
						? Float.parseFloat(model.getTotsellavgprc())
						: 0;
				float realizedpnl = StringUtil.isNotNullOrEmpty(model.getRpnl()) ? Float.parseFloat(model.getRpnl())
						: 0;
				int multiplier = StringUtil.isNotNullOrEmpty(model.getMult()) ? Integer.parseInt(model.getMult()) : 0;

				float breakevenPrice = StringUtil.isNotNullOrEmpty(model.getBep()) ? Float.parseFloat(model.getBep())
						: 0;

				float netUploadedPrice = StringUtil.isNotNullOrEmpty(model.getNetupldprc())
						? Float.parseFloat(model.getNetupldprc())
						: 0;
				float uploadedPrice = StringUtil.isNotNullOrEmpty(model.getUpldprc())
						? Float.parseFloat(model.getUpldprc())
						: 0;

				/** Day Data **/
				int dayBuyQty = StringUtil.isNotNullOrEmpty(model.getDaybuyqty())
						? Integer.parseInt(model.getDaybuyqty())
						: 0;
				int daySellQty = StringUtil.isNotNullOrEmpty(model.getDaysellqty())
						? Integer.parseInt(model.getDaysellqty())
						: 0;
				/** To multiply DB Lot size with quantity **/
				if (exch.equalsIgnoreCase("MCX")) {
					dayBuyQty = dayBuyQty != 0 ? ((dayBuyQty / restLotSize) * lotSize) : dayBuyQty;
					daySellQty = daySellQty != 0 ? ((daySellQty / restLotSize) * lotSize) : daySellQty;
				}

				float dayBuyAmount = StringUtil.isNotNullOrEmpty(model.getDaybuyamt())
						? Float.parseFloat(model.getDaybuyamt())
						: 0;
				float dayBuyAvgPrice = StringUtil.isNotNullOrEmpty(model.getDaybuyavgprc())
						? Float.parseFloat(model.getDaybuyavgprc())
						: 0;
				float daySellAmount = StringUtil.isNotNullOrEmpty(model.getDaysellamt())
						? Float.parseFloat(model.getDaysellamt())
						: 0;
				float daySellAvgPrice = StringUtil.isNotNullOrEmpty(model.getDaysellavgprc())
						? Float.parseFloat(model.getDaysellavgprc())
						: 0;

				/** Carry forward Data **/
				int cfBuyQty = StringUtil.isNotNullOrEmpty(model.getCfbuyqty()) ? Integer.parseInt(model.getCfbuyqty())
						: 0;
				float cfBuyAmount = StringUtil.isNotNullOrEmpty(model.getCfbuyamt())
						? Float.parseFloat(model.getCfbuyamt())
						: 0;
				float cfBuyAvgPrice = StringUtil.isNotNullOrEmpty(model.getCfbuyavgprc())
						? Float.parseFloat(model.getCfbuyavgprc())
						: 0;
				int cfSellQty = StringUtil.isNotNullOrEmpty(model.getCfsellqty())
						? Integer.parseInt(model.getCfsellqty())
						: 0;
				float cfSellAvgPrice = StringUtil.isNotNullOrEmpty(model.getCfsellavgprc())
						? Float.parseFloat(model.getCfsellavgprc())
						: 0;
				float cfSellAmount = StringUtil.isNotNullOrEmpty(model.getCfsellamt())
						? Float.parseFloat(model.getCfsellamt())
						: 0;

				extract.setDisplayName(formattedInsName);
				extract.setTradingsymbol(model.getTsym());
				extract.setExchange(exch);

				if (StringUtil.isNotNullOrEmpty(model.getPrd())) {
					if (model.getPrd().equalsIgnoreCase(AppConstants.REST_BRACKET)
							|| model.getPrd().equalsIgnoreCase(AppConstants.REST_COVER)) {
						extract.setOrderType(appUtil.getOrderType(model.getPrd()));
						extract.setProduct(appUtil.getProductType(AppConstants.REST_PRODUCT_MIS));
					} else {
						extract.setOrderType(appUtil.getOrderType(AppConstants.REST_PRODUCT_NRML));
						extract.setProduct(appUtil.getProductType(model.getPrd()));
					}
				}

				if (dbAvgPriceEntities != null && dbAvgPriceEntities.size() > 0) {
					for (PositionAvgPriceEntity avgPriceEntity : dbAvgPriceEntities) {
						if (avgPriceEntity.getToken() != null && token.equals(avgPriceEntity.getToken())) {

							if (StringUtil.isNotNullOrEmpty(avgPriceEntity.getNetQty())
									&& StringUtil.isNotNullOrEmpty(avgPriceEntity.getNetRate())) {
								String dbQty = avgPriceEntity.getNetQty();
								String dbAvgPrice = avgPriceEntity.getNetRate();
								uploadedPrice = Float.parseFloat(dbAvgPrice);
								if (dbQty.contains("-")) {
									dbQty = dbQty.replace("-", "");
									cfSellQty = Integer.parseInt(dbQty);
//									cfSellAvgPrice = Float.parseFloat(dbAvgPrice);
								} else {
									cfBuyQty = Integer.parseInt(dbQty);
//									cfBuyAvgPrice = Float.parseFloat(dbAvgPrice);
								}
							}

						}
					}
				}

				extract.setNetAvgPrice(String.valueOf(netUploadedPrice));
				extract.setNetQty(String.valueOf(netQty));
				extract.setOvernightQty(cfBuyQty > 0 ? String.valueOf(cfBuyQty) : String.valueOf(-cfSellQty));
				extract.setOvernightPrice(String.valueOf(uploadedPrice));
				extract.setBuyQty(String.valueOf(dayBuyQty + cfBuyQty));
				extract.setSellQty(String.valueOf(daySellQty + cfSellQty));
				extract.setRealizedPnl(String.valueOf(realizedpnl));
				extract.setUnrealizedPnl(model.getUrmtom());
				extract.setMultiplier(String.valueOf(multiplier));
				extract.setLotsize(String.valueOf(restLotSize));
				extract.setPnlLotsize(String.valueOf(lotSize));
				extract.setTicksize(model.getTi());
				extract.setPdc(pdc);
				extract.setLtp(model.getLp());
				extract.setToken(token);
				extract.setBreakevenPrice(String.valueOf(breakevenPrice));

				/** Buy/sell price calculation **/
				/** If exch is MCX divide the Qty by lot size **/
				if (exch.equalsIgnoreCase("MCX")) {
					cfSellQty = cfSellQty > 0 ? (cfSellQty / lotSize) : cfSellQty;
					cfBuyQty = cfBuyQty > 0 ? (cfBuyQty / lotSize) : cfBuyQty;
					dayBuyQty = dayBuyQty > 0 ? (dayBuyQty / lotSize) : dayBuyQty;
					daySellQty = daySellQty > 0 ? (daySellQty / lotSize) : daySellQty;
				}
				if (dayBuyQty > 0 || cfBuyQty > 0) {
					buyPrice = ((cfBuyQty * uploadedPrice) + (dayBuyQty * dayBuyAvgPrice)) / (cfBuyQty + dayBuyQty);
					mtmBuyPrice = ((cfBuyQty * cfBuyAvgPrice) + (dayBuyQty * dayBuyAvgPrice)) / (cfBuyQty + dayBuyQty);
				}
				if (daySellQty > 0 || cfSellQty > 0) {
					sellPrice = ((cfSellQty * uploadedPrice) + (daySellQty * daySellAvgPrice))
							/ (cfSellQty + daySellQty);
					mtmSellPrice = ((cfSellQty * cfSellAvgPrice) + (daySellQty * daySellAvgPrice))
							/ (cfSellQty + daySellQty);
				}
				extract.setBuyPrice(String.valueOf(buyPrice));
				extract.setSellPrice(String.valueOf(sellPrice));
				extract.setMtmBuyPrice(String.valueOf(mtmBuyPrice));
				extract.setMtmSellprice(String.valueOf(mtmSellPrice));

				responseList.add(extract);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return responseList;

	}

	/**
	 * 
	 * Method to get position data from db
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @return
	 */
	private List<PositionAvgPriceEntity> getUploadedData(String userId) {
		List<PositionAvgPriceEntity> avgPriceEntities = new ArrayList<>();
		try {
			if (HazelcastConfig.getInstance().getPositionsAvgPrice().get(userId) != null
					&& HazelcastConfig.getInstance().getPositionsAvgPrice().get(userId).size() > 0) {
				avgPriceEntities = HazelcastConfig.getInstance().getPositionsAvgPrice().get(userId);
			} else {
				if (HazelcastConfig.getInstance().getPositionsAvgPrice().containsKey(userId)) {
					return null;
				} else {
					avgPriceEntities = repository.findAllByClientId(userId);
					if (avgPriceEntities != null && avgPriceEntities.size() > 0) {
						HazelcastConfig.getInstance().getPositionsAvgPrice().put(userId, avgPriceEntities);
					} else {
						HazelcastConfig.getInstance().getPositionsAvgPrice().put(userId, avgPriceEntities);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return avgPriceEntities;
	}
}
