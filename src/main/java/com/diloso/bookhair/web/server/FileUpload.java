package com.diloso.bookhair.web.server;

import java.io.IOException;
import java.util.Map;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FileUpload extends HttpServlet {
	protected final static BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		try {
			//Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
			Map<String, BlobKey> blobs = null;
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
