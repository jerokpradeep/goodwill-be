package in.codifi.funds.entity.primary;

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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_BO_PAYMENT_LOGS")
@Getter
@Setter
public class BoPayInLogsEntity implements Serializable {

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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "IST")
	private Date createdOn;
	@Column(name = "METHOD")
	private String method;
	@Column(name = "ORDER_ID")
	private String orderId;
	@Column(name = "USER_ID")
	private String userId;
	@Column(name = "REQUEST")
	private String request;
	@Column(name = "RESPONSE")
	private String response;
	@Column(name = "ERROR_MSG")
	private String errorMsg;
	@Column(name = "IS_ERROR")
	private int isError;

}
