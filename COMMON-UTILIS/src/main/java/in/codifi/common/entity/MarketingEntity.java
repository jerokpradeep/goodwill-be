package in.codifi.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_MARKETING")
@Getter
@Setter
public class MarketingEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "SUB_TITLE")
	private String subTitle;

	@Column(name = "IMAGE_URL")
	private String imageUrl;

	@Column(name = "CARD_VALUE")
	private String cardValue;

	@Column(name = "BUTTON_NAME")
	private String buttonName;

	@Column(name = "BUTTON_URL")
	private String buttonUrl;

	@Column(name = "IN_APP")
	private String inApp;

	@Column(name = "CARD_COLOR")
	private String cardColor;

	@Column(name = "BUTTON_COLOR")
	private String buttonColor;

}
