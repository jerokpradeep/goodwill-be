package in.codifi.api.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author Pradeep Ravichandran
 *
 */
public class DateUtil {
	public static final String DDMMYYYY = "dd/MM/yyyy";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final DateFormat DATEFORMAT_YYYYMMDD = new SimpleDateFormat(YYYYMMDD);
	public static final DateFormat DATEFORMAT_DDMMYYYY = new SimpleDateFormat(DDMMYYYY);

	public static String parseDate(Date date) {
		String date2 = null;
		DateFormat formatter;
		formatter = new SimpleDateFormat("dd-MMM-yy");
		date2 = (String) formatter.format(date);

		return date2;
	}

	public static Calendar getCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static Date parseString(String dateString, DateFormat format) {
		Date date = null;
		if (StringUtil.isNotNullOrEmpty(dateString) && format != null) {
			try {
				date = (Date) format.parse(dateString);
			} catch (ParseException e) {
				System.out.println("ParseException :" + e);
			} catch (Exception e) {
				System.out.println("Exception :" + e);
			}
		}
		return date;
	}

	public static String formatDate(Date date, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return formatDateByformat(date, dateFormat);
	}

	public static String formatDateByformat(Date date, DateFormat dateFormat) {
		String dateStr = null;
		try {
			if (date != null) {
				dateStr = dateFormat.format(date);
			}
		} catch (Exception e) {
			return null;
		}
		return dateStr;
	}

	public static Date parseString(String dateString, String format) {
		Date date = null;
		SimpleDateFormat formatter = null;
		if (StringUtil.isNotNullOrEmpty(dateString)) {
			try {
				formatter = new SimpleDateFormat(format);
				date = (Date) formatter.parse(dateString);
			} catch (ParseException e) {
				System.out.println("Exception :" + e);
			}
		}
		return date;
	}

	public static Date getTodayDate() {
		String date = DATEFORMAT_DDMMYYYY.format(getNewDateWithCurrentTime());
		return parseString(date, DATEFORMAT_DDMMYYYY);
	}

	public static Date getNewDateWithCurrentTime() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

}
