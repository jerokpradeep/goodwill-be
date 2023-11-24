package in.codifi.notify.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_CONFIRM_CONSENT")
public class ConsentEntity extends CommonEntity implements Serializable {

	/**
	 * @author LOKESH
	 */

	private static final long serialVersionUID = 1L;

	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name = "MESSAGE_ID")
	private int messageId;
	
	@Column(name = "MESSAGE_TITLE")
	private String messageTitle;

	@Column(name = "SOURCE")
	private String source;

	@Column(name = "DATE")
	private Date date;

	@Column(name = "DEVICE_ID")
	private String deviceId;

	@Column(name = "IP_ADDRESS")
	private String ipAddress;

}