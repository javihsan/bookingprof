package com.diloso.bookhair.app.datastore.data;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.diloso.bookhair.app.datastore.UtilidadesData;
import com.googlecode.objectify.Key;

public class StringIdCRUDService<T extends IStorable<String>>
        extends GenericCRUDService<String, T> {

    @Override
    public boolean isAutogenerateIds() {
        return false;
    }

    @Override
    public boolean isNullId(String id) {
        return id == null || "".equals(id.trim());
    }

    @Override
    public T create(T object) {
        setTimestamps(object, UtilidadesData.now());
        Key<T> key = getService().save(object);
        if (key.getName() == null && isAutogenerateIds()) {
            object.setId(String.valueOf(key.getId()));
        } else {
            object.setId(key.getName());
        }
        return object;
    }

    @Override
    public void delete(String id) {
        getService().delete(getDataClass(), id);
    }

    @Override
    public void delete(List<String> ids) {
        getService().deleteByStringList(getDataClass(), ids);
    }

    @Override
    public T get(String id) {
        return getService().get(getDataClass(), id);
    }

    @Override
    public Map<String, T> getByList(Collection<String> ids) {
        return getService().getByStringList(getDataClass(), ids);
    }

}
