package in.codifi.api.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvancedMWModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private ScreenersModel screeners;
	private boolean research;
	private boolean event;
	private double mtfMargin;

}
