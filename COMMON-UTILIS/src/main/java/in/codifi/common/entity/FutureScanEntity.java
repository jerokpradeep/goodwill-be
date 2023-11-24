package in.codifi.common.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_FUTURE_SCAN")
@Getter
@Setter
public class FutureScanEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "SCAN_ID")
	private int scanId;

	@Column(name = "SCAN_NAME")
	private String scanName;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "SCAN_ID", referencedColumnName = "SCAN_ID")
	@OrderBy("id")
	private List<FutureScanDetailEntity> scrips;

}
