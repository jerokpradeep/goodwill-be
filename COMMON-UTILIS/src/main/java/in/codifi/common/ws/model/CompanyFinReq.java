package in.codifi.common.ws.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyFinReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> segment;
	private List<String> token;

}
