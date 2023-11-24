package in.codifi.api.cache;

import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.config.ConfigProvider;
import org.json.simple.JSONObject;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import in.codifi.api.entity.primary.PredefinedMwEntity;
import in.codifi.api.entity.primary.TickerTapeEntity;
import in.codifi.api.model.AdvancedMWModel;
import in.codifi.api.model.UserPerferencePreDefModel;
import in.codifi.cache.model.AnalysisRespModel;
import in.codifi.cache.model.ContractMasterModel;
import in.codifi.cache.model.EventDataModel;
import in.codifi.cache.model.MtfDataModel;
import in.codifi.cache.model.PreferenceModel;

public class HazelCacheController {
	public static HazelCacheController HazelCacheController = null;
	private HazelcastInstance hz = null;

	public static HazelCacheController getInstance() {
		if (HazelCacheController == null) {
			HazelCacheController = new HazelCacheController();

		}
		return HazelCacheController;
	}

	public HazelcastInstance getHz() {
		if (hz == null) {
			ClientConfig clientConfig = new ClientConfig();
			clientConfig.setClusterName(ConfigProvider.getConfig().getValue("config.app.hazel.cluster", String.class));
			clientConfig.getNetworkConfig()
					.addAddress(ConfigProvider.getConfig().getValue("config.app.hazel.address", String.class));
			hz = HazelcastClient.newHazelcastClient(clientConfig);
		}
		return hz;
	}

	private Map<String, List<JSONObject>> mwListByUserId = getHz().getMap("mwListByUserId");
	private Map<String, List<JSONObject>> advanceMWListByUserId = getHz().getMap("advanceMWListByUserId");

	private Map<String, ContractMasterModel> contractMaster = getHz().getMap("contractMaster");
	private Map<String, List<PredefinedMwEntity>> predefinedMwList = getHz().getMap("predefinedMwList");
	private Map<String, List<TickerTapeEntity>> TickerTapeList = getHz().getMap("TickerTapeList");
	private Map<String, List<PreferenceModel>> perference = getHz().getMap("perference");
	private Map<String, List<PredefinedMwEntity>> masterPredefinedMwList = getHz().getMap("masterPredefinedMwList");
	private Map<String, UserPerferencePreDefModel> userPerferenceModel = getHz().getMap("userPerferenceModel");

	private Map<String, MtfDataModel> mtfDataModel = getHz().getMap("mtfDataModel");
	private Map<String, EventDataModel> eventData = getHz().getMap("eventData");
	private Map<String, AnalysisRespModel> topGainers = getHz().getMap("topGainers");
	private Map<String, AnalysisRespModel> topLosers = getHz().getMap("topLosers");
	private Map<String, AnalysisRespModel> fiftyTwoWeekHigh = getHz().getMap("fiftyTwoWeekHigh");
	private Map<String, AnalysisRespModel> fiftyTwoWeekLow = getHz().getMap("fiftyTwoWeekLow");
	private Map<String, List<AnalysisRespModel>> analysisData = getHz().getMap("analysisData");

	private Map<String, List<AnalysisRespModel>> analysisfiftyTwoWeekHigh = getHz().getMap("analysisfiftyTwoWeekHigh");
	private Map<String, List<AnalysisRespModel>> analysisfiftyTwoWeekLow = getHz().getMap("analysisfiftyTwoWeekLow");
	private Map<String, List<AnalysisRespModel>> analysistopGainers = getHz().getMap("analysistopGainers");
	private Map<String, List<AnalysisRespModel>> analysistopLosers = getHz().getMap("analysistopLosers");

	private Map<String, AdvancedMWModel> advPredefinedMW = getHz().getMap("advPredefinedMW");
	private Map<String, AdvancedMWModel> advMWData = getHz().getMap("advMWData");

	public Map<String, List<JSONObject>> getMwListByUserId() {
		return mwListByUserId;
	}

	public void setMwListByUserId(Map<String, List<JSONObject>> mwListByUserId) {
		this.mwListByUserId = mwListByUserId;
	}

	public Map<String, ContractMasterModel> getContractMaster() {
		return contractMaster;
	}

	public void setContractMaster(Map<String, ContractMasterModel> contractMaster) {
		this.contractMaster = contractMaster;
	}

	public Map<String, List<PredefinedMwEntity>> getPredefinedMwList() {
		return predefinedMwList;
	}

	public void setPredefinedMwList(Map<String, List<PredefinedMwEntity>> predefinedMwList) {
		this.predefinedMwList = predefinedMwList;
	}

	public Map<String, List<TickerTapeEntity>> getTickerTapeList() {
		return TickerTapeList;
	}

	public void setTickerTapeList(Map<String, List<TickerTapeEntity>> tickerTapeList) {
		TickerTapeList = tickerTapeList;
	}

