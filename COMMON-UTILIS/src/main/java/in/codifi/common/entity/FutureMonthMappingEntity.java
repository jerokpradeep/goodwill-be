package in.codifi.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_FUTURE_MONTH_DATA_MAP")
@Getter
@Setter
public class FutureMonthMappingEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "EXCH")
	private String exch;
	@Column(name = "SYMBOL")
	private String symbol;
	@Column(name = "INS_TYPE")
	private String insType;

}
