package in.codifi.common.entity.log;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessLogModel {

	private long id;
	private String uri;
	private String url;
	private String ucc;
	private String userId;
	private String reqId;
	private String source;
	private String vendor;
	private Timestamp inTime;
	private Timestamp outTime;
	private long lagTime;
	private String module;
	private String method;
	private String reqBody;
	private String resBody;
	private String deviceIp;
	private String userAgent;
	private String domain;
	private String contentType;
	private String session;
	private String tableName;
	private Date elapsed_time;
	private Timestamp createdOn;
	private Timestamp updatedOn;
	private Timestamp totalTime;
}
