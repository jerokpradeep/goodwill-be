package in.codifi.scrips.entity.chartdb;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromptModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String isin;
	private String symbol;
	private String company_name;
	private String token;
	private String exch;
	private String msg;
	private String type;
	private String prompt;
	private String severity;

}
