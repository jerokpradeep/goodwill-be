package in.codifi.client.entity.primary;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_USER_PREFERENCES")
public class PreferenceEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "TAG")
	private String tag;

	@Column(name = "VALUE")
	private String value;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "SOURCE")
	private String source;

}
