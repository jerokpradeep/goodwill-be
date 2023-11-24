package in.codifi.holdings.model.transformation;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NonPoaHoldingsRespModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String product;
	public List<NonPoaHoldings> holdings;

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class NonPoaHoldings implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public String totalQty;
		public String approvedQty;
		public String avgPrice;
		public String boId;
		public String t1Qty;
		public String isin;
		public String settlementType;
		private List<NonPoaSymbol> symbol;

		@Getter
		@Setter
		public class NonPoaSymbol implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			private String exchange;
			private String token;
			private String tradingSymbol;
			private String pdc;
			private String ltp;
		}
	}
}
