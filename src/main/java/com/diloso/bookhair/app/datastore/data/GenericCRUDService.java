package com.diloso.bookhair.app.datastore.data;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.diloso.bookhair.app.datastore.UtilidadesData;
import com.google.appengine.api.datastore.Cursor;

/**
 * Implements CRUD operations for a given type using a service Can be extended
 * to add more operations as needed
 *
 * @author Alejandro Aranda
 *
 * @param <T>
 */
public abstract class GenericCRUDService<I, T extends IStorable<I>> implements
        ICRUDService<I, T> {

    private final Class<T> dataClass;
    
    private boolean cacheEnable;

    /**
     * Constructor
     */
    public GenericCRUDService() {
        this.dataClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.cacheEnable = true;
    }

    @Override
    public List<T> list() {
        return getService().list(this.dataClass, false);
    }

    @Override
    public List<T> listOrder(List<String> orders) {
        return getService().listOrder(this.dataClass, false, orders);
    }

    @Override
    public List<T> listFilter(Map<String, Object> filters) {
        return getService().filter(this.dataClass, false, filters);
    }

    @Override
    public List<T> listOrderFilter(Map<String, Object> filters,
            List<String> orders) {
        return getService().filterOrder(this.dataClass, false, filters, orders);
    }

    @Override
    public PageBean page(String from, int itemsPerPage) {
        return getService().page(this.dataClass, false, from, itemsPerPage);
    }

    @Override
    public PageBean pagedOrder(String from, int itemsPerPage,
            List<String> orders) {
        return getService().pagedOrder(this.dataClass, false, from,
                itemsPerPage, orders);
    }

    @Override
    public PageBean pagedFilter(String from, int itemsPerPage,
            Map<String, Object> filters) {
        return getService().pagedFilter(this.dataClass, false, from,
                itemsPerPage, filters);
    }

    @Override
    public PageBean pagedOrderFilter(String from,
            int itemsPerPage, Map<String, Object> filters, List<String> orders) {
        return getService().pagedFilterOrder(this.dataClass, false, from,
                itemsPerPage, filters, orders);
    }

    @Override
    public PageKeyBean<T> pagedOnlyKeys(Cursor cursor, int itemsPerPage, Map<String, Object> filters, List<String> orders) {
        return getService().pagedOnlyKeys(this.dataClass, cursor, itemsPerPage, filters);
    }

    @Override
    public T update(T object) {
        setTimestamps(object, UtilidadesData.now());
        getService().update(object);
        return object;
    }

    /**
     * Set timestamps if object has creation or modification date
     *
     * @param object
     * @param now
     */
    protected final void setTimestamps(T object, DateTime now) {
        // Set modification timestamp if object has that information
        if (object instanceof IObjectWithModificationTimestamp) {
            ((IObjectWithModificationTimestamp) object)
                    .setModificationTimestamp(now);
        }
        if (object instanceof IObjectWithCreationTimestamp) {
            // Set creation timestamp if object has that information (and we are
            // creating it)
            if (((IObjectWithCreationTimestamp) object).getCreationTimestamp() == null) {
                ((IObjectWithCreationTimestamp) object)
                        .setCreationTimestamp(now);
            }
        }
    }

    @Override
    public List<T> updateAll(List<T> objects) {
        DateTime now = UtilidadesData.now();
        for (T o : objects) {
            setTimestamps(o, now);
        }
        getService().updateAll(objects);
        return objects;
    }

    @Override
    public List<T> createAll(List<T> objects) {
        DateTime now = UtilidadesData.now();
        for (T o : objects) {
            setTimestamps(o, now);
        }
        getService().createAll(objects);
        return objects;
    }

    @Override
    public void deleteAll() {
        getService().deleteAll(this.dataClass);
    }

    @Override
    public void deleteAll(List<T> objects) {
        getService().delete(this.dataClass, objects);
    }

    protected ObjectifyService getService() {
        return ObjectifyService.getService(cacheEnable);
    }

    protected Class<T> getDataClass() {
        return this.dataClass;
    }

    @Override
    public Object formatForFilter(String name, String value) {
        return value;
    }

    @Override
    public int count(Map<String, Object> filters) {
        return getService().count(this.dataClass, false, filters);
    }

    @Override
    public int count() {
        return getService().count(this.dataClass, false);
    }
    
    @Override
    public void clearCache() {
        getService().clear();
    }
    
     /**
     * @param cacheEnable
     *            the cacheEnable to set
     */
    public void setCacheEnable(boolean cacheEnable) {
        this.cacheEnable = cacheEnable;
    }
}
