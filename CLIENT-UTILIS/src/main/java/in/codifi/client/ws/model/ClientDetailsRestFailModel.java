package in.codifi.client.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDetailsRestFailModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("stat")
	private String stat;

	@JsonProperty("emsg")
	private String emsg;

	@JsonProperty("request_time")
	private String request_time;

}
