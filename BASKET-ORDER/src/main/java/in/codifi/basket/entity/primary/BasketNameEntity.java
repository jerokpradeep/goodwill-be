package in.codifi.basket.entity.primary;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name = "TBL_BASKET_ORDER")
public class BasketNameEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "BASKET_ID")
	private long basketId;

	@Column(name = "BASKET_NAME")
	private String basketName;

	@Column(name = "IS_EXECUTED")
	private String isExecuted = "0";

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "BASKET_ID", referencedColumnName = "BASKET_ID")
	@OrderBy("id")
	private List<BasketScripEntity> basketScrip;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getBasketId() {
		return basketId;
	}

	public void setBasketId(long basketId) {
		this.basketId = basketId;
	}

	public String getBasketName() {
		return basketName;
	}

	public void setBasketName(String basketName) {
		this.basketName = basketName;
	}

	public String getIsExecuted() {
		return isExecuted;
	}

	public void setIsExecuted(String isExecuted) {
		this.isExecuted = isExecuted;
	}

	public List<BasketScripEntity> getBasketScrip() {
		return basketScrip;
	}

	public void setBasketScrip(List<BasketScripEntity> basketScrip) {
		this.basketScrip = basketScrip;
	}

}
