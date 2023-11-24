package in.codifi.scrips.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import in.codifi.scrips.config.HazelcastConfig;
import in.codifi.scrips.entity.chartdb.PromptModel;
import io.quarkus.logging.Log;

@ApplicationScoped
public class PromptDao {

	@Named("chartdb")
	@Inject
	DataSource dataSource;

	/**
	 * 
	 * Method to load prompt data into cache
	 * 
	 * @author Dinesh Kumar
	 *
	 */
	public void loadPromptData() {

		PreparedStatement pStmt = null;
		Connection conn = null;
		ResultSet rSet = null;
		List<PromptModel> response = new ArrayList<PromptModel>();
		HazelcastConfig.getInstance().getPromptMaster().clear();
		try {
			conn = dataSource.getConnection();
			String query = "SELECT isin, exch,company_name, msg, type, severity, prompt FROM tbl_asm_gsm ";
			pStmt = conn.prepareStatement(query);
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					PromptModel result = new PromptModel();
					String isin = rSet.getString("isin");
					String exch = rSet.getString("exch");
					String key = (isin + "_" + exch).toUpperCase();
					result.setIsin(isin);
					result.setExch(exch);
					result.setCompany_name(rSet.getString("company_name"));
					result.setMsg(rSet.getString("msg"));
					result.setType(rSet.getString("type"));
					result.setSeverity(rSet.getString("severity"));
					result.setPrompt(rSet.getString("prompt"));
					response = HazelcastConfig.getInstance().getPromptMaster().get(key);
					if (response != null && response.size() > 0) {
						response = HazelcastConfig.getInstance().getPromptMaster().get(key);
						response.add(result);
						HazelcastConfig.getInstance().getPromptMaster().put(key, response);
					} else {
						response = new ArrayList<>();
						response.add(result);
						HazelcastConfig.getInstance().getPromptMaster().put(key, response);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} finally {
			try {
				rSet.close();
				pStmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
