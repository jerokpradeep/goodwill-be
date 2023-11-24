package in.codifi.client.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Admin
 *
 */
@Getter
@Setter
public class PinToStartbarModel {

	private String token;
	private String exchange;
	private String symbol;
	private int sortOrder;

}
