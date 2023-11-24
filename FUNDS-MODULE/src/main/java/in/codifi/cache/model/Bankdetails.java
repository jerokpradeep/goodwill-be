package in.codifi.cache.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bankdetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bankName;
	private String accNumber;

}
