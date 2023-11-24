package in.codifi.common.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {

	public static boolean isNotNullOrEmptyAfterTrim(String str) {
		if (StringUtil.isNotNullOrEmpty(str) && str.trim().length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEqual(String str1, String str2) {
		boolean isEqual = false;
		if (str1 != null && str2 != null && str1.equalsIgnoreCase(str2)) {
			isEqual = true;
		}
		return isEqual;
	}

	public static boolean isNotNullOrEmpty(String str) {
		return !isNullOrEmpty(str);
	}

	public static boolean isNullOrEmpty(String str) {
		if (str != null && str.length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isNotEqual(String str1, String str2) {
		return !isEqual(str1, str2);
	}

	public static boolean isNullOrEqual(String str1, String str2) {
		boolean isNullOrEqual = false;
		if (str1 == null && str2 == null) {
			isNullOrEqual = true;
		} else {
			isNullOrEqual = isEqual(str1, str2);
		}
		return isNullOrEqual;
	}

	public static String[] split(String str, String seperator) {
		String strArray[] = null;
		if (isNotNullOrEmpty(str)) {
			strArray = str.split(seperator);
		}
		return strArray;
	}

	public static List<String> splitOnlyNonEmpty(String str, String seperator) {
		List<String> values = new ArrayList<String>();
		String strArray[] = split(str, seperator);
		if (strArray != null) {
			for (String val : strArray) {
				if (StringUtil.isNotNullOrEmpty(val)) {
					values.add(val);
				}
			}
		}
		return values;
	}

	public static String reverseString(String str) {
		return new StringBuffer(str).reverse().toString();
	}

	public static String replace(String str, String toBeReplaced, String toReplaceWith) {
		if (isNotNullOrEmpty(str) && isNotNullOrEmpty(toBeReplaced)) {
			str = str.replace(toBeReplaced, getString(toReplaceWith));
		}
		return str;
	}

	public static String getString(String str) {
		return StringUtil.isNullOrEmpty(str) ? "" : str;
	}

	public static String isImageReSizeExist(String imageFilepath, String application_id, String fileType) {
		for (int i = 0; i <= 1000; i++) {
			String fileName = i + "-" + application_id + fileType;
			File tmpDir = new File(imageFilepath + fileName);
			if (!tmpDir.exists()) {
				fileName = i + "-" + application_id + fileType;
				return fileName;
			}
		}
		return null;
	}

	public static boolean isStrContainsWithEqIgnoreCase(String strValue, String value) {
		boolean isContains = false;
		String value1 = "";
		String value2 = "";
		if (StringUtil.isNotNullOrEmpty(strValue) && StringUtil.isNotNullOrEmpty(value)) {
			value1 = strValue.toLowerCase();
			value2 = value.toLowerCase();
			if (value1.contains(value2)) {
				isContains = true;
			}
		}
		return isContains;
	}

	public static boolean isStringContainsSpecialChars(String name) {
		boolean isContains = false;
		char[] splChars = { '\'', '\"', '&', '%', ':' };

		for (int index = 0; index < splChars.length; index++) {
			if (!(name.indexOf(splChars[index]) == -1)) {
				isContains = true;
				break;
			}
		}
		return isContains;
	}

	public static String convertConditionsListToString(List<String> conditions) {
		String stringConditions = "";
		int size = conditions.size();
		for (int i = 0; i < size; i++) {
			stringConditions = stringConditions + " " + conditions.get(i) + " ";
			if ((i + 1) < size) {
				stringConditions = stringConditions + " and ";
			}
		}
		return stringConditions;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isListNullOrEmpty(List list) {
		boolean isNullOrEmpty = false;
		if (list == null || list.isEmpty()) {
			isNullOrEmpty = true;
		}
		return isNullOrEmpty;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isListNotNullOrEmpty(List list) {
		return !isListNullOrEmpty(list);
	}

	public static String getString(Integer value) {
		String str = "";
		if (value != null) {
			str = Integer.toString(value);
		}
		return str;
	}

	public static double getdDouble(String value) {
		double d = 0;
		if (StringUtil.isNotNullOrEmpty(value)) {
			value = StringUtil.replace(value, ",", "");
			try {
				Double dbl = new Double(value);
				d = dbl.doubleValue();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return d;
	}

	public static String getGroupByInSearch(List<String> values) {
		String searchText = "";
		int size = values.size();
		for (int i = 0; i < size; i++) {
			searchText += values.get(i);
			if ((i + 1) < size) {
				searchText = searchText + ",";
			}
		}
		return searchText;
	}

	public static String getOrderByColumn(String queryString, String orderByColumn) {
		if (queryString.toLowerCase().contains("group by")) {
			orderByColumn = "max(" + orderByColumn + ")";
		}
		return orderByColumn;
	}

	public static String getInSearch(List<String> values) {
		StringBuffer searchText = new StringBuffer();
		Object[] valuesArray = values.toArray();
		int count = 0;
		searchText.append("(");
		for (Object value : valuesArray) {
			if (count > 0) {
				searchText.append(",");
			}
			searchText.append("'" + (String) value + "'");
			count++;
		}
		searchText.append(")");
		return searchText.toString();
	}

	public static String getInSearchInt(List<Integer> values) {
		StringBuffer searchText = new StringBuffer();
		Object[] valuesArray = values.toArray();
		int count = 0;
		searchText.append("(");
		for (Object value : valuesArray) {
			if (count > 0) {
				searchText.append(",");
			}
			searchText.append("'" + value + "'");
			count++;
		}
		searchText.append(")");
		return searchText.toString();
	}

	public static String getLikeSearch(String searchText) {
		if (isNullOrEmpty(searchText)) {
			searchText = "";
		}
		return "%" + encryptDataForSQL(searchText) + "%";
	}

	public static String encryptDataForSQL(String value) {
		if (value.contains("'")) {
			value = replace(value, "'", "''");
		}
		return value;
	}
}
