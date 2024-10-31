package com.diloso.bookhair.app.persist.manager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EMF implements IEMF{
    protected static final EntityManagerFactory emfInstance =
        Persistence.createEntityManagerFactory("transactions-optional");

    protected EMF() {}

    public EntityManagerFactory get() {
        return emfInstance;
    }
}
