package in.codifi.common.model.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShareHoldingsResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String companyCode;
	private ShareHoldingsScripDataResp scripDataNSE;
	private ShareHoldingsScripDataResp scripDataBSE;
	private String yrc;
	private String FIIHolding;
	private String mutualFundHolding;
	private String promoterHolding;
	private String others;

}
