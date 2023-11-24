package in.codifi.cache.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDataModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String date;
	private String exch;
	private String companyCode;
	private String symbol;
	private String companyLongName;
	private String mktSegmentIdNSE;
	private String seriesNSE;
	private String odincodeNSE;
	private String mktSegmentIdBSE;
	private String seriesBSE;
	private String odincodeBSE;
	private String bseCode;
	private String caption;
	private String memo;

}
