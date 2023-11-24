package in.codifi.admin.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import in.codifi.admin.repository.ContractMasterDao;

@ApplicationScoped
public class AppUtils {

	@Inject
	ContractMasterDao entityManager;

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

	public static String generatealpanumericNew(int size) {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(size);
		for (int i = 0; i < size; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		String num = sb.toString();
		return num;
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

	public boolean isPan(String input) {
		Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}

//	public String preparepositionCountMail(String msg, String subject) {
//		String response = AppConstants.SUCCESS_STATUS;
//		String emails = "dinesh@codifi.in,gowrisankar@codifi.in,rgssankar007@gmail.com";
//		String[] emailIds = emails.split(",");
//		try {
//			String hs = "<!DOCTYPE html><html><head><style>*{font-family:'Open Sans',"
//					+ " Helvetica, Arial;color: #1e3465}table {margin-left:100px;font-family: arial, sans-serif;"
//					+ "border-collapse:collapse !important;}td, th {border: 1px solid #1e3465;text-align: left;padding: 8px;}"
//					+ "th{background :#1e3465;color:white;}</style></head><body><div>"
//					+ "<div  style='font-size:14px'><p>Dear Team,</p>"
//					+ " <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<h4> Check Position Average Price Count Difference, </h4> "
//					+ " </p><p>" + msg + "</p></div>" + "<div><p align='left'>" + "<b>Regards,"
//					+ "<br>Codifi.</b></p></div></div></body></html>";
//			/*
//			 * Set the sendor JSON
//			 */
//			JSONObject senderJson = new JSONObject();
//			senderJson.put("name", "Codifi Support");
//			senderJson.put("email", "hello@codifi.in");
//			/*
//			 * Set the TO JSON
//			 */
//			JSONArray toJsonArray = new JSONArray();
//			for (String email : emailIds) {
//				JSONObject toJson = new JSONObject();
//				toJson.put("name", "");
//				toJson.put("email", email);
//				toJsonArray.add(toJson);
//			}
//			JSONObject json = new JSONObject();
//			json.put("sender", senderJson);
//			json.put("to", toJsonArray);
//			json.put("subject", subject);
//			json.put("htmlContent", hs);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}

	public void loadTokenForHoldings() {
		entityManager.loadTokenForHoldings();
	}

	public void loadTokenForPosition() {
		entityManager.loadTokenForPosition();
	}

}