	public Map<String, List<PreferenceModel>> getPerference() {
		return perference;
	}

	public void setPerference(Map<String, List<PreferenceModel>> perference) {
		this.perference = perference;
	}

	public Map<String, List<PredefinedMwEntity>> getMasterPredefinedMwList() {
		return masterPredefinedMwList;
	}

	public void setMasterPredefinedMwList(Map<String, List<PredefinedMwEntity>> masterPredefinedMwList) {
		this.masterPredefinedMwList = masterPredefinedMwList;
	}

	public Map<String, UserPerferencePreDefModel> getUserPerferenceModel() {
		return userPerferenceModel;
	}

	public void setUserPerferenceModel(Map<String, UserPerferencePreDefModel> userPerferenceModel) {
		this.userPerferenceModel = userPerferenceModel;
	}

	public Map<String, List<JSONObject>> getAdvanceMWListByUserId() {
		return advanceMWListByUserId;
	}

	public void setAdvanceMWListByUserId(Map<String, List<JSONObject>> advanceMWListByUserId) {
		this.advanceMWListByUserId = advanceMWListByUserId;
	}

	public Map<String, EventDataModel> getEventData() {
		return eventData;
	}

	public void setEventData(Map<String, EventDataModel> eventData) {
		this.eventData = eventData;
	}

	public Map<String, AnalysisRespModel> getTopGainers() {
		return topGainers;
	}

	public void setTopGainers(Map<String, AnalysisRespModel> topGainers) {
		this.topGainers = topGainers;
	}

	public Map<String, AnalysisRespModel> getTopLosers() {
		return topLosers;
	}

	public void setTopLosers(Map<String, AnalysisRespModel> topLosers) {
		this.topLosers = topLosers;
	}

	public Map<String, AnalysisRespModel> getFiftyTwoWeekHigh() {
		return fiftyTwoWeekHigh;
	}

	public void setFiftyTwoWeekHigh(Map<String, AnalysisRespModel> fiftyTwoWeekHigh) {
		this.fiftyTwoWeekHigh = fiftyTwoWeekHigh;
	}

	public Map<String, AnalysisRespModel> getFiftyTwoWeekLow() {
		return fiftyTwoWeekLow;
	}

	public void setFiftyTwoWeekLow(Map<String, AnalysisRespModel> fiftyTwoWeekLow) {
		this.fiftyTwoWeekLow = fiftyTwoWeekLow;
	}

	public Map<String, List<AnalysisRespModel>> getAnalysisData() {
		return analysisData;
	}

	public void setAnalysisData(Map<String, List<AnalysisRespModel>> analysisData) {
		this.analysisData = analysisData;
	}

	public Map<String, List<AnalysisRespModel>> getAnalysisfiftyTwoWeekHigh() {
		return analysisfiftyTwoWeekHigh;
	}

	public void setAnalysisfiftyTwoWeekHigh(Map<String, List<AnalysisRespModel>> analysisfiftyTwoWeekHigh) {
		this.analysisfiftyTwoWeekHigh = analysisfiftyTwoWeekHigh;
	}

	public Map<String, List<AnalysisRespModel>> getAnalysisfiftyTwoWeekLow() {
		return analysisfiftyTwoWeekLow;
	}

	public void setAnalysisfiftyTwoWeekLow(Map<String, List<AnalysisRespModel>> analysisfiftyTwoWeekLow) {
		this.analysisfiftyTwoWeekLow = analysisfiftyTwoWeekLow;
	}

	public Map<String, List<AnalysisRespModel>> getAnalysistopGainers() {
		return analysistopGainers;
	}

	public void setAnalysistopGainers(Map<String, List<AnalysisRespModel>> analysistopGainers) {
		this.analysistopGainers = analysistopGainers;
	}

	public Map<String, List<AnalysisRespModel>> getAnalysistopLosers() {
		return analysistopLosers;
	}

	public void setAnalysistopLosers(Map<String, List<AnalysisRespModel>> analysistopLosers) {
		this.analysistopLosers = analysistopLosers;
	}

	public Map<String, AdvancedMWModel> getAdvPredefinedMW() {
		return advPredefinedMW;
	}

	public void setAdvPredefinedMW(Map<String, AdvancedMWModel> advPredefinedMW) {
		this.advPredefinedMW = advPredefinedMW;
	}

	public Map<String, AdvancedMWModel> getAdvMWData() {
		return advMWData;
	}

	public void setAdvMWData(Map<String, AdvancedMWModel> advMWData) {
		this.advMWData = advMWData;
	}

	public void setHz(HazelcastInstance hz) {
		this.hz = hz;
	}

	public Map<String, MtfDataModel> getMtfDataModel() {
		return mtfDataModel;
	}

	public void setMtfDataModel(Map<String, MtfDataModel> mtfDataModel) {
		this.mtfDataModel = mtfDataModel;
	}

}
