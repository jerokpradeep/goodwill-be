package in.codifi.cache.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisRespModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dateval;
	private String symbol;
	private Double ltp;
	private Double pdc;
	private Integer closePerChg;
	private String direction;
	private String highlight;
	private String isin;
	private String token;
	private String exch;
}
