package in.codifi.auth.entity.primary;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_DEVICE_MAPPING")
public class DeviceMappingEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "DEVICE_ID")
	private String deviceId;

	@Column(name = "DEVICE_TYPE")
	private String deviceType;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "CREATED_ON", insertable = true, updatable = false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdOn;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "UPDATED_ON")
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedOn;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "CREATED_BY")
	private String createdBy;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "ACTIVE_STATUS")
	private int activeStatus = 1;
}
