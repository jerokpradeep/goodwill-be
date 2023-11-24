package in.codifi.admin.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.json.simple.JSONObject;

import in.codifi.admin.model.response.LogDetailsResponseModel;
import in.codifi.cache.CacheController;
import io.quarkus.logging.Log;

@SuppressWarnings("unchecked")
@ApplicationScoped
public class AdminLogsDAO {

	@Named("logs")
	@Inject
	DataSource datasource;

	/**
	 * 
	 * Method to get the total logged in details for past days from data base
	 * 
	 * 
	 * 
	 * @author LOKESH
	 * 
	 * @return
	 * 
	 */

	public List<LogDetailsResponseModel> getUserLogDetails() {

		List<LogDetailsResponseModel> response = null;

		Connection conn = null;

		PreparedStatement pStmt = null;

		ResultSet rSet = null;

		LogDetailsResponseModel tempDto = null;

		try {
			conn = datasource.getConnection();
			pStmt = conn.prepareStatement("SELECT date, web, mob, api,unique_web, unique_mob, "
					+ "unique_api FROM tbl_daily_log_reports order by date desc limit 30");
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				response = new ArrayList<LogDetailsResponseModel>();
				while (rSet.next()) {
					tempDto = new LogDetailsResponseModel();
					tempDto.setDate(rSet.getString("date"));
					tempDto.setTotalWeb(rSet.getString("web"));
					tempDto.setTotalMobile(rSet.getString("mob"));
					tempDto.setTotalApi(rSet.getString("api"));
					tempDto.setUniqueWeb(rSet.getString("unique_web"));
					tempDto.setUniqueApi(rSet.getString("unique_api"));
					response.add(tempDto);
				}
			}
			conn.close();
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

				Log.error("getCountBySource -" + e);

			}

		}

		return response;

	}

	public void getUserRec() {
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		try {
			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
			String outputDate = formatter.format(currentDate);

			for (int i = 1; i < 23; i++) {
				String Formatted = String.format("%02d", i);
				conn = datasource.getConnection();
				pStmt = conn.prepareStatement(
						"SELECT distinct(user_id) as users ,  device_ip, count(*) as counts FROM tbl_" + outputDate
								+ "_access_log_" + Formatted + "group by user_id, device_ip order by counts desc ");
				rSet = pStmt.executeQuery();
				if (rSet != null) {
					while (rSet.next()) {
						CacheController.getUserRec().put(rSet.getString("users") + "_" + rSet.getString("device_ip"),
								rSet.getString("counts"));
					}
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
				Log.error(e.getMessage());
			}
		}
	}

	/**
	 * Method to get the url based top 10 records
	 * 
	 * @author LOKESH
	 * @return
	 */

	public List<JSONObject> getUrlBasedRecords() {
		List<JSONObject> response = null;
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		JSONObject tempJson = null;
		try {
			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
			String outputDate = formatter.format(currentDate);
			LocalTime time = LocalTime.now();
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH");
			String outputDate1 = formatter1.format(time);
			int intValue = Integer.parseInt(outputDate1);
			int Hr = 1;
			int hour = ((intValue) - (Hr));
			conn = datasource.getConnection();
			pStmt = conn.prepareStatement("SELECT distinct(uri) as url , count(*) as counts FROM tbl_" + outputDate
					+ "_access_log_" + hour + " group by url order by counts desc limit 15");
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				response = new ArrayList<JSONObject>();
				while (rSet.next()) {
					tempJson = new JSONObject();
					tempJson.put("url", rSet.getString("url"));
					tempJson.put("count", rSet.getString("counts"));
					response.add(tempJson);
				}
			}
//			}
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
				Log.error(e.getMessage());
			}
		}
		return response;
	}

	/**
	 * Method to get last 12 hour login count
	 * 
	 * @author LOKESH
	 * @return
	 */
	public List<JSONObject> getLast12hourLoginCount() {
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			conn = datasource.getConnection();
			pStmt = conn.prepareStatement("SELECT id,access_time,login_count,active_count,active_web_count, "
					+ " active_mob_count,active_api_count FROM tbl_login_count "
					+ " where access_time > DATE_SUB(NOW(), INTERVAL 12 HOUR)");
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					JSONObject result = new JSONObject();
					result.put("id", rSet.getString("id"));
					result.put("access_time", rSet.getString("access_time"));
					result.put("login_count", rSet.getString("login_count"));
					result.put("active_count", rSet.getString("active_count"));
					result.put("active_web_count", rSet.getString("active_web_count"));
					result.put("active_mob_count", rSet.getString("active_mob_count"));
					result.put("active_api_count", rSet.getString("active_api_count"));
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
				Log.error(e.getMessage());
			}
		}
		return jsonList;
	}

	/**
	 * Method to get distinct url for drop down
	 * 
	 * @author LOKESH
	 * @return
	 */
	public List<String> getDistinctUrl() {
		List<String> response = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		try {
			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
			String outputDate = formatter.format(currentDate);

			for (int i = 1; i < 23; i++) {
				String Formatted = String.format("%02d", i);
				conn = datasource.getConnection();
				pStmt = conn.prepareStatement(
						"select distinct uri as uri from tbl_" + outputDate + "_access_log_" + Formatted);
				rSet = pStmt.executeQuery();
				if (rSet != null) {
					while (rSet.next()) {
						response.add(rSet.getString("uri"));
					}
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
				Log.error(e.getMessage());
			}
		}
		return response;
	}

}
