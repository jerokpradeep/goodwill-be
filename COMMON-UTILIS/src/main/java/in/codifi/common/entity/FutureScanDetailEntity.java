package in.codifi.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_FUTURE_SCAN_DETAILS")
@Getter
@Setter
public class FutureScanDetailEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "SCAN_ID")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private int scanId;

	@Column(name = "SCRIP_NAME")
	private String scripName;

	@Column(name = "EXCHANGE")
	private String exchange;

	@Column(name = "SEGMENT")
	private String segment;

	@Column(name = "TOKEN")
	private String token;

}
