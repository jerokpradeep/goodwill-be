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

@Entity(name = "TBL_SECTOR_HEATMAP")
@Getter
@Setter
public class SectorHeatMapEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "SECTOR_ID")
	private int sectorId;

	@Column(name = "sector_name")
	private String sectorName;

	@Column(name = "ONE_DAY")
	private String oneDay;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "SECTOR_ID", referencedColumnName = "SECTOR_ID")
	@OrderBy("id")
	private List<SectorHeatMapDetailsEntity> scrips;

}
