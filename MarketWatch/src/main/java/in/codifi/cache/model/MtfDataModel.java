package in.codifi.cache.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MtfDataModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exch;
	private String companyName;
	private int token;
	private String isin;
	private String symbol;
	private String status;
	private double mtfMargin;
	private int multiplier;

}
