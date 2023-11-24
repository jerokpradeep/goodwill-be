package in.codifi.common.entity;

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
@Entity(name = "TBL_APP_VERSION")
public class VersionEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "VERSION")
	private String version;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "UPDATE_AVAILABLE")
	private int isUpdateAvailable;

	@Column(name = "OS")
	private String os;
}
