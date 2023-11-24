package in.codifi.basket.ws.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpanMarginRestReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String actid;
	private List<SpanMarginPos> pos;

}
