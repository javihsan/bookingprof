package com.diloso.bookhair.app.datastore.search;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.diloso.bookhair.app.datastore.StringUtils;
import com.google.api.client.util.Sets;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;

/**
 * Utilidad para crear documentos indexables
 *
 * Contiene utilidades para indexar subcadenas de palabras en campos de tipo
 * texto
 *
 * @author aaranda
 *
 */
public class DocumentBuilder  {

    /**
     * Número mínimo de caracteres de los tokens de búsqueda
     */
    private static final int SEARCH_TOKEN_MIN_SIZE = 1;

    private final Document.Builder builder;

    /**
     * @return nueva instancia
     */
    public static DocumentBuilder newBuilder() {
        return new DocumentBuilder();
    }

    /**
     * Constructor privado. Utilizar <code>newBuilder</code>
     */
    private DocumentBuilder() {
        this.builder = Document.newBuilder();
    }

    /**
     * Asigna el id al documento en construcción
     *
     * @param id
     * @return
     */
    public DocumentBuilder setId(String id) {
        this.builder.setId(id);
        return this;
    }

    /**
     * Añade un atributo de tipo numérico
     *
     * @param name
     * @param value
     * @return
     */
    public DocumentBuilder addNumber(String name, double value) {
        this.builder
                .addField(Field.newBuilder().setName(name).setNumber(value));
        return this;
    }

    /**
     * Añade un atributo de tipo texto
     *
     * @param name
     * @param value
     * @return
     */
    public DocumentBuilder addText(String name, String value) {
        this.builder.addField(Field.newBuilder().setName(name).setText(value));
        return this;
    }

    /**
     * Añade un atributo de tipo texto "tokenizado", es decir, se indexan cada
     * una de las subcadenas de cada palabra contadas desde el principio, con un
     * mínimo de <code>SEARCH_TOKEN_MIN_SIZE</code> letras
     *
     * @param name
     * @param value
     * @return
     */
    public DocumentBuilder addTokenizedText(String name, String value) {
        this.builder.addField(Field.newBuilder().setName(name).setText(value));
        this.builder.addField(Field
                .newBuilder()
                .setName(name + "_tokenized")
                .setText(
                        StringUtils.generatePhraseTokens(SEARCH_TOKEN_MIN_SIZE,
                                value)));
        return this;
    }

    /**
     * Añade un atributo de tipo Atom
     *
     * @param name
     * @param value
     * @return
     */
    public DocumentBuilder addAtom(String name, String value) {
        this.builder.addField(Field.newBuilder().setName(name).setAtom(value));
        return this;
    }

    /**
     * Añade un atributo de tipo java.util.Date
     *
     * @param name
     * @param value
     * @return
     */
    public DocumentBuilder addDate(String name, Date value) {
        this.builder.addField(Field.newBuilder().setName(name).setDate(value));
        return this;
    }

    /**
     * @return el documento construido
     */
    public Document build() {
        return this.builder.build();
    }

    /**
     * @return Añade posición en el ranking
     */
    public DocumentBuilder setRank(int rank) {
        this.builder.setRank(rank);
        return this;
    }

    /**
     * Añade un atributo de tipo texto para poder realizar búsquedas tipo LIKE
     *
     * @param name
     * @param value
     * @return
     */
    public DocumentBuilder addTextLike(String name, String value) {
        this.builder.addField(Field
                .newBuilder()
                .setName(name + "_tokenized")
                .setText(
                        StringUtils.getStringTokenized(SEARCH_TOKEN_MIN_SIZE,
                                value)));
        return this;
    }

    /**
     * Añade un atributo de tipo texto para poder realizar búsquedas tipo LIKE
     *
     * @param name
     * @param value
     * @return
     */
    public DocumentBuilder addTextContainsLike(String name, String value) {
        Set<String> setTokens = Sets.newHashSet();

        StringUtils.getStringTokenized(SEARCH_TOKEN_MIN_SIZE,
                value, setTokens);

        List<String> tokens = setTokens.stream().collect(Collectors.toList());

        this.builder.addField(Field
                .newBuilder()
                .setName(name + "_tokenized")
                .setText(StringUtils.join(" ", tokens)));
        return this;
    }
}
