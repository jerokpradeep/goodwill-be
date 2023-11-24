package in.codifi.orders.model.transformation;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericOrderMariginRespModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String remarks;
//	private String cash;
//	private String orderMargin;
//	private String marginUsedPrev;
	private float marginUsed;
	private float openingBalance;
	private float requiredMargin;
	private float MarginShortfall;
	private float availableMargin;



}