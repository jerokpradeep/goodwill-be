package in.codifi.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_FII_INDEX")
@Getter
@Setter
public class FiiIndexEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "SYMBOL")
	private String symbol;

	@Column(name = "TURNOVER")
	private String turnover;

}
