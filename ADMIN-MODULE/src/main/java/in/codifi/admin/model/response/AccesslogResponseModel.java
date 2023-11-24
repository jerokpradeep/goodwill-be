package in.codifi.admin.model.response;

import java.sql.Date;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement
public class AccesslogResponseModel {

	private int id;
	private String uri;
	private String user_id;
	private Date entry_time;
	private String device_ip;
	private String user_agent;
	private String content_type;
	private int authenticate_token;
	private String response_data;
	private String elapsed_time;
	private String input;
	private java.sql.Timestamp created_on;
	private String created_by;
	private Date updated_on;
	private String updated_by;
	private int active_status;
	private String domain;
	private String userSessionID;
	private int sessionExpiredTag;
	private int offset;
	private String count;

}
