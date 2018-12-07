package com.diloso.bookhair.web.server;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class FileUpload extends HttpServlet {
	protected final static BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		try {
			Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
			BlobKey blobKey = null;
			for (String key : blobs.keySet()) {
				blobKey = blobs.get(key);
				req.setAttribute(key, blobKey.getKeyString());
			}
			
			String urlRedirect = req.getParameter("urlRedirect");
			
			ServletContext sc = super.getServletConfig().getServletContext(); 
			RequestDispatcher rd = sc.getRequestDispatcher("/web/"+urlRedirect);
			rd.forward(req, res);
		} catch (Exception e) {
		}
		
	}
}
