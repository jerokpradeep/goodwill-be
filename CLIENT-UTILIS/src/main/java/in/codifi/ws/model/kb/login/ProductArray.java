package in.codifi.ws.model.kb.login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductArray implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("prd")
	private String prd;
	@JsonProperty("s_prdt_ali")
	private String sPrdtAli;
	@JsonProperty("exch")
	private List<String> exch = new ArrayList<>();

}
