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
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_BO_PAYOUT_LOG")
@Getter
@Setter
public class BOPaymentLogEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "USER_ID")
	private String userId;
	@Column(name = "BANK_ACC_NO")
	private String bankActNo;
	@Column(name = "IFSC_CODE")
	private String ifscCode;
	@Column(name = "EXCH_SEG")
	private String segment;
	@Column(name = "AMOUNT")
	private String amount;
	@Column(name = "REQUEST")
	private String request;
	@Column(name = "RESPONSE")
	private String response;
	@Column(name = "PAYOUT_REASON")
	private String payoutReason;

	@Id
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

//	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "CREATED_ON", insertable = false, updatable = false)
//	@Temporal(TemporalType.TIMESTAMP)
	@Temporal(TemporalType.DATE)
//	@CreationTimestamp
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

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus = 1;

}
