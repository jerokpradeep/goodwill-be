package in.codifi.auth.entity.primary;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_VENDOR_APP")
@Getter
@Setter
public class VendorEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "CREATED_ON", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
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

	@Column(name = "app_name")
	private String appName;

	@Column(name = "API_KEY")
	private String apiKey;

	@Column(name = "API_SECRET")
	private String apiSecret;

	@Column(name = "CLIENT_ID")
	private String clientId;

	@Column(name = "CONTACT_NAME")
	private String contactName;

	@Column(name = "MOBILE_NO")
	private String mobileNo;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "ICON_URL")
	@Lob
	private String iconUrl;
	@Lob
	@Column(name = "REDIRECT_URL")
	private String redirectUrl;
	@Lob
	@Column(name = "POSTBACK_URL")
	private String postbackUrl;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "AUTHORIZATION_STATUS")
	private int authorizationStatus;

	@Column(name = "IS_ACCEPTED")
	private int isAccepted;
	@Lob
	@Column(name = "REJECTED_RESON")
	private String rejectedReson;

	@Column(name = "TYPE")
	private String type;

}
