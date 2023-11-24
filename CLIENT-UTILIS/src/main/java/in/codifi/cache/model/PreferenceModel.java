package in.codifi.cache.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferenceModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String tag;
	private String value;
	private String source;
}
