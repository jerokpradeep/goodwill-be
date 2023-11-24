package in.codifi.client.entity.primary;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Admin
 *
 */
@Getter
@Setter
@Entity(name = "TBL_PIN_START_BAR_MAPPING")
public class PinStartBarMappingEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "SYMBOL")
	private String symbol;
	@Column(name = "EXCHANGE")
	private String exchange;
	@Column(name = "SEGMENT")
	private String segment;
	@Column(name = "TOKEN")
	private String token;
	@Column(name = "SORT_ORDER")
	private int sortOrder;

}
