package in.codifi.cache.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionsAvgPrice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;
	private String netRate;
	private String netQty;
	private String token;
}
