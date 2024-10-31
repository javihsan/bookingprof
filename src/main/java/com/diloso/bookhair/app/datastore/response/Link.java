package com.diloso.bookhair.app.datastore.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Objeto para devolver un listado paginado
 * Created by jpelaez on 12/7/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Link {
    /**
     * URI a la primera página.
     Será la URI al listado sin elemento de paginación.
     */
    private String first;
    /**
     * URI a la última página.
     Teniendo en cuenta el tamaño de página.
     */
    private String last;
    /**
     * URI a la página anterior.
     */
    private String previous;
    /**
     * URI a la página siguiente.
     */
    private String next;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
