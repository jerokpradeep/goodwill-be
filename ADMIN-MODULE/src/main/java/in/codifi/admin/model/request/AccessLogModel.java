package in.codifi.admin.model.request;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessLogModel {

	private int id;
	private String uri;
	private String ucc;
	private String user_id;
	private String req_id;
	private String source;
	private String vendor;
	private Timestamp in_time;
	private Timestamp out_time;
	private Long lag_time;
	private String module;
	private String method;
	private String req_body;
	private String res_body;
	private String deviceIp;
	private String userAgent;
	private String domain;
	private String content_type;
	private String session;
	private Date elapsed_time;
	private String created_on;
	private String updated_on;
	private String total_count;
}
