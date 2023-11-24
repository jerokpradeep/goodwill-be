package in.codifi.auth.utility;

import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.DatatypeConverter;

import in.codifi.auth.config.SMSPropertiesConfig;

@ApplicationScoped
public class CommonUtils {

	@Inject
	SMSPropertiesConfig props;

	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	static Random randomNumberGenerator = new Random();

	public static void trustedManagement() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}

	public static String encryptWithSHA256(String input) {
		String response = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			response = DatatypeConverter.printHexBinary(hash).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 
	 * Method to generate random keys
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param count
	 * @return
	 */
	public static String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
	

	public String generatealpanumeric() {
		int size = 128;
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(size);
		for (int i = 0; i < size; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		String num = sb.toString();
		return num;
	}

	/**
	 * 
	 * Method to generate OTP
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public String generateOTP() {
		int otp = randomNumberGenerator.nextInt(999999);
		return String.format("%06d", otp);
	}

	/**
	 * Method to send OTP
	 * 
	 * @param otp
	 * @param mobile
	 * @param userName
	 */
//	public void sendOTPMessage(String otp, String mobile, String userName) {
//		ExecutorService pool = Executors.newSingleThreadExecutor();
//		pool.execute(new Runnable() {
//			@Override
//			public void run() {
//				int randomNum = (int) Math.random();
//				if (randomNum % 2 == 0) {
//					sendMtakzMessage(otp, mobile, userName);
//				} else {
//					sendMtakzMessage(otp, mobile, userName);
//				}
//			}
//		});
//		pool.shutdown();
//	}

	/**
	 * Method to send OTP
	 * 
	 * @param otp
	 * @param mobile
	 * @param userName
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	public boolean sendOTPMessage(String otp, String mobile, String message) {
//		try {
//			HttpURLConnection conn = null;
//			JSONObject json = new JSONObject();
//			json.put("apikey", props.getApiKey());
//			json.put("senderid", props.getSenderId());
//			json.put("number", mobile);
//			json.put("message", "Dear User, " + otp + message);
//			URL url = new URL(props.getUrl());
//			conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("POST");
//			conn.setRequestProperty("Content-Type", "application/json");
//			conn.setDoOutput(true);
//			try (OutputStream os = conn.getOutputStream()) {
//				byte[] input = json.toString().getBytes("utf-8");
//				os.write(input, 0, input.length);
//			}
//			if (conn.getResponseCode() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//			}
//			BufferedReader br1 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//			String output;
//			while ((output = br1.readLine()) != null) {
//				Object object = JSONValue.parse(output);
//			}
//			return true;
//		} catch (Exception e) {
//			Log.error(e);
//		}
//		return false;
//	}

//	/**
//	 * Method to send OTP
//	 * 
//	 * @param otp
//	 * @param mobile
//	 * @param message
//	 * @return
//	 */
//	public boolean sendOTPMessage(String otp, String mobile, String message) {
//
//		if (HazelcastConfig.getInstance().getOtpMessageSettingsKB().get(AppConstants.MESSAGE_SETTINGS) != null) {
//			long messageSetting = HazelcastConfig.getInstance().getOtpMessageSettingsKB()
//					.get(AppConstants.MESSAGE_SETTINGS);
//			if (messageSetting == 0) {
//				Random random = new Random();
//				int randomNum = random.nextInt(1000);
//				if (randomNum % 2 == 0) {
//					return sendAirtelMessage(otp, mobile, message);
//				} else {
//					return sendMtalkzMessage(otp, mobile, message);
//				}
//			} else if (messageSetting == 1) {
//				return sendAirtelMessage(otp, mobile, message);
//			} else if (messageSetting == 2) {
//				return sendMtalkzMessage(otp, mobile, message);
//			} else {
//				Random random = new Random();
//				int randomNum = random.nextInt(1000);
//				if (randomNum % 2 == 0) {
//					return sendAirtelMessage(otp, mobile, message);
//				} else {
//					return sendMtalkzMessage(otp, mobile, message);
//				}
//			}
//		} else {
//			Random random = new Random();
//			int randomNum = random.nextInt(1000);
//			if (randomNum % 2 == 0) {
//				return sendAirtelMessage(otp, mobile, message);
//			} else {
//				return sendMtalkzMessage(otp, mobile, message);
//			}
//		}
//	}

//	/**
//	 * Method to send OTP
//	 * 
//	 * @param otp
//	 * @param mobile
//	 * @param userName
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public boolean sendMtalkzMessage(String otp, String mobile, String message) {
//		try {
//			HttpURLConnection conn = null;
//			JSONObject json = new JSONObject();
//			json.put("apikey", props.getApiKey());
//			json.put("senderid", props.getSenderId());
//			json.put("number", mobile);
//			json.put("message", "Dear User, " + otp + message);
//			URL url = new URL(props.getUrl());
//			conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("POST");
//			conn.setRequestProperty("Content-Type", "application/json");
//			conn.setDoOutput(true);
//			try (OutputStream os = conn.getOutputStream()) {
//				byte[] input = json.toString().getBytes("utf-8");
//				os.write(input, 0, input.length);
//			}
//			if (conn.getResponseCode() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//			}
//			BufferedReader br1 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//			String output;
//			while ((output = br1.readLine()) != null) {
//				Object object = JSONValue.parse(output);
//			}
//			return true;
//		} catch (Exception e) {
//			System.out.println("Send Mtalkz Message Failed");
//			Log.error(e);
//		}
//		return false;
//	}
//
//	@SuppressWarnings("unchecked")
//	private boolean sendAirtelMessage(String otp, String mobile, String message) {
//		try {
//			System.out.println("Sending message by Airtel " + mobile);
//			Object object = null;
//			HttpURLConnection conn = null;
//			JSONObject json = new JSONObject();
//			json.put("customerId", props.getAirtelCustId());
//			json.put("destinationAddress", mobile);
//			json.put("message", "Dear User, " + otp + message);
//			json.put("sourceAddress", props.getAirtelSenderId());
//			json.put("messageType", props.getAirtelMsgType());
//			json.put("dltTemplateId", props.getAirtelDltId());
//			json.put("entityId", props.getAirtelEntityId());
//			json.put("otp", true);
//			URL url = new URL(props.getAirtelUrl());
//			conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("POST");
//			conn.setRequestProperty("Content-Type", "application/json");
//			conn.setRequestProperty("Authorization",
//					"Basic QUxJQ0VfQkxVRV9ZNTRzWGY1em5pRXhLaDhvQW9SSzpJMVVUOU5MKiFZVnIkeDc2");
//			conn.setDoOutput(true);
//			try (OutputStream os = conn.getOutputStream()) {
//				byte[] input = json.toString().getBytes("utf-8");
//				os.write(input, 0, input.length);
//			}
//			if (conn.getResponseCode() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//			}
//			BufferedReader br1 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//			String output;
//			while ((output = br1.readLine()) != null) {
//				object = JSONValue.parse(output);
//			}
//			return true;
//		} catch (Exception e) {
//			System.out.println("Send Airtel Message Failed");
//			Log.error(e);
//		}
//		return false;
//	}

	/**
	 * Method to encrypt the user API key and encrypted key using SHA256 algorithm
	 * and store it in caches
	 * 
	 * @param pObject
	 * @return
	 * @author Dinesh
	 */
	public String generateSHAKey(String pUserId, String authCode, String apiSecret) {
		String encSHAKey = null;
		try {
			String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			StringBuilder builder = new StringBuilder();
			int count = 32;
			while (count-- != 0) {
				int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
				builder.append(ALPHA_NUMERIC_STRING.charAt(character));
			}
			String data = pUserId + authCode + apiSecret;
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(data.getBytes("UTF-8"));
			encSHAKey = DatatypeConverter.printHexBinary(hash);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encSHAKey;
	}

	/**
	 * to get seconds between current time and midnight
	 * 
	 * @return
	 */
	public static long getExpiryInMilleSeconds() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		long milliSeconds = cal.getTimeInMillis();
		return milliSeconds;
	}
}
