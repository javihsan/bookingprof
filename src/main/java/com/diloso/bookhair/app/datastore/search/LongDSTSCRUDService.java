package com.diloso.bookhair.app.datastore.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import com.diloso.bookhair.app.datastore.data.IStorable;
import com.diloso.bookhair.app.datastore.data.LongIdCRUDService;
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
public class LongDSTSCRUDService<T extends IStorable<Long>>  extends LongIdCRUDService<T>  {

    private IBeanToDocumentMapper<T> mapper;
    private String indice;

    public LongDSTSCRUDService(IBeanToDocumentMapper<T> mapper,
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
    public void delete(Long id) {
        new Indexador<T>(null, null, this.indice)
                .elimina(String.valueOf(id));
        super.delete(id);
    }

    @Override
    public void deleteAll(List<T> objects) {
        IIndexador indexador = new Indexador<T>(null, null, this.indice);
        Collection<String> transform = Collections2.transform(objects, new Function<T, String>() {
            @Nullable
            @Override
            public String apply(@Nullable T input) {
                return String.valueOf(input.getId());
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
    public void delete(List<Long> lstIds) {
        IIndexador indexador = new Indexador<T>(null, null, this.indice);
        Collection<String> transform = Collections2.transform(lstIds, new Function<Long, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Long input) {
                return String.valueOf(input);
            }
        });
        indexador.elimina(Lists.newArrayList(transform));
        super.delete(lstIds);
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


//    @Override
//    public List<T> list() {
//        return longIdCRUDService.list();
//    }
//
//    @Override
//    public List<T> listOrder(List<String> orders) {
//        return longIdCRUDService.listOrder(orders);
//    }
//
//    @Override
//    public List<T> listFilter(Map<String, Object> filters) {
//        return longIdCRUDService.listFilter(filters);
//    }
//
//    @Override
//    public List<T> listOrderFilter(Map<String, Object> filters, List<String> orders) {
//        return longIdCRUDService.listOrderFilter(filters,orders);
//    }
//
//    @Override
//    public PageBean page(String from, int itemsPerPage) {
//        return longIdCRUDService.page(from, itemsPerPage);
//    }
//
//    @Override
//    public PageBean pagedOrder(String from, int itemsPerPage, List<String> orders) {
//        return longIdCRUDService.pagedOrder(from, itemsPerPage, orders);
//    }
//
//    @Override
//    public PageBean pagedFilter(String from, int itemsPerPage, Map<String, Object> filters) {
//        return longIdCRUDService.pagedFilter(from, itemsPerPage, filters);
//    }
//
//    @Override
//    public PageKeyBean<T> pagedOnlyKeys(Cursor cursor, int itemsPerPage, Map<String, Object> filters, List<String> orders) {
//        return longIdCRUDService.pagedOnlyKeys(cursor, itemsPerPage, filters, orders);
//    }
//
//    @Override
//    public T get(Long id) {
//        return longIdCRUDService.get(id);
//    }
//
//    @Override
//    public boolean isAutogenerateIds() {
//        return longIdCRUDService.isAutogenerateIds();
//    }
//
//    @Override
//    public boolean isNullId(Long id) {
//        return longIdCRUDService.isNullId(id);
//    }
//
//    @Override
//    public Object formatForFilter(String name, String value) {
//        return longIdCRUDService.formatForFilter(name, value);
//    }
//
//    @Override
//    public int count(Map<String, Object> filters) {
//        return longIdCRUDService.count(filters);
//    }
//
//    @Override
//    public Map<Long, T> getByList(Collection<Long> ids) {
//        return longIdCRUDService.getByList(ids);
//    }
//
//    @Override
//    public PageBean pagedOrderFilter(String from, int itemsPerPage, Map<String, Object> filters, List<String> orders) {
//        return longIdCRUDService.pagedOrderFilter(from, itemsPerPage, filters,orders);
//    }
//
//    @Override
//    public void clearCache() {
//        longIdCRUDService.clearCache();
//
//    }
}