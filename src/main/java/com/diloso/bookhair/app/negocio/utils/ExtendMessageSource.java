package com.diloso.bookhair.app.negocio.utils;

import java.util.Locale;
import java.util.Properties;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class ExtendMessageSource extends ReloadableResourceBundleMessageSource {
	
	 public Properties getResolvedProps(String lanCode) {
		 PropertiesHolder pr = super.getMergedProperties(new Locale(lanCode));
         return pr.getProperties();
	 }
	
	
}
