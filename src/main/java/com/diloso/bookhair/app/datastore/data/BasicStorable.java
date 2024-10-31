package com.diloso.bookhair.app.datastore.data;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Storable básico incluyendo tanto timestamp de creación como de modificación
 * 
 * @author aaranda
 * 
 * @param <T>
 */
public abstract class BasicStorable<T> implements IStorable<T>,
        IObjectWithCreationTimestamp, IObjectWithModificationTimestamp {

    @JsonIgnore
    private DateTime creationTimestamp;

    @JsonIgnore
    private DateTime modificationTimestamp;

    @Override
    public void setCreationTimestamp(DateTime dateTime) {
        this.creationTimestamp = dateTime;
    }

    @Override
    public DateTime getCreationTimestamp() {
        return this.creationTimestamp;
    }

    @Override
    public void setModificationTimestamp(DateTime dateTime) {
        this.modificationTimestamp = dateTime;
    }

    @Override
    public DateTime getModificationTimestamp() {
        return this.modificationTimestamp;
    }
}
