package in.codifi.auth.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.DatatypeConverter;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import in.codifi.auth.config.FilePropertiesConfig;
import in.codifi.auth.config.HazelcastConfig;
import in.codifi.auth.config.SMSPropertiesConfig;
import in.codifi.auth.controller.DefaultRestController;
import in.codifi.auth.entity.primary.TotpDetailsEntity;
import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.ws.model.kb.login.QuickAuthRespModel;
import io.quarkus.logging.Log;

@ApplicationScoped
public class AppUtils extends DefaultRestController {

	@Inject
	FilePropertiesConfig filePropsConfig;

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

	/**
	 * Method to encrypt input
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param input
	 * @return
	 */
	public String encryptWithSHA256(String input) {
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
	 * method to get user session
	 * 
	 * @author SowmiyaThangaraj
	 * @param userId
	 * @return
	 */
	public static String getUserSession(String userId) {
		String userSession = "";
		String hzUserSessionKey = userId + AppConstants.HAZEL_KEY_REST_SESSION;
		userSession = HazelcastConfig.getInstance().getRestUserSession().get(hzUserSessionKey);
		return userSession;
	}

	/**
	 * 
	 * Method to get client info
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public ClinetInfoModel getClientInfo() {
		ClinetInfoModel model = clientInfo();
		return model;
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
	public String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
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
	 * Method to send otp to Mobile Number
	 * 
	 * @author Dinesh Kumar
	 * @param otp
	 * @param mobile Number
	 * @return
	 */
	public boolean sendOtpToMobile(String otp, long mobileNumber, String message) {
		try {
			StringBuffer data = new StringBuffer();
			data.append("feedid=" + props.getSmsFeedId());
			data.append("&senderid=" + props.getSmsSenderId());
			data.append("&username=" + props.getSmsUserName());
			data.append("&password=" + props.getSmsPassword());
			data.append("&To=" + mobileNumber);
			String msg = "&Text=" + otp + message.replace(" ", "%20");
			data.append(msg);
			URL url = new URL(props.getSmsUrl() + data.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			StringBuffer buffer = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				buffer.append(line);
			}
			rd.close();
			conn.disconnect();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * Method to get cache key
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @param source
	 * @return
	 */
	public String getuserIdAndSourceKey(String userId, String source) {
		return userId + "_" + source;
	}

	/**
	 * 
	 * Method to generate scanner
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 */
	public TotpDetailsEntity createScanner(String userId) {
		TotpDetailsEntity entity = new TotpDetailsEntity();
		try {
			String secretKey = generateAlphaNumeric(32).toUpperCase();
			String barCodeUrl = getGoogleAuthenticatorBarCode(secretKey, userId, AppConstants.COMPANY_NAME);
			String qrCodePath = filePropsConfig.getQrCodePath();
			File qrCodeDirectory = new File(qrCodePath);
			if (!qrCodeDirectory.exists()) {
				qrCodeDirectory.mkdirs();
			}
			String filePath = qrCodePath + File.separator + secretKey + "_" + userId + ".png";
			String qrCode = createQRCode(barCodeUrl, filePath, 400, 400);
			if (StringUtil.isNotNullOrEmpty(qrCode)) {
				entity.setCompanyName(AppConstants.COMPANY_NAME);
				entity.setSecretKey(secretKey);
				entity.setImg(qrCode);
				entity.setUserId(userId);
				entity.setCreatedBy(userId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return entity;
	}

	/**
	 * Generate generate alpha numeric
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param size
	 * @return
	 */
	public static String generateAlphaNumeric(int size) {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(size);
		for (int i = 0; i < size; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		String num = sb.toString();
		return num;
	}

	private static String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) {
		try {
			return "otpauth://totp/" + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
					+ "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20") + "&issuer="
					+ URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	private String createQRCode(String barCodeData, String filePath, int height, int width)
			throws IOException, WriterException {
		String base64EncodedImageBytes = "";
		BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, width, height);
		try (FileOutputStream out = new FileOutputStream(filePath)) {
			MatrixToImageWriter.writeToStream(matrix, "png", out);
			Path pathToImage = Paths.get(filePath);
			// 1. Convert image to an array of bytes
			byte[] imageBytes = Files.readAllBytes(pathToImage);
			// 2. Encode image bytes[] to Base64 encoded String
			base64EncodedImageBytes = Base64.getEncoder().encodeToString(imageBytes);
			base64EncodedImageBytes = "data:image/png;base64," + base64EncodedImageBytes;
		}
		return base64EncodedImageBytes;
	}

	/**
	 * Method to validate give input is mobile number
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param input
	 * @return
	 */
	public boolean isMobileNumber(String input) {
		Pattern pattern = Pattern.compile("^\\d{10}$"); // Regular expression for a 10-digit mobile number
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}

	/**
	 * Method to validate give input is Email
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param input
	 * @return
	 */
	public boolean isEmail(String input) {
		Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"); // Regular expression for an email
																					// address
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}

	/**
	 * Method to validate give input is PAN number
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param input
	 * @return
	 */
	public boolean isPAN(String input) {
		Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}"); // Regular expression for a PAN number
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}

	/**
	 * method to get user information
	 * 
	 * @author SowmiyaThangaraj
	 * @param userId
	 * @return
	 */
	public QuickAuthRespModel getUserInfo(String userId) {
		QuickAuthRespModel authModel = new QuickAuthRespModel();
		String hzKey = userId + AppConstants.HAZEL_KEY_USER_DETAILS;
		authModel = HazelcastConfig.getInstance().getUserSessionDetails().get(hzKey);
		return authModel;
	}

}
