package in.codifi.sso.auth.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import in.codifi.sso.auth.entity.logs.AccessLogModel;
import in.codifi.sso.auth.entity.logs.RestAccessLogModel;
import in.codifi.sso.auth.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class AccessLogManager {
	@Named("logs")
	@Inject
	DataSource dataSource;

	public void insertAccessLog(AccessLogModel accLogModel) {

		Date inTimeDate;
		if (accLogModel.getInTime() != null) {
			inTimeDate = new Date(accLogModel.getInTime().getTime());
		} else {
			inTimeDate = new Date();
		}
		Connection connection = null;
		Statement state = null;
		PreparedStatement statement = null;
		String date = new SimpleDateFormat("ddMMYYYY").format(inTimeDate);
		String hour = new SimpleDateFormat("HH").format(inTimeDate);
		String tableName = "tbl_" + date + "_access_log_" + hour;
		try {

			connection = dataSource.getConnection();
			state = connection.createStatement();

			String insertQuery = "INSERT INTO " + tableName
					+ "(user_id, ucc, req_id, source, vendor, in_time, out_time, lag_time,  module, method, req_body,"
					+ " res_body, device_ip, user_agent, domain, content_type, session, uri) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			statement = connection.prepareStatement(insertQuery);
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
//			statement.setString(paramPos++, accLogModel.getResBody());
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
			statement.executeUpdate();

			statement.close();
			state.close();
			connection.close();
		} catch (Exception e) {
			Log.error("KB - SSO - insertAccessLog -" + e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (state != null) {
					state.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				Log.error("KB - SSO - insertAccessLog -" + e);
			}
		}

	}

	/**
	 * 
	 * Method to insert rest access log
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param accLogModel
	 */
	public void insertRestAccessLog(RestAccessLogModel accLogModel) {

		Date inTimeDate = new Date();

		String date = new SimpleDateFormat("ddMMYYYY").format(inTimeDate);
		String tableName = "tbl_" + date + "_rest_access_log";
		PreparedStatement statement = null;
		Connection connection = null;
		Statement state = null;
		try {

			connection = dataSource.getConnection();
			state = connection.createStatement();

			String insertQuery = "INSERT INTO " + tableName + "(user_id, url, in_time, out_time, total_time, module,"
					+ " method, req_body, res_body,vendor) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			statement = connection.prepareStatement(insertQuery);
			int paramPos = 1;
			statement.setString(paramPos++, accLogModel.getUserId());
			statement.setString(paramPos++, accLogModel.getUrl());
			statement.setTimestamp(paramPos++, accLogModel.getInTime());
			statement.setTimestamp(paramPos++, accLogModel.getOutTime());
			statement.setString(paramPos++, accLogModel.getTotalTime());
			statement.setString(paramPos++, accLogModel.getModule());
			statement.setString(paramPos++, accLogModel.getMethod());
			statement.setString(paramPos++, accLogModel.getReqBody());
//			statement.setString(paramPos++, accLogModel.getResBody());
			String respBody = "";
			int maxLength = 8192;
			if (StringUtil.isNotNullOrEmpty(accLogModel.getResBody())
					&& accLogModel.getResBody().length() > maxLength) {
				respBody = accLogModel.getResBody().substring(0, maxLength);
			} else {
				respBody = accLogModel.getResBody();
			}
			statement.setString(paramPos++, respBody);
			statement.setString(paramPos++, "KB");
			statement.executeUpdate();

			statement.close();
			state.close();
			connection.close();
		} catch (Exception e) {
			Log.error("KB - SSO - insertRestAccessLog -" + e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (state != null) {
					state.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				Log.error("KB - SSO - insertRestAccessLog -" + e);
			}
		}
	}
}