package in.codifi.common.entity;

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
@Entity(name = "TBL_ETF_DETAILS")
public class EtfDetailsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "ID")
	private Long id;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "ETF_ID")
	private int etfId;

	@Column(name = "EXCHANGE")
	private String exchange;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "SYMBOL")
	private String symbol;

	@Column(name = "TRADING_SYMBOL")
	private String tradingSymbol;

	@Column(name = "FORMATTED_INS_NAME")
	private String formattedInsName;

	@Column(name = "SORT_ORDER")
	private String sortOrder;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "SEGMENT")
	private String segment;

}
