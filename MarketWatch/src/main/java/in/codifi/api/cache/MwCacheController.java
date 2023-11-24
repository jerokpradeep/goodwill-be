package in.codifi.api.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;

import org.json.simple.JSONObject;

import in.codifi.api.entity.primary.PredefinedMwEntity;
import in.codifi.api.model.AdvancedMWModel;

@ApplicationScoped
public class MwCacheController {
	private static Map<String, List<JSONObject>> advanceMWListByUserId = new ConcurrentHashMap<>();
	private static Map<String, List<PredefinedMwEntity>> predefinedMwList = new ConcurrentHashMap<>();
	private static Map<String, List<PredefinedMwEntity>> masterPredefinedMwList = new ConcurrentHashMap<>();
	private static Map<String, AdvancedMWModel> advPredefinedMW = new ConcurrentHashMap<>();

	public static Map<String, List<JSONObject>> getAdvanceMWListByUserId() {
		return advanceMWListByUserId;
	}

	public static void setAdvanceMWListByUserId(Map<String, List<JSONObject>> advanceMWListByUserId) {
		MwCacheController.advanceMWListByUserId = advanceMWListByUserId;
	}

	public static Map<String, List<PredefinedMwEntity>> getPredefinedMwList() {
		return predefinedMwList;
	}

	public static void setPredefinedMwList(Map<String, List<PredefinedMwEntity>> predefinedMwList) {
		MwCacheController.predefinedMwList = predefinedMwList;
	}

	public static Map<String, List<PredefinedMwEntity>> getMasterPredefinedMwList() {
		return masterPredefinedMwList;
	}

	public static void setMasterPredefinedMwList(Map<String, List<PredefinedMwEntity>> masterPredefinedMwList) {
		MwCacheController.masterPredefinedMwList = masterPredefinedMwList;
	}

	public static Map<String, AdvancedMWModel> getAdvPredefinedMW() {
		return advPredefinedMW;
	}

	public static void setAdvPredefinedMW(Map<String, AdvancedMWModel> advPredefinedMW) {
		MwCacheController.advPredefinedMW = advPredefinedMW;
	}

}
