package com.diloso.bookhair.app.negocio.utils;

import com.diloso.bookhair.app.persist.entities.AnnualDiary;
import com.diloso.bookhair.app.persist.entities.Billed;
import com.diloso.bookhair.app.persist.entities.Calendar;
import com.diloso.bookhair.app.persist.entities.Client;
import com.diloso.bookhair.app.persist.entities.Diary;
import com.diloso.bookhair.app.persist.entities.Event;
import com.diloso.bookhair.app.persist.entities.Firm;
import com.diloso.bookhair.app.persist.entities.Invoice;
import com.diloso.bookhair.app.persist.entities.Lang;
import com.diloso.bookhair.app.persist.entities.Local;
import com.diloso.bookhair.app.persist.entities.LocalTask;
import com.diloso.bookhair.app.persist.entities.MultiText;
import com.diloso.bookhair.app.persist.entities.Product;
import com.diloso.bookhair.app.persist.entities.ProductClass;
import com.diloso.bookhair.app.persist.entities.Professional;
import com.diloso.bookhair.app.persist.entities.Repeat;
import com.diloso.bookhair.app.persist.entities.RepeatClient;
import com.diloso.bookhair.app.persist.entities.Resource;
import com.diloso.bookhair.app.persist.entities.SemanalDiary;
import com.diloso.bookhair.app.persist.entities.Sincro;
import com.diloso.bookhair.app.persist.entities.Task;
import com.diloso.bookhair.app.persist.entities.TaskClass;
import com.diloso.bookhair.app.persist.entities.Where;
import com.diloso.bookhair.app.persist.entities.Who;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;


/**
 * Class to load bean to persistent into DataStore.
 *
 * @author emaria.ruiz
 * @version 1.0
 *
 */
public class ObjectifyLoader implements ServletContextListener {

    static {
        JodaTimeTranslators.add(ObjectifyService.factory()); 

        ObjectifyService.register(AnnualDiary.class);
        ObjectifyService.register(Billed.class);
        ObjectifyService.register(Calendar.class);
        ObjectifyService.register(Client.class);
        ObjectifyService.register(Diary.class);  
        ObjectifyService.register(Event.class);  
        ObjectifyService.register(Firm.class);  
        ObjectifyService.register(Invoice.class);  
        ObjectifyService.register(Lang.class);  
        ObjectifyService.register(Local.class);  
        ObjectifyService.register(LocalTask.class);  
        ObjectifyService.register(MultiText.class);  
        ObjectifyService.register(Product.class);  
        ObjectifyService.register(ProductClass.class);  
        ObjectifyService.register(Professional.class);  
        ObjectifyService.register(Repeat.class);  
        ObjectifyService.register(RepeatClient.class);  
        ObjectifyService.register(Resource.class);  
        ObjectifyService.register(SemanalDiary.class);  
        ObjectifyService.register(Sincro.class);  
        ObjectifyService.register(Task.class);  
        ObjectifyService.register(TaskClass.class);
        ObjectifyService.register(Where.class);
        ObjectifyService.register(Who.class);

    }

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
    }
}
