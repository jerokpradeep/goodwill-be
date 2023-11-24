package in.codifi.funds.entity.primary;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_PAYMENT_REF")
@Getter
@Setter
public class PaymentRefEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "USER_ID")
	private String userId;
	@Column(name = "ORDER_ID")
	private String orderId;
	@Column(name = "RECEIPT_ID")
	private String receiptId;
	@Column(name = "BANK_NAME")
	private String bankName;
	@Column(name = "ACC_NUM")
	private String accNum;
	@Column(name = "EXCH_SEG")
	private String exchSeg;
	@Column(name = "PAYMENT_METHOD")
	private String paymentMethod;
	@Column(name = "UPI_ID")
	private String upiId;
	@Column(name = "AMOUNT")
	private double amount;
	@Column(name = "REQUEST")
	private String request;
	@Column(name = "PAYMENT_STATUS")
	private String paymentStatus;
	@Column(name = "BO_UPDATE")
	private int boUpdate;
	@Column(name = "VOUCHER_NO")
	private String voucherNo;

}
