package com.diloso.bookhair.app.datastore;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.base.Splitter;

/**
 * Utilidades para String
 * 
 * @author Alejandro Aranda
 * 
 */
public final class StringUtils {

    private StringUtils() {
    }

    /**
     * Devuelve una lista de long
     * 
     * @param list
     * @return
     */
    public static List<Long> getListOfLong(String list) {
        if (list != null) {
            String[] array = list.split(",");
            if (array != null && array.length > 0) {
                List<Long> listOfIds = new ArrayList<Long>();
                for (String id : array) {
                    try {
                        listOfIds.add(Long.parseLong(id));
                    } catch (NumberFormatException e) {
                        Logger.getLogger(StringUtils.class.getName())
                                .warning(e.getMessage());
                        return new ArrayList<Long>();
                    }
                }
                return listOfIds;
            }
        }
        return new ArrayList<Long>();
    }

    /**
     * @param separator
     * @param objects
     * @return cadena uniendo los objetos con el separador
     */
    public static String join(String separator, List<String> objects) {
        StringBuilder sb = new StringBuilder();
        if (separator != null && objects != null && !objects.isEmpty()) {
            for (int i = 0; i < objects.size() - 1; i++) {
                sb.append(objects.get(i));
                sb.append(separator);
            }
            sb.append(objects.get(objects.size() - 1));
        }
        return sb.toString();
    }

    /**
     * @param separator
     * @param objects
     * @return cadena uniendo los objetos con el separador
     */
    public static String joinForUrl(String separator, List<String> objects) {
        StringBuilder sb = new StringBuilder();
        if (separator != null && objects != null && !objects.isEmpty()) {
            for (int i = 0; i < objects.size() - 1; i++) {
                if (i == 0) {
                    sb.append(objects.get(i));
                    sb.append("?");
                } else {
                    sb.append(objects.get(i));
                    sb.append(separator);
                }
            }
            sb.append(objects.get(objects.size() - 1));
        }
        return sb.toString();
    }

    /**
     * Devuelve un String con la concatenación de la representación de String de
     * los objetos pasados
     * 
     * @param objects
     * @return
     */
    public static String getString(Object... objects) {
        final StringBuilder sb = new StringBuilder();
        if (objects != null) {
            for (Object o : objects) {
                if (o != null) {
                    sb.append(o.toString());
                }
            }
        }
        return sb.toString();
    }

