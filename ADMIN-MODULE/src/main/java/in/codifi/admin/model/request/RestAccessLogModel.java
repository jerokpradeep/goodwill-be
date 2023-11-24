package in.codifi.admin.model.request;


import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestAccessLogModel {

	private int id;
	private String url;
	private String user_id;
	private Timestamp in_time;
	private Timestamp out_time;
	private Timestamp total_time;
	private String module;
	private String method;
	private String req_body;
	private String res_body;
	private Timestamp created_on;
	private Timestamp updated_on;
	private String total_count;

}
