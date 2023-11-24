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

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_EQUITY_SCAN")
@Getter
@Setter
public class EquityScanEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "SCAN_ID")
	private int scannerId;

	@Column(name = "SCAN_NAME")
	private String scannerName;

	@Column(name = "VOLUME")
	private String volume;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "SCAN_ID", referencedColumnName = "SCAN_ID")
	@OrderBy("id")
	private List<EquityScanDetailsEntity> scrips;

}
