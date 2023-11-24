package in.codifi.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_ANNOUCEMENTS_DATA")
public class AnnoucementsDataEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "ID")
	private long id;

	@Column(name = "DATE")
	private String date;
	
	@Column(name = "EXCH")
	private String exch;

	@Column(name = "COMPANY_CODE")
	private String companyCode;
	
	@Column(name = "SYMBOL")
	private String symbol;
	
	@Column(name = "COMPANY_LONG_NAME")
	private String companyLongName;
	
	@Column(name = "MKT_SEGMENT_ID_NSE")
	private String mktSegmentIdNSE;
	
	@Column(name = "SERIES_NSE")
	private String seriesNSE;
	
	@Column(name = "ODINCODE_NSE")
	private String odincodeNSE;

	@Column(name = "MKT_SEGMENT_ID_BSE")
	private String mktSegmentIdBSE;
	
	@Column(name = "SERIES_BSE")
	private String seriesBSE;
	
	@Column(name = "ODINCODE_BSE")
	private String odincodeBSE;
	
	@Column(name = "BSE_CODE")
	private String bseCode;
	
	@Column(name = "CAPTION")
	private String caption;
	
	@Column(name = "MEMO")
	private String memo;
	

}
