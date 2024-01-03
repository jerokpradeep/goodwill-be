package in.codifi.admin.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.simple.JSONObject;

import in.codifi.admin.config.ApplicationProperties;
import in.codifi.admin.model.request.SendNoficationReqModel;

@ApplicationScoped
public class PushNoficationUtils {
	
	@Inject
	ApplicationProperties props;

	
	/**
	 * Method to send recommendation message
	 * 
	 * @author Gowthaman
	 * @param deviceIds
	 * @param reqModel
	 */
	@SuppressWarnings("unchecked")
	public void sendNofification(List<String> deviceIds, SendNoficationReqModel reqModel) {
		try {
			System.out.println(props.getFcmApiKey());
			URL url = new URL(props.getFcmBaseUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "key=" + props.getFcmApiKey());
			conn.setRequestProperty("Content-Type", "application/json");
			JSONObject requestNew = new JSONObject();
			requestNew.put("click_action", "FLUTTER_NOTIFICATION_CLICK");
			requestNew.put("collapse_key", "1234");

			JSONObject androidNew = new JSONObject();
			JSONObject notificationAndroidNew = new JSONObject();
			notificationAndroidNew.put("channel_id", "goodwill_notification");
			androidNew.put("notification", notificationAndroidNew);
			requestNew.put("android", androidNew);

			JSONObject noramlNotification = new JSONObject();
			noramlNotification.put("android_channel_id", "goodwill_notification");
			noramlNotification.put("channel_id", "goodwill_notification");
			noramlNotification.put("title_color", "#2a6d57");
			noramlNotification.put("title", reqModel.getTitle());
			noramlNotification.put("body", reqModel.getMessage());
			requestNew.put("notification", noramlNotification);
			requestNew.put("registration_ids", deviceIds);

			JSONObject data = new JSONObject();

			if (reqModel.getMessageType().equalsIgnoreCase("Info")) {
				data.put("type", "Info");
			} else if (reqModel.getMessageType().equalsIgnoreCase("url")) {
				data.put("type", "ext_url");
				data.put("url", reqModel.getUrl());
			} else if (reqModel.getMessageType().equalsIgnoreCase("Order")) {
				data.put("type", "Order");
				data.put("orderRecommentation", reqModel.getOrderRecommendation());
			}

			requestNew.put("data", data);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(requestNew.toString());
			wr.flush();
			wr.close();
			int responseCode = conn.getResponseCode();
			System.out.println("Response Code : " + responseCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
