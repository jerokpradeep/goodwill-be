package in.codifi.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_INDICES_DETAILS")
public class IndicesEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "ID")
	private long id;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "SYMBOL")
	private String symbol;

	@Column(name = "TRADING_SYMBOL")
	private String tradingSymbol;

	@Column(name = "FORMATTED_INS_NAME")
	private String formattedInsName;

	@Column(name = "EXCHANGE")
	private String exchange;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "SEGMENT")
	private String segment;

	@Temporal(TemporalType.DATE)
	@Column(name = "EXPIRY")
	private Date expiry;

	@Column(name = "PDC")
	private String pdc;

	@Column(name = "SORT_ORDER")
	private int sortOrder;
}
