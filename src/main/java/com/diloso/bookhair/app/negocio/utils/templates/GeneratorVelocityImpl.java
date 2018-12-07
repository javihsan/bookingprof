package com.diloso.bookhair.app.negocio.utils.templates;


import java.io.IOException;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Component;

@Component("generatorVelocity")
public class GeneratorVelocityImpl<T> implements Generator<T> {
		
	protected VelocityContext _ctx;	

	public GeneratorVelocityImpl() {
		
	}
		
	/**
	 * Inicializa la instancia de Velocity. Configura el buscador de recursos como 
	 * tipo classpath.
	 * 
	 * @throws Exception
	 */
	protected void initVelocity() throws Exception {
		Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		Velocity.init();

		_ctx = new VelocityContext();
	}
	
	public StringBuffer generate(T model, String templatePath) throws Exception {
		
		try {
			initVelocity();
		} catch (Exception e) {
			throw new Exception("Error inicializando velocity", e);
		}
			
		Template tpl = null;
		try {
			tpl = Velocity.getTemplate(templatePath);
		} catch (ResourceNotFoundException e) {
			throw new Exception("No se ha podido encontrar la plantilla '" + templatePath + "'", e);
		} catch (ParseErrorException e) {
			throw new Exception("Plantilla '" + templatePath 
					+ "' está mal formada.", e);
		} catch (Exception e) {
			throw new Exception("Error accediendo a la plantilla '" + templatePath + "'", e);
		}
		 
		// Agrega una variable al contexto luego puede ser usada
		// como $salida
		_ctx.put("model", model);
 
		// Creo un Writer para almacenar la salida
		StringWriter writer = new StringWriter();
 
		// Genero la mezcla del template con sus las variables
		// del contexto y macros de velocity
		try {
			tpl.merge(_ctx, writer);
		} catch (ResourceNotFoundException e) {
			throw new Exception("Error procesando la plantilla '" + templatePath , e);
		} catch (ParseErrorException e) {
			throw new Exception("Error procesando la plantilla '" + templatePath, e);
		} catch (MethodInvocationException e) {
			throw new Exception("Error procesando la plantilla '" + templatePath, e);
		} catch (IOException e) {
			throw new Exception("Error procesando la plantilla '" + templatePath, e);
		}
		
		return writer.getBuffer();
	}

	
	public StringBuffer generateContent(T model, String templateContent) throws Exception {
		
		try {
			initVelocity();
		} catch (Exception e) {
			throw new Exception("Error inicializando velocity", e);
		}
		
		// Agrega una variable al contexto luego puede ser usada
		// como $salida
		_ctx.put("model", model);
 
		// Creo un Writer para almacenar la salida
		StringWriter writer = new StringWriter();
		
		try {
			Velocity.evaluate(_ctx, writer, getClass().getName(), templateContent);
		} catch (ParseErrorException e) {
			throw new Exception("Plantilla '" + templateContent 
					+ "' está mal formada.", e);
		} 
				
		return writer.getBuffer();
	}
	
}
