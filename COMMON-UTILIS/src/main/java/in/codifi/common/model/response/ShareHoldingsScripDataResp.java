package in.codifi.common.model.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShareHoldingsScripDataResp implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String companyCode;
	private String mktSegmentId;
	private String ODINCode;
	private String symbol;
	private String series;

}
