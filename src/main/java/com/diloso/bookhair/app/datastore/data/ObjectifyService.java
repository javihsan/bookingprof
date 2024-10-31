package com.diloso.bookhair.app.datastore.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.diloso.bookhair.app.datastore.UtilidadesData;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.common.collect.Lists;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.SaveException;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;
import com.googlecode.objectify.cmd.QueryKeys;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;

/**
 * Responsible of performing DataStore operations using Objectify Access using
 * singleton instance:
 *
 * ObjectifyService.getService()
 *
 * @author Alejandro Aranda
 *
 */
public final class ObjectifyService {


    public static final int MAX_ATTEMPTS = 4;
    public static final int INI_ATTEMPT = 1;
    public static final String LOG_ERROR_DS = "Error en el acceso a DS. Superado el número de intentos máximo ";
    public static final String LOG_ERROR_DS_ATTEMPT = "Reintentando .... Error en el acceso a DS. Intento ";

    private static final String LOG_DELETED_LIST = "Objects of type '%s' with IDs '%s' deleted in DataStore";
    private static final String LOG_DELETED = "Object of type '%s' with ID '%s' deleted in DataStore";
    private static final String LOG_DELETED_ALL = "Objects of type '%s' deleted in DataStore";
    private static final String LOG_UPDATED = "Object of type '%s' with ID '%s' updated in DataStore";
    private static final String LOG_CREATED_ALL = "Objects of type '%s' ('%d' objects) created in DataStore";
    private static final String LOG_UPDATED_ALL = "Objects of type '%s' ('%d' objects) updated in DataStore";
    private static final String LOG_CREATION = "Object of type '%s' with ID '%s' created in DataStore";
    private static final String REGISTERING_OBJECTIFY_CLASS = "Registering Objectify class ";
    /**
     * Singleton instance
     */
    private static final ObjectifyService SERVICE = new ObjectifyService(true);
    private static final ObjectifyService SERVICE_WITHOUT_CACHE = new ObjectifyService(
            false);

    private static final Logger LOGGER = Logger
            .getLogger(ObjectifyService.class.getName());

    private boolean cacheEnable = true;

    /**
     * Private Constructor
     */
    private ObjectifyService() {
    }

    /**
     * Private Constructor with param
     */
    private ObjectifyService(boolean cacheEnable) {
        this.cacheEnable = cacheEnable;
    }

    /**
     * @return singleton instance with cache (memcache) enable or not.
     */
    public static ObjectifyService getService(boolean cacheEnable) {
        if (cacheEnable) {
            return SERVICE;
        } else {
            return SERVICE_WITHOUT_CACHE;
        }
    }

    /**
     * Initialize service with list of storable classes
     */
    static void initialize(List<Class<?>> classes) {
        ObjectifyFactory factory = com.googlecode.objectify.ObjectifyService
                .factory();
        if (classes != null) {
            for (Class<?> clazz : classes) {
                LOGGER.info(
                        REGISTERING_OBJECTIFY_CLASS + " " + clazz.getName());
                factory.register(clazz);
            }
        }
        JodaTimeTranslators.add(factory);
    }

    private Objectify ofy() {
        return com.googlecode.objectify.ObjectifyService.ofy();
    }

    private <T extends IStorable<?>> void logOperation(String message,
                                                       T entity) {
        LOGGER.fine(String.format(message, entity.getClass().getName(),
                entity.getId() != null ? entity.getId().toString() : ""));
    }

    private <T extends IStorable<?>> void logOperation(String message,
            Class<?> c, List<T> entities) {
        LOGGER.fine(String.format(message, c.getName(), entities.size()));
    }

    private void logOperation(String message, String type) {
        LOGGER.fine(String.format(message, type));
    }

    private void logOperation(String message, String type, String id) {
        LOGGER.fine(String.format(message, type, id));
    }



