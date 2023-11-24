package in.codifi.notify.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_POPUP_NOTIFICATION")
public class PopUpNotificationEntity extends CommonEntity implements Serializable {

	/**
	 * @author LOKESH
	 */

	private static final long serialVersionUID = 1L;

	@Column(name = "MESSAGE")
	private String message;
	
	@Column(name = "MESSAGE_TITLE")
	private String messageTitle;
	
	@Column(name = "USER_TYPE")
	private String userType;

	@Column(name = "STATUS")
	private int status;

	@Column(name = "OK_EXIST")
	private int okExist;

	@Column(name = "OK_TITLE")
	private String okTitle;

	@Column(name = "CANCEL_EXIST")
	private int cancelExist;

	@Column(name = "CANCEL_TITLE")
	private String cancelTitle;

	@Column(name = "SOURCE_EXIST")
	private int sourceExist;

	@Column(name = "SOURCE_MESSAGE")
	private String sourceMsg;

	@Column(name = "SOURCE_LINK")
	private String sourceLink;
	
	@Column(name = "DESTINATION")
	private String destination;
	
	@Column(name = "DISPLAY_TYPE")
	private String displayType;
	
//	@Column(name = "SSO_LOGIN")
//	private int ssoLogin;

}