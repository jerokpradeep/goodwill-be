package in.codifi.admin.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import in.codifi.admin.model.request.MarketWatchReqModel;
import in.codifi.admin.model.response.MarketWatchResponseModel;

@ApplicationScoped
public class MarketWatchDAO {
	@Named("mw")
	@Inject
	DataSource datasource;

	/**
	 * Method to get market watch data by users
	 * 
	 * @author LOKESH
	 * @param requestContext
	 * @return
	 */
	public List<MarketWatchResponseModel> getMarketWatchdata(MarketWatchReqModel mwReqModel) {
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		List<MarketWatchResponseModel> jsonList = new ArrayList<MarketWatchResponseModel>();
		try {
			conn = datasource.getConnection();
			int paramPos = 1;
			pStmt = conn.prepareStatement("SELECT id, user_id, mw_id, token, alter_token, exch, "
					+ "exch_seg, trading_symbol, formatted_ins_name, group_name, instrument_type, expiry_date, lot_size, "
					+ "option_type, pdc, sorting_order, strike_price, symbol, tick_size FROM tbl_market_watch where user_id= ?");
			pStmt.setString(paramPos++, mwReqModel.getUserId());
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					MarketWatchResponseModel result = new MarketWatchResponseModel();
					result.setId(rSet.getInt("id"));
					result.setUserId(rSet.getString("user_id"));
					result.setMwId(rSet.getString("mw_id"));
					result.setToken(rSet.getString("token"));
					result.setAlterToken(rSet.getString("alter_token"));
					result.setExch(rSet.getString("exch"));
					result.setExchSeg(rSet.getString("exch_seg"));
					result.setTradingSymbol(rSet.getString("trading_symbol"));
					result.setFormattedInsName(rSet.getString("formatted_ins_name"));
					result.setGroupName(rSet.getString("group_name"));
					result.setInstrumentType(rSet.getString("instrument_type"));
					result.setExpiryDate(rSet.getString("expiry_date"));
					result.setLotSize(rSet.getString("lot_size"));
					result.setOptionType(rSet.getString("option_type"));
					result.setPdc(rSet.getString("pdc"));
					result.setSortingOrder(rSet.getString("sorting_order"));
					result.setStrikePrice(rSet.getString("strike_price"));
					result.setSymbol(rSet.getString("symbol"));
					result.setTickSize(rSet.getString("tick_size"));

					jsonList.add(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rSet != null) {
					rSet.close();
				}
				if (pStmt != null) {
					pStmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonList;
	}
}
