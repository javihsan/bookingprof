package com.diloso.bookhair.app.datastore.response;

import java.util.List;

/**
 * Created by jpelaez on 1/8/17.
 */
public interface IProcesResponse<T> {
    ResponsePage getResponse(String pageSize, String next, int numElems,
                             int contador, List<T> filteredList);
}
