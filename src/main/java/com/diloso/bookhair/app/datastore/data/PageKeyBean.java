package com.diloso.bookhair.app.datastore.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.googlecode.objectify.Key;

/**
 * Object used for pagination
 *
 * @author Alejandro Aranda
 *
 */
@JsonInclude(Include.NON_NULL)
public class PageKeyBean<T> {

    private List<Key<T>> data;

    private String next;

    /**
     * @return the data
     */
    public List<Key<T>> getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(List<Key<T>> data) {
        this.data = data;
    }

    /**
     * @return the next
     */
    public String getNext() {
        return this.next;
    }

    /**
     * @param next
     *            the next to set
     */
    public void setNext(String next) {
        this.next = next;
    }

}
