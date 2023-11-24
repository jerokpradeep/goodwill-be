package in.codifi.common.model.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CFRatioResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String year;
	private String eps;
	private String pe;
	private String peg;
	private String dividendYield;
	private String currentRatio;
	private String debtEquityRatio;
	private String ebitdaGrowth;
	private String patGrowth;
	private String netSalesGrowth;
	private String quickRatio;

}
