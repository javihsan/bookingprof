package com.diloso.bookhair.web.negocio.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	
	public static String textoInxistente = "No existen datos";

	public static String formatoFecha = "'DD/MM/YYYY'";
	public static String formatoFechaHora = "'DD/MM/YYYY HH24:MI'";
	public static String formatoFechaHoraMi = "'HH24:MI'";
	public static String formatoFechaJava = "dd/MM/yyyy";
	public static String formatoFechaHoraJava = "dd/MM/yyyy HH:mm";
	public static String formatoFechaHHJava = "HH";
	public static String formatoFechaMMJava = "mm";
	public static String formatoFechaBBDD = "dd'-'MMM'-'yyyy";

	/** Da formato a una fecha para que sea aceptada por la base de datos. **/
	public static String getCurrentDate(String formato) {
		String resultado = "";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(formato);
			Date currentTime_1 = new Date();
			resultado = formatter.format(currentTime_1);
		} catch (Exception e) {
		}
		return resultado;
	}

	/** Da formato a una fecha para que sea aceptada por la base de datos. **/
	public static String getCurrentDate() {
		return getCurrentDate(formatoFechaJava);
	}

	/** Da formato a una fecha para que sea aceptada por la base de datos. **/
	public static String getCurrentDateHora() {
		return getCurrentDate(formatoFechaHoraJava);
	}

	/** Da formato a una fecha para que sea aceptada por la base de datos. **/
	public static String getCurrentDateBBDD() {
		return getCurrentDate(formatoFechaBBDD);
	}
	
	/** Da formato a una fecha para que sea aceptada por la base de datos. **/
	@SuppressWarnings("deprecation")
	public static String getFormat(String fecha, String formato) {
		return getFormat(new Date(fecha), formato);
	}

	/** Da formato a una fecha para que sea aceptada por la base de datos. **/
	public static String getFormat(String fecha) {
		return getFormat(fecha, formatoFechaJava);
	}

	public static String getFormatHora(String fecha) {
		return getFormat(fecha, formatoFechaHoraJava);
	}

	/** Da formato a una fecha para que sea aceptada por la base de datos. **/
	public static String getFormat(Date fecha, String formato) {
		String resultado = "";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(formato);
			resultado = formatter.format(fecha);
		} catch (Exception e) {
		}
		return resultado;
	}

	/** Da formato a una fecha para que sea aceptada por la base de datos. **/
	public static String getFormat(Date fecha) {
		return getFormat(fecha, formatoFechaJava);
	}

	public static String getFormatHora(Date fecha) {
		return getFormat(fecha, formatoFechaHoraJava);
	}

	public static Date getDate(String fecha, String formato) {
		Date resultado = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(formato);
			resultado = formatter.parse(fecha);
		} catch (Exception e) {

		}
		return resultado;
	}

	/** Obtiene una fecha Date de un String **/
	public static Date getDate(String fecha) {
		return getDate(fecha, formatoFechaJava);
	}

	public static Date getDateHora(String fecha) {
		return getDate(fecha, formatoFechaHoraJava);
	}

	public static boolean validaFecha(String fecha) {
		boolean error = true;
		try {
			final String caracter = "/";
			int posicionA = 0;
			int posicionD = fecha.indexOf(caracter);
			String d = fecha.substring(posicionA, posicionD);
			posicionA = posicionD + 1;
			posicionD = fecha.indexOf(caracter, posicionA);
			String m = fecha.substring(posicionA, posicionD);
			posicionA = posicionD + 1;
			String a = fecha.substring(posicionA);
			error = validaFecha(a, m, d);
			if (!error) {
				Date fechaTime = getDate(fecha);
				String fechasys = getCurrentDate();
				Date fechaHoy = getDate(fechasys);
				error = fechaTime.before(fechaHoy);
			}
		} catch (Exception e) {
		}
		return error;
	}

	public static boolean validaFecha(String a, String m, String d) {

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

	public static int cuentaCaracter(String str, String car) {
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

	public static String rellenarCadena(String cadena, String carRelleno,
			int num) {
		String resultado = "";
		try {
			resultado = cadena;
			for (int i = cadena.length(); i < num; i++) {
				resultado = carRelleno + resultado;
			}
		} catch (Exception e) {
		}
		return resultado;
	}

}
