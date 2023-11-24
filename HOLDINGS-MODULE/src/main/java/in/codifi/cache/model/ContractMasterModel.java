package in.codifi.cache.model;

import java.io.Serializable;
import java.util.Date;

public class ContractMasterModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String exch;
	private String segment;
	private String token;
	private String alterToken;
	private String symbol;
	private String tradingSymbol;
	private String formattedInsName;
	private String isin;
	private String groupName;
	private String insType;
	private String optionType;
	private String strikePrice;
	private Date expiry;
	private String lotSize;
	private String tickSize;
	private String freezQty;
	private String pdc;
	private String weekTag;

	public String getExch() {
		return exch;
	}

	public void setExch(String exch) {
		this.exch = exch;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAlterToken() {
		return alterToken;
	}

	public void setAlterToken(String alterToken) {
		this.alterToken = alterToken;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getTradingSymbol() {
		return tradingSymbol;
	}

	public void setTradingSymbol(String tradingSymbol) {
		this.tradingSymbol = tradingSymbol;
	}

	public String getFormattedInsName() {
		return formattedInsName;
	}

	public void setFormattedInsName(String formattedInsName) {
		this.formattedInsName = formattedInsName;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getInsType() {
		return insType;
	}

	public void setInsType(String insType) {
		this.insType = insType;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public String getStrikePrice() {
		return strikePrice;
	}

	public void setStrikePrice(String strikePrice) {
		this.strikePrice = strikePrice;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public String getLotSize() {
		return lotSize;
	}

	public void setLotSize(String lotSize) {
		this.lotSize = lotSize;
	}

	public String getTickSize() {
		return tickSize;
	}

	public void setTickSize(String tickSize) {
		this.tickSize = tickSize;
	}

	public String getFreezQty() {
		return freezQty;
	}

	public void setFreezQty(String freezQty) {
		this.freezQty = freezQty;
	}

	public String getPdc() {
		return pdc;
	}

	public void setPdc(String pdc) {
		this.pdc = pdc;
	}

	public String getWeekTag() {
		return weekTag;
	}

	public void setWeekTag(String weekTag) {
		this.weekTag = weekTag;
	}

}
