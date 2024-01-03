package in.codifi.auth.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

import in.codifi.auth.entity.primary.VendorSubcriptionEntity;

@ApplicationScoped
public class VendorSubcriptionDAO {
	@Inject
	DataSource dataSource;

	public List<VendorSubcriptionEntity> findAllByUserIdAndAppIdAndActiveStatus(String userId, long appId,
			int activeStatus) {

		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		List<VendorSubcriptionEntity> resDto = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					"Select * from tbl_vendor_app_subcription WHERE user_id = ? and app_id = ?  and  active_status = ?");
			int paramPos = 1;
			pStmt.setString(paramPos++, userId);
			pStmt.setLong(paramPos++, appId);
			pStmt.setInt(paramPos++, activeStatus);
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					VendorSubcriptionEntity entity = new VendorSubcriptionEntity();
					entity.setId(rSet.getLong("id"));
					entity.setAppId(rSet.getLong("app_id"));
					entity.setAuthorizationStatus(rSet.getInt("authorization_status"));
					entity.setUserId(rSet.getString("user_id"));
					resDto.add(entity);
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
		return resDto;
	}

	/**
	 * Method to authorize the user for vendor
	 * 
	 * @author Gowrisankar
	 * @param appId
	 * @param pUserId
	 * @return
	 */
	public boolean authorizeUser(long appId, String pUserId) {
		boolean isSuccessful = false;
		Connection conn = null;
		PreparedStatement pStmt = null;
		java.sql.Timestamp timestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
		int count = 0;
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					"INSERT INTO tbl_vendor_app_subcription(app_id, user_id, authorization_status, created_on, created_by, "
							+ "updated_on, updated_by, active_status) VALUES(?,?,?,?,?,?,?,?)");
			int paramPos = 1;
			pStmt.setLong(paramPos++, appId);
			pStmt.setString(paramPos++, pUserId);
			pStmt.setInt(paramPos++, 1);
			pStmt.setTimestamp(paramPos++, timestamp);
			pStmt.setString(paramPos++, pUserId);
			pStmt.setTimestamp(paramPos++, timestamp);
			pStmt.setString(paramPos++, pUserId);
			pStmt.setInt(paramPos++, 1);
			count = pStmt.executeUpdate();
			if (count > 0) {
				isSuccessful = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
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
		return isSuccessful;
	}
}
