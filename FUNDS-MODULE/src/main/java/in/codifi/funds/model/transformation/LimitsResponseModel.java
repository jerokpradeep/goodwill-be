package in.codifi.funds.model.transformation;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LimitsResponseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float availableMargin;
	private float openingBalance;
	private float marginUsed;
	private float payin;
	private float stockPledge;
	private float holdingSellCredit;
//	private String peakMargin;//TODO
	private float brokerage;
	private float exposure;
	private float span;
	private float premium;
	private float unclearedCash;
	private float payout;

}
