package com.diloso.weblogin;

import java.io.IOException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SessionCleanServe extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {

		try {
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Query query = new Query("_ah_SESSION");
			query.setKeysOnly();
			PreparedQuery results = datastore.prepare(query);

			int cleared = 0;

			for (Entity session : results.asIterable(FetchOptions.Builder.withLimit(100))) {
				datastore.delete(session.getKey());
				cleared++;
			}

		} catch (Exception e) {
		}

	}
}
