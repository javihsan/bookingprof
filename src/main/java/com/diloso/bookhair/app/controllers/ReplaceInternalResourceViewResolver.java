package com.diloso.bookhair.app.controllers;
import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public class ReplaceInternalResourceViewResolver extends 
		InternalResourceViewResolver {

	protected String identAPP;
	protected String prefixAPP;
	protected String sufixAPP;
	
	protected String prefixAPPMaterial;
	protected String sufixAPPMaterial;
	
	protected String prefixWEB;
	protected String sufixWEB;
	
    @Override
    public View resolveViewName(String viewName, Locale locale)
            throws Exception {
    	int indx = viewName.indexOf(getIdentAPP());
    	if (indx>=0){
    		if (viewName.endsWith("/booking") || viewName.endsWith("/bookingOperator")){
    			viewName = viewName.substring(indx+getIdentAPP().length());
    			viewName += getSufixAPPMaterial();
    		} else {
    			viewName = getPrefixAPP() + viewName.substring(indx+getIdentAPP().length());
    			viewName += getSufixAPP();
    		}
    	} else {
    		viewName = getPrefixWEB() + viewName;
    		viewName += getSufixWEB();
    	}
    	
        return super.resolveViewName(viewName, locale);
    }
    
	public String getIdentAPP() {
		return identAPP;
	}

	public void setIdentAPP(String identAPP) {
		this.identAPP = identAPP;
	}

	public String getPrefixAPP() {
		return prefixAPP;
	}

	public void setPrefixAPP(String prefixAPP) {
		this.prefixAPP = prefixAPP;
	}

	public String getSufixAPP() {
		return sufixAPP;
	}

	public void setSufixAPP(String sufixAPP) {
		this.sufixAPP = sufixAPP;
	}

	public String getPrefixAPPMaterial() {
		return prefixAPPMaterial;
	}

	public void setPrefixAPPMaterial(String prefixAPPMaterial) {
		this.prefixAPPMaterial = prefixAPPMaterial;
	}

	public String getSufixAPPMaterial() {
		return sufixAPPMaterial;
	}

	public void setSufixAPPMaterial(String sufixAPPMaterial) {
		this.sufixAPPMaterial = sufixAPPMaterial;
	}
	
	public String getPrefixWEB() {
		return prefixWEB;
	}

	public void setPrefixWEB(String prefixWEB) {
		this.prefixWEB = prefixWEB;
	}

	public String getSufixWEB() {
		return sufixWEB;
	}

	public void setSufixWEB(String sufixWEB) {
		this.sufixWEB = sufixWEB;
	}
    
    
    
}
