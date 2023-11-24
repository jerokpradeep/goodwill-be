package in.codifi.admin.config;

import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.config.ConfigProvider;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import in.codifi.admin.entity.HoldingsJSONEntity;
import in.codifi.admin.entity.PositionAvgPriceEntity;
import in.codifi.admin.ws.model.kc.GetUserInfoResp;
import in.codifi.cache.model.EdisRecordModel;
import in.codifi.cache.model.HoldingsData;
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

	private Map<String, String> positionContract = getHz().getMap("positionContract");
	private Map<String, String> holdingsContract = getHz().getMap("holdingsContract");
	private Map<String, List<PositionAvgPriceEntity>> positionsAvgPrice = getHz().getMap("positionsAvgPrice");
	private Map<String, String> keycloakAdminSession = getHz().getMap("keycloakAdminSession");
	private Map<String, List<HoldingsData>> uploadedHoldings = getHz().getMap("uploadedHoldings");
	private Map<String, HoldingsJSONEntity> holdingsData = getHz().getMap("holdingsData");
	private Map<String, String> poaStatus = getHz().getMap("poaStatus");
	private Map<String, List<EdisRecordModel>> pledgeData = getHz().getMap("pledgeData");

	private Map<String, List<GetUserInfoResp>> keycloakUserDetails = getHz().getMap("keycloakUserDetails");

}
