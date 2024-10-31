package com.diloso.bookhair.app.datastore.search;

import com.google.appengine.api.search.Cursor;
import com.google.appengine.api.search.QueryOptions;

/**
 * Builds Query for text search
 * 
 * @author aaranda
 * 
 */
public interface IQueryHandler {

    /**
     * @return query
     */
    String getQuery(String query);

    /**
     * @param limit
     * @return QueryOptions with given limit and aditional options
     */
    QueryOptions getQueryOptions(int limit);

    /**
     * Hace la llamada que obtiene 20 resultados (valor por defecto)
     * 
     * @param
     * @return
     */
    QueryOptions getQueryOptions();

    /**
     * @param limit
     * @return QueryOptions with given limit and aditional options
     */
    QueryOptions getQueryOptions(Cursor cursor);
}
