package com.dlvn.mcustomerportal.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * @arthor nn.tai
 * @date Oct 20, 2016
 */
public class DateUtils {

	private static Pattern pattern;
	private static Matcher matcher;

	private static final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";

	private DateUtils() {
		pattern = Pattern.compile(DATE_PATTERN);
	}

	public static int getWeekYear(Long miliTime) {
		if (miliTime == null) {
			miliTime = getTime(new Date());
		}
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTimeInMillis(miliTime);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	public static int getWeekYear(Date date) {
		if (date == null) {
			date = new Date();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	public static Double getFirstDateOfYear(String year) throws ParseException {
		if (TextUtils.isEmpty(year)) {
			return null;
		}
		String date = "01-01-" + year;
		return (double) getTime(parseDate(date));
	}

	public static Double getLastDateOfYear(String year) throws ParseException {
		String date = "31-12-" + year;
		return (double) getTime(parseDate(date));
	}

	public static String formatDate(Date date) {

		if (date == null) {
			return null;
		}
		return android.text.format.DateFormat.format("dd/MM/yyyy", date).toString();
	}

	public static String formatDate(Double dbl) {

		if (dbl == null) {
			return null;
		}
		Date date = parseDate(dbl);
		return formatDate(date);
	}

	public static String formatDate(Long dbl) {

		if (dbl == null) {
			return null;
		}
		Date date = parseDate(dbl);
		return formatDate(date);
	}

	public static String formatTime(Date date) {

		if (date == null) {
			return null;
		}
		return android.text.format.DateFormat.format("kk:mm", date).toString();
	}

	public static String formatTime(Double dbl) {

		if (dbl == null) {
			return null;
		}
		Date date = parseDate(dbl);
		return formatTime(date);
	}

	public static String formatTime(Long dbl) {

		if (dbl == null) {
			return null;
		}
		Date date = parseDate(dbl);
		return formatTime(date);
	}

	public static String formatDateTime(Date date) {

		if (date == null) {
			return null;
		}
		return android.text.format.DateFormat.format("dd/MM/yyyy kk:mm", date).toString();
	}

	public static String formatyyyyMMddHHmmss(Date date) {

		if (date == null) {
			return null;
		}

		return android.text.format.DateFormat.format("yyyyMMddkkmmss", date).toString();
	}

	public static String formatDateTime(Double dbl) {

		if (dbl == null) {
			return null;
		}
		Date date = parseDate(dbl);
		return formatDateTime(date);
	}

	public static String formatDateYM(Date date) {

		if (date == null) {
			return null;
		}
		return android.text.format.DateFormat.format("MM-yyyy", date).toString();
	}

	public static Date parseDate(String str) throws java.text.ParseException {
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
			return format.parse(str);
		} catch (ParseException e) {
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * input a string date time, return a string date by format dd/MM/yyyy HH:mm:ss a
	 * @param str
	 * @return
	 * @throws java.text.ParseException
	 * @arthor nn.tai
	 * @date Nov 23, 2016 2:33:45 PM
	 */
	public static String parseDateTime(String str) throws java.text.ParseException {
		try {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa", Locale.US);
			Date newDate = new Date();
			newDate = format.parse(str.toLowerCase());

			SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a", Locale.US);
			return format2.format(newDate).toUpperCase();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * input a string date time, return a string date by format dd/MM/yyyy
	 * @param str
	 * @return
	 * @throws java.text.ParseException
	 * @arthor nn.tai
	 * @date Nov 23, 2016 2:33:14 PM
	 */
	public static String parseDateOnly(String str) throws java.text.ParseException {
		try {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa", Locale.US);
			Date newDate = format.parse(str.toLowerCase());

			format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
			return format.format(newDate).toUpperCase();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date parseTime(String str) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
			return format.parse(str);
		} catch (ParseException e) {
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date parseDate(Integer year, Integer month, Integer day) {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		return clearTime(cal);
	}

	public static Date parseDate(Double dbl) {
		if (dbl == null) {
			return null;
		}
		return parseDate(dbl.longValue());
	}

	public static Date parseDate(Long dbl) {
		if (dbl == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(dbl);
		return cal.getTime();
	}

	public static Date addDays(Date date, int amount) {

		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, amount);
		return cal.getTime();
	}

	public static Date getStartWeek(Long miliTime, Integer weekYear) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(miliTime);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.WEEK_OF_YEAR, weekYear);
		return cal.getTime();
	}

	public static Date clearTime(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return clearTime(cal);
	}

	private static Date clearTime(Calendar cal) {
		if (cal == null) {
			return null;
		}
		Date temp = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		temp = cal.getTime();
		return temp;
	}

	public static Date maxTime(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return maxTime(cal);
	}

	private static Date maxTime(Calendar cal) {
		if (cal == null) {
			return null;
		}
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	public static Date getEndWeek(Date stDate) {
		if (stDate == null) {
			return null;
		}
		return clearTime(addDays(stDate, 6));
	}

	public static Long getTime(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getTimeInMillis();
	}

	public static int getCurrentMonth() {

		Calendar cal = Calendar.getInstance();
		cal.setTime(cal.getTime());
		int month = cal.get(Calendar.MONTH);
		myLog.E("Month = " + (month + 1));
		return month + 1;
	}

	public static int getCurrentYear() {

		Calendar cal = Calendar.getInstance();
		cal.setTime(cal.getTime());
		int year = cal.get(Calendar.YEAR);
		myLog.E("Year = " + year);
		return year;
	}

	public static String getCurrentDate() {

		Calendar cal = Calendar.getInstance();
		String date = formatDate(cal.getTime());
		myLog.E("Date = " + date);
		return date;
	}

	public static boolean validate(final String date) {

		pattern = Pattern.compile(DATE_PATTERN);
		matcher = pattern.matcher(date);

		if (matcher.matches()) {

			matcher.reset();

			if (matcher.find()) {

				String day = matcher.group(1);
				String month = matcher.group(2);
				int year = Integer.parseInt(matcher.group(3));

				if (day.equals("31") && (month.equals("4") || month.equals("6") || month.equals("9")
						|| month.equals("11") || month.equals("04") || month.equals("06") || month.equals("09"))) {
					return false; // only 1,3,5,7,8,10,12 has 31 days
				} else if (month.equals("2") || month.equals("02")) {
					// leap year
					if (year % 4 == 0) {
						if (day.equals("30") || day.equals("31")) {
							return false;
						} else {
							return true;
						}
					} else {
						if (day.equals("29") || day.equals("30") || day.equals("31")) {
							return false;
						} else {
							return true;
						}
					}
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
