package com.diloso.bookhair.app.datastore.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import com.diloso.bookhair.app.datastore.data.IStorable;
import com.diloso.bookhair.app.datastore.data.StringIdCRUDService;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * Sobreescribe los CRUD de los DAOS incluyendo los mismos en TSearch
 * 
 * @author jpelaez
 *
 * @param <T>
 */
public class StringDSTSCRUDService<T extends IStorable<String>>  extends StringIdCRUDService<T>  {

    private IBeanToDocumentMapper<T> mapper;
    private String indice;

    public StringDSTSCRUDService(IBeanToDocumentMapper<T> mapper,
                                 String indice) {
        this.mapper = mapper;
        this.indice = indice;
    }

    @Override
    public T create(T object) {
        object = super.create(object);
        List<T> lstBeans = new ArrayList<T>();
        lstBeans.add(object);
        // Ver que se haya modificado el id de la entidad
        IIndexador indexador = new Indexador<T>(lstBeans, this.mapper,
                this.indice);
        indexador.indexar();
        return object;
    }

    @Override
    public List<T> createAll(List<T> objects) {
        List<T> lstFuncionalidades = super.createAll(objects);
        IIndexador indexador = new Indexador<T>(lstFuncionalidades,
                this.mapper, this.indice);
        indexador.indexar();
        return lstFuncionalidades;
    }

    @Override
    public void delete(String id) {
        new Indexador<T>(null, null, this.indice)
                .elimina(id);
        super.delete(id);
    }

    @Override
    public void deleteAll(List<T> objects) {
        IIndexador indexador = new Indexador<T>(null, null, this.indice);
        Collection<String> transform = Collections2.transform(objects, new Function<T, String>() {
            @Nullable
            @Override
            public String apply(@Nullable T input) {
                return input.getId();
            }
        });
        indexador.elimina(Lists.newArrayList(transform));
        super.deleteAll(objects);
    }

    @Override
    public void deleteAll() {
        new Indexador<T>(null, null, this.indice).eliminaTodos();
        super.deleteAll();
    }

    @Override
    public void delete(List<String> ids) {
        IIndexador indexador = new Indexador<T>(null, null, this.indice);
        indexador.elimina(Lists.newArrayList(ids));
        super.delete(ids);

    }

    @Override
    public T update(T object) {
        object = super.update(object);
        List<T> lstBeans = new ArrayList<T>();
        lstBeans.add(object);
        IIndexador indexador = new Indexador<T>(lstBeans, this.mapper,
                this.indice);
        indexador.indexar();
        return object;
    }

    @Override
    public List<T> updateAll(List<T> objects) {
        List<T> lstFuncionalidades = super.updateAll(objects);
        IIndexador indexador = new Indexador<T>(lstFuncionalidades,
                this.mapper, this.indice);
        indexador.indexar();
        return lstFuncionalidades;
    }

    /**
     * Borra y carga de nuevo DS y TS
     */
    public void reindexaAll() {
        List<T> all = list();
		deleteAll();
		createAll(all);
	}

//
//    @Override
//    public List<T> list() {
//        return super.list();
//    }
//
//    @Override
//    public List<T> listOrder(List<String> orders) {
//        return super.listOrder(orders);
//    }
//
//    @Override
//    public List<T> listFilter(Map<String, Object> filters) {
//        return super.listFilter(filters);
//    }
//
//    @Override
//    public List<T> listOrderFilter(Map<String, Object> filters, List<String> orders) {
//        return super.listOrderFilter(filters,orders);
//    }
//
//    @Override
//    public PageBean page(String from, int itemsPerPage) {
//        return super.page(from, itemsPerPage);
//    }
//
//    @Override
//    public PageBean pagedOrder(String from, int itemsPerPage, List<String> orders) {
//        return super.pagedOrder(from, itemsPerPage, orders);
//    }
//
//    @Override
//    public PageBean pagedFilter(String from, int itemsPerPage, Map<String, Object> filters) {
//        return super.pagedFilter(from, itemsPerPage, filters);
//    }
//
//    @Override
//    public PageKeyBean<T> pagedOnlyKeys(Cursor cursor, int itemsPerPage, Map<String, Object> filters, List<String> orders) {
//        return super.pagedOnlyKeys(cursor, itemsPerPage, filters, orders);
//    }
//
//    @Override
//    public T get(String id) {
//        return super.get(id);
//    }
//
//    @Override
//    public boolean isAutogenerateIds() {
//        return super.isAutogenerateIds();
//    }
//
//    @Override
//    public boolean isNullId(String id) {
//        return super.isNullId(id);
//    }
//
//    @Override
//    public Object formatForFilter(String name, String value) {
//        return super.formatForFilter(name, value);
//    }
//
//    @Override
//    public int count(Map<String, Object> filters) {
//        return super.count(filters);
//    }
//
//    @Override
//    public Map<String, T> getByList(Collection<String> ids) {
//        return super.getByList(ids);
//    }
//
//    @Override
//    public PageBean pagedOrderFilter(String from, int itemsPerPage, Map<String, Object> filters, List<String> orders) {
//        return super.pagedOrderFilter(from, itemsPerPage, filters,orders);
//    }
//
//    @Override
//    public void clearCache() {
//        super.clearCache();
//
//    }
}