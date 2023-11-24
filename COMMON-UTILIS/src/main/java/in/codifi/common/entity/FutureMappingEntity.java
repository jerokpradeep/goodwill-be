package in.codifi.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_FUTURES_DATA_MAP")
@Getter
@Setter
public class FutureMappingEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "FUTURE_ID")
	private int futureId;

	@Column(name = "FUTURE_NAME")
	private String futureName;

	@Column(name = "INS_TYPE")
	private String insType;

	@Column(name = "SYMBOL")
	private String symbol;

	@Column(name = "exch")
	private String exch;

}
