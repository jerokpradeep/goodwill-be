package in.codifi.basket.model.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasketOrderReq {

	private String basketName;
	private int basketId;
	private ScripRequestModel scrips;
	private List<ScripRequestModel> scripsList;
	List<Long> scripsId = new ArrayList<Long>();
}
