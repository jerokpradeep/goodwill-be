package in.codifi.notify.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_POPUP_NOTIFICATION_USERS")
public class PopUpNotificationUserEntity extends CommonEntity implements Serializable {

	/**
	 * @author LOKESH
	 */

	private static final long serialVersionUID = 1L;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "POPUP_ID")
	private Long popupId;

}
