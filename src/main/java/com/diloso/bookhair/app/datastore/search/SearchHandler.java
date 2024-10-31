package com.diloso.bookhair.app.datastore.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.diloso.bookhair.app.datastore.data.IStorable;
import com.google.appengine.api.search.Cursor;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.GetResponse;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Gestiona diferentes opciones sobre un Ã­ndice de bÃºsqueda
 * 
 * @author aaranda
 * 
 */
public class SearchHandler {

    public static final int SIZE = 200;
    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    /**
     * NÃºmero mÃ¡ximo de intentos de indexado de documentos
     */
    private static final int MAX_INDEX_RETRIES = 3;

    private final Index index;

    /**
     * Inicializa el gestor para un Ã­ndice especÃ­fico
     * 
     * @param indexName
     */
    public SearchHandler(String indexName) {
        this.index = SearchServiceFactory.getSearchService().getIndex(
                IndexSpec.newBuilder().setName(indexName));
    }

    /**
     * @param queryHandler
     *            es el manejador de la query
     * @return si el indice esta vacio o no
     */
    public boolean isEmpty(IQueryHandler queryHandler) {
        return count(queryHandler) == 0L;
    }

    /**
     * @param queryHandler
     *            es el manejador de la query
     * @return el numero de registros existentes en el indice
     */
    public long count(IQueryHandler queryHandler) {
        try {
            return search("", 0, queryHandler).getNumberFound();
        } catch (Exception e) {
            LOGGER.info("An error was occured during the index documents count. Exception: "
                    + e.getLocalizedMessage());
            return 0L;
        }
    }

    /**
     * @param query
     * @param limit
     * @param mapper
     * @param queryHandler
     * @return
     */
    public <T> List<T> search(String query, int limit,
            IDocumentToBeanMapper<T> mapper, IQueryHandler queryHandler) {
        Results<ScoredDocument> sr = search(query, limit, queryHandler);
        List<T> result = new ArrayList<T>();
        for (ScoredDocument document : sr.getResults()) {
            result.add(mapper.create(document));
        }
        return result;
    }

    /**
     * @param query
     * @param mapper
     * @param queryHandler
     * @return
     */
    public <T> List<T> search(String query, IDocumentToBeanMapper<T> mapper,
            IQueryHandler queryHandler) {
        Results<ScoredDocument> sr = search(query, queryHandler);
        List<T> result = new ArrayList<T>();
        for (ScoredDocument document : sr.getResults()) {
            result.add(mapper.create(document));
        }
        return result;
    }

    /**
     * @param query
     * @param cursor
     * @param mapper
     * @param queryHandler
     * @return
     */
    public <T> Map<Cursor, List<T>> search(String query, Cursor cursor,
            IDocumentToBeanMapper<T> mapper, IQueryHandler queryHandler) {
        Results<ScoredDocument> sr = search(query, cursor, queryHandler);
        List<T> result = new ArrayList<T>();
        for (ScoredDocument document : sr.getResults()) {
            result.add(mapper.create(document));
        }
        Map<Cursor, List<T>> mapCursor = new HashMap<Cursor, List<T>>();
        Cursor newCursor = sr.getCursor();
        mapCursor.put(newCursor, result);
        return mapCursor;
    }

    /**
     * @param query
     * @param limit
     * @param queryHandler
     * @return
     */
    public Results<ScoredDocument> search(String query, int limit,
            IQueryHandler queryHandler) {
        return this.index.search(Query.newBuilder()
                .setOptions(queryHandler.getQueryOptions(limit))
                .build(queryHandler.getQuery(query)));
    }

    /**
     * Recupera 20 resultados (valor por defecto de text search)
     * 
     * @param query
     * @param queryHandler
     * @return
     */
    public Results<ScoredDocument> search(String query,
            IQueryHandler queryHandler) {
        return this.index.search(Query.newBuilder()
                .setOptions(queryHandler.getQueryOptions())
                .build(queryHandler.getQuery(query)));
    }

    /**
     * @param query
     * @param cursor
     * @param queryHandler
     * @return
     */
    public Results<ScoredDocument> search(String query, Cursor cursor,
            IQueryHandler queryHandler) {
        return this.index.search(Query.newBuilder()
                .setOptions(queryHandler.getQueryOptions(cursor))
                .build(queryHandler.getQuery(query)));
    }

    /**
     * Borra todos los documentos existentes en el Ã­ndice
     */
    public void deleteAll() {
        LOGGER.info("Deleting documents in index " + this.index.getName());
        boolean notEmpty = true;
        while (notEmpty) {
            GetRequest request = GetRequest.newBuilder()
                    .setReturningIdsOnly(true).build();
            GetResponse<Document> response = this.index.getRange(request);
            if (response.getResults().isEmpty()) {
                notEmpty = false;
            } else {
                List<String> docIds = new ArrayList<String>();
                for (Document doc : response) {

                    docIds.add(doc.getId());
                    if (docIds.size() == 100
                            || docIds.size() == response.getResults().size()) {
                        this.index.delete(docIds);
                        docIds = new ArrayList<String>();
                    }
                }
            }
        }
        LOGGER.info("Deleted documents in index " + this.index.getName());
    }

    /**
     * Borra el documento cuyo id se pasa como parÃ¡metro en el Ã­ndice
     */
    public void deleteDocument(String idDoc) {
        LOGGER.info("Deleting document " + idDoc + " in index "
                + this.index.getName());

        this.index.delete(idDoc);

        LOGGER.info("Deleted document " + idDoc + " in index "
                + this.index.getName());
    }

    /**
     * Genera los documentos necesarios para los objetos que se pasan y los
     * inserta
     * 
     * @param objects
     * @param mapper
     */
    public <T extends IStorable<?>> void put(List<T> objects,
            IBeanToDocumentMapper<T> mapper) {
        Preconditions.checkNotNull(objects, "Null list of objects");
        Preconditions.checkNotNull(mapper, "Null mapper");
        List<Document> docs = new ArrayList<Document>();
        for (T o : objects) {
            Document d = mapper.create(o);
            if (d != null) {
                docs.add(d);
            }
        }
        put(docs);

    }

    /**
     * Inserta en bloques en el Ã­ndice
     * 
     * @param d
     */
    public void put(List<Document> d) {
        // Partimos la lista en fragmentos de 200, que es el mÃ¡ximo que el API
        // de Text Search permite insertar en modo batch
        List<List<Document>> chunks = Lists.partition(d, SIZE);
        for (List<Document> chunk : chunks) {
            put(this.index, 1, chunk);
        }
    }

    /**
     * Inserta en bloques en el Ã­ndice si el nÃºmero de intentos no se ha
     * excedido
     * 
     * @param index
     * @param intento
     * @param d
     */
    private void put(Index index, int intento, List<Document> d) {
        if (intento <= MAX_INDEX_RETRIES) {
            try {
                index.put(d);
            } catch (PutException e) {
                LOGGER.warning("Error al cargar indice" + e);
                put(index, intento + 1, d);
            }
        } else {
            LOGGER.severe("Maximum number of index put retries exceeded");
        }
    }

    /**
     * Recupera un documento indexado
     * @param id
     * @return
     */
    public Document getDocument(String id) {
        return this.index.get(id);
    }


    public void borrarStrIdsTS(List<String> lstIds) {
        List<List<String>> partition = Lists.partition(lstIds, SIZE);
        for (List<String> p : partition) {
            this.index.delete(p);
        }
    }
}
