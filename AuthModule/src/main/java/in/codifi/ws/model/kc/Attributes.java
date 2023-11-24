package in.codifi.ws.model.kc;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attributes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("ucc")
	private List<String> ucc;
	@JsonProperty("pan")
	private List<String> pan;
	@JsonProperty("mobile")
	private List<String> mobile;
}
