package in.codifi.basket.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class StringUtil {
	public static String checkObjectNull(String object) {
		String outPut = "";
		if (object != null) {
			outPut = object;
		}
		return outPut;
	}

	/**
	 * Method to check String is Not null or empty
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNotNullOrEmpty(String str) {
		return !isNullOrEmpty(str);
	}

	public static boolean isNotNullOrEmpty(Long num) {
		if (num != null) {
			return true;
		}
		return false;
	}

	/**
	 * Method to check String is null or empty
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str) {
		if (str != null && str.trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Method to check two String is not equal
	 *
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isNotEqual(String str1, String str2) {
		return !isEqual(str1, str2);
	}

	/**
	 * Method to check two String is equal
	 *
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isEqual(String str1, String str2) {
		boolean isEqual = false;
		if (str1 != null && str2 != null && str1.equalsIgnoreCase(str2)) {
			isEqual = true;
		}
		return isEqual;
	}

	/**
	 * Method to check list is null or empty
	 *
	 * @param list
	 * @return
	 */
	public static boolean isListNullOrEmpty(@SuppressWarnings("rawtypes") List list) {
		boolean isNullOrEmpty = false;
		if (list == null || list.isEmpty()) {
			isNullOrEmpty = true;
		}
		return isNullOrEmpty;
	}

	/**
	 * Method to check list is not null or empty
	 *
	 * @param list
	 * @return
	 */
	public static boolean isListNotNullOrEmpty(@SuppressWarnings("rawtypes") List list) {
		return !isListNullOrEmpty(list);
	}

	/**
	 * Method to split non-empty string by key
	 *
	 * @param str
	 * @param separator
	 * @return
	 */
	public static List<String> splitOnlyNonEmpty(String str, String separator) {
		List<String> values = new ArrayList<String>();
		String strArray[] = split(str, separator);
		if (strArray != null) {
			for (String val : strArray) {
				if (StringUtil.isNotNullOrEmpty(val)) {
					values.add(val);
				}
			}
		}
		return values;
	}

	/**
	 * Method to split string by key
	 *
	 * @param str
	 * @param separator
	 * @return
	 */
	public static String[] split(String str, String separator) {
		String strArray[] = null;
		if (isNotNullOrEmpty(str)) {
			strArray = str.split(separator);
		}
		return strArray;
	}

	public static String convertConditionsListToString(List<String> conditions, String condition) {
		String stringConditions = "";
		if (conditions != null && conditions.size() > 0) {
			int size = conditions.size();
			for (int i = 0; i < size; i++) {
				stringConditions += conditions.get(i);
				if ((i + 1) < size) {
					stringConditions = stringConditions + condition;
				}
			}
		}
		return stringConditions;
	}

	public static boolean stringContainsWord(String str, String strToFind) {
		boolean isContains = false;
		if (str.contains(strToFind)) {
			isContains = true;
		}
		return isContains;
	}

	public static String convertTimeByZone(String date, String timeZone) {
		String parsedDate = "";
		if (isNotNullOrEmpty(date)) {
			if (date.length() == 17) {
				date = date.replace("Z", ":00Z");
			}
			try {
				TimeZone utc = TimeZone.getTimeZone("UTC");
				SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
				sourceFormat.setTimeZone(utc);
				Date convertedDate = sourceFormat.parse(date.replaceAll("Z$", "+0000"));
				SimpleDateFormat sdfmad = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				if (isNullOrEmpty(timeZone)) {
					sdfmad.setTimeZone(TimeZone.getTimeZone("Asia/Dubai"));
				} else {
					sdfmad.setTimeZone(TimeZone.getTimeZone(timeZone));
				}
				parsedDate = sdfmad.format(convertedDate);
			} catch (Exception e) {
				e.printStackTrace();
				return date;
			}
		}
		return parsedDate;
	}

	public static String replaceChar(String messageParams) {
		String[] specialCharacters = new String[] { "?", "*", "@", "(", ")", "'", "!", "\"" };
		for (String specialChar : specialCharacters) {
			if (messageParams.contains(specialChar)) {
				messageParams = messageParams.replace(specialChar, "");
			}
		}
		return messageParams;
	}

	public static boolean restrictCharacter(String messageParams) {
		boolean charLength = true;
		if (messageParams.contains("sender_message_reference")) {
			String value = messageParams.substring(messageParams.lastIndexOf("sender_message_reference=") + 25);
			if (value.contains("&")) {
				value = value.substring(0, value.indexOf("&"));
			}
			if (value.length() > 50) {
				charLength = false;
			}
		}
		return charLength;
	}

}
