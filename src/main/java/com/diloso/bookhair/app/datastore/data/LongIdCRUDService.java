package com.diloso.bookhair.app.datastore.data;


import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.diloso.bookhair.app.datastore.UtilidadesData;
import com.googlecode.objectify.Key;

public class LongIdCRUDService<T extends IStorable<Long>>
        extends GenericCRUDService<Long, T> {

    @Override
    public boolean isAutogenerateIds() {
        return true;
    }

    @Override
    public boolean isNullId(Long id) {
        return id == null || id == 0;
    }

    @Override
    public T create(T object) {
        //setTimestamps(object, UtilidadesData.now());
        Key<T> key = getService().save(object);
        object.setId(key.getId());
        return object;
    }

    @Override
    public void delete(Long id) {
        getService().delete(getDataClass(), id);
    }


    @Override
    public void delete(List<Long> ids) {
        getService().deleteByLongList(getDataClass(), ids);
    }


    @Override
    public T get(Long id) {
        return getService().get(getDataClass(), id);
    }

    @Override
    public Map<Long, T> getByList(Collection<Long> ids) {
        return getService().getByLongList(getDataClass(), ids);
    }

}
