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

import in.codifi.admin.req.model.MobVersionReqModel;
import io.quarkus.logging.Log;

@ApplicationScoped
public class CommonEntityManager {
	@Named("common")
	@Inject
	DataSource datasource;

	public List<MobVersionReqModel> getMobVersion() {
		List<MobVersionReqModel> response = null;
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		MobVersionReqModel MobVer = null;
		try {
			conn = datasource.getConnection();
			pStmt = conn.prepareStatement(
					"SELECT id, version, type, os, update_available, created_on FROM tbl_app_version ");
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				response = new ArrayList<MobVersionReqModel>();
				while (rSet.next()) {
					MobVer = new MobVersionReqModel();
					MobVer.setId(rSet.getInt("id"));
					MobVer.setVersion(rSet.getString("version"));
					MobVer.setType(rSet.getString("type"));
					MobVer.setOs(rSet.getString("os"));
					MobVer.setUpdateAvailable(rSet.getInt("update_available"));
					MobVer.setCreatedOn(rSet.getString("created_on"));
					response.add(MobVer);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return response;
	}

	public boolean addMobVersion(MobVersionReqModel model) {
		Connection conn = null;
		boolean isSuccessful = false;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		try {
			conn = datasource.getConnection();
			pStmt = conn.prepareStatement(
					"INSERT INTO tbl_app_version(version, type, os, update_available) values(?,?,?,?)");
			int paramPos = 1;
			pStmt.setString(paramPos++, model.getVersion());
			pStmt.setString(paramPos++, model.getType());
			pStmt.setString(paramPos++, model.getOs());
			pStmt.setInt(paramPos++, model.getUpdateAvailable());

			isSuccessful = pStmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
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
		return isSuccessful;
	}

	public int updateMobVersion(int updateAvailable, String version) {
		Connection conn = null;
		int isSuccessful = 0;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		try {
			conn = datasource.getConnection();
			pStmt = conn.prepareStatement("UPDATE tbl_app_version SET update_available = ? WHERE version = ? ");
			int paramPos = 1;
			pStmt.setInt(paramPos++, updateAvailable);
			pStmt.setString(paramPos++, version);

			isSuccessful = pStmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
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
		return isSuccessful;
	}

	public long deleteMobVersion(String type, String version) {
		Connection conn = null;
		int isSuccessful = 0;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		try {
			conn = datasource.getConnection();
			pStmt = conn.prepareStatement("DELETE FROM tbl_app_version WHERE version =? and type =? ");
			int paramPos = 1;
			pStmt.setString(paramPos++, version);
			pStmt.setString(paramPos++, type);

			isSuccessful = pStmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
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
		return isSuccessful;
	}

}
