package com.diloso.bookhair.app.datastore.data;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;	

/**
 * @author emondelo
 *
 */
public final class ObjectifyServiceMod extends ObjectifyService {

    private static final Logger LOGGER = Logger
            .getLogger(ObjectifyService.class.getName());

    private static final String LOG_UPDATED_ALL = "Objects of type '%s' ('%d' objects) updated in DataStore";

    /**
     * Singleton instance
     */
    private static final ObjectifyServiceMod SERVICE = new ObjectifyServiceMod();

    /**
     * Private Constructor
     */
    private ObjectifyServiceMod() {
    }

    /**
     * @return singleton instance
     */
    public static ObjectifyServiceMod getService() {
        return SERVICE;
    }

    /**
     * Updates a list of entities without memcache
     *
     * @param entities
     * @return keys of entities in the DataStore
     */
    public <T extends IStorable<?>> Map<Key<T>, T> updateAllWithoutMemcache(
            List<T> entities) {
        Map<Key<T>, T> keys = ofy().cache(false).save().entities(entities)
                .now();
        logOperation(LOG_UPDATED_ALL, getClass(), entities);
        return keys;
    }

    private <T extends IStorable<?>> void logOperation(String message,
            Class<?> c, List<T> entities) {
        String mesagge = String.format(message, c.getName(), entities.size());
        LOGGER.fine(mesagge);
    }
}
