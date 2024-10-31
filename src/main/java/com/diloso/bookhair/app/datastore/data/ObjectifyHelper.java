package com.diloso.bookhair.app.datastore.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.Ref;

/**
 * Objectify Utilities
 * 
 * @author Alejandro Aranda
 * 
 */
public class ObjectifyHelper {

    /**
     * Private constructor
     */
    private ObjectifyHelper() {

    }

    /**
     * Transforms a list of references to a list of objects
     * 
     * @param listOfReferences
     * @return list of objects retrieved
     */
    public static <T> List<T> resolveReferences(
            Collection<Ref<T>> listOfReferences) {
        List<T> list = new ArrayList<T>();
        if (listOfReferences != null) {
            for (Ref<T> ref : listOfReferences) {
                T value = resolveReference(ref);
                if (value != null) {
                    list.add(value);
                }
            }
        }
        return list;
    }

    /**
     * Returns object given a reference
     * 
     * @param reference
     * @return object referenced
     */
    public static <T> T resolveReference(Ref<T> reference) {
        if (reference != null) {
            return reference.getValue();
        }
        return null;
    }

    /**
     * Transforms a list of objects in a list of references
     * 
     * @param listOfObjects
     * @return list of references
     */
    public static <T> List<Ref<T>> createReferences(List<T> listOfObjects) {
        List<Ref<T>> listOfReferences = new ArrayList<Ref<T>>();
        if (listOfObjects != null) {
            for (T object : listOfObjects) {
                Ref<T> ref = createReference(object);
                if (ref != null) {
                    listOfReferences.add(ref);
                }
            }
        }
        return listOfReferences;
    }

    /**
     * Creates a reference to an object
     * 
     * @param object
     * @return reference
     */
    public static <T> Ref<T> createReference(T object) {
        if (object != null) {
            return Ref.create(object);
        }
        return null;
    }

    /**
     * @param list
     * @return list of ids
     */
    public static <T extends IStorable<Long>> List<Long> getLongIds(List<T> list) {
        List<Long> ids = new ArrayList<Long>();
        if (list != null) {
            for (IStorable<Long> item : list) {
                ids.add(item.getId());
            }
        }
        return ids;
    }

    /**
     * @param list
     * @return list of ids
     */
    public static <T extends IStorable<String>> List<String> getStringIds(
            List<T> list) {
        List<String> ids = new ArrayList<String>();
        if (list != null) {
            for (IStorable<String> item : list) {
                ids.add(item.getId());
            }
        }
        return ids;
    }

}
