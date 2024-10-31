package com.diloso.bookhair.app.datastore.search;

import com.google.appengine.api.search.Document;

/**
 * Genera un bean a partir de un documento indexado
 * 
 * @author aaranda
 * 
 */
public interface IDocumentToBeanMapper<T> {

    /**
     * @param document
     * @return objeto creado
     */
    T create(Document document);
}
