package in.codifi.orders.entity.primary;

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
@Entity(name = "TBL_HOLDINGS_DATA")
public class HoldingsEntity implements Serializable {

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
	@Column(name = "HOLDINGS_TYPE")
	private String holdingsType;
	@Column(name = "ISIN")
	private String isin;
	@Column(name = "QTY")
	private int qty;
	@Column(name = "COLLATERAL_QTY")
	private int collateralQty;
	@Column(name = "HAIRCUT")
	private String haircut;
	@Column(name = "BROKER_COLL_QTY")
	private int brokerCollQty;
	@Column(name = "DP_QTY")
	private int dpQty;
	@Column(name = "BEN_QTY")
	private int benQty;
	@Column(name = "UNPLEDGE_QTY")
	private int unpledgeQy;
	@Column(name = "CLOSE_PRICE")
	private double closePrice;
	@Column(name = "ACTUAL_PRICE")
	private double actualPrice;
	@Column(name = "PRODUCT")
	private String product;
	@Column(name = "POA_Status")
	private String poaStatus;
	@Column(name = "AUTH_FLAG")
	private int authFlag;
	@Column(name = "AUTH_QTY")
	private int authQty;
	@Column(name = "REQ_ID")
	private String reqId;
	@Column(name = "TXN_ID")
	private String txnId;

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
}
