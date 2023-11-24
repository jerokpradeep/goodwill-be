package in.codifi.scrips.model.transformation;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityInfoRespModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String exchange;
	private String tradingSymbol;
	private String companyName;
	private String symbolName;
	private String segment;
	private String instrumentName;
	private String isin;
	private String pricePrecision;
	private String lotSize;
	private String tickSize;
	private String multiplier;
	private String tradeUnits;
	private String deliveryUnits;
	private String token;
	private String varMargin;
	private String prcftr_d;
	private String expiry;

	private String strikePrice;
	private String optionType;
	private String gp_nd;
	private String priceUnit;
	private String priceQuoteQty;
	private String freezeQty;
	private String scripupdateGsmInd;
	private String elmBuyMargin;
	private String elmSellMargin;
	private String additionalLongMargin;
	private String additionalShortMargin;
	private String specialLongMargin;
	private String SpecialShortMargin;
	private String deliveryMargin;
	private String tenderMargin;
	private String tenderStartDate;

	private String tenderEndEate;
	private String exerciseStartDate;
	private String exerciseEndDate;
	private String markettype;
	private String issuedate;
	private String listingDate;
	private String lastTradingDate;
	private String elmMargin;
	private String exposureMargin;
	private String weekly;
	private String Nontradableinstruments;
	private String dname;

}
