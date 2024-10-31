package com.diloso.bookhair.app.datastore.data;


/**
 * Object stored in DataStore
 * 
 * @author Alejandro Aranda
 * 
 * @param <I>
 */
public interface IStorable<I> {

    /**
     * Object ID
     * 
     * @param id
     */
    void setId(I id);

    /**
     * @return object ID
     */
    I getId();
}
