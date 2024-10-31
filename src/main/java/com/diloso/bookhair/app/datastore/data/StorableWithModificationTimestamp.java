package com.diloso.bookhair.app.datastore.data;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Storable básico incluyendo modificación
 * 
 * @author aaranda
 * 
 * @param <T>
 */
public abstract class StorableWithModificationTimestamp<T> implements
        IStorable<T>, IObjectWithModificationTimestamp {

    @JsonIgnore
    private DateTime modificationTimestamp;

    @Override
    public void setModificationTimestamp(DateTime dateTime) {
        this.modificationTimestamp = dateTime;
    }

    @Override
    public DateTime getModificationTimestamp() {
        return this.modificationTimestamp;
    }
}
