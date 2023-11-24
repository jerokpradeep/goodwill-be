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

@Entity(name = "TBL_SECTOR_HEATMAP_DETAIL")
@Getter
@Setter
public class SectorHeatMapDetailsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "SECTOR_ID")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private long sectorId;

	@Column(name = "SCRIP_NAME")
	private String scripName;

	@Column(name = "EXCHANGE")
	private String exchange;

	@Column(name = "SEGMENT")
	private String segment;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "SORTING_ORDER")
	private int sortingOrder;

	@Column(name = "TRADING_SYMBOL")
	private String tradingSymbol;

}
