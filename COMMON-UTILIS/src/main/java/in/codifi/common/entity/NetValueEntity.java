package in.codifi.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_NET_VALUE_DATA")
public class NetValueEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "FII_YESTERDAY")
	private String fiiYesterday;

	@Column(name = "FII_MONTH")
	private String fiiMonth;

	@Column(name = "DII_YESTERDAY")
	private String diiYesterday;

	@Column(name = "DII_MONTH")
	private String diiMonth;

}
