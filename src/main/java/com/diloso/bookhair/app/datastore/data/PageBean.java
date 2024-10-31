package com.diloso.bookhair.app.datastore.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Object used for pagination
 * 
 * @author Alejandro Aranda
 * 
 */
@JsonInclude(Include.NON_NULL)
public class PageBean {

    private List<? extends IStorable<?>> data;

    private String next;

    /**
     * @return the data
     */
    public List<? extends IStorable<?>> getData() {
        return this.data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(List<? extends IStorable<?>> data) {
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
