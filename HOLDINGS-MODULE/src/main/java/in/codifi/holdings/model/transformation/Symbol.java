package in.codifi.holdings.model.transformation;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Symbol implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String exchange;
	private String token;
	private String tradingSymbol;
	private String pdc;
	private String ltp;
}
