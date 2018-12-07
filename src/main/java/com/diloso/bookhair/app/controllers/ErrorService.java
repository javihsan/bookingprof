package com.diloso.bookhair.app.controllers;

import org.springframework.dao.UncategorizedDataAccessException;

public class ErrorService extends UncategorizedDataAccessException {

	protected static final long serialVersionUID = 1L;

	public ErrorService(String msg, Throwable cause) {
		super(msg, cause);
	}

}
