package in.codifi.cache.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockReturnModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String exch;
	private String symbol;
	private String token;
	private Date dateOfClose;
	private String close;
	private String prevDayClose;
	private String changePerc;
	private String tagOfPeriod;
	

}
