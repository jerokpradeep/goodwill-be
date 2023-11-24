package in.codifi.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_FUTURE_MONTH")
@Getter
@Setter
public class FutureMonthEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "SCRIP_NAME")
	private String scripName;

	@Column(name = "SORT_ORDER")
	private String sortOrder;

	@Column(name = "EXCHANGE")
	private String exchange;

	@Column(name = "EXPIRY")
	private Date expiry;

	@Column(name = "PDC")
	private String pdc;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "SYMBOL")
	private String symbol;

}
