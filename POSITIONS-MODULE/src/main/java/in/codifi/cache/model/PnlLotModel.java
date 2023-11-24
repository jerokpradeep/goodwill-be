package in.codifi.cache.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PnlLotModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String exch;
	private String token;
	private String symbol;
	private String lotSize;
	private String tradingSymbol;
	private Date expiry;

}
