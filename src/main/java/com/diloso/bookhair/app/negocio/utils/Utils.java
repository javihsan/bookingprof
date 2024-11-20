package com.diloso.bookhair.app.negocio.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import com.diloso.bookhair.app.controllers.CalendarController;

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
	
	public static String getStrCalendar(Calendar calendarGreg) {
		StringBuffer strBuffer = new StringBuffer(String.valueOf(calendarGreg.get(Calendar.YEAR)));
		strBuffer.append(CalendarController.CHAR_SEP_DATE);
		strBuffer.append(StringUtils.leftPad(String.valueOf(calendarGreg.get(Calendar.MONTH)+1), 2, "0"));
		strBuffer.append(CalendarController.CHAR_SEP_DATE);
		strBuffer.append(StringUtils.leftPad(String.valueOf(calendarGreg.get(Calendar.DAY_OF_MONTH)), 2, "0"));
		return strBuffer.toString();
	}
	
	public static boolean validateDate(String a, String m, String d) {

		boolean error = true;
		int anyo = Integer.parseInt(a);
		int mes = Integer.parseInt(m);
		int dia = Integer.parseInt(d);

		try {
			if ((anyo < 1900) || (anyo > 2050) || (mes < 1) || (mes > 12)
					|| (dia < 1) || (dia > 31))
				error = true;
			else if ((anyo % 4 != 0) && (mes == 2) && (dia > 28))
				error = true;
			else if ((((mes == 4) || (mes == 6) || (mes == 9) || (mes == 11)) && (dia > 30))
					|| ((mes == 2) && (dia > 29)))
				error = true;
			else
				error = false;

		} catch (Exception e) {
		}
		return error;
	}

	public static int countChar(String str, String car) {
		int res = 0;
		try {
			int s = 0;
			int e = 0;
			while ((e = str.indexOf(car, s)) >= 0) {
				res++;
				s = e + car.length();
			}
		} catch (Exception e) {
		}
		return res;
	}
}
