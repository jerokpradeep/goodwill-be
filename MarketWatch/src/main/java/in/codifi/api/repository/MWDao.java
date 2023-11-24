package in.codifi.api.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.entity.primary.MarketWatchNameDTO;
import in.codifi.api.entity.primary.MarketWatchScripDetailsDTO;
import in.codifi.api.model.CacheMwAdvDetailsModel;
import in.codifi.api.model.ResponseModel;
import in.codifi.api.util.AppConstants;
import in.codifi.api.util.PrepareResponse;
import io.quarkus.logging.Log;

@ApplicationScoped
public class MWDao {

	@Inject
	DataSource dataSource;

	@Inject
	PrepareResponse prepareResponse;

	/**
	 * Method to get MarketWatch By UserId
	 * 
	 * @author Dinesh Kumar
	 * @param userId
	 * @return
	 */
	public List<CacheMwAdvDetailsModel> getMarketWatchByUserId(String userId) {
		List<CacheMwAdvDetailsModel> response = new ArrayList<>();

		PreparedStatement pStmt = null;
		Connection conn = null;
		ResultSet rSet = null;
		try {
			conn = dataSource.getConnection();
			String query = "SELECT A.mw_name, A.user_id,(case when A.mw_id is null then 0 else A.mw_id end) as mw_id,"
					+ " B.exch, B.exch_seg, B.token, B.symbol, B.trading_symbol, B.formatted_ins_name, B.expiry_date, B.pdc,"
					+ " (case when B.sorting_order is null then 0 else B.sorting_order end) as sorting_order"
					+ " FROM tbl_market_watch_name as A  LEFT JOIN tbl_market_watch B on  A.mw_id = B.mw_id and"
					+ " A.user_id = B.user_id where A.user_id = ? order by A.user_id, A.mw_id , B.sorting_order";
			pStmt = conn.prepareStatement(query);
			int paramPos = 1;
			pStmt.setString(paramPos++, userId);
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					CacheMwAdvDetailsModel model = new CacheMwAdvDetailsModel();
					model.setMwName(rSet.getString("mw_name"));
					model.setUserId(rSet.getString("user_id"));
					model.setMwId(rSet.getInt("mw_id"));
					model.setExchange(rSet.getString("exch"));
					model.setSegment(rSet.getString("exch_seg"));
					model.setToken(rSet.getString("token"));
					model.setSymbol(rSet.getString("symbol"));
					model.setTradingSymbol(rSet.getString("trading_symbol"));
					model.setFormattedInsName(rSet.getString("formatted_ins_name"));
					model.setExpiry(rSet.getDate("expiry_date"));
					model.setPdc(rSet.getString("pdc"));
					model.setSortOrder(rSet.getInt("sorting_order"));
					response.add(model);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} finally {
			try {
				rSet.close();
				pStmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return response;
	}

	/**
	 * Method to find mw name by user id
	 * 
	 * @author Dinesh Kumar
	 * @param userId
	 * @return
	 */
	public List<MarketWatchNameDTO> findAllByUserId(String userId) {
		List<MarketWatchNameDTO> response = new ArrayList<>();
		PreparedStatement pStmt = null;
		Connection conn = null;
		ResultSet rSet = null;
		try {
			conn = dataSource.getConnection();
			String query = "SELECT id, mw_name,user_id,(case when mw_id is null then 0 else mw_id end) as mw_id  FROM tbl_market_watch_name where user_id = ?";
			pStmt = conn.prepareStatement(query);
			int paramPos = 1;
			pStmt.setString(paramPos++, userId);
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					MarketWatchNameDTO model = new MarketWatchNameDTO();
					model.setId(rSet.getLong("id"));
					model.setMwName(rSet.getString("mw_name"));
					model.setUserId(rSet.getString("user_id"));
					model.setMwId(rSet.getInt("mw_id"));
					response.add(model);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} finally {
			try {
				rSet.close();
				pStmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return response;
	}

	/**
	 * Method to update mwname
	 * 
	 * @author Dinesh Kumar
	 * @param mwName
	 * @param mwId
	 * @param userId
	 * @return
	 */
	public int updateMWName(String mwName, int mwId, String userId) {
		int isSuccessfull = 0;
		PreparedStatement pStmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pStmt = conn
					.prepareStatement("UPDATE tbl_market_watch_name SET mw_name = ? WHERE mw_id = ? AND user_id = ?");
			int paramPos = 1;
			pStmt.setString(paramPos++, mwName);
			pStmt.setInt(paramPos++, mwId);
			pStmt.setString(paramPos++, userId);
			pStmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} finally {
			try {
				pStmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isSuccessfull;
	}

	/**
	 * Method to delete expired contract in MW List on DB
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param currentDate
	 * @return
	 */
	public RestResponse<ResponseModel> deleteExpiredContract(String currentDate) {
		PreparedStatement pStmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					"DELETE FROM tbl_market_watch a where a.expiry_date IS NOT NULL and a.expiry_date < ?");
			int paramPos = 1;
			pStmt.setString(paramPos++, currentDate);
			pStmt.execute();
			return prepareResponse.prepareSuccessMessage(AppConstants.RECORD_DELETED);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} finally {
			try {
				pStmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return prepareResponse.prepareFailedResponse(AppConstants.DELETE_FAILED);
	}

	/**
	 * Method to insert MW scrips data
	 * 
	 * @author dinesh kumar
	 * @param mwScripsDto
	 * @return
	 */
	public boolean insertMwData(List<MarketWatchScripDetailsDTO> mwScripsDto) {
		PreparedStatement pStmt = null;
		Connection conn = null;
		int count = 1;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement("INSERT INTO tbl_market_watch (user_id,mw_id,token,alter_token,exch,"
					+ "exch_seg,trading_symbol,formatted_ins_name,group_name,instrument_type,expiry_date,lot_size,"
					+ "option_type,pdc,sorting_order,strike_price,symbol,tick_size) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for (MarketWatchScripDetailsDTO dto : mwScripsDto) {
				int paramPos = 1;
				pStmt.setString(paramPos++, dto.getUserId());
				pStmt.setInt(paramPos++, dto.getMwId());
				pStmt.setString(paramPos++, dto.getToken());
				pStmt.setString(paramPos++, dto.getAlterToken());
				pStmt.setString(paramPos++, dto.getEx());
				pStmt.setString(paramPos++, dto.getExSeg());
				pStmt.setString(paramPos++, dto.getTradingSymbol());
				pStmt.setString(paramPos++, dto.getFormattedName());
				pStmt.setString(paramPos++, dto.getGroupName());
				pStmt.setString(paramPos++, dto.getInstrumentType());
				if (dto.getExpDt() != null) {
					java.sql.Date sqldate = (java.sql.Date) dto.getExpDt();
					pStmt.setDate(paramPos++, sqldate);
				} else {
					pStmt.setDate(paramPos++, null);
				}
				pStmt.setString(paramPos++, dto.getLotSize());
				pStmt.setString(paramPos++, dto.getOptionType());
				pStmt.setString(paramPos++, dto.getPdc());
				pStmt.setInt(paramPos++, dto.getSortingOrder());
				pStmt.setString(paramPos++, dto.getStrikePrice());
				pStmt.setString(paramPos++, dto.getSymbol());
				pStmt.setString(paramPos++, dto.getTickSize());
				count++;
				pStmt.addBatch();
				if (count == 1000) {
					pStmt.executeBatch();
					count = 1;
				}
			}
			if (count > 1) {
				pStmt.executeBatch();
			}
			Log.info("MW scrips inserted");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} finally {
			try {
				pStmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				Log.error(e.getMessage());
			}
		}

		return false;
	}

	/**
	 * Method to insert Mw Name
	 * 
	 * @author Dinesh Kumar
	 * @param mwNameDto
	 * @return
	 */
	public boolean insertMwName(List<MarketWatchNameDTO> mwNameDto) {
		PreparedStatement pStmt = null;
		Connection conn = null;
		int count = 1;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					"INSERT INTO tbl_market_watch_name (user_id,mw_id,mw_name,position) VALUES (?,?,?,?)");
			for (MarketWatchNameDTO dto : mwNameDto) {
				int paramPos = 1;
				pStmt.setString(paramPos++, dto.getUserId());
				pStmt.setInt(paramPos++, dto.getMwId());
				pStmt.setString(paramPos++, dto.getMwName());
				pStmt.setLong(paramPos++, dto.getPosition());
				count++;
				pStmt.addBatch();
				if (count == 1000) {
					pStmt.executeBatch();
					count = 1;
				}
			}
			if (count > 1) {
				pStmt.executeBatch();
			}
			Log.info("MW Name inserted");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} finally {
			try {
				pStmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				Log.error(e.getMessage());
			}
		}

		return false;
	}

	/**
	 * Method to delete scrip from MW
	 * 
	 * @param pUserId
	 * @param exch
	 * @param token
	 * @param mwId
	 * @return
	 */
	public long deleteScripFomDataBase(String pUserId, String exch, String token, int mwId) {
		long resp = 0;
		PreparedStatement pStmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					"DELETE FROM tbl_market_watch  WHERE mw_id = ? and user_id = ? and token = ? and exch = ? ");
			int paramPos = 1;
			pStmt.setInt(paramPos++, mwId);
			pStmt.setString(paramPos++, pUserId);
			pStmt.setString(paramPos++, token);
			pStmt.setString(paramPos++, exch);
			pStmt.execute();
			resp = 1;
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} finally {
			try {
				pStmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resp;
	}

	/**
	 * Method to get mw scrip details by userid and mwid
	 * 
	 * @author Dinesh Kumar
	 * @param userId
	 * @param mwId
	 * @return
	 */
	public List<MarketWatchScripDetailsDTO> findAllByUserIdAndMwId(String userId, int mwId) {
		List<MarketWatchScripDetailsDTO> response = new ArrayList<>();

		PreparedStatement pStmt = null;
		Connection conn = null;
		ResultSet rSet = null;
		try {
			conn = dataSource.getConnection();
			String query = "SELECT token,exch,(case when sorting_order is null then 0 else sorting_order end) as sorting_order,id from tbl_market_watch where user_id = ? and mw_id = ?";
			pStmt = conn.prepareStatement(query);
			int paramPos = 1;
			pStmt.setString(paramPos++, userId);
			pStmt.setInt(paramPos++, mwId);
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					MarketWatchScripDetailsDTO model = new MarketWatchScripDetailsDTO();
					model.setToken(rSet.getString("token"));
					model.setEx(rSet.getString("exch"));
					model.setSortingOrder(rSet.getInt("sorting_order"));
					model.setId(rSet.getLong("id"));
					response.add(model);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} finally {
			try {
				rSet.close();
				pStmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return response;
	}

	/**
	 * Method to update mw scrips
	 * 
	 * @author Dinesh Kumar
	 * @param scripsDto
	 * @param userId
	 * @param mwId
	 * @return
	 */
	public int updateMWScrips(List<MarketWatchScripDetailsDTO> scripsDto, String userId, int mwId) {
		int isSuccessfull = 0;
		PreparedStatement pStmt = null;
		Connection conn = null;
		int count = 1;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					"UPDATE tbl_market_watch SET sorting_order = ? where mw_id = ? and user_id = ? and id = ?");
			for (MarketWatchScripDetailsDTO dto : scripsDto) {
				int paramPos = 1;
				pStmt.setInt(paramPos++, dto.getSortingOrder());
				pStmt.setInt(paramPos++, dto.getMwId());
				pStmt.setString(paramPos++, dto.getUserId());
				pStmt.setLong(paramPos++, dto.getId());
				count++;
				pStmt.addBatch();
				if (count == 1000) {
					pStmt.executeBatch();
					count = 1;
				}
			}
			if (count > 1) {
				pStmt.executeBatch();
			}
			isSuccessfull = 1;
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} finally {
			try {
				pStmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isSuccessfull;
	}

}
