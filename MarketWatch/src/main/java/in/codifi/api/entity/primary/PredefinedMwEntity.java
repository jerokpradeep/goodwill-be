package in.codifi.api.entity.primary;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_PRE_DEFINED_MARKET_WATCH")
public class PredefinedMwEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "MW_ID")
	private int mwId;

	@Column(name = "MW_NAME")
	private String mwName;

	@Column(name = "POSITION")
	private Long position;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "MW_ID", referencedColumnName = "MW_ID")
	private List<PredefinedMwScripsEntity> scrips;

}
