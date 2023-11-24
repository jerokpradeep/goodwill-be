package in.codifi.holdings.model.transformation;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EdisRespModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String reqId;
	private String dpId;
	private String encData;
	private String edisUrl;

}
