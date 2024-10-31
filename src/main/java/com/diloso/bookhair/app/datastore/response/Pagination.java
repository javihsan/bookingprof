package com.diloso.bookhair.app.datastore.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by jpelaez on 17/7/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pagination {
    private List<Link> links;
    /**
     * Número de página. Comenzando en 0 para la primera.
     */
    private int page;
    /**
     * Número total de páginas para el pageSize dado.
     */
    private int totalPages;
    /**
     * Número total de elementos para el listado solicitado, teniendo en cuenta posibles criterios de filtrado si se especifican.
     */
    private int totalElements;
    /**
     * Cantidad de elementos solicitados. Coincide con el enviado en la Request.
     En caso de no haber sido informado en entrada se utilizará un valor por defecto dependiendo de la implementación
     */
    private int pageSize;

    /**
     * Cursor de la siguiente página
     * @return
     */
    private String next;

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
