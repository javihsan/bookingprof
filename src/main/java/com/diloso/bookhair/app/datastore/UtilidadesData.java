package com.diloso.bookhair.app.datastore;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;


/**
 * Created by jpelaez on 15/1/18.
 */
public class UtilidadesData {

	 private static final DateTimeFormatter ISO = ISODateTimeFormat
	            .dateHourMinuteSecond();

	
    public static String printException(final String message, Exception e) {
        StringWriter error = new StringWriter();
        e.printStackTrace(new PrintWriter(error));
        return message + ": " + error;
    }

    public static int reintento(int attempt, Logger LOG) {
        final int MULTIPLO = 5000;
        LOG.warning("Timeout de DStore/TSearch. Intento "
                + attempt + " esperando " + attempt * MULTIPLO + "segundos");
        try {
            Thread.sleep(attempt * MULTIPLO);
        } catch (InterruptedException e1) {
            printException(e1.getMessage(), e1);
            Thread.currentThread().interrupt();
        }
        return attempt+1;
    }
    
    /**
     * @return current date and time
     */
    public static DateTime now() {
        return new DateTime();
    }

    /**
     * @param dateTime
     * @return date and time as an iso string
     */
    public static String toString(DateTime dateTime) {
        if (dateTime != null) {
            return ISO.print(dateTime);
        }
        return null;
    }

    /**
     * Devuelve un Datetime de un String tipo datetime --> 2015-04-08T12:10
     * 
     * @param strDate
     * @return
     */
    public static DateTime toDate(String strDate) {
        if (strDate != null) {
            return ISODateTimeFormat.dateTimeParser().parseDateTime(strDate);
        }
        return null;
    }
    
    /**
     * Cuenta los dias de diferencia entre una fecha y otra
     */
    public static int daysDifference(Date startDate, Date endDate){

		//milliseconds
		long different = endDate.getTime() - startDate.getTime();

		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;

		long elapsedDays = different / daysInMilli;

//		different = different % daysInMilli;
//		long elapsedHours = different / hoursInMilli;

//		different = different % hoursInMilli;
//		long elapsedMinutes = different / minutesInMilli;

//		different = different % minutesInMilli;
//		long elapsedSeconds = different / secondsInMilli;

		return (int)elapsedDays;
	}
}
