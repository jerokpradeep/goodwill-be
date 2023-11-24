package in.codifi.admin.entity;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity(name = "tbl_user_notification")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserNotification implements Serializable {

	@Id
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "message")
	private String message;

	@Column(name = "url")
	private String url;

	@Column(name = "title")
	private String title;

	@Column(name = "message_type")
	private String messageType;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "user_type")
	private String userType;

	@Column(name = "icon")
	private String icon;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "validity")
	private Date validity;

	@Lob
	@Column(name = "order_recommendation")
	private String orderRecommendation;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "created_on", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdOn;

	@Column(name = "created_by")
	private String createdBy;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "active_status")
	private Boolean activeStatus;

}