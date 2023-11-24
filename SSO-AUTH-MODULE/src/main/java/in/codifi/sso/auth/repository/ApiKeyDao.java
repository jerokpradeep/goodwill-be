package in.codifi.sso.auth.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

import in.codifi.sso.auth.entity.primary.ApiKeyEntity;
import in.codifi.sso.auth.utility.AppConstants;

@ApplicationScoped
public class ApiKeyDao {

	@Inject
	DataSource dataSource;

	public ApiKeyEntity getAPIDetails(String userId) {

		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		ApiKeyEntity resDto = new ApiKeyEntity();
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement("Select * from tbl_api_subscription WHERE user_id = ?");
			int paramPos = 1;
			pStmt.setString(paramPos++, userId);
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					resDto.setApi_key(rSet.getString("api_key"));
					resDto.setUserId(rSet.getString("user_id"));
					resDto.setExpiryDate(rSet.getDate("expiry_date"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rSet.close();
				pStmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resDto;
	}

	/**
	 * Method to create API key
	 * 
	 * @author Dinesh Kumar
	 * @param user_id
	 * @param api_key
	 * @param exp
	 * @return
	 */
	public String activateSubcripstion(String user_id, String api_key, String exp) {
		String isSuccessful = AppConstants.FAILED_STATUS;
		java.sql.Timestamp timestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
		Connection conn = null;
		PreparedStatement pStmt = null;
		int count = 0;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					"INSERT INTO tbl_api_subscription(user_id , api_key , expiry_date , created_on , created_by , updated_on , updated_by , is_active , is_deleted )"
							+ "VALUES(?,?,?,?,?,?,?,?,?)");
			int paramPos = 1;
			pStmt.setString(paramPos++, user_id);
			pStmt.setString(paramPos++, api_key);
			pStmt.setString(paramPos++, exp);
			pStmt.setTimestamp(paramPos++, timestamp);
			pStmt.setString(paramPos++, user_id);
			pStmt.setTimestamp(paramPos++, timestamp);
			pStmt.setString(paramPos++, user_id);
			pStmt.setInt(paramPos++, 1);
			pStmt.setInt(paramPos++, 0);
			count = pStmt.executeUpdate();
			if (count > 0) {
				isSuccessful = AppConstants.SUCCESS_STATUS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pStmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isSuccessful;
	}

	/**
	 * Method to regenerate API key
	 * 
	 * @author Dinesh Kumar
	 * @param userId
	 * @param apiKey
	 * @param exp
	 * @return
	 */
	public String regenerateApiKey(String userId, String apiKey, String exp) {
		Connection conn = null;
		PreparedStatement pStmt = null;
		String isSuccessful = AppConstants.FAILED_STATUS;
		java.sql.Timestamp timestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement("UPDATE tbl_api_subscription set api_key = ?, is_active = ?,"
					+ " updated_on=?, updated_by=?, expiry_date=?  WHERE user_id = ?");
			int paramPos = 1;
			pStmt.setString(paramPos++, apiKey);
			pStmt.setInt(paramPos++, 1);
			pStmt.setTimestamp(paramPos++, timestamp);
			pStmt.setString(paramPos++, userId);
			pStmt.setString(paramPos++, exp);
			pStmt.setString(paramPos++, userId);
			pStmt.executeUpdate();
			isSuccessful = AppConstants.SUCCESS_STATUS;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pStmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isSuccessful;
	}

}
