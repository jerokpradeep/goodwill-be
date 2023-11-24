package in.codifi.scrips.entity.primary;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Entity(name = "tbl_global_returnofscrip")
public class StockReturnEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "ID")
	private long id;

	@Column(name = "EXCH")
	private String exch;

	@Column(name = "SYMBOL")
	private String symbol;

	@Column(name = "TOKEN")
	private String token;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_OF_CLOSE")
	private Date dateOfClose;

	@Column(name = "CLOSE")
	private String close;

	@Column(name = "PREV_DAY_CLOSE")
	private String prevDayClose;

	@Column(name = "CHANGE_PERC")
	private String changePerc;

	@Column(name = "TAG_OF_PERIOD")
	private String tagOfPeriod;

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
}
