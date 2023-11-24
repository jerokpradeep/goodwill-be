package in.codifi.common.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_FUTURES_MASTER")
@Getter
@Setter
public class FutureEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "FUTURES_ID")
	private int futuresList;

	@Column(name = "FUTURES_NAME")
	private String futuresName;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "FUTURES_ID", referencedColumnName = "FUTURES_ID")
	@OrderBy("id")

	private List<FutureDetailsEntity> scrips;

}
