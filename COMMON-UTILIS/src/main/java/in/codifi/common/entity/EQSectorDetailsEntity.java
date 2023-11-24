package in.codifi.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_EQSECTOR_DETAILS")
@Getter
@Setter
public class EQSectorDetailsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "ID")
	private long id;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "SECTOR_ID")
	private int sectorId;

	@Column(name = "SCRIP_NAME")
	private String scripName;

	@Column(name = "SORT_ORDER")
	private int sortOrder;

	@Column(name = "EXCHANGE")
	private String exchange;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "SEGMENT")
	private String segment;

	@Temporal(TemporalType.DATE)
	@Column(name = "EXPIRY")
	private Date expiry;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "PDC")
	private String pdc;

	@Column(name = "SYMBOL")
	private String symbol;

}
