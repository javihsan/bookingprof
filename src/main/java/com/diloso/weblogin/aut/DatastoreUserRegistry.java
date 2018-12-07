package com.diloso.weblogin.aut;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class DatastoreUserRegistry implements UserRegistry {
    protected static final String USER_TYPE = "GwtUser";
    protected static final String USER_ENABLED = "enabled";
    protected static final String USER_AUTHORITIES = "authorities";

    public static final String CHAR_SEP_KEY = "_";
    
    public AppUser findUser(String email, Long firmId) {
        
    	String keyName = email;
    	if (firmId!=null){
    		keyName += CHAR_SEP_KEY+firmId;
    	}
    	Key key = KeyFactory.createKey(USER_TYPE, keyName);
        
    	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        try {
            Entity user = datastore.get(key);

            long binaryAuthorities = (Long)user.getProperty(USER_AUTHORITIES);
            Set<AppRole> roles = EnumSet.noneOf(AppRole.class);

            for (AppRole r : AppRole.values()) {
                if ((binaryAuthorities & (1 << r.getBit())) != 0) {
                    roles.add(r);
                }
            }

            AppUser gaeUser = new AppUser(
            		email,
                    roles,
                    (Boolean)user.getProperty(USER_ENABLED),
                    firmId);

            return gaeUser;

        } catch (EntityNotFoundException e) {
            //logger.debug(userId + " not found in datastore");
            return null;
        }
    }

    public void registerUser(AppUser newUser) {
    	
    	String keyName = newUser.getEmail();
    	if (newUser.getFirmId()!=null){
    		keyName += CHAR_SEP_KEY+newUser.getFirmId();
    	}
    	Key key = KeyFactory.createKey(USER_TYPE, keyName);
        Entity user = new Entity(key);
        user.setUnindexedProperty(USER_ENABLED, newUser.isEnabled());

        Collection<? extends GrantedAuthority> roles = newUser.getAuthorities();

        long binaryAuthorities = 0;

        for (GrantedAuthority r : roles) {
            binaryAuthorities |= 1 << ((AppRole)r).getBit();
        }

        user.setUnindexedProperty(USER_AUTHORITIES, binaryAuthorities);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(user);
    }

    public void removeUser(String email, Long firmId) {
        
    	String keyName = email;
    	if (firmId!=null){
    		keyName += CHAR_SEP_KEY+firmId;
    	}
    	Key key = KeyFactory.createKey(USER_TYPE, keyName);
        
    	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.delete(key);
    }
}