    /**
     * Saves an entity
     *
     * @param entity
     * @return key of entity in the DataStore
     */
    public <T extends IStorable<?>> Key<T> save(T entity) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                Key<T> key = ofy().cache(cacheEnable).save().entity(entity).now();
                logOperation(LOG_CREATION, entity);
                return key;
            } catch (DatastoreTimeoutException | SaveException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt,LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
        return null;
    }

    /**
     * Updates an entity
     *
     * @param entity
     * @return key of entity in the DataStore
     */
    public <T extends IStorable<?>> Key<T> update(T entity) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                Key<T> key = ofy().cache(cacheEnable).save().entity(entity).now();
                logOperation(LOG_UPDATED, entity);
                return key;
            } catch (DatastoreTimeoutException | SaveException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt,LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
        return null;
    }

    /**
     * Updates a list of entities
     *
     * @param entities
     * @return keys of entities in the DataStore
     */
    public <T extends IStorable<?>> Map<Key<T>, T> updateAll(List<T> entities) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                Map<Key<T>, T> keys = ofy().cache(cacheEnable).save().entities(entities)
                        .now();
                logOperation(LOG_UPDATED_ALL, getClass(), entities);
                return keys;
            } catch (DatastoreTimeoutException | SaveException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt,LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
        return null;
    }

    /**
     * Creates a list of entities
     *
     * @param entities
     * @return keys of entities in the DataStore
     */
    public <T extends IStorable<?>> Map<Key<T>, T> createAll(List<T> entities) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                Map<Key<T>, T> keys = ofy().cache(cacheEnable).save().entities(entities)
                        .now();
                logOperation(LOG_CREATED_ALL, getClass(), entities);
                return keys;
            } catch (DatastoreTimeoutException | SaveException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt,LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
        return null;
    }

    /**
     * Returns object of given type and id
     *
     * @param clazz
     * @param id
     * @return object or null if not found
     */
    public <T extends IStorable<Long>> T get(Class<T> clazz, Long id) {
        return id != null
                ? (T) ofy().cache(cacheEnable).load().type(clazz).id(id).now()
                : null;
    }

    /**
     * Returns object of given type and id
     *
     * @param clazz
     * @param id
     * @return object or null if not found
     */
    public <T extends IStorable<String>> T get(Class<T> clazz, String id) {
        return id != null
                ? (T) ofy().cache(cacheEnable).load().type(clazz).id(id).now()
                : null;
    }

    /**
     * Returns objects of given type and id
     *
     * @param clazz
     * @param id
     * @return objects or null if not found
     */
    public <T extends IStorable<Long>> Map<Long, T> getByLongList(
            Class<T> clazz, Collection<Long> ids) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                return (ids != null && ids.size() > 0 ? (Map<Long, T>) ofy()
                        .cache(cacheEnable).load().type(clazz).ids(ids) : null);
            } catch (DatastoreTimeoutException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt, LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
        LOGGER.severe(LOG_ERROR_DS + attempt);
        // return ObjectifyService.ofy().load().type(this.dataClass).list();
        return null;


    }



    /**
     * Returns objects of given type and id
     *
     * @param clazz
     * @param id
     * @return objects or null if not found
     */
    public <T extends IStorable<String>> Map<String, T> getByStringList(
            Class<T> clazz, Collection<String> ids) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                return (ids != null && ids.size() > 0 ? (Map<String, T>) ofy()
                        .cache(cacheEnable).load().type(clazz).ids(ids) : null);
            } catch (DatastoreTimeoutException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt, LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
        return null;

    }


    /**
     * Deletes object of given type and id NOTE: Does not check if object exists
     *
     * @param clazz
     * @param id
     */
    public <T extends IStorable<Long>> void delete(Class<T> clazz, Long id) {
        ofy().cache(cacheEnable).delete().type(clazz).id(id).now();
        logOperation(LOG_DELETED, clazz.getName(), id.toString());
    }


    /**
     * Deletes list of objects of given type and id NOTE: Does not check if
     * object exists
     *
     * @param clazz
     * @param id
     */
    public <T extends IStorable<Long>> void deleteByLongList(Class<T> clazz,
            List<Long> ids) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                ofy().cache(cacheEnable).delete().type(clazz).ids(ids).now();
                logOperation(LOG_DELETED_LIST, clazz.getName(), getListOfLongIds(ids));
                break;
            } catch (DatastoreTimeoutException | SaveException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt,LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
    }


    /**
     * Deletes object of given type and id NOTE: Does not check if object exists
     *
     * @param clazz
     * @param id
     */
    public <T extends IStorable<String>> void delete(Class<T> clazz,
            String id) {
        ofy().cache(cacheEnable).delete().type(clazz).id(id).now();
        logOperation(LOG_DELETED, clazz.getName(), id);
    }



    /**
     * Deletes list of objects of given type and id NOTE: Does not check if
     * object exists
     *
     * @param clazz
     * @param id
     */
    public <T extends IStorable<String>> void deleteByStringList(Class<T> clazz,
            List<String> ids) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                ofy().cache(cacheEnable).delete().type(clazz).ids(ids).now();
                LOGGER.info(String.format(LOG_DELETED_LIST, clazz.getName(),
                        getListOfStringIds(ids)));
                break;
            } catch (DatastoreTimeoutException | SaveException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt,LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
    }


    /**
     * Deletes all objects of given type
     *
     * @param clazz
     */
    public <T extends IStorable<?>> void deleteAll(Class<T> clazz) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                ofy().cache(cacheEnable).delete()
                        .keys(ofy().cache(cacheEnable).load().type(clazz).keys().list())
                        .now();
                logOperation(LOG_DELETED_ALL, clazz.getName());
                break;
            } catch (DatastoreTimeoutException | SaveException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt,LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
    }

    /**
     * Returns all objects of given type with full data or not
     *
     * @param clazz
     * @param full
     * @return list of objects
     */
    public <T extends IStorable<?>> List<T> list(Class<T> clazz, boolean full) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                List<T> list = ofy().cache(cacheEnable).load()
                        .group(full ? FullLoadGroup.class : PartialLoadGroup.class)
                        .type(clazz).list();
                // Filtramos los elementos null debido a que si el acceso al Datastore
                // se realiza mediante una consulta híbrida (cuando el bean tiene
                // @Cache)
                // el acceso se hace con consistencia eventual y se pueden devolver
                // objetos que acaban de ser borrados
                // Ver información sobre Hybrid Queries aquí:
                // https://code.google.com/p/objectify-appengine/wiki/Caching
                return filterNull(list);
            } catch (DatastoreTimeoutException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt, LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
        return Collections.emptyList();
    }

    /**
     * Returns all objects of given type with full data or not, and ordering
     *
     * @param clazz
     * @param full
     * @param orders
     * @return list of objects
     */
    public <T extends IStorable<?>> List<T> listOrder(Class<T> clazz,
            boolean full, List<String> orders) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                Query<T> query = ofy().cache(cacheEnable).load()
                        .group(full ? FullLoadGroup.class : PartialLoadGroup.class)
                        .type(clazz);
                if (orders != null && orders.size() > 0) {
                    for (String order : orders) {
                        query = query.order(order);
                    }
                }
                List<T> list = query.list();
                // Filtramos los elementos null debido a que si el acceso al Datastore
                // se realiza mediante una consulta híbrida (cuando el bean tiene
                // @Cache)
                // el acceso se hace con consistencia eventual y se pueden devolver
                // objetos que acaban de ser borrados
                // Ver información sobre Hybrid Queries aquí:
                // https://code.google.com/p/objectify-appengine/wiki/Caching
                return filterNull(list);
            } catch (DatastoreTimeoutException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt, LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
        // return ObjectifyService.ofy().load().type(this.dataClass).list();
        return Collections.emptyList();
    }



    /**
     * Returns a page of results
     *
     * @param clazz
     * @param full
     * @param from
     * @param itemsPerPage
     * @return
     */
    public <T extends IStorable<?>> PageBean page(Class<T> clazz, boolean full,
            String from, int itemsPerPage) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                Query<T> query = ofy().cache(cacheEnable).load()
                        .group(full ? FullLoadGroup.class : PartialLoadGroup.class)
                        .type(clazz).limit(itemsPerPage);
                if (from != null) {
                    query = query.startAt(Cursor.fromWebSafeString(from));
                }
                PageBean page = new PageBean();
                QueryResultIterator<T> iterator = query.iterator();
                // Se filtran los null por el mismo motivo que en el listado
                // Ver comentario en método list
                page.setData(filterNull(iterator));
                Cursor cursor = iterator.getCursor();
                if (hasMorePages(clazz, cursor)) {
                    page.setNext(cursor.toWebSafeString());
                }
                return page;
            } catch (DatastoreTimeoutException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt, LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
        // return ObjectifyService.ofy().load().type(this.dataClass).list();
        return null;
    }

    /**
     * Returns a page of results with ordering
     *
     * @param clazz
     * @param full
     * @param from
     * @param itemsPerPage
     * @param orders
     * @return
     */
    public <T extends IStorable<?>> PageBean pagedOrder(Class<T> clazz,
            boolean full, String from, int itemsPerPage, List<String> orders) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                LoadType<T> loadType = ofy().cache(cacheEnable).load()
                        .group(full ? FullLoadGroup.class : PartialLoadGroup.class)
                        .type(clazz);
                Query<T> query = loadType.limit(itemsPerPage);
                if (from != null) {
                    query = query.startAt(Cursor.fromWebSafeString(from));
                }
                if (orders != null && orders.size() > 0) {
                    query = applyOrders(loadType, query, orders);
                }
                PageBean page = new PageBean();
                QueryResultIterator<T> iterator = query.iterator();
                // Se filtran los null por el mismo motivo que en el listado
                // Ver comentario en método list
                page.setData(filterNull(iterator));
                Cursor cursor = iterator.getCursor();
                if (hasMorePagesOrder(clazz, cursor, orders)) {
                    page.setNext(cursor.toWebSafeString());
                }
                return page;
            } catch (DatastoreTimeoutException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt, LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
        // return ObjectifyService.ofy().load().type(this.dataClass).list();
        return null;
    }



    /**
     * Returns a list of objects of given type matching value for given field
     * name
     *
     * @param clazz
     * @param full
     * @param filters
     * @return list of objects matching
     */
    public <T extends IStorable<?>> List<T> filter(Class<T> clazz, boolean full,
            Map<String, Object> filters) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                Query<T> query = applyFilters(ofy().cache(cacheEnable).load()
                        .group(full ? FullLoadGroup.class : PartialLoadGroup.class)
                        .type(clazz), filters);
                return filterNull(query.list());
            } catch (DatastoreTimeoutException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt, LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
        // return ObjectifyService.ofy().load().type(this.dataClass).list();
        return Collections.emptyList();
    }

    /**
     * Recupera del Datastore con filtros y criterios de ordenación
     *
     * @param clazz
     * @param full
     * @param filters
     * @return list of objects matching
     */
    public <T extends IStorable<?>> List<T> filterOrder(Class<T> clazz,
            boolean full, Map<String, Object> filters, List<String> orders) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                LoadType<T> loadType = ofy().cache(cacheEnable).load()
                        .group(full ? FullLoadGroup.class : PartialLoadGroup.class)
                        .type(clazz);
                Query<T> query = applyFilters(loadType, filters);
                if (orders != null && orders.size() > 0) {
                    query = applyOrders(loadType, query, orders);
                }
                return filterNull(query.list());
            } catch (DatastoreTimeoutException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt, LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
        // return ObjectifyService.ofy().load().type(this.dataClass).list();
        return Collections.emptyList();
    }




    /**
     * Returns a list of objects of given type matching value for given field
     * name
     *
     * @param clazz
     * @param full
     * @param filterName
     * @param filterValue
     * @return
     */
    public <T extends IStorable<?>> List<T> filter(Class<T> clazz, boolean full,
            String filterName, Object filterValue) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                Query<T> query = ofy().cache(cacheEnable).load()
                        .group(full ? FullLoadGroup.class : PartialLoadGroup.class)
                        .type(clazz).filter(filterName, filterValue);
                return filterNull(query.list());
            } catch (DatastoreTimeoutException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt, LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
        // return ObjectifyService.ofy().load().type(this.dataClass).list();
        return Collections.emptyList();
    }

    /**
     * Returns a page of results
     *
     * @param clazz
     * @param full
     * @param from
     * @param itemsPerPage
     * @param filters
     * @return
     */
    public <T extends IStorable<?>> PageBean pagedFilter(Class<T> clazz,
            boolean full, String from, int itemsPerPage,
            Map<String, Object> filters) {
        return pagedFilterOrder(clazz, full, from, itemsPerPage, filters, null);
    }

    /**
     * Returns a page of ordered results
     *
     * @param clazz
     * @param full
     * @param from
     * @param itemsPerPage
     * @param filters
     * @return
     */
    public <T extends IStorable<?>> PageBean pagedFilterOrder(Class<T> clazz,
            boolean full, String from, int itemsPerPage,
            Map<String, Object> filters, List<String> orders) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                LoadType<T> loadType = ofy().cache(cacheEnable).load()
                        .group(full ? FullLoadGroup.class : PartialLoadGroup.class)
                        .type(clazz);
                Query<T> query = applyFilters(loadType, filters).limit(itemsPerPage);
                if (from != null) {
                    try {
                        query = query.startAt(Cursor.fromWebSafeString(from));
                    } catch (IllegalArgumentException e) {
                        LOGGER.warning(e.getMessage());
                    }
                }
                if (orders != null && orders.size() > 0) {
                    query = applyOrders(loadType, query, orders);
                }
                PageBean page = new PageBean();
                QueryResultIterator<T> iterator = query.iterator();
                page.setData(filterNull(iterator));
                Cursor cursor = iterator.getCursor();
                if (hasMorePagesFilterOrderer(clazz, cursor, filters, orders)) {
                    page.setNext(cursor.toWebSafeString());
                }
                return page;
            } catch (DatastoreTimeoutException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt, LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
        // return ObjectifyService.ofy().load().type(this.dataClass).list();
        return null;
    }



    /**
     * Executes a transaction
     *
     * @param transaction
     */
    public <T> T transaction(final ITransaction<T> transaction) {
        return ofy().cache(cacheEnable).transact(new Work<T>() {
            @Override
            public T run() {
                return transaction.execute(ObjectifyService.this);
            }
        });
    }

    public com.googlecode.objectify.Objectify transactionless() {
        return ofy().cache(cacheEnable).transactionless();
    }

    /**
     * Deletes objects
     *
     * @param objects
     */
    public void delete(Class<?> clazz, List<? extends IStorable<?>> objects) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                ofy().cache(cacheEnable).delete().entities(objects).now();
                logOperation(LOG_DELETED_ALL, clazz.getName());
                break;
            } catch (DatastoreTimeoutException | SaveException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt,LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);




    }

    /**
     * Cuenta el número de registros de una entidad (con filtro)
     *
     * @param clazz
     * @param full
     * @return
     */
    public <T extends IStorable<?>> int count(Class<T> clazz, boolean full) {

        /**
         * List<T> list = ofy().cache(cacheEnable).load()
         .group(full ? FullLoadGroup.class : PartialLoadGroup.class)
         .type(clazz).list();
         */


        /**
         *  Query<T> query = applyFilters(ofy().cache(cacheEnable).load()
         .group(full ? FullLoadGroup.class : PartialLoadGroup.class)
         .type(clazz), filters);
         */
        int attempt = INI_ATTEMPT;
        do {
            try {
                return ofy().cache(cacheEnable).load()
                        .group(full ? FullLoadGroup.class : PartialLoadGroup.class)
                        .type(clazz).count();
            } catch (DatastoreTimeoutException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt,LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
        return 0;
    }

    /**
     * Cuenta el número de registros de una entidad (con filtro)
     *
     * @param clazz
     * @param full
     * @param filters
     * @return
     */
    public <T extends IStorable<?>> int count(Class<T> clazz, boolean full,
            Map<String, Object> filters) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                Query<T> query = applyFilters(ofy().cache(cacheEnable).load()
                        .group(full ? FullLoadGroup.class : PartialLoadGroup.class)
                        .type(clazz), filters);
                return query.count();
            } catch (DatastoreTimeoutException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt,LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
        return 0;
    }

    /**
     * Limpia la caché de sesión usada por Objectify, es una caché diferente al
     * uso de Memcache.
     */
    public void clear() {
        ofy().clear();
    }


    public <T extends IStorable<?>> PageKeyBean<T> pagedOnlyKeys(Class<T> clazz, Cursor cursor, int itemsPerPage, Map<String, Object> filters) {
        LoadType<T> loadType = ofy().cache(cacheEnable).load()
                .group(PartialLoadGroup.class)
                .type(clazz);
        Query<T> query = applyFilters(loadType, filters).limit(itemsPerPage);
        int attempt = INI_ATTEMPT;
        do {
            try {
                QueryKeys<T> queryKeys = null;
                if (cursor != null) {
                    queryKeys = query.startAt(cursor).keys();
                } else {
                    queryKeys = query.keys();
                }

                return getPageKey(clazz, queryKeys);
            } catch (DatastoreTimeoutException e) {
                LOGGER.severe(LOG_ERROR_DS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt, LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
//        LOGGER.severe(LOG_ERROR_DS + attempt);
        return null;
    }
    private <T extends IStorable<?>> PageKeyBean<T> getPageKey(Class<T> clazz, QueryKeys<T> query) {
        QueryResultIterator<Key<T>> iterator = query.iterator();
        PageKeyBean<T> pageBean = new PageKeyBean<>();
        pageBean.setData(filterNullLst(iterator));
        Cursor next = iterator.getCursor();
        if (hasMorePages(clazz, next)) {
            pageBean.setNext(getStrCursorDS(next));
        }
        return pageBean;
    }
    private <T> List<T> filterNullLst(QueryResultIterator<T> iterator) {
        List<T> filteredList = Lists.newArrayList();
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (item != null) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    /**
     * @param list
     * @return list without null elements
     */
    private <T extends IStorable<?>> List<T> filterNull(List<T> list) {
        List<T> filteredList = new ArrayList<T>();
        for (T item : list) {
            if (item != null) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    /**
     * @param iterator
     * @return list without null elements
     */
    private <T extends IStorable<?>> List<T> filterNull(
            QueryResultIterator<T> iterator) {
        List<T> filteredList = new ArrayList<T>();
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (item != null) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    private static String getStrCursorDS(Cursor cursor) {
        String strCursor = null;
        // Recuperando cursor
        if (cursor == null) {
            LOGGER.info("No hay más páginas... ");
        } else {
            strCursor = cursor.toWebSafeString();
        }
        return strCursor;
    }

    private String getListOfLongIds(List<Long> ids) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            sb.append(ids.get(i).toString());
            if (i < ids.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private String getListOfStringIds(List<String> ids) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            sb.append(ids.get(i).toString());
            if (i < ids.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private <T extends IStorable<?>> boolean hasMorePages(Class<T> clazz,
                                                          Cursor cursor) {
        return ofy().cache(cacheEnable).load().type(clazz).limit(1)
                .startAt(cursor).iterator().hasNext();
    }

    private <T extends IStorable<?>> boolean hasMorePagesOrder(Class<T> clazz,
                                                               Cursor cursor, List<String> orders) {
        Query<T> query = ofy().cache(cacheEnable).load().type(clazz).limit(1);
        if (orders != null && orders.size() > 0) {
            for (String order : orders) {
                query = query.order(order);
            }
        }
        return query.startAt(cursor).iterator().hasNext();
    }
    /**
     * Establece los criterios de ordenación de la qry
     *
     * @param loadType
     * @param query
     * @param orders
     * @return
     */
    private <T extends IStorable<?>> Query<T> applyOrders(LoadType<T> loadType,
                                                          Query<T> query, List<String> orders) {
        for (String order : orders) {
            if (query == null) {
                query = loadType.order(order);
            } else {
                query = query.order(order);
            }
        }
        return query;
    }

    private <T extends IStorable<?>> boolean hasMorePagesFilterOrderer(
            Class<T> clazz, Cursor cursor, Map<String, Object> filters,
            List<String> orders) {
        LoadType<T> loadType = ofy().cache(cacheEnable).load().type(clazz);
        Query<T> query = applyFilters(loadType, filters).limit(1)
                .startAt(cursor);
        if (orders != null && orders.size() > 0) {
            query = applyOrders(loadType, query, orders);
        }
        return query.iterator().hasNext();
    }

    private <T extends IStorable<?>> Query<T> applyFilters(LoadType<T> loadType,
                                                           Map<String, Object> filters) {
        Query<T> query = null;
        for (Map.Entry<String, Object> filter : filters.entrySet()) {
            if (query == null) {
                query = loadType.filter(filter.getKey(), filter.getValue());
            } else {
                query = query.filter(filter.getKey(), filter.getValue());
            }
        }
        return query;
    }


}