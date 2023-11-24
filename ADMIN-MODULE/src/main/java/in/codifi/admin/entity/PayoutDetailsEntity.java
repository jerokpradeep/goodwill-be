package in.codifi.admin.entity;

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
@Entity(name = "TBL_PAYOUT_DETAILS")
public class PayoutDetailsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

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

	@Column(name = "BANK_NAME")
	private String bankName;

	@Column(name = "bank_act_no")
	private String bankActNo;

	@Column(name = "SEGMENT")
	private String segment;

	@Column(name = "UPI_ID")
	private String upiId;

	@Column(name = "PAY_METHOD")
	private String payMethod;

	@Column(name = "AMOUNT")
	private String amount;

	@Column(name = "order_id")
	private String orderId;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "PAYMENT_STATUS")
	private String paymentStatus;
	
	@Column(name = "PAYMENT")
	private String payment;

	@Column(name = "receipt")
	private String receipt;

	@Column(name = "IFSC_CODE")
	private String ifscCode;

	@Column(name = "CLIENT_NAME")
	private String clientName;

	@Column(name = "DEVICE")
	private String device;

	@Column(name = "PAYMENT_REASON")
	private String paymentReason;

	@Column(name = "BANK_CODE")
	private String bankCode;

	@Column(name = "BACKOFFICE_CODE")
	private String backofficeCode;

}