    /**
     * Devuelve true si la cadena es nula o vacía
     * 
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Devuelve true si ninguna de las cadenas está vacía
     * 
     * @param strings
     * @return
     */
    public static boolean notEmpty(String... strings) {
        for (String s : strings) {
            if (isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Remplaza la última occurrencia encontrada de la cadena introducida.
     * 
     * @param string
     *            cadena inicial
     * @param toReplace
     *            cadena a remplazar
     * @param replacement
     *            cadena por la que se remplaza
     * @return cadena inicial con el remplazo.
     */
    public static String replaceLast(final String string,
            final String toReplace, final String replacement) {
        final int pos = string.lastIndexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos) + replacement + string
                    .substring(pos + toReplace.length(), string.length());
        } else {
            return string;
        }
    }

    /**
     * Obtiene una lista de String a partir de una cadena separada por un
     * separador. Si no contiene el separador entonces devuelve
     * <code>null</code>.
     * 
     * @param string
     *            cadena para listar
     * @param separator
     *            separador
     * @return lista de String
     */
    public static List<String> getListByStringAndSeparator(final String string,
            final String separator) {
        if (string != null) {
            return Arrays.asList(string.split(separator));
        } else {
            return new ArrayList<String>();
        }
    }

    /**
     * Obtiene una lista de String a partir de una cadena separada por un
     * separador permitiendo quitar los espacios iniciales y finales. Si no
     * contiene el separador entonces devuelve <code>null</code>.
     * 
     * @param string
     *            cadena para listar
     * @param separator
     *            separador
     * @return lista de String
     */
    public static List<String> getTrimmedListByStringAndSeparator(
            final String string, final String separator, boolean trim) {

        if (string != null) {
            List<String> values = Arrays.asList(string.split(separator));
            if (trim) {
                List<String> newValues = new ArrayList<String>();
                for (String value : values) {
                    if (value != null) {
                        value = value.trim();
                    }
                    newValues.add(value);
                }
                return newValues;
            } else {
                return values;
            }
        } else {
            return new ArrayList<String>();
        }
    }

    /**
     * Devuelve una lista de string
     * 
     * @param list
     * @return
     */
    public static List<String> getListOfString(String list) {
        if (list != null) {
            String[] array = list.split(",");
            if (array != null && array.length > 0) {
                List<String> listOfIds = new ArrayList<String>();
                for (String id : array) {
                    listOfIds.add(id);
                }
                return listOfIds;
            }
        }
        return new ArrayList<String>();
    }

    /**
     * Get string or empty string if null
     * 
     * @param str
     * @return
     */
    public static String getStringOrEmptyString(String str) {
        if (str != null) {
            return str;
        }
        return "";
    }

    /**
     * @param value
     * @return valor o null si es vacía
     */
    public static String getStringIfNotEmpty(Object value) {
        if (value instanceof String && !"".equals(((String) value).trim())) {
            return (String) value;
        }
        return null;
    }

    /**
     * @param minSize
     * @param phrase
     * @return string con las palabras de la frase que se pasa como parámetro
     *         partidas en todas las posibles subcadenas de al menos la longitud
     *         indicada
     */
    public static String generatePhraseTokens(int minSize, String phrase) {
        if (minSize <= 0) {
            throw new IllegalArgumentException("minSize = " + minSize);
        }
        List<String> list = new ArrayList<String>();
        if (!isEmpty(phrase)) {
            String[] tokens = phrase.replaceAll(" +", " ").trim().split(" ");
            for (String token : tokens) {
                list.addAll(generateWordTokens(minSize, token));
            }
        }
        return join(" ", list);
    }

    /**
     * @param minSize
     * @param word
     * @return string con la palabra que se pasa como parámetro partida en todas
     *         las posibles subcadenas de al menos la longitud indicada
     */
    private static List<String> generateWordTokens(int minSize, String word) {
        List<String> list = new ArrayList<String>();
        if (!isEmpty(word)) {
            for (int i = minSize; i < word.length(); i++) {
                list.add(word.substring(0, i));
            }
            list.add(word);
        }
        return list;
    }

    /**
     * @param minSize
     * @param texto
     * @return string con la palabra que se pasa como parámetro partida en todas
     *         las posibles subcadenas de al menos la longitud indicada y de
     *         todos los tamaños posibles (como máximo la longitud de la cadena
     *         original) separados por espacios
     * @Ejemplo: Para una cadena de entrada "Lorem ipsum" y un minSize = 1
     *           tendremos un resultado como
     *           "L o r e m i p s u m Lo or re em ip ps su um Lor ore rem ips psu sum Lore ...... Lorem ipsum"
     */
    public static String getStringTokenized(int minSize, String texto) {
        if (minSize <= 0) {
            throw new IllegalArgumentException("minSize = " + minSize);
        }
        List<String> list = new ArrayList<>();
        if (!isEmpty(texto)) {
            String[] tokens = texto.trim().split(" ");
            for (String token : tokens) {
                for (int i = minSize; i <= token.length(); i++) {
                    list.addAll(Splitter.fixedLength(i).splitToList(token));
                }
            }
        }
        return join(" ", list);
    }

    /**
     * Devuelve el texto tokenizado para poder buscar no solo por "comienza
     * por..." sino por "contiene..."
     *
     *  @param minSize
     * @param texto
     * @param set - Enviar vacío. Se va informando por referencia con los tokens generados
     */
    public static void getStringTokenized(int minSize, String texto, Set<String> set) {
        if (minSize <= 0) {
            throw new IllegalArgumentException("minSize = " + minSize);
        } else {
            String parametro = texto;

            if (!StringUtils.isEmpty(parametro)) {
                String[] tokens = parametro.trim().split(" ");
                for (String token : tokens) {
                    for (int i = minSize; i <= token.length(); i++) {
                        set.add(token.substring(0, i));
                    }
                }

                parametro = parametro.substring(1, parametro.length());
            }

            if (!StringUtils.isEmpty(parametro)) {
                getStringTokenized(minSize, parametro, set);
            }

        }
    }

    /**
     * Logs sin + --> LOG.info(getStringReemplazos(prueba {0}, primera))
     * @param cadena
     * @param params
     * @return
     */
    public static String getStringReemplazos(String cadena, Object ... params){
        return MessageFormat.format(cadena, params);
    }

}
