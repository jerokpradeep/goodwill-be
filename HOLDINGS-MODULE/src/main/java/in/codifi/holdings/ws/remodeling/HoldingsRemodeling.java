package in.codifi.holdings.ws.remodeling;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import in.codifi.holdings.model.transformation.Holdings;
import in.codifi.holdings.model.transformation.HoldingsRespModel;
import in.codifi.holdings.model.transformation.NonPoaHoldingsRespModel;
import in.codifi.holdings.model.transformation.NonPoaHoldingsRespModel.NonPoaHoldings;
import in.codifi.holdings.model.transformation.NonPoaHoldingsRespModel.NonPoaHoldings.NonPoaSymbol;
import in.codifi.holdings.model.transformation.Symbol;
import in.codifi.holdings.utility.AppUtil;
import in.codifi.holdings.utility.StringUtil;
import in.codifi.holdings.ws.model.ExchTsym;
import in.codifi.holdings.ws.model.ExchTsymList;
import in.codifi.holdings.ws.model.NonPoaHoldingsSuccess;
import in.codifi.holdings.ws.model.Success;
import in.codifi.holdings.ws.service.LTPRestService;
import io.quarkus.logging.Log;

@ApplicationScoped
public class HoldingsRemodeling {

	@Inject
	LTPRestService ltpRestService;

	/**
	 * Method to bind holding data
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param success
	 * @return
	 */
	public HoldingsRespModel bindHoldingData(List<Success> success, String product) {
		HoldingsRespModel respModel = new HoldingsRespModel();
		try {

//			for (Success sucessResp : success) {
//				String ltpReq = "";
//				for (ExchTsym exchTsymModel : sucessResp.getExchTsym()) {
//					if(exchTsymModel.getExch()!= null && exchTsymModel.getExch().equalsIgnoreCase()
//				}
//				sucessResp.gett
//				String ltpResp = ltpRestService.getLTP(null);
//			}

			respModel.setPoa(false);// TODO
			respModel.setProduct(product);
			List<Holdings> holdingsList = new ArrayList<>();
			for (Success model : success) {
				float ltp = 0f;
				Holdings holdingData = new Holdings();
				List<Symbol> symbols = new ArrayList<>();
				String isin = "";
				for (ExchTsym exchTsymModel : model.getExchTsym()) {
					Symbol symbolDetail = new Symbol();
					String exch = exchTsymModel.getExch();
					String token = exchTsymModel.getToken();
					String symbol = exchTsymModel.getTsym();
					if (StringUtil.isNotNullOrEmpty(exchTsymModel.getIsin())) {
						isin = exchTsymModel.getIsin();
					}
					symbolDetail.setExchange(exch);
					symbolDetail.setToken(token);
					symbolDetail.setTradingSymbol(symbol);
					symbolDetail.setPdc(AppUtil.getPdc(exch, token));
					symbolDetail.setLtp(symbolDetail.getPdc());// TODO Need to change
					symbols.add(symbolDetail);
				}

				holdingData.setSymbol(symbols);

				int btstqty = StringUtil.isNotNullOrEmpty(model.getBtstqty()) ? Integer.parseInt(model.getBtstqty())
						: 0;
				int totalQty = StringUtil.isNotNullOrEmpty(model.getHoldqty()) ? Integer.parseInt(model.getHoldqty())
						: 0;
				int unpledgedQty = StringUtil.isNotNullOrEmpty(model.getUnplgdQty())
						? Integer.parseInt(model.getUnplgdQty())
						: 0;
				int benQty = StringUtil.isNotNullOrEmpty(model.getBenQty()) ? Integer.parseInt(model.getBenQty()) : 0;
				int dpQty = StringUtil.isNotNullOrEmpty(model.getDpQty()) ? Integer.parseInt(model.getDpQty()) : 0;
				int usedQty = StringUtil.isNotNullOrEmpty(model.getUsedqty()) ? Integer.parseInt(model.getUsedqty())
						: 0;
				int nonPoaQty = StringUtil.isNotNullOrEmpty(model.getNpoadqty()) ? Integer.parseInt(model.getNpoadqty())
						: 0;
				int holdQty = StringUtil.isNotNullOrEmpty(model.getHoldqty()) ? Integer.parseInt(model.getHoldqty())
						: 0;
				int tradedQty = StringUtil.isNotNullOrEmpty(model.getTrdqty()) ? Integer.parseInt(model.getTrdqty())
						: 0;
				int collateralQty = StringUtil.isNotNullOrEmpty(model.getColqty()) ? Integer.parseInt(model.getColqty())
						: 0;
				int brokerCollQty = StringUtil.isNotNullOrEmpty(model.getBrkColQty())
						? Integer.parseInt(model.getBrkColQty())
						: 0;
				/** sellableQty calculation **/
				int sellableQty = (btstqty + totalQty + unpledgedQty + benQty + dpQty) - usedQty;
				int netQty = btstqty + holdQty + brokerCollQty + unpledgedQty + benQty + Math.max(nonPoaQty, dpQty);

				float sellAmount = StringUtil.isNotNullOrEmpty(model.getSellAmt())
						? Float.parseFloat(model.getSellAmt())
						: 0;

				float uploadedPrc = StringUtil.isNotNullOrEmpty(model.getUpldprc())
						? Float.parseFloat(model.getUpldprc())
						: 0;
//				int netOty = (holdQty + nonPoaQty) - tradedQty;
				float realizedPnl = sellAmount - (uploadedPrc * tradedQty);
//				float unrealizedPnl = netQty * (buyPrice - ltp);
				float unrealizedPnl = (netQty - tradedQty) * (ltp - uploadedPrc);
				float netPnl = realizedPnl + unrealizedPnl;
				holdingData.setIsin(isin);
				holdingData.setSellAmount(String.valueOf(sellAmount));
				holdingData.setRealizedPnl(String.valueOf(realizedPnl));
				holdingData.setUnrealizedPnl(String.valueOf(unrealizedPnl));
				holdingData.setNetPnl(String.valueOf(netPnl));
				holdingData.setBuyPrice(String.valueOf(uploadedPrc));
				holdingData.setNetQty(String.valueOf(netQty));
				holdingData.setHoldQty(String.valueOf(holdQty));
				holdingData.setDpQty(String.valueOf(dpQty));
				holdingData.setBenQty(String.valueOf(benQty));
				holdingData.setUnpledgedQty(String.valueOf(unpledgedQty));
				holdingData.setCollateralQty(String.valueOf(collateralQty));
				holdingData.setBrkCollQty(String.valueOf(brokerCollQty));
				holdingData.setBtstQty(String.valueOf(btstqty));
				holdingData.setUsedQty(String.valueOf(usedQty));
				holdingData.setTradedQty(String.valueOf(tradedQty));
				holdingData.setSellableQty(String.valueOf(sellableQty));
				holdingData.setAuthQty(String.valueOf(0));// TODO Need to change
				holdingsList.add(holdingData);
			}
			respModel.setHoldings(holdingsList);
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
			throw new RuntimeException();
		}
		return respModel;
	}

