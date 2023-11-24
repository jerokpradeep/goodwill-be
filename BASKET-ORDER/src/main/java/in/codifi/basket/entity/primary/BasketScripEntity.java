package in.codifi.basket.entity.primary;

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
@Entity(name = "TBL_BASKET_ORDER_SCRIP")
public class BasketScripEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "BASKET_ID")
	private long basketId;

	@Column(name = "SORT_ORDER")
	private String sortOrder;

	@Column(name = "EXCHANGE")
	private String exchange;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "TRADING_SYMBOL")
	private String tradingSymbol;

	@Column(name = "QTY")
	private String qty;

	@Column(name = "PRICE")
	private String price;

	@Column(name = "EXPIRY")
	private Date expiry;

	@Column(name = "PRODUCT")
	private String product;

	@Column(name = "TRANS_TYPE")
	private String transType;

	@Column(name = "PRICE_TYPE")
	private String priceType;

	@Column(name = "ORDER_TYPE")
	private String orderType;

	@Column(name = "RET")
	private String ret;

	@Column(name = "TRIGGER_PRICE")
	private String triggerPrice;

	@Column(name = "DIS_CLOSED_QTY")
	private String disClosedQty;

	@Column(name = "MKT_PROTECTION")
	private String mktProtection;

	@Column(name = "TARGET")
	private String target;

	@Column(name = "STOP_LOSS")
	private String stopLoss;

	@Column(name = "TRAILING_STOP_LOSS")
	private String trailingStopLoss;

	@Column(name = "FORMATTED_INS_NAME")
	private String formattedInsName;

	@Column(name = "week_tag")
	private String weekTag;

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
