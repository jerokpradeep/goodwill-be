package in.codifi.auth.repository;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.json.simple.JSONObject;

import in.codifi.auth.cache.AccessLogCache;
import in.codifi.auth.config.HazelcastConfig;
import in.codifi.auth.entity.logs.AccessLogModel;
import in.codifi.auth.entity.logs.RestAccessLogModel;
import in.codifi.auth.model.response.UsersLoggedInModel;
import in.codifi.auth.model.response.UsersLoggedInRespModel;
import in.codifi.auth.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class AccessLogManager {
	@Named("logs")
	@Inject
	DataSource dataSource;

	/**
	 * method to insert access log
	 * 
	 * @author SowmiyaThangaraj
	 * @param accLogModel
	 */
	public void insertAccessLog(AccessLogModel accLogModel) {
		Date inTimeDate;
		if (accLogModel.getInTime() != null) {
			inTimeDate = new Date(accLogModel.getInTime().getTime());
		} else {
			inTimeDate = new Date();
		}
		String date = new SimpleDateFormat("ddMMYYYY").format(inTimeDate);
		String hour = new SimpleDateFormat("HH").format(inTimeDate);
		String tableName = "tbl_" + date + "_access_log_" + hour;
		accLogModel.setTableName(tableName);

		List<AccessLogModel> cacheAccessLogModels = new ArrayList<>(AccessLogCache.getInstance().getBatchAccessModel());
		if (cacheAccessLogModels.size() > 0) {
			if (cacheAccessLogModels.get(0).getTableName().equalsIgnoreCase(tableName)) {
				AccessLogCache.getInstance().getBatchAccessModel().add(accLogModel);
			} else {
				AccessLogCache.getInstance().getBatchAccessModel().clear();
				AccessLogCache.getInstance().setBatchAccessModel(new ArrayList<>());
				insertBatchAccessLog(cacheAccessLogModels);
				AccessLogCache.getInstance().getBatchAccessModel().add(accLogModel);
			}
		} else {
			AccessLogCache.getInstance().getBatchAccessModel().add(accLogModel);
		}

		if (AccessLogCache.getInstance().getBatchAccessModel().size() >= 25) {
			List<AccessLogModel> accessLogModels = new ArrayList<>(AccessLogCache.getInstance().getBatchAccessModel());
			AccessLogCache.getInstance().getBatchAccessModel().clear();
			AccessLogCache.getInstance().setBatchAccessModel(new ArrayList<>());
			insertBatchAccessLog(accessLogModels);
		}
	}

	/**
	 * Method to insert batch access log
	 * 
	 * @author Dinesh Kumar
	 * @param batchLogs
	 */
	public void insertBatchAccessLog(List<AccessLogModel> batchLogs) {

		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			PreparedStatement statement = null;
			Connection connection = null;

			@Override
			public void run() {
				try {
					connection = dataSource.getConnection();
					if (batchLogs != null && batchLogs.size() > 0) {
						String insertQuery = "INSERT INTO " + batchLogs.get(0).getTableName() + " "
								+ " (user_id, ucc, req_id, source, vendor, in_time, out_time, lag_time,  module, method, req_body,"
								+ " res_body, device_ip, user_agent, domain, content_type, session, uri) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
						statement = connection.prepareStatement(insertQuery);
						for (AccessLogModel accLogModel : batchLogs) {
							int paramPos = 1;
							statement.setString(paramPos++, accLogModel.getUserId());
							statement.setString(paramPos++, accLogModel.getUcc());
							statement.setString(paramPos++, accLogModel.getReqId());
							statement.setString(paramPos++, accLogModel.getSource());
							statement.setString(paramPos++, accLogModel.getVendor());
							statement.setTimestamp(paramPos++, accLogModel.getInTime());
							statement.setTimestamp(paramPos++, accLogModel.getOutTime());
							statement.setLong(paramPos++, accLogModel.getLagTime());
							statement.setString(paramPos++, accLogModel.getModule());
							statement.setString(paramPos++, accLogModel.getMethod());
							statement.setString(paramPos++, accLogModel.getReqBody());
							String respBody = "";
							int maxLength = 8192;
							if (StringUtil.isNotNullOrEmpty(accLogModel.getResBody())
									&& accLogModel.getResBody().length() > maxLength) {
								respBody = accLogModel.getResBody().substring(0, maxLength);
							} else {
								respBody = accLogModel.getResBody();
							}
							statement.setString(paramPos++, respBody);
							statement.setString(paramPos++, accLogModel.getDeviceIp());
							statement.setString(paramPos++, accLogModel.getUserAgent());
							statement.setString(paramPos++, accLogModel.getDomain());
							statement.setString(paramPos++, accLogModel.getContentType());
							statement.setString(paramPos++, accLogModel.getSession());
							statement.setString(paramPos++, accLogModel.getUri());
							statement.addBatch();
						}
						statement.executeBatch();
					}
					statement.close();
					connection.close();
				} catch (Exception e) {
					Log.error("Auth - insertAccessLog -" + e);
				} finally {
					try {
						if (statement != null) {
							statement.close();
						}
						if (connection != null) {
							connection.close();
						}
					} catch (Exception e) {
						Log.error(" Auth - insertAccessLog -" + e);
					}
				}
			}
		});
	}

	/**
	 * Method to insert rest access log
	 * 
	 * @author Dinesh Kumar
	 * @param accLogModel
	 */
	public void insertRestAccessLog(RestAccessLogModel accLogModel) {

		Date inTimeDate = new Date();
		String date = new SimpleDateFormat("ddMMYYYY").format(inTimeDate);
		String tableName = "tbl_" + date + "_rest_access_log";
		AccessLogCache.getInstance().getBatchRestAccessModel().add(accLogModel);
		if (AccessLogCache.getInstance().getBatchRestAccessModel().size() >= 25) {
			List<RestAccessLogModel> accessLogModels = new ArrayList<>(
					AccessLogCache.getInstance().getBatchRestAccessModel());
			AccessLogCache.getInstance().getBatchRestAccessModel().clear();
			AccessLogCache.getInstance().setBatchRestAccessModel(new ArrayList<>());
			insertBatchRestAccessLog(tableName, accessLogModels);
		}
	}

	/**
	 * Method to insert batch Rest Access Log
	 * 
	 * @author Dinesh kumar
	 * @param tableName
	 * @param batchLogs
	 */
	public void insertBatchRestAccessLog(String tableName, List<RestAccessLogModel> logsData) {

		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				List<RestAccessLogModel> batchLogs = new ArrayList<>();
				batchLogs = logsData;
				PreparedStatement statement = null;
				Connection connection = null;
				try {
					connection = dataSource.getConnection();
					if (batchLogs != null && batchLogs.size() > 0) {

						String insertQuery = "INSERT INTO " + tableName
								+ "(user_id, url, in_time, out_time, total_time, module,"
								+ " method, req_body, res_body) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

						statement = connection.prepareStatement(insertQuery);
						for (RestAccessLogModel accLogModel : batchLogs) {
							int paramPos = 1;
							statement.setString(paramPos++, accLogModel.getUserId());
							statement.setString(paramPos++, accLogModel.getUrl());
							statement.setTimestamp(paramPos++, accLogModel.getInTime());
							statement.setTimestamp(paramPos++, accLogModel.getOutTime());
							statement.setString(paramPos++, accLogModel.getTotalTime());
							statement.setString(paramPos++, accLogModel.getModule());
							statement.setString(paramPos++, accLogModel.getMethod());
							statement.setString(paramPos++, accLogModel.getReqBody());
							String respBody = "";
							int maxLength = 8192;
							if (StringUtil.isNotNullOrEmpty(accLogModel.getResBody())
									&& accLogModel.getResBody().length() > maxLength) {
								respBody = accLogModel.getResBody().substring(0, maxLength);
							} else {
								respBody = accLogModel.getResBody();
							}
							statement.setString(paramPos++, respBody);
							statement.addBatch();
						}
						statement.executeBatch();
					} else {
						System.out.println("0");
					}

					statement.close();
					connection.close();
				} catch (Exception e) {
					Log.error("Auth - insertRestAccessLog -" + e);
				} finally {
					try {
						if (statement != null) {
							statement.close();
						}
						if (connection != null) {
							connection.close();
						}
					} catch (Exception e) {
						Log.error("Auth- insertRestAccessLog -" + e);
					}
				}
			}
		});
	}

	/**
	 * method to insert access log
	 * 
	 * @author SowmiyaThangaraj
	 * @param accLogModel
	 */
	public void insert24RestAccessLog(RestAccessLogModel accLogModel) {
		Date inTimeDate;
		if (accLogModel.getInTime() != null) {
			inTimeDate = new Date(accLogModel.getInTime().getTime());
		} else {
			inTimeDate = new Date();
		}
		String date = new SimpleDateFormat("ddMMYYYY").format(inTimeDate);
		String hour = new SimpleDateFormat("HH").format(inTimeDate);
		String tableName = "tbl_" + date + "_rest_access_log_" + hour;
		accLogModel.setTableName(tableName);

		List<RestAccessLogModel> cacheAccessLogModels = new ArrayList<>(
				AccessLogCache.getInstance().getBatchRestAccessModel());
		if (cacheAccessLogModels.size() > 0) {
			if (cacheAccessLogModels.get(0).getTableName().equalsIgnoreCase(tableName)) {
				AccessLogCache.getInstance().getBatchRestAccessModel().add(accLogModel);
			} else {
				AccessLogCache.getInstance().getBatchRestAccessModel().clear();
				AccessLogCache.getInstance().setBatchRestAccessModel(new ArrayList<>());
				insertBatch24RestAccessLog(cacheAccessLogModels);
				AccessLogCache.getInstance().getBatchRestAccessModel().add(accLogModel);
			}
		} else {
			AccessLogCache.getInstance().getBatchRestAccessModel().add(accLogModel);
		}

		if (AccessLogCache.getInstance().getBatchRestAccessModel().size() >= 0) {
			List<RestAccessLogModel> accessLogModels = new ArrayList<>(
					AccessLogCache.getInstance().getBatchRestAccessModel());
			AccessLogCache.getInstance().getBatchRestAccessModel().clear();
			AccessLogCache.getInstance().setBatchRestAccessModel(new ArrayList<>());
			insertBatch24RestAccessLog(accessLogModels);
		}
	}

	/**
	 * method to insert 24 rest access logs into the table
	 * 
	 * @author SowmiyaThangaraj
	 * @param cacheAccessLogModels
	 */
	private void insertBatch24RestAccessLog(List<RestAccessLogModel> cacheAccessLogModels) {
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			PreparedStatement statement = null;
			Connection connection = null;

			@Override
			public void run() {
				try {
					connection = dataSource.getConnection();
					// Check if the table exists, create it if not
					if (!doesTableExist(connection, cacheAccessLogModels.get(0).getTableName())) {
						createRestTable(connection, cacheAccessLogModels.get(0).getTableName());
					}
					if (cacheAccessLogModels != null && cacheAccessLogModels.size() > 0) {
						String insertQuery = "INSERT INTO " + cacheAccessLogModels.get(0).getTableName()
								+ "(user_id, url, in_time, out_time, total_time, module,"
								+ " method, req_body, res_body) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

						statement = connection.prepareStatement(insertQuery);
						for (RestAccessLogModel accLogModel : cacheAccessLogModels) {
							int paramPos = 1;
							statement.setString(paramPos++, accLogModel.getUserId());
							statement.setString(paramPos++, accLogModel.getUrl());
							statement.setTimestamp(paramPos++, accLogModel.getInTime());
							statement.setTimestamp(paramPos++, accLogModel.getOutTime());
							statement.setString(paramPos++, accLogModel.getTotalTime());
							statement.setString(paramPos++, accLogModel.getModule());
							statement.setString(paramPos++, accLogModel.getMethod());
							statement.setString(paramPos++, accLogModel.getReqBody());
							String respBody = "";
							int maxLength = 8192;
							if (StringUtil.isNotNullOrEmpty(accLogModel.getResBody())
									&& accLogModel.getResBody().length() > maxLength) {
								respBody = accLogModel.getResBody().substring(0, maxLength);
							} else {
								respBody = accLogModel.getResBody();
							}
							statement.setString(paramPos++, respBody);
							statement.addBatch();
						}
						statement.executeBatch();
					}
					statement.close();
					connection.close();
				} catch (Exception e) {
					Log.error("Auth - insertRest24AccessLog -" + e);
				} finally {
					try {
						if (statement != null) {
							statement.close();
						}
						if (connection != null) {
							connection.close();
						}
					} catch (Exception e) {
						Log.error("Auth - insertRest24AccessLog -" + e);
					}
				}
			}

		});
	}

	/**
	 * method to create rest table
	 * 
	 * @author SowmiyaThangaraj
	 * @param connection
	 * @param tableName
	 */
	private void createRestTable(Connection connection, String tableName) throws SQLException {
		// Your table creation SQL statement
		String createTableQuery = "CREATE TABLE " + tableName + " (" + "id INT PRIMARY KEY AUTO_INCREMENT, "
				+ "user_id VARCHAR(20), " + "url VARCHAR(50), " + "in_time TIMESTAMP, " + "out_time TIMESTAMP, "
				+ "total_time VARCHAR(50), " + "module VARCHAR(50), " + "method VARCHAR(50), " + "req_body TEXT, "
				+ "res_body TEXT " + ")";

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(createTableQuery);
		}
	}

	/**
	 * method to check table if exit or not
	 * 
	 * @author SowmiyaThangaraj
	 * @param connection
	 * @param tableName
	 * @return
	 */
	private boolean doesTableExist(Connection connection, String tableName) throws SQLException {
		DatabaseMetaData metadata = connection.getMetaData();
		try (ResultSet resultSet = metadata.getTables(null, null, tableName, null)) {
			return resultSet.next();
		}
	}

