package in.codifi.scrips.model.response;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScripSearchResp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exchange;
	private String segment;
	private String symbol;
	private String token;
	private String formattedInsName;
	private String weekTag;
	private String companyName;
	private Date expiry;
}
