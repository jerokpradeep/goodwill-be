package in.codifi.admin.resp.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessLogRespModel {
	
	
	private long id;
	private String userId;
	private String url;
	private String inTime;
	private String outTime;
	private Timestamp totalTime;
	private String module;
	private String method;
	private String reqBody;
	private String resBody;
	private String createdOn;
	private String updatedOn;
	
	
//	private long id;
//	private String userId;
//	private String url;
//	private Timestamp inTime;
//	private Timestamp outTime;
//	private Timestamp totalTime;
//	private String module;
//	private String method;
//	private String reqBody;
//	private String resBody;
//	private Timestamp createdOn;
//	private String createdOn;
//	private Timestamp updatedOn;

}
