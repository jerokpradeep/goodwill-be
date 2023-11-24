package in.codifi.api.entity.primary;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_PRE_DEFINED_MARKET_WATCH_SCRIPS")
public class PredefinedMwScripsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "MW_ID")
	private int mwId;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "EXCH")
	private String exchange;

	@Column(name = "EXCH_SEG")
	private String segment;

	@Column(name = "SYMBOL")
	private String symbol;

	@Column(name = "TRADING_SYMBOL")
	private String tradingSymbol;

	@Column(name = "FORMATTED_INS_NAME")
	private String formattedInsName;

	@Column(name = "PDC")
	private String pdc;

	@Column(name = "LOT_SIZE")
	private String lotSize;

	@Column(name = "TICK_SIZE")
	private String tickSize;

	@Column(name = "SORTING_ORDER")
	private int sortOrder;

}
