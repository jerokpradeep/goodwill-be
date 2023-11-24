
package in.codifi.admin.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_POSITION_AVG_PRICE")
public class PositionAvgPriceEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "CLIENT_ID")
	private String clientId;
	@Column(name = "SYMBOL")
	private String symbol;
	@Column(name = "TOKEN")
	private String token;
	@Column(name = "EXCHANGE")
	private String exchange;
	@Column(name = "INSTRUMENT_NAME")
	private String instrumentName;
	@Column(name = "INSTRUMENT_TYPE")
	private String instrumentType;
	@Column(name = "EXPIRY")
	private String expiry;
	@Column(name = "STRIKE_PRICE")
	private String strikePrice;
	@Column(name = "OPTION_TYPE")
	private String optionType;
	@Column(name = "NET_QTY")
	private String netQty;
	@Column(name = "NET_RATE")
	private String netRate;

}
