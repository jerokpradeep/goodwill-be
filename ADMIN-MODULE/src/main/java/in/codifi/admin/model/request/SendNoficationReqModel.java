package in.codifi.admin.model.request;

import java.util.Date;

import org.json.simple.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendNoficationReqModel {
	private String message;
	private String url;
	private String title;
	private String messageType;
	private String[] userId;
	private String userType;
	private JSONObject orderRecommendation;
	private String icon;
	private Date validity;

}
