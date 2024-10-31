package com.diloso.bookhair.app.datastore.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa una respuesta de un servicio que se puede serializar
 * 
 * @author aaranda
 * 
 */
@JsonInclude(Include.NON_NULL)
public class ResultBean {

	@JsonProperty
	private boolean success;

	@JsonProperty
	private String message;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @param success
	 * @param message
	 */
	public ResultBean(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	/**
	 * Default constructor
	 */
	public ResultBean() {

	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return this.success;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
