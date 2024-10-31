package com.diloso.weblogin.aut;

import java.util.Arrays;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class FirmSecurityDAO {
	
	protected static final String FIRM_TYPE = "Firm";
	protected static final String FIR_SERVER = "firServer";
	protected static final String FIR_DOMAIN = "firDomain";
	protected static final String ENABLED = "enabled";
	
	public Entity findFirmServer(String server) {

		return findFirm(FIR_SERVER, server);	
	}	

	
	public Entity findFirmDomain(String domain) {

    	return findFirm(FIR_DOMAIN, domain);	
	}
	
	protected Entity findFirm(String key, String value) {

	   	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    	Query query = new Query(FIRM_TYPE);
    	
    	Filter filter1 = new Query.FilterPredicate(key, FilterOperator.EQUAL, value);
    	Filter filter2 = new Query.FilterPredicate(ENABLED, FilterOperator.EQUAL, 1);
    
    	CompositeFilter compositeFilter = new CompositeFilter(CompositeFilterOperator.AND, Arrays.asList(filter1, filter2)); 
    	query.setFilter(compositeFilter);
    	PreparedQuery prepare = datastore.prepare(query);
    	Entity firm = prepare.asSingleEntity();
    	return firm;		
	}
	
}