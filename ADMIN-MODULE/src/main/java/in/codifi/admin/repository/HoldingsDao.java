package in.codifi.admin.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

import in.codifi.admin.entity.HoldingsEntity;
import io.quarkus.logging.Log;

@ApplicationScoped
public class HoldingsDao {

	@Inject
	DataSource dataSource;

	/**
	 * 
	 * Method to insert DP Holdings
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param entity
	 * @return
	 */
	public boolean inserDPHoldingsData(List<HoldingsEntity> entity) {

		/** Delete old data **/
		deleteDPHoldingsArchiveTable();

		PreparedStatement pStmt = null;
		Connection conn = null;
		int count = 1;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					"INSERT INTO tbl_holdings_dp_latest(user_id,holdings_type,isin,close_price,actual_price,qty,collateral_qty,broker_coll_qty,"
							+ "dp_qty,ben_qty,unpledge_qty,product,haircut,poa_status,auth_flag,auth_qty) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for (HoldingsEntity dto : entity) {
				int paramPos = 1;
				pStmt.setString(paramPos++, dto.getUserId());
				pStmt.setString(paramPos++, dto.getHoldingsType());
				pStmt.setString(paramPos++, dto.getIsin());
				pStmt.setDouble(paramPos++, dto.getClosePrice());
				pStmt.setDouble(paramPos++, dto.getActualPrice());
				pStmt.setInt(paramPos++, dto.getQty());
				pStmt.setInt(paramPos++, dto.getCollateralQty());
				pStmt.setInt(paramPos++, dto.getBrokerCollQty());
				pStmt.setInt(paramPos++, dto.getDpQty());
				pStmt.setInt(paramPos++, dto.getBenQty());
				pStmt.setInt(paramPos++, dto.getUnpledgeQy());
				pStmt.setString(paramPos++, dto.getProduct());
				pStmt.setString(paramPos++, dto.getHaircut());
				pStmt.setString(paramPos++, dto.getPoaStatus());
				pStmt.setInt(paramPos++, dto.getAuthFlag());
				pStmt.setInt(paramPos++, dto.getAuthQty());
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
			Log.info("DB holdings file inserted");
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
	 * 
	 * Method to insert T1 holdings data
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param entity
	 * @return
	 */
	public boolean inserT1HoldingsData(List<HoldingsEntity> entity) {

		/** Delete old data **/
		deleteT1HoldingsTable();

		PreparedStatement pStmt = null;
		Connection conn = null;
		int count = 1;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					"INSERT INTO tbl_holdings_t1_latest(user_id,holdings_type,isin,close_price,actual_price,qty,collateral_qty,product,haircut,"
							+ "poa_status,auth_flag,auth_qty) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
			for (HoldingsEntity dto : entity) {
				int paramPos = 1;
				pStmt.setString(paramPos++, dto.getUserId());
				pStmt.setString(paramPos++, dto.getHoldingsType());
				pStmt.setString(paramPos++, dto.getIsin());
				pStmt.setDouble(paramPos++, dto.getClosePrice());
				pStmt.setDouble(paramPos++, dto.getActualPrice());
				pStmt.setInt(paramPos++, dto.getQty());
				pStmt.setInt(paramPos++, dto.getCollateralQty());
				pStmt.setString(paramPos++, dto.getProduct());
				pStmt.setString(paramPos++, dto.getHaircut());
				pStmt.setString(paramPos++, dto.getPoaStatus());
				pStmt.setInt(paramPos++, dto.getAuthFlag());
				pStmt.setInt(paramPos++, dto.getAuthQty());
				count++;
				pStmt.addBatch();

				if (count == 10000) {
					count = 1;
					pStmt.executeBatch();
				}
			}
			if (count > 1) {
				pStmt.executeBatch();
			}
			Log.info("T1 holdings file inserted");
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

	public boolean deleteDPHoldingsArchiveTable() {

		PreparedStatement pStmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement("TRUNCATE TABLE tbl_holdings_dp_latest");
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

	public boolean deleteT1HoldingsTable() {

		PreparedStatement pStmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement("TRUNCATE TABLE tbl_holdings_t1_latest");
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
	 * 
	 * Method to truncate holding data from archive
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public boolean deleteHoldingsArchiveTable() {

		PreparedStatement pStmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement("TRUNCATE TABLE tbl_holdings_archive");
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
	 * 
	 * Method to insert latest holding data by getting data from T1 latest
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public boolean inserT1HoldingsDataIntoLatest() {

		PreparedStatement pStmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					"INSERT INTO tbl_holdings_latest(user_id,holdings_type,isin,close_price,actual_price,qty,collateral_qty,"
							+ "product,haircut,poa_status,auth_flag,auth_qty) select user_id,holdings_type,isin,close_price,actual_price,qty,collateral_qty,"
							+ "product,haircut,poa_status,auth_flag,auth_qty from tbl_holdings_t1_latest");

			pStmt.execute();
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
	 * 
	 * Method to insert latest holding data by getting data from DP latest
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public boolean inserDPHoldingsDataIntoLatest() {

		PreparedStatement pStmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					"INSERT INTO tbl_holdings_latest(user_id,holdings_type,isin,close_price,actual_price,qty,collateral_qty,"
							+ "product,haircut,poa_status,auth_flag,auth_qty) select user_id,holdings_type,isin,close_price,actual_price,qty,collateral_qty,"
							+ "product,haircut,poa_status,auth_flag,auth_qty from tbl_holdings_dp_latest");

			pStmt.execute();
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
	 * 
	 * Method to move latest data into holdings table
	 * 
	 * @author Dinesh Kumar
	 *
	 *
	 * @return
	 */
	public boolean moveHodings() {

		PreparedStatement pStmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement("RENAME TABLE tbl_holdings_archive TO tbl_holdings_temp, "
					+ "tbl_holdings_data TO tbl_holdings_archive, tbl_holdings_latest TO tbl_holdings_data,"
					+ "tbl_holdings_temp TO tbl_holdings_latest");
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
}
