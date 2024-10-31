package com.diloso.bookhair.app.datastore.response;

/**
 * Created by jpelaez on 12/7/17.
 */
public class Parameter {
    String name;
    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
