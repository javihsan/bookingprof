package com.diloso.bookhair.app.negocio.utils;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;

public class NullAwareBeanUtilsBean extends BeanUtilsBean {

    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if(value==null)return;
        try {
        	Object destValue = PropertyUtils.getProperty(dest, name);
			if(destValue!=null) return;
		} catch (NoSuchMethodException e) {
			return;
		}
        super.copyProperty(dest, name, value);
    }

}

