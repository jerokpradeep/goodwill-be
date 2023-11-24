package in.codifi.client.entity.primary;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_PIN_START_BAR")
public class PinStartBarEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "USER_ID")
	private String userId;
	@Column(name = "SYMBOL")
	private String symbol;
	@Column(name = "TRADING_SYMBOL")
	private String tradingSymbol;
	@Column(name = "FORMATTED_INS_NAME")
	private String formattedInsName;
	@Column(name = "EXCHANGE")
	private String exchange;
	@Column(name = "SEGMENT")
	private String segment;
	@Column(name = "TOKEN")
	private String token;
	@Temporal(TemporalType.DATE)
	@Column(name = "EXPIRY")
	private Date expiry;
	@Column(name = "PDC")
	private String pdc;
	@Column(name = "SORT_ORDER")
	private int sortOrder;
	@Column(name = "SOURCE")
	private String source;


}
