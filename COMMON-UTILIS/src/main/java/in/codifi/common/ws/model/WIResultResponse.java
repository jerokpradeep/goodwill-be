package in.codifi.common.ws.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WIResultResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String token;
	public String date;
	public String highlight;
	public String symbol;
	public String ltp;
	public String closePerChg;
	public String direction;
	public String isin;
	public String pClose;
	public String exchange;
	public String indexID;
	public String indexName;

}