	/**
	 * Method to bind non POA Holdings response
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param success
	 * @param product
	 * @return
	 */
	public NonPoaHoldingsRespModel bindNonPoaHoldingData(List<NonPoaHoldingsSuccess> success, String product) {
		NonPoaHoldingsRespModel respModel = new NonPoaHoldingsRespModel();
		try {

			respModel.setProduct(product);
			List<NonPoaHoldings> holdingsList = new ArrayList<>();
			for (NonPoaHoldingsSuccess model : success) {
				float ltp = 0f;
				NonPoaHoldingsRespModel holdingsRespModel = new NonPoaHoldingsRespModel();
				NonPoaHoldings holdingData = holdingsRespModel.new NonPoaHoldings();
				List<NonPoaSymbol> symbols = new ArrayList<>();
				for (ExchTsymList exchTsymModel : model.getExchTsym()) {
					NonPoaSymbol symbolDetail = holdingData.new NonPoaSymbol();
					String exch = exchTsymModel.getExch();
					String token = exchTsymModel.getToken();
					String symbol = exchTsymModel.getTsym();
					symbolDetail.setExchange(exch);
					symbolDetail.setToken(token);
					symbolDetail.setTradingSymbol(symbol);
					symbolDetail.setPdc(AppUtil.getPdc(exch, token));
					symbolDetail.setLtp(symbolDetail.getPdc());// TODO Need to change
					symbols.add(symbolDetail);
				}

				holdingData.setSymbol(symbols);

				int totalQty = StringUtil.isNotNullOrEmpty(model.getTotqty()) ? Integer.parseInt(model.getTotqty()) : 0;
				int approvedQty = StringUtil.isNotNullOrEmpty(model.getApprovedqty())
						? Integer.parseInt(model.getApprovedqty())
						: 0;
				float uploadedPrc = StringUtil.isNotNullOrEmpty(model.getUpldprc())
						? Float.parseFloat(model.getUpldprc())
						: 0;

				holdingData.setIsin(model.getIsin());
				holdingData.setTotalQty(String.valueOf(totalQty));
				holdingData.setApprovedQty(String.valueOf(approvedQty));
				holdingData.setAvgPrice(String.valueOf(uploadedPrc));
				holdingData.setBoId(model.getBoid());
				holdingData.setSettlementType(model.getSettlementType());
				holdingData.setT1Qty(model.getT1qty());
				holdingsList.add(holdingData);
			}
			respModel.setHoldings(holdingsList);
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
			throw new RuntimeException();
		}
		return respModel;
	}
}
