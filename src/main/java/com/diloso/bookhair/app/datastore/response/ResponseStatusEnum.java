package com.diloso.bookhair.app.datastore.response;

/**
 * Created by jpelaez on 12/7/17.
 */
public enum ResponseStatusEnum {
    /**
     * Nombre de los campos.
     */
    OK("0"),
    KO("1"),
    INFO("2");

    /**
     * Nombre del campo.
     */
    private String value;

    /**
     * Constructor por defecto.
     *
     * @param value
     *            Nombre del campo.
     */
    private ResponseStatusEnum(final String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public final String getValue() {
        return value;
    }
}

