package in.codifi.cache;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import in.codifi.common.entity.log.AccessLogModel;
import in.codifi.common.model.request.RestAccessLogModel;
import lombok.Getter;
import lombok.Setter;

@ApplicationScoped
@Getter
@Setter
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

}
