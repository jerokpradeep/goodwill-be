package in.codifi.admin.model.response;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ExchangeResponseModel {

	private String exch;
	private String exchange_segment;
	private String group_name;
	private String symbol;
	private String token;
	private String instrument_type;
	private String option_type;
	private String strike_price;
	private String formatted_ins_name;
	private String trading_symbol;
	private String company_name;
	private String expiry_date;
	private String lot_size;
	private String tick_size;
	private String pdc;
	private String alter_token;
	private String freeze_qty;
	private String isin;
	private String week_tag;
	private int sort_order_1;
	private int sort_order_2;
	private int sort_order_3;
	private String instrument_name;
}
