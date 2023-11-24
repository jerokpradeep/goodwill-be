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

import org.jboss.resteasy.reactive.RestResponse;
import org.json.simple.JSONObject;

import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.model.response.UsersLoggedInModel;
import io.quarkus.logging.Log;

@ApplicationScoped
public class AccessLogManager {

	@Named("logs")
	@Inject
	DataSource dataSource;

	/**
	 * method to get count by vendor
	 * 
	 * @author LOKESH
	 * @param vendor
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JSONObject> getCountByVendor(List<String> vendors) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<JSONObject> ssoModel = new ArrayList<>();
		try {
			connection = dataSource.getConnection();

			for (String vendor : vendors) {
				String selectQuery = "select vendor,count(user_id)as userCount from tbl_user_loggedin_report where vendor = ?"
						+ "GROUP BY vendor";
				statement = connection.prepareStatement(selectQuery);
				statement.setString(1, vendor);
				resultSet = statement.executeQuery();
				if (resultSet != null) {
					while (resultSet.next()) {
						JSONObject json = new JSONObject();
						json.put("vendor", resultSet.getString("vendor"));
						json.put("userCount", resultSet.getInt("userCount"));
						ssoModel.add(json);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("getCountBySource", e);

		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				Log.error("getCountBySource -" + e);
			}
		}
		return ssoModel;
	}

	/**
	 * method to get count by source
	 * 
	 * @author LOKESH
	 * @param source
	 * @return
	 */
	public UsersLoggedInModel getCountBySource() {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		UsersLoggedInModel respModel = new UsersLoggedInModel();
		String source = " ('WEB','MOB','API') ";
		try {
			connection = dataSource.getConnection();

			String selectQuery = " select source,count(user_id)as userCount from tbl_user_loggedin_report where source in "
					+ source + " GROUP BY source";
			statement = connection.prepareStatement(selectQuery);
//				statement.setString(1, source);
			resultSet = statement.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					String sourceValue = resultSet.getString("source");
					int userCount = resultSet.getInt("userCount");
					if (sourceValue.equalsIgnoreCase("WEB")) {
						respModel.setWeb(userCount);
					}
					if (sourceValue.equalsIgnoreCase("MOB")) {
						respModel.setMob(userCount);
					}
					if (sourceValue.equalsIgnoreCase("API")) {
						respModel.setApi(userCount);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("getCountBySource", e);

		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				Log.error("getCountBySource -" + e);
			}
		}
		return respModel;
	}

	/**
	 * method to find distinct vendors
	 * 
	 * @author LOKESH
	 * @return
	 */
	public List<String> findDistinctVendors() {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<String> vendorList = new ArrayList<>();
		try {
			connection = dataSource.getConnection();
			String selectQuery = "SELECT DISTINCT vendor FROM tbl_user_loggedin_report WHERE vendor IS NOT NULL";
			statement = connection.prepareStatement(selectQuery);
			resultSet = statement.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					String vendor = resultSet.getString("vendor");
					vendorList.add(vendor);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("getCountBySource", e);

		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				Log.error("getCountBySource -" + e);
			}
		}
		return vendorList;
	}

	@SuppressWarnings("unused")
	public RestResponse<GenericResponse> truncateUserLoggedInDetails() {
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = dataSource.getConnection();
			String truncateQuery = "TRUNCATE TABLE tbl_user_loggedin_report";
			statement = connection.prepareStatement(truncateQuery);
			int rowsAffected = statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("getCountBySource", e);
		}
		return null;
	}

	/**
	 * method to get Total users logged in details
	 * 
	 * @author LOKESH
	 */
	public int getTotalUserLoggedInDetails() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int userCount = 0;
		try {
			connection = dataSource.getConnection();
			String selectQuery = "select count(DISTINCT(user_id)) as userCount from tbl_user_loggedin_report";
			statement = connection.prepareStatement(selectQuery);
			resultSet = statement.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					userCount = resultSet.getInt("userCount");
				}
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("getCountBySource", e);

		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				Log.error("getCountBySource -" + e);
			}
		}
		return userCount;
	}

}
