package in.codifi.admin.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_VENDOR_APP")
public class VendorAppEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "app_name")
	private String appName;

	@Column(name = "api_key")
	private String apiKey;

	@Column(name = "api_secret")
	private String apiSecret;

	@Column(name = "client_id")
	private String client_id;

	@Column(name = "contact_name")
	private String contact_name;

	@Column(name = "mobile_no")
	private String mobile_no;

	@Column(name = "email")
	private String email;

	@Column(name = "type")
	private String type;

	@Column(name = "icon_url")
	private String icon_url;

	@Column(name = "redirect_url")
	private String redirect_url;

	@Column(name = "postback_url")
	private String postback_url;

	@Column(name = "description")
	private String description;

	@Column(name = "authorization_status")
	private int authorization_status;

	@Column(name = "is_accepted")
	private int is_accepted;

	@Column(name = "rejected_reson")
	private String rejected_reson;

}
