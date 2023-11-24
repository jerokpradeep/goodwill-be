package in.codifi.basket.config;

import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.config.ConfigProvider;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import in.codifi.cache.model.ContractMasterModel;
import in.codifi.cache.model.ProductMasterModel;
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

	private Map<String, String> restUserSession = getHz().getMap("restUserSession");
	private Map<String, ContractMasterModel> contractMaster = getHz().getMap("contractMaster");

	private Map<String, List<ProductMasterModel>> productTypes = getHz().getMap("productTypes");
	private Map<String, List<ProductMasterModel>> orderTypes = getHz().getMap("orderTypes");
	private Map<String, List<ProductMasterModel>> priceTypes = getHz().getMap("priceTypes");

}
