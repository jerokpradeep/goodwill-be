package in.codifi.admin.cache;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import in.codifi.admin.entity.logs.AccessLogModel;

@ApplicationScoped

public class AccessLogCache {

	private static AccessLogCache instance = null;

	public static synchronized AccessLogCache getInstance() {
		if (instance == null) {
			instance = new AccessLogCache();
		}
		return instance;
	}

	private List<AccessLogModel> batchAccessModel = new ArrayList<>();

	public List<AccessLogModel> getBatchAccessModel() {
		return batchAccessModel;
	}

	public void setBatchAccessModel(List<AccessLogModel> batchAccessModel) {
		this.batchAccessModel = batchAccessModel;
	}

}
