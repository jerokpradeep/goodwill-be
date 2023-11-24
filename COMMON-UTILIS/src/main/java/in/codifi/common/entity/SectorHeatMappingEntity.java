package in.codifi.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_SECTOR_HEATMAP_DATA_MAP")
@Getter
@Setter
public class SectorHeatMappingEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "SCRIPS")
	private String scrips;

	@Column(name = "SECTOR_ID")
	private int sectorId;

	@Column(name = "SECTOR_NAME")
	private String sectorName;

}
