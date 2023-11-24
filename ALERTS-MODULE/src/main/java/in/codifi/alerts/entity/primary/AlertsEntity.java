package in.codifi.alerts.entity.primary;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_ALERT_DETAILS")
public class AlertsEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "ALERT_NAME")
	private String alertName;

	@Column(name = "SCRIP_NAME")
	private String scripName;

	@Column(name = "EXCH")
	private String exch;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "ALERT_TYPE")
	private String alertType;

	@Column(name = "OPERATOR")
	private String operator;

	@Column(name = "VALUE")
	private double value;

	@Column(name = "TRIGGER_STATUS")
	private String triggerStatus = "0";

	@Column(name = "EXPIRY")
	private Date expiry;

	@Column(name = "TRIGGERED_TIME")
	private String triggeredTime;

	@Column(name = "EXCH_SEG")
	private String exchSeg;

}
