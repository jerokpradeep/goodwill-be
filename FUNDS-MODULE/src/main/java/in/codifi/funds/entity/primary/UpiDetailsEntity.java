package in.codifi.funds.entity.primary;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_UPI_DETAILS")
@Getter
@Setter
public class UpiDetailsEntity extends CommonEntity {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "USER_ID")
	private String userId;
	@Column(name = "UPI_ID")
	private String upiId;

}
