package in.codifi.common.ws.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FIIDIIResp implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fiiYesterday;
	private String fiiMonth;
	private String fiiWeek;
	private String diiYesterday;
	private String diiWeek;
	private String diiMonth;

}
