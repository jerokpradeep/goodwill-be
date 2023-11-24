package in.codifi.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_DASHBOARD_PREFERENCES")
public class DashboardPreferencesEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "TAG")
	private String tag;

	@Column(name = "KEY_VARIABLE")
	private String keyVariable;

	@Column(name = "VALUE")
	private String value;

	@Column(name = "SOURCE")
	private String source;

	@Column(name = "SORT_ORDER")
	private String sortOrder;
}
