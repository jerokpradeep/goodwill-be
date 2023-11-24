package in.codifi.cache.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductMasterModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String keyVariable;
	private String value;
}
