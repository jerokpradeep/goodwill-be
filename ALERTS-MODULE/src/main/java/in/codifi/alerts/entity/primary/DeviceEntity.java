package in.codifi.alerts.entity.primary;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_DEVICE_MAPPING")
public class DeviceEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "DEVICE_ID")
	private String deviceId;

	@Column(name = "DEVICE_TYPE")
	private String deviceType;

}
