package in.codifi.admin.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogDetailsResponseModel {
	private String uniqueMob;
	private String uniqueWeb;
	private String uniqueApi;
	private String totalMobile;
	private String totalWeb;
	private String totalApi;
	private String date;

}
