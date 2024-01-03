package in.codifi.funds.ws.remodeling;

import javax.enterprise.context.ApplicationScoped;

import in.codifi.funds.model.transformation.LimitsResponseModel;
import in.codifi.funds.utility.StringUtil;
import in.codifi.funds.ws.model.RestLimitsResp;
import io.quarkus.logging.Log;

@ApplicationScoped
public class LimitsRemodeling {

	/**
	 * Bind data for limits response
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param model
	 * @return
	 */
	public LimitsResponseModel bindLimitsResponse(RestLimitsResp model) {
		LimitsResponseModel response = new LimitsResponseModel();

		try {
			float openingBalance = StringUtil.isNotNullOrEmpty(model.getCash()) ? Float.parseFloat(model.getCash()) : 0;
			float payin = StringUtil.isNotNullOrEmpty(model.getPayin()) ? Float.parseFloat(model.getPayin()) : 0;
			float payout = StringUtil.isNotNullOrEmpty(model.getPayout()) ? Float.parseFloat(model.getPayout()) : 0;
			float unclearedCash = StringUtil.isNotNullOrEmpty(model.getUnclearedcash())
					? Float.parseFloat(model.getUnclearedcash())
					: 0;
			float marginUsed = StringUtil.isNotNullOrEmpty(model.getMarginUsed())
					? Float.parseFloat(model.getMarginUsed())
					: 0;
			float holdingSellCredit = StringUtil.isNotNullOrEmpty(model.getCacSellCredits())
					? Float.parseFloat(model.getCacSellCredits())
					: 0;
			float brokerage = StringUtil.isNotNullOrEmpty(model.getBrokerage()) ? Float.parseFloat(model.getBrokerage())
					: 0;
			float stockPledge = StringUtil.isNotNullOrEmpty(model.getCollateral())
					? Float.parseFloat(model.getCollateral())
					: 0;

//			float respPayin = payin + unclearedCash;
//			float availableMarigin = (openingBalance + payin + unclearedCash + stockPledge + holdingSellCredit)
//					- (marginUsed + payout);
			/** Change request by Raghuram - 17-05-23 **/
			float availableMarigin = (openingBalance + payin + unclearedCash + stockPledge) - (marginUsed);

			float span = StringUtil.isNotNullOrEmpty(model.getSpan()) ? Float.parseFloat(model.getSpan()) : 0;
			float exposure = StringUtil.isNotNullOrEmpty(model.getExpo()) ? Float.parseFloat(model.getExpo()) : 0;
			float premium = StringUtil.isNotNullOrEmpty(model.getPremium()) ? Float.parseFloat(model.getPremium()) : 0;
			float daycash = StringUtil.isNotNullOrEmpty(model.getDaycash()) ? Float.parseFloat(model.getDaycash()) : 0;
			response.setOpeningBalance(openingBalance);
			response.setPayin(payin);
			response.setMarginUsed(marginUsed);
			response.setHoldingSellCredit(holdingSellCredit);
			response.setBrokerage(brokerage);
			response.setAvailableMargin(availableMarigin);
			response.setStockPledge(stockPledge);
			response.setSpan(span);
			response.setExposure(exposure);
			response.setPremium(premium);
			response.setUnclearedCash(unclearedCash);
			response.setPayout(payout);
			response.setDayCash(daycash);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return response;
	}

}
