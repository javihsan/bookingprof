package com.diloso.bookhair.app.datastore.data;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Storable básico incluyendo creación
 * 
 * @author aaranda
 * 
 * @param <T>
 */
public abstract class StorableWithCreationTimestamp<T> implements IStorable<T>,
        IObjectWithCreationTimestamp {

    @JsonIgnore
    private DateTime creationTimestamp;

    @Override
    public DateTime getCreationTimestamp() {
        return this.creationTimestamp;
    }

    @Override
    public void setCreationTimestamp(DateTime dateTime) {
        this.creationTimestamp = dateTime;
    }
}
