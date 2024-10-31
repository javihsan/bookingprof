package com.diloso.bookhair.app.datastore.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by jpelaez on 12/7/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseOk<T>  {
    public ResponseOk() {
    }
    public ResponseOk(T data, String status) {
        this.data = data;
        this.status = status;
    }
    private T data;
    /**
     * 0-OK / 1-Error
     */
    private String status;
    /**
     * Lista de errores funcionales que se han producido en la app
     */
    private List<ResponseMessage> messages;

    public List<ResponseMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ResponseMessage> messages) {
        this.messages = messages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
