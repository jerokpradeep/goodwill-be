package in.codifi.basket.model.request;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetrieveBasketModel {

	private long basketId;

	private String basketName;

	private String isExecuted;

	private Date createdOn;

	private long scripCount;

}
