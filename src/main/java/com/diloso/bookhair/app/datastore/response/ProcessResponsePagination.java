package com.diloso.bookhair.app.datastore.response;

import java.util.Arrays;
import java.util.List;

import com.diloso.bookhair.app.datastore.StringUtils;

/**
 * Created by jpelaez on 21/7/17.
 */
public class ProcessResponsePagination<T> {
    private static final String PARAM_NEXT = "&paginationKey=";

    public ResponsePage getResponse(String pageSize, String next, int numElems,
            int contador, List<T> filteredList, String strLink) {
        Link link = new Link();
        link.setFirst(strLink + pageSize);
        if (!StringUtils.isEmpty(next)) {
            link.setNext(strLink + pageSize + PARAM_NEXT + next);
        }
        Pagination pagina = new Pagination();
        pagina.setPageSize(numElems);
        pagina.setTotalElements(contador);
        pagina.setLinks(Arrays.asList(link));
        pagina.setNext(next);
        ResponsePage response = new ResponsePage();
        response.setData(filteredList);
        response.setStatus(ResponseStatusEnum.OK.name());
        response.setPagination(pagina);
        return response;
    }

}
