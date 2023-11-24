package in.codifi.admin.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

import in.codifi.admin.entity.PositionAvgPriceEntity;
import io.quarkus.logging.Log;

@ApplicationScoped
public class PositionsDao {

	@Inject
	DataSource dataSource;

	/**
	 * Method to insert position file
	 *
	 * @author SOWMIYA
	 *
	 * @param list
	 * @return
	 */
	public boolean insertPositionFile(List<PositionAvgPriceEntity> list) {

		PreparedStatement pStmt = null;
		Connection conn = null;
		int count = 1;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					"INSERT INTO tbl_position_avg_price_latest(client_id, exchange,token, instrument_type, symbol, expiry,"
							+ " strike_price, option_type, instrument_name, net_qty, net_rate) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			for (PositionAvgPriceEntity values : list) {
				int paramPos = 1;
				pStmt.setString(paramPos++, values.getClientId());
				pStmt.setString(paramPos++, values.getExchange());
				pStmt.setString(paramPos++, values.getToken());
				pStmt.setString(paramPos++, values.getInstrumentType());
				pStmt.setString(paramPos++, values.getSymbol());
				pStmt.setString(paramPos++, values.getExpiry());
				pStmt.setString(paramPos++, values.getStrikePrice());
				pStmt.setString(paramPos++, values.getOptionType());
				pStmt.setString(paramPos++, values.getInstrumentName());
				pStmt.setString(paramPos++, values.getNetQty());
				pStmt.setString(paramPos++, values.getNetRate());
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
			Log.info("Position file inserted");
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
	 * Method to delete position archive
	 *
	 * @author SOWMIYA
	 *
	 */
	public boolean deletePositionArchive() {

		PreparedStatement pStmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement("TRUNCATE TABLE tbl_position_avg_price_archive");
			pStmt.executeUpdate();
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
	 * Method to move position file
	 *
	 * @author SOWMIYA
	 *
	 * @return
	 */
	public boolean moveAvgPrice() {

		PreparedStatement pStmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement("RENAME TABLE tbl_position_avg_price_archive TO tbl_position_avg_price_temp,"
					+ "tbl_position_avg_price TO tbl_position_avg_price_archive,"
					+ "tbl_position_avg_price_latest TO tbl_position_avg_price,"
					+ "tbl_position_avg_price_temp TO tbl_position_avg_price_latest");
			pStmt.executeUpdate();
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
	 * Method to get Position Avg Price Data Count
	 * 
	 * @author Gowrisankar
	 */
	public List<String> getPositionAvgPriceCount() {
		List<String> positionAvgPriceCount = new ArrayList<String>();
		Connection conn1 = null;
		PreparedStatement pStmt = null;
		ResultSet rSet1 = null;
		try {
			conn1 = dataSource.getConnection();
			pStmt = conn1.prepareStatement("SELECT exchange, count(*) FROM tbl_position_avg_price group by exchange");
			rSet1 = pStmt.executeQuery();
			if (rSet1 != null) {
				while (rSet1.next()) {
					positionAvgPriceCount.add(rSet1.getString(1) + "-" + rSet1.getString(2));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pStmt.close();
				rSet1.close();
				conn1.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return positionAvgPriceCount;
	}
}
