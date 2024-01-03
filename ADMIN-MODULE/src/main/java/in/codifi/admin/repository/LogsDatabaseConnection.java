package in.codifi.admin.repository;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import in.codifi.admin.model.request.AccessLogModel;
import in.codifi.admin.resp.model.AccessLogRespModel;
import in.codifi.admin.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class LogsDatabaseConnection {

	@Named("logs")
	@Inject
	DataSource dataSource;

	/**
	 * method to get specific database total number of table names
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public List<String> getExistingTables() {
		List<String> tableNames = new ArrayList<>();
		try {
			Connection connection = dataSource.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();
			String[] tableTypes = { "TABLE" };
			ResultSet resultSet = metaData.getTables("logs_db", null, "%", tableTypes);
			while (resultSet.next()) {
				String tableName = resultSet.getString("TABLE_NAME");
				tableNames.add(tableName);
			}
			resultSet.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tableNames;
	}

	/**
	 * method to create a table from database
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public void createTables(List<String> tableToCreate) {
		try {
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			String databaseName = "logs_db";
			for (String tableName : tableToCreate) {
				String sql = "CREATE TABLE " + databaseName + "." + tableName
						+ " (id int AUTO_INCREMENT  PRIMARY KEY, user_id VARCHAR(100),"
						+ "ucc VARCHAR(15),uri varchar(150),module varchar(50),method varchar(150),source VARCHAR(10),"
						+ "req_body text,res_body text,device_ip varchar(100),req_id VARCHAR(15),"
						+ "user_agent longtext,domain longtext,content_type varchar(200),session mediumtext,"
						+ "in_time timestamp(3),out_time timestamp(3),lag_time varchar(100),"
						+ "elapsed_time datetime,vendor VARCHAR(50),"
						+ "created_on datetime DEFAULT CURRENT_TIMESTAMP,updated_on datetime DEFAULT CURRENT_TIMESTAMP)";
				statement.executeUpdate(sql);
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}

	}

	public void createRestAccessTables(List<String> tableToCreate) {
		try {
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			String databaseName = "logs_db";
			for (String tableName : tableToCreate) {
				String sql = "CREATE TABLE " + databaseName + "." + tableName
						+ " (id int AUTO_INCREMENT  PRIMARY KEY, user_id VARCHAR(100), module VARCHAR(45), method varchar(45),"
						+ "url varchar(150),req_body text,res_body text,in_time timestamp(3),out_time timestamp(3),total_time timestamp(3),"
						+ " created_on datetime DEFAULT CURRENT_TIMESTAMP,updated_on datetime DEFAULT CURRENT_TIMESTAMP)";
//				System.out.println(tableName);
				statement.executeUpdate(sql);
			}
			statement.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}

	}

	/**
	 * method to get rest access logs from tables
	 * 
	 * @author SOWMIYA
	 * 
	 * @param tableNames
	 * @return
	 */

	public List<AccessLogRespModel> getRestAccessLogs(List<String> tableNames, String userId) {
		List<AccessLogRespModel> accessRespModel = new ArrayList<>();
		try {
			Connection connection = dataSource.getConnection();
			String databaseName = "logs_db";
			for (String tableName : tableNames) {
				System.out.println("logs tableName-- " + databaseName + "." + tableName);
				String sql = "select "
						+ "id , user_id, module, method, url, req_body, res_body, in_time, out_time, total_time, created_on, updated_on from "
						+ databaseName + "." + tableName + " where user_id = ?";
				try (PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setString(1, userId);
					try (ResultSet rSet = statement.executeQuery()) {
						while (rSet.next()) {
							AccessLogRespModel model = new AccessLogRespModel();
							model.setUserId(rSet.getString("user_id"));
							model.setModule(rSet.getString("module"));
							model.setMethod(rSet.getString("method"));
							model.setUrl(rSet.getString("url"));
							model.setReqBody(rSet.getString("req_body"));
							model.setResBody(rSet.getString("res_body"));
							model.setInTime(rSet.getTimestamp("in_time").toString());
							if (rSet.getTimestamp("out_time") != null) {
								model.setOutTime(rSet.getTimestamp("out_time").toString());
							}
							model.setTotalTime(rSet.getTimestamp("total_time"));
							model.setCreatedOn(rSet.getString("created_on"));
							model.setUpdatedOn(rSet.getString("updated_on"));
							accessRespModel.add(model);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accessRespModel;
	}

	/**
	 * method to get rest access logs from tables
	 * 
	 * @author SOWMIYA
	 * 
	 * @param tableNames
	 * @return
	 */

	public List<AccessLogRespModel> getRestAccessLogsFromTables(List<String> tableNames, String userId, String fromDate,
			String toDate, int pageNo, int pageSize) {
		List<AccessLogRespModel> accessRespModel = new ArrayList<>();
		List<AccessLogRespModel> paginatedList = null;
		if (StringUtil.isNotNullOrEmpty(userId)) {
			try {
				Connection connection = dataSource.getConnection();
				String databaseName = "logs_db";

				for (String tableName : tableNames) {
					System.out.println("logs tableName-- " + databaseName + "." + tableName);
					String sql = "select "
							+ "id , user_id, module, method, url, req_body, res_body, in_time, out_time, total_time, created_on, updated_on from "
							+ databaseName + "." + tableName + " where user_id = ? and in_time >= ? and out_time <= ?";
					try (PreparedStatement statement = connection.prepareStatement(sql)) {
						int parampose = 1;
						statement.setString(parampose++, userId);
						statement.setString(parampose++, fromDate);
						statement.setString(parampose++, toDate);
						try (ResultSet rSet = statement.executeQuery()) {
							while (rSet.next()) {
								AccessLogRespModel model = new AccessLogRespModel();
								model.setId(rSet.getInt("id"));
								model.setUserId(rSet.getString("user_id"));
								model.setModule(rSet.getString("module"));
								model.setMethod(rSet.getString("method"));
								model.setUrl(rSet.getString("url"));
								model.setReqBody(rSet.getString("req_body"));
								model.setResBody(rSet.getString("res_body"));
								model.setInTime(rSet.getTimestamp("in_time").toString());
								if (rSet.getTimestamp("out_time") != null) {
									model.setOutTime(rSet.getTimestamp("out_time").toString());
								}
								model.setTotalTime(rSet.getTimestamp("total_time"));
								model.setCreatedOn(rSet.getString("created_on"));
								model.setUpdatedOn(rSet.getString("updated_on"));
								accessRespModel.add(model);
								paginatedList = getPaginatedList(accessRespModel, pageNo, pageSize);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				Connection connection = dataSource.getConnection();
				String databaseName = "logs_db";

				for (String tableName : tableNames) {
					System.out.println("logs tableName-- " + databaseName + "." + tableName);
					String sql = "select "
							+ "id , user_id, module, method, url, req_body, res_body, in_time, out_time, total_time, created_on, updated_on from "
							+ databaseName + "." + tableName + " where in_time >= ? and out_time <= ?";
					try (PreparedStatement statement = connection.prepareStatement(sql)) {
						int parampose = 1;
						statement.setString(parampose++, fromDate);
						statement.setString(parampose++, toDate);
						try (ResultSet rSet = statement.executeQuery()) {
							while (rSet.next()) {
								AccessLogRespModel model = new AccessLogRespModel();
								model.setId(rSet.getInt("id"));
								model.setUserId(rSet.getString("user_id"));
								model.setModule(rSet.getString("module"));
								model.setMethod(rSet.getString("method"));
								model.setUrl(rSet.getString("url"));
								model.setReqBody(rSet.getString("req_body"));
								model.setResBody(rSet.getString("res_body"));
								model.setInTime(rSet.getTimestamp("in_time").toString());
								if (rSet.getTimestamp("out_time") != null) {
									model.setOutTime(rSet.getTimestamp("out_time").toString());
								}
								model.setTotalTime(rSet.getTimestamp("total_time"));
								model.setCreatedOn(rSet.getString("created_on"));
								model.setUpdatedOn(rSet.getString("updated_on"));
								accessRespModel.add(model);
								paginatedList = getPaginatedList(accessRespModel, pageNo, pageSize);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return paginatedList;
	}

	/**
	 * method to get Pagination for rest access log
	 * 
	 * @param accessRespModel
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private List<AccessLogRespModel> getPaginatedList(List<AccessLogRespModel> accessRespModel, int pageNo,
			int pageSize) {
		List<AccessLogRespModel> paginatedList = new ArrayList<>();

		int startIndex = (pageNo - 1) * pageSize;
		int endIndex = Math.min(startIndex + pageSize, accessRespModel.size());

		if (startIndex < endIndex) {
			paginatedList = accessRespModel.subList(startIndex, endIndex);
		}

		return paginatedList;
	}

	/**
	 * method to get the access log table with pagination
	 * 
	 * @author LOKESH
	 * @param accLogModel
	 * @throws SQLException
	 */
	public List<AccessLogModel> getAccessTableswithPage(List<String> tableNames, String userId, String uri,
			String fromDate, String toDate, int pageNo, int pageSize) {
		List<AccessLogModel> response = new ArrayList<>();
		List<AccessLogModel> PaginationList = null;
		if (StringUtil.isNotNullOrEmpty(userId) && StringUtil.isNotNullOrEmpty(uri)) {
			try {
				Connection connection = dataSource.getConnection();
				String databaseName = "logs_db";
				for (String tableName : tableNames) {
					String sql = "SELECT id,user_id, ucc, uri, module, method, source, req_body, res_body, device_ip, req_id, user_agent, domain, content_type, session, in_time, out_time, "
							+ " elapsed_time, vendor, created_on FROM " + databaseName + "." + tableName
							+ " WHERE user_id = ? and uri = ? ";
					try (PreparedStatement statement = connection.prepareStatement(sql)) {
						int parampose = 1;
						statement.setString(parampose++, userId);
						statement.setString(parampose++, uri);
						try (ResultSet rSet = statement.executeQuery()) {
							while (rSet.next()) {
								AccessLogModel result = new AccessLogModel();
								result.setId(rSet.getInt("id"));
								result.setUri(rSet.getString("uri"));
								result.setUcc(rSet.getString("ucc"));
								result.setUser_id(rSet.getString("user_id"));
								result.setReq_id(rSet.getString("req_id"));
								result.setSource(rSet.getString("source"));
								result.setVendor(rSet.getString("vendor"));
								result.setIn_time(rSet.getTimestamp("in_time"));
								result.setOut_time(rSet.getTimestamp("out_time"));
								result.setModule(rSet.getString("module"));
								result.setMethod(rSet.getString("method"));
								result.setReq_body(rSet.getString("req_body"));
								result.setRes_body(rSet.getString("res_body"));
								result.setDeviceIp(rSet.getString("device_ip"));
								result.setUserAgent(rSet.getString("user_agent"));
								result.setDomain(rSet.getString("domain"));
								result.setContent_type(rSet.getString("content_type"));
								result.setSession(rSet.getString("session"));
								result.setCreated_on(rSet.getString("created_on"));
								response.add(result);
								PaginationList = getPaginationList(response, pageNo, pageSize);
							}
						}
						statement.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (StringUtil.isNotNullOrEmpty(userId)) {
			try {
				Connection connection = dataSource.getConnection();
				String databaseName = "logs_db";
				for (String tableName : tableNames) {
					String sql = "SELECT id,user_id, ucc, uri, module, method, source, req_body, res_body, device_ip, req_id, user_agent, domain, content_type, session, in_time, out_time, "
							+ " elapsed_time, vendor, created_on FROM " + databaseName + "." + tableName
							+ " WHERE user_id = ? ";
					try (PreparedStatement statement = connection.prepareStatement(sql)) {
						int parampose = 1;
						statement.setString(parampose++, userId);
						try (ResultSet rSet = statement.executeQuery()) {
							while (rSet.next()) {
								AccessLogModel result = new AccessLogModel();
								result.setId(rSet.getInt("id"));
								result.setUri(rSet.getString("uri"));
								result.setUcc(rSet.getString("ucc"));
								result.setUser_id(rSet.getString("user_id"));
								result.setReq_id(rSet.getString("req_id"));
								result.setSource(rSet.getString("source"));
								result.setVendor(rSet.getString("vendor"));
								result.setIn_time(rSet.getTimestamp("in_time"));
								result.setOut_time(rSet.getTimestamp("out_time"));
								result.setModule(rSet.getString("module"));
								result.setMethod(rSet.getString("method"));
								result.setReq_body(rSet.getString("req_body"));
								result.setRes_body(rSet.getString("res_body"));
								result.setDeviceIp(rSet.getString("device_ip"));
								result.setUserAgent(rSet.getString("user_agent"));
								result.setDomain(rSet.getString("domain"));
								result.setContent_type(rSet.getString("content_type"));
								result.setSession(rSet.getString("session"));
								result.setCreated_on(rSet.getString("created_on"));
								response.add(result);
								PaginationList = getPaginationList(response, pageNo, pageSize);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (StringUtil.isNotNullOrEmpty(uri)) {
			try {
				Connection connection = dataSource.getConnection();
				String databaseName = "logs_db";
				for (String tableName : tableNames) {
					int parampose = 1;
					String sql = "SELECT id,user_id, ucc, uri, module, method, source, req_body, res_body, device_ip, req_id, user_agent, domain, content_type, session, in_time, out_time, "
							+ " elapsed_time, vendor, created_on FROM " + databaseName + "." + tableName
							+ " WHERE uri = ? ";
					try (PreparedStatement statement = connection.prepareStatement(sql)) {
						statement.setString(parampose++, uri);
						try (ResultSet rSet = statement.executeQuery()) {
							while (rSet.next()) {
								AccessLogModel result = new AccessLogModel();
								result.setId(rSet.getInt("id"));
								result.setUri(rSet.getString("uri"));
								result.setUcc(rSet.getString("ucc"));
								result.setUser_id(rSet.getString("user_id"));
								result.setReq_id(rSet.getString("req_id"));
								result.setSource(rSet.getString("source"));
								result.setVendor(rSet.getString("vendor"));
								result.setIn_time(rSet.getTimestamp("in_time"));
								result.setOut_time(rSet.getTimestamp("out_time"));
								result.setModule(rSet.getString("module"));
								result.setMethod(rSet.getString("method"));
								result.setReq_body(rSet.getString("req_body"));
								result.setRes_body(rSet.getString("res_body"));
								result.setDeviceIp(rSet.getString("device_ip"));
								result.setUserAgent(rSet.getString("user_agent"));
								result.setDomain(rSet.getString("domain"));
								result.setContent_type(rSet.getString("content_type"));
								result.setSession(rSet.getString("session"));
								result.setCreated_on(rSet.getString("created_on"));
								response.add(result);
								PaginationList = getPaginationList(response, pageNo, pageSize);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				Connection connection = dataSource.getConnection();
				String databaseName = "logs_db";
				for (String tableName : tableNames) {
					String sql = "SELECT id,user_id, ucc, uri, module, method, source, req_body, res_body, device_ip, req_id, user_agent, domain, content_type, session, in_time, out_time, "
							+ " elapsed_time, vendor, created_on FROM " + databaseName + "." + tableName;
					try (PreparedStatement statement = connection.prepareStatement(sql)) {
						try (ResultSet rSet = statement.executeQuery()) {
							while (rSet.next()) {
								AccessLogModel result = new AccessLogModel();
								result.setId(rSet.getInt("id"));
								result.setUri(rSet.getString("uri"));
								result.setUcc(rSet.getString("ucc"));
								result.setUser_id(rSet.getString("user_id"));
								result.setReq_id(rSet.getString("req_id"));
								result.setSource(rSet.getString("source"));
								result.setVendor(rSet.getString("vendor"));
								result.setIn_time(rSet.getTimestamp("in_time"));
								result.setOut_time(rSet.getTimestamp("out_time"));
								result.setModule(rSet.getString("module"));
								result.setMethod(rSet.getString("method"));
								result.setReq_body(rSet.getString("req_body"));
								result.setRes_body(rSet.getString("res_body"));
								result.setDeviceIp(rSet.getString("device_ip"));
								result.setUserAgent(rSet.getString("user_agent"));
								result.setDomain(rSet.getString("domain"));
								result.setContent_type(rSet.getString("content_type"));
								result.setSession(rSet.getString("session"));
								result.setCreated_on(rSet.getString("created_on"));
								response.add(result);
								PaginationList = getPaginationList(response, pageNo, pageSize);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return PaginationList;
	}

	/**
	 * method to get Pagination for access log
	 * 
	 * @param response
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private List<AccessLogModel> getPaginationList(List<AccessLogModel> response, int pageNo, int pageSize) {
		List<AccessLogModel> paginatedList = new ArrayList<>();

		int startIndex = (pageNo - 1) * pageSize;
		int endIndex = Math.min(startIndex + pageSize, response.size());

		if (startIndex < endIndex) {
			paginatedList = response.subList(startIndex, endIndex);
		}

		return paginatedList;
	}

	/**
	 * method to get rest access logs from date to to date
	 * 
	 * @author SowmiyaThangaraj
	 * @param tableNames
	 * @param userId
	 * @param uri
	 * @param fromDate
	 * @param toDate
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<AccessLogRespModel> getRestAccessTableswithPage(List<String> tableNames, String userId, String uri,
			String fromDate, String toDate, int pageNo, int pageSize) {
		List<AccessLogRespModel> accessRespModel = new ArrayList<>();
		List<AccessLogRespModel> paginatedList = null;
		if (StringUtil.isNotNullOrEmpty(userId)) {
			try {
				Connection connection = dataSource.getConnection();
				String databaseName = "logs_db";

				for (String tableName : tableNames) {
					System.out.println("logs tableName-- " + databaseName + "." + tableName);
					String sql = "select "
							+ "id , user_id, module, method, url, req_body, res_body, in_time, out_time, total_time, created_on, updated_on from "
							+ databaseName + "." + tableName + " where user_id = ? and in_time >= ? and out_time <= ?";
					try (PreparedStatement statement = connection.prepareStatement(sql)) {
						int parampose = 1;
						statement.setString(parampose++, userId);
						statement.setString(parampose++, fromDate);
						statement.setString(parampose++, toDate);
						try (ResultSet rSet = statement.executeQuery()) {
							while (rSet.next()) {
								AccessLogRespModel model = new AccessLogRespModel();
								model.setId(rSet.getInt("id"));
								model.setUserId(rSet.getString("user_id"));
								model.setModule(rSet.getString("module"));
								model.setMethod(rSet.getString("method"));
								model.setUrl(rSet.getString("url"));
								model.setReqBody(rSet.getString("req_body"));
								model.setResBody(rSet.getString("res_body"));
								model.setInTime(rSet.getTimestamp("in_time").toString());
								if (rSet.getTimestamp("out_time") != null) {
									model.setOutTime(rSet.getTimestamp("out_time").toString());
								}
								model.setTotalTime(rSet.getTimestamp("total_time"));
								model.setCreatedOn(rSet.getString("created_on"));
								model.setUpdatedOn(rSet.getString("updated_on"));
								accessRespModel.add(model);
								paginatedList = getPaginatedList(accessRespModel, pageNo, pageSize);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				Connection connection = dataSource.getConnection();
				String databaseName = "logs_db";

				for (String tableName : tableNames) {
					System.out.println("logs tableName-- " + databaseName + "." + tableName);
					String sql = "select "
							+ "id , user_id, module, method, url, req_body, res_body, in_time, out_time, total_time, created_on, updated_on from "
							+ databaseName + "." + tableName + " where in_time >= ? and out_time <= ?";
					try (PreparedStatement statement = connection.prepareStatement(sql)) {
						int parampose = 1;
						statement.setString(parampose++, fromDate);
						statement.setString(parampose++, toDate);
						try (ResultSet rSet = statement.executeQuery()) {
							while (rSet.next()) {
								AccessLogRespModel model = new AccessLogRespModel();
								model.setId(rSet.getInt("id"));
								model.setUserId(rSet.getString("user_id"));
								model.setModule(rSet.getString("module"));
								model.setMethod(rSet.getString("method"));
								model.setUrl(rSet.getString("url"));
								model.setReqBody(rSet.getString("req_body"));
								model.setResBody(rSet.getString("res_body"));
								model.setInTime(rSet.getTimestamp("in_time").toString());
								if (rSet.getTimestamp("out_time") != null) {
									model.setOutTime(rSet.getTimestamp("out_time").toString());
								}
								model.setTotalTime(rSet.getTimestamp("total_time"));
								model.setCreatedOn(rSet.getString("created_on"));
								model.setUpdatedOn(rSet.getString("updated_on"));
								accessRespModel.add(model);
								paginatedList = getPaginatedList(accessRespModel, pageNo, pageSize);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return paginatedList;
	}

}