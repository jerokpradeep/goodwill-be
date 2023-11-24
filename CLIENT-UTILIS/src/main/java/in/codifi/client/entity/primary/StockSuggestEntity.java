package in.codifi.client.entity.primary;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_STOCK_SUGGEST")
public class StockSuggestEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "SYMBOL")
	private String symbol;

	@Column(name = "TRADING_SYMBOL")
	private String tradingSymbol;

	@Column(name = "FORMATTED_INS_NAME")
	private String formattedInsName;

	@Column(name = "EXCH")
	private String exchange;

	@Column(name = "EXCH_SEG")
	private String segment;

	@Column(name = "COMPANY_NAME")
	private String companyName;

	@Column(name = "ALTER_TOKEN")
	private String alterToken;

}
