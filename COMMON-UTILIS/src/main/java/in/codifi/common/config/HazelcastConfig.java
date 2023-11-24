package in.codifi.common.config;

import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.config.ConfigProvider;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import in.codifi.cache.model.AnalysisRespModel;
import in.codifi.cache.model.ProductMasterModel;
import in.codifi.cache.model.StockReturnModel;
import in.codifi.common.entity.DashboardPreferencesEntity;
import in.codifi.common.entity.EQSectorEntity;
import in.codifi.common.entity.EtfEntity;
import in.codifi.common.entity.FiiIndexEntity;
import in.codifi.common.entity.FutureEntity;
import in.codifi.common.entity.FutureMonthEntity;
import in.codifi.common.entity.IndicesEntity;
import in.codifi.common.entity.MarketingEntity;
import in.codifi.common.entity.SectorHeatMapEntity;
import in.codifi.common.entity.VersionEntity;
import in.codifi.common.ws.model.FIIDIIResp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HazelcastConfig {

	public static HazelcastConfig HazelcastConfig = null;
	private HazelcastInstance hz = null;

	public static HazelcastConfig getInstance() {
		if (HazelcastConfig == null) {
			HazelcastConfig = new HazelcastConfig();

		}
		return HazelcastConfig;
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

	private Map<String, List<IndicesEntity>> indicesDetails = getHz().getMap("indicesDetails");
	private Map<String, List<EtfEntity>> etfDetails = getHz().getMap("etfDetails");
	private Map<String, List<EQSectorEntity>> eqSectorDetails = getHz().getMap("eqSector");
	private Map<String, List<SectorHeatMapEntity>> sectorHeatMap = getHz().getMap("sectorHeatMap");
	private Map<String, List<FutureMonthEntity>> futureMonth = getHz().getMap("futureMonth");
	private Map<String, List<FutureEntity>> futureDetails = getHz().getMap("futureDetails");
	private Map<String, List<MarketingEntity>> marketingEntity = getHz().getMap("marketingEntity");
	private Map<String, List<DashboardPreferencesEntity>> preferences = getHz().getMap("preferences");
	private Map<String, List<VersionEntity>> version = getHz().getMap("version");

	private Map<String, List<FiiIndexEntity>> fiiIndexEntity = getHz().getMap("fiiIndexEntity");
	private Map<String, FIIDIIResp> activityData = getHz().getMap("activityData");
	private Map<String, Long> analysisUpdateTime = getHz().getMap("analysisUpdateTime");
	private Map<String, String> nseTokenCache = getHz().getMap("nseTokenCache");

	private Map<String, List<ProductMasterModel>> productTypes = getHz().getMap("productTypes");
	private Map<String, List<ProductMasterModel>> orderTypes = getHz().getMap("orderTypes");
	private Map<String, List<ProductMasterModel>> priceTypes = getHz().getMap("priceTypes");
	private Map<String, List<StockReturnModel>> stockReturnDetails = getHz().getMap("stockReturnDetails");

	private Map<String, List<AnalysisRespModel>> analysisData = getHz().getMap("analysisData");
	private Map<String, List<AnalysisRespModel>> analysistopGainers = getHz().getMap("analysistopGainers");

	private Map<String, List<AnalysisRespModel>> analysistopLosers = getHz().getMap("analysistopLosers");

	private Map<String, List<AnalysisRespModel>> analysisfiftyTwoWeekHigh = getHz().getMap("analysisfiftyTwoWeekHigh");

	private Map<String, List<AnalysisRespModel>> analysisfiftyTwoWeekLow = getHz().getMap("analysisfiftyTwoWeekLow");

}
