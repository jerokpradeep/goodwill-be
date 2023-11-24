package in.codifi.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_EQSECTOR_MASTER_MAP")
@Getter
@Setter
public class EQSectorMasterMappingEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "SECTOR_ID")
	private int sectorList;

	@Column(name = "SECTOR_NAME")
	private String sectorName;

	@Column(name = "IMAGE_URL")
	private String imageUrl;

	@Column(name = "THREE_MONTHS")
	private String threeMonths;

	@Column(name = "SIX_MONTHS")
	private String sixMonths;

	@Column(name = "ONE_YEAR")
	private String oneYear;
}
