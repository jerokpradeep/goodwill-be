package in.codifi.basket.ws.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpanMarginPos {

	private String prd;
	private String exch;
	private String instname;
	private String symname;
	private String exd;
	private String optt;
	private String strprc;
	private String buyqty;
	private String sellqty;
	private String netqty;

//	private String prd;
//	private String exch;
//	private String exd;
//	private String strprc;
//	private String instname;
//	private String ls;
//	private String optt;
//	private String symname;
//	private String token;
//	private String tsym;
//	private String ttype;
//	private String netqty;
}
