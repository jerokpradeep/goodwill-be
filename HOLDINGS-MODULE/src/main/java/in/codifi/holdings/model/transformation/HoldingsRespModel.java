package in.codifi.holdings.model.transformation;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HoldingsRespModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean poa;
	private String product;
	private List<Holdings> holdings;

}