//	public void insertAccessLog(AccessLogModel accLogModel) {
//
//		Date inTimeDate;
//		if (accLogModel.getInTime() != null) {
//			inTimeDate = new Date(accLogModel.getInTime().getTime());
//		} else {
//			inTimeDate = new Date();
//		}
//
//		String date = new SimpleDateFormat("ddMMYYYY").format(inTimeDate);
//		String hour = new SimpleDateFormat("HH").format(inTimeDate);
//		String tableName = "tbl_" + date + "_access_log_" + hour;
//
//		try {
//
//			Connection connection = dataSource.getConnection();
//			Statement state = connection.createStatement();
//
//			String insertQuery = "INSERT INTO " + tableName
//					+ "(user_id, ucc, req_id, source, vendor, in_time, out_time, lag_time,  module, method, req_body,"
//					+ " res_body, device_ip, user_agent, domain, content_type, session, uri) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
//			PreparedStatement statement = connection.prepareStatement(insertQuery);
//			statement.setString(1, accLogModel.getUserId());
//			statement.setString(2, accLogModel.getUcc());
//			statement.setString(3, accLogModel.getReqId());
//			statement.setString(4, accLogModel.getSource());
//			statement.setString(5, accLogModel.getVendor());
//			statement.setTimestamp(6, accLogModel.getInTime());
//			statement.setTimestamp(7, accLogModel.getOutTime());
//			statement.setLong(8, accLogModel.getLagTime());
//			statement.setString(9, accLogModel.getModule());
//			statement.setString(10, accLogModel.getMethod());
//			statement.setString(11, accLogModel.getReqBody());
//			statement.setString(12, accLogModel.getResBody());
//			statement.setString(13, accLogModel.getDeviceIp());
//			statement.setString(14, accLogModel.getUserAgent());
//			statement.setString(15, accLogModel.getDomain());
//			statement.setString(16, accLogModel.getContentType());
//			statement.setString(17, accLogModel.getSession());
//			statement.setString(18, accLogModel.getUri());
//			statement.executeUpdate();
//
//			statement.close();
//			state.close();
//			connection.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	/**
//	 * 
//	 * Method to insert rest access log
//	 * 
//	 * @author Dinesh Kumar
//	 *
//	 * @param accLogModel
//	 */
//	public void insertRestAccessLog(RestAccessLogModel accLogModel) {
//
//		Date inTimeDate = new Date();
//
//		String date = new SimpleDateFormat("ddMMYYYY").format(inTimeDate);
//		String tableName = "tbl_" + date + "_rest_access_log";
//
//		try {
//
//			Connection connection = dataSource.getConnection();
//			Statement state = connection.createStatement();
//
//			String insertQuery = "INSERT INTO " + tableName + "(user_id, url, in_time, out_time, total_time, module,"
//					+ " method, req_body, res_body) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
//			PreparedStatement statement = connection.prepareStatement(insertQuery);
//			statement.setString(1, accLogModel.getUserId());
//			statement.setString(2, accLogModel.getUrl());
//			statement.setTimestamp(3, accLogModel.getInTime());
//			statement.setTimestamp(4, accLogModel.getOutTime());
//			statement.setString(5, accLogModel.getTotalTime());
//			statement.setString(6, accLogModel.getModule());
//			statement.setString(7, accLogModel.getMethod());
//			statement.setString(8, accLogModel.getReqBody());
//			statement.setString(9, accLogModel.getResBody());
//			statement.executeUpdate();
//
//			statement.close();
//			state.close();
//			connection.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Method to insert user count log
	 * 
	 * @param userId
	 * @param source
	 * @param hazelKeySsoVendor
	 */
	public void insertUserLogginedInDetails(String userId, String source, String hazelKeySsoVendor) {
		UsersLoggedInRespModel loggedModel = new UsersLoggedInRespModel();
		if (source.equalsIgnoreCase("WEB")) {
			loggedModel = HazelcastConfig.getInstance().getWebLoggedInUsers().get(userId);
		}
		if (source.equalsIgnoreCase("MOB")) {
			loggedModel = HazelcastConfig.getInstance().getMobLoggedInUsers().get(userId);
		}
		if (source.equalsIgnoreCase("API")) {
			loggedModel = HazelcastConfig.getInstance().getApiLoggedInUsers().get(userId);
		}
		if (StringUtil.isNotNullOrEmpty(hazelKeySsoVendor) && hazelKeySsoVendor != null) {
			loggedModel = HazelcastConfig.getInstance().getSsoLoggedInUsers().get(hazelKeySsoVendor);
		}
		AccessLogCache.getInstance().getUsersLoggedInModel().add(loggedModel);
		if (AccessLogCache.getInstance().getUsersLoggedInModel().size() >= 1) {
			List<UsersLoggedInRespModel> accessLogModels = new ArrayList<>(
					AccessLogCache.getInstance().getUsersLoggedInModel());
			AccessLogCache.getInstance().getUsersLoggedInModel().clear();
			AccessLogCache.getInstance().setUsersLoggedInModel(new ArrayList<>());
			insertUserLogginedInDetailsIntoDB(accessLogModels);
		}
	}

	/**
	 * Method to insert user count log
	 * 
	 * @param accessLogModels
	 */
	public void insertUserLogginedInDetailsIntoDB(List<UsersLoggedInRespModel> accessLogModels) {
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				List<UsersLoggedInRespModel> logRespModel = new ArrayList<>();
				logRespModel = accessLogModels;
				String tableName = "tbl_user_loggedin_report";
				PreparedStatement statement = null;
				Connection connection = null;

				String insertQuery = "INSERT INTO " + tableName
						+ "(user_id, source, visitors, vendor) VALUES ( ?, ?, ?, ?)";
				try {
					connection = dataSource.getConnection();
					if (logRespModel != null && logRespModel.size() > 0) {
						statement = connection.prepareStatement(insertQuery);
						for (UsersLoggedInRespModel loggedModel : logRespModel) {
							int paramPos = 1;
							statement.setString(paramPos++, loggedModel.getUserId());
							statement.setString(paramPos++, loggedModel.getSource());
							statement.setInt(paramPos++, 1);
							statement.setString(paramPos++, loggedModel.getVendor());
							statement.addBatch();
						}
						statement.executeBatch();

					} else {
						System.out.println("0");
					}
					statement.close();
					connection.close();
				} catch (Exception e) {
					Log.error("Goodwill - Auth - insertUserLogginedInDetails -" + e);
				} finally {
					try {
						if (statement != null) {
							statement.close();
						}
						if (connection != null) {
							connection.close();
						}
					} catch (Exception e) {
						Log.error("Goodwill - Auth - insertUserLogginedInDetails -" + e);
					}
				}
			}
		});
	}

	/**
	 * method to find distinct vendors
	 * 
	 * @author SowmiyaThangaraj
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

	/**
	 * method to get count by source
	 * 
	 * @author SowmiyaThangaraj
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
//			statement.setString(1, source);
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
			resultSet.close();
			statement.close();
			connection.close();
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
	 * method to get count by vendor
	 * 
	 * @author SowmiyaThangaraj
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
}
