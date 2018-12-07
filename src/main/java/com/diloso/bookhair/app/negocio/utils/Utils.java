package com.diloso.bookhair.app.negocio.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {
	
	public static String formatDateICS = "yyyyMMdd'T'HHmmss";
	public static String formatDateText = "EEEE, dd MMMM yyyy HH:mm";
	public static String formatDateTextNoHour = "EEEE, dd MMMM yyyy";
	public static String formatDateJava = "yyyy-MM-dd";
	public static String formatoFechaYearLast = "dd-MM-yyyy";

	public static String getFormat(Date date, String format, Locale locale) {
		return getFormat(date, format, locale, null);
	}
	
	public static String getFormat(Date date, String format, Locale locale, TimeZone timeZone) {
		String result = "";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format, locale);
			if (timeZone!=null){
				formatter.setTimeZone(timeZone);
			}	
			date =  new Date(date.getTime() - timeZone.getOffset(date.getTime()));
			result = formatter.format(date);
		} catch (Exception e) {
		}
		return result;
	}
	
	public static String getFormatICS(Date date, Locale locale, TimeZone timeZone) {
		return getFormat(date, formatDateICS, locale, timeZone);
	}
	
	public static String getFormatText(Date date, Locale locale, TimeZone timeZone) {
		return getFormat(date, formatDateText, locale, timeZone);
	}
	
	public static String getFormatTextNoHour(Date date, Locale locale, TimeZone timeZone) {
		return getFormat(date, formatDateTextNoHour, locale, timeZone);
	}	
	
	public static Date getDate(String date, String format, Locale locale) {
		Date result = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format, locale);
			result = formatter.parse(date);
		} catch (Exception e) {

		}
		return result;
	}
	
	public static Date getDate(String date, Locale locale) {
		return getDate(date, formatDateJava, locale);
	}
	
	public static Date getDateYearLast(String fecha, Locale locale) {
		return getDate(fecha, formatoFechaYearLast, locale);
	}
}
