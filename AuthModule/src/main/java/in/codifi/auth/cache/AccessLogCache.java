package in.codifi.auth.cache;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import in.codifi.auth.entity.logs.AccessLogModel;
import in.codifi.auth.entity.logs.RestAccessLogModel;
import in.codifi.auth.model.response.UsersLoggedInRespModel;

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
	private List<RestAccessLogModel> batchRestAccessModel = new ArrayList<>();
	private List<UsersLoggedInRespModel> usersLoggedInModel = new ArrayList<>();

	public List<AccessLogModel> getBatchAccessModel() {
		return batchAccessModel;
	}

	public void setBatchAccessModel(List<AccessLogModel> batchAccessModel) {
		this.batchAccessModel = batchAccessModel;
	}

	public List<RestAccessLogModel> getBatchRestAccessModel() {
		return batchRestAccessModel;
	}

	public void setBatchRestAccessModel(List<RestAccessLogModel> batchRestAccessModel) {
		this.batchRestAccessModel = batchRestAccessModel;
	}

	public List<UsersLoggedInRespModel> getUsersLoggedInModel() {
		return usersLoggedInModel;
	}

	public void setUsersLoggedInModel(List<UsersLoggedInRespModel> usersLoggedInModel) {
		this.usersLoggedInModel = usersLoggedInModel;
	}

}
