package org.fossa.goods4refugees.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	
	public static String showDateStringGerman(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
		if (date != null) {
			return dateFormat.format(date);
		} else {
			return "";
		}		
	}
	
	public static String showDateStringSAP(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (date != null) {
			return dateFormat.format(date);
		} else {
			return " - ";
		}		
	}
	
	public static Date parseGermanDate(String dateString) {
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date cutTimeOffDate(Date date) {
		if (date==null) {
			return null;
		}
		Calendar gc = GregorianCalendar.getInstance();
		gc.setTime(date);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);			
		date.setTime(gc.getTimeInMillis());
		return date;			
	}

	/**
	 * Warning: no leap years included!
	 */
	public static boolean datesWithinRangeDaysRoughCalculation(Date date1, Date date2, int days) {
		Calendar earlier = GregorianCalendar.getInstance();
		Calendar later = GregorianCalendar.getInstance();
		if (date1.compareTo(date2) < 0) {
			earlier.setTime(date1);
			later.setTime(date2);
		} else {
			earlier.setTime(date2);
			later.setTime(date1);
		}
		int days1 = earlier.get(Calendar.DAY_OF_YEAR);
		int days2 = later.get(Calendar.DAY_OF_YEAR);

		days2 = days2 +  (365 * (later.get(Calendar.YEAR)- earlier.get(Calendar.YEAR))); 
		return days >= (days2 - days1);
	}
	
	public static float getDifferenceInHours(Time von, Time bis) {
		if (von == null || bis == null) {
			return 0.0f;
		}
		long diff = bis.getTime() - von.getTime();
		long diffMinutes = diff / (60 * 1000);
		
		return (diffMinutes/60.00f);
	}		
}
