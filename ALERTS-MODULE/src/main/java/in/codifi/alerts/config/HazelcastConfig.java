package in.codifi.alerts.config;

import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.config.ConfigProvider;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import in.codifi.ws.model.kc.GetIntroSpectResponse;
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
			List<String> hazelAddress = List
					.of(ConfigProvider.getConfig().getValue("config.app.hazel.address", String.class).split(","));
			hazelAddress.stream().forEach(address -> {
				clientConfig.getNetworkConfig().addAddress(address);
			});

			hz = HazelcastClient.newHazelcastClient(clientConfig);
		}
		return hz;
	}

	private Map<String, String> restUserSession = getHz().getMap("restUserSession");
	private Map<String, GetIntroSpectResponse> keycloakUserInfo = getHz().getMap("keycloakUserInfo");
	private Map<String, Long> otpMailSettings = getHz().getMap("otpMailSettings");

}
