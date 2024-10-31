package com.diloso.bookhair.app.datastore.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by jpelaez on 12/7/17.
 * Mensajes de error en las respuestas
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage {
    /**
     * mandatoryParameterMissing
     */
    private String code;
    private String message;
    /**
     * WARNING, ERROR, FATAL, CRITICAL
     */
    private String type;
    private List<Parameter> lstParameters;

    public ResponseMessage(String code, String message, String type) {
        this.code = code;
        this.message = message;
        this.type = type;
    }

    public ResponseMessage() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Parameter> getLstParameters() {
        return lstParameters;
    }

    public void setLstParameters(List<Parameter> lstParameters) {
        this.lstParameters = lstParameters;
    }
}
