package com.diloso.bookhair.app.datastore.search;

import java.util.List;

public interface IIndexador {

    public abstract void indexar();

    public abstract void elimina(String id);

    public abstract void eliminaTodos();

    public abstract void elimina(List<String> ids);

}