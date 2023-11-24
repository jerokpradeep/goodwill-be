package in.codifi.ws.model.kb.login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Mws implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("NewGroup")
	private List<NewGroup> newGroup = new ArrayList<>();

	public List<NewGroup> getNewGroup() {
		return newGroup;
	}

	public void setNewGroup(List<NewGroup> newGroup) {
		this.newGroup = newGroup;
	}

}
