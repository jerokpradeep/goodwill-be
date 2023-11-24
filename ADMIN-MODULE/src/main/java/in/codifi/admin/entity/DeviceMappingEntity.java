package in.codifi.admin.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_DEVICE_MAPPING")
@Getter
@Setter
public class DeviceMappingEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "user_name")
	private String userName;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "device_id")
	private String deviceId;
//	@Column(name = "source")
//	private String source;
	@Column(name = "device_type")
	private String deviceType;

}
