package in.codifi.api.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPerferencePreDefModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int screeners;
	private int event;
	private int research;
	private int mtfMargin;

}
