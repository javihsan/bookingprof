package com.diloso.bookhair.web.controllers;

import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.negocio.dto.generator.NotifCalendarDTO;
import com.diloso.bookhair.app.negocio.utils.ApplicationContextProvider;
import com.diloso.bookhair.app.negocio.utils.templates.Generator;

@Controller
@RequestMapping("/webmail")
public class WebMailController {

	protected static final Logger log = Logger.getLogger(WebMailController.class.getName());
	
	@Autowired
	protected MessageSource messageSourceApp;
	
	@Autowired
	protected Generator generatorVelocity;
		
	protected void sendMail(HttpServletRequest arg0, HttpServletResponse arg1, 
			NotifCalendarDTO modelNot, String templateKey) throws Exception {
		
		try {
			
			Locale locale = RequestContextUtils.getLocale(arg0);
			
			if (messageSourceApp==null){
				messageSourceApp = (MessageSource) ApplicationContextProvider.getApplicationContext().getBean("messageSource");
				generatorVelocity = (Generator) ApplicationContextProvider.getApplicationContext().getBean("generatorVelocity");
			}
			
			String sender = messageSourceApp.getMessage("web.mail.sender", null, locale);
			String recipientCC=null;
			try {
				recipientCC = messageSourceApp.getMessage("web.mail.recipientCC", null, locale);
			} catch (NoSuchMessageException e) {
			}
						
			String recEmail = messageSourceApp.getMessage("web.mail.recipient", null, locale);
									
			if (recEmail!=null){
				Properties props = new Properties();
				Session mailSession = Session.getDefaultInstance(props, null);    
				MimeMessage msg = new MimeMessage(mailSession);
				msg.setFrom(new InternetAddress(sender));
				msg.addRecipient(Message.RecipientType.TO,
				                 new InternetAddress(recEmail));
				if (recipientCC!= null){
					msg.addRecipient(Message.RecipientType.BCC,
		                 new InternetAddress(recipientCC));
				}			
				msg.setSubject(modelNot.getNocSummary(), "UTF-8");
	
				Multipart multipart = new MimeMultipart();
				
				// Text/html
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(modelNot.getNocContent(),  "text/html; charset=UTF-8");
				multipart.addBodyPart(messageBodyPart);
				
				/*
				// Attachment ics
				String fileName = messageSourceApp.getMessage(templateKey+".fileName", null, locale);
				String templatePath = messageSourceApp.getMessage(templateKey+".template", null, locale);
				String encode = messageSourceApp.getMessage(templateKey+".encode", null, locale);

				messageBodyPart = new MimeBodyPart();
			    messageBodyPart.setFileName(fileName);
			    modelNot.setNocOrgEmail(sender);
			    String content = generatorVelocity.generate(modelNot, templatePath).toString();
				DataSource src = new ByteArrayDataSource(content, encode);
				messageBodyPart.setDataHandler(new DataHandler(src));
				multipart.addBodyPart(messageBodyPart);*/
			    
			    msg.setContent(multipart);
							
				Transport.send(msg);
				
				log.info("WebMailController mail enviado : "+modelNot.getNocSummary());
				
			}
				
		} catch (SendFailedException e) {
			log.severe(e.getMessage());
		}

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/sendMailInfo")
	public void sendMailInfo(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		String infName = arg0.getParameter("name");
		String infSurname = arg0.getParameter("surname");
		String infEmail = arg0.getParameter("email");
		String infTelf = arg0.getParameter("telf");
		String infComment = arg0.getParameter("comment");
		String infAddress = "";//arg0.getParameter("address");
		
		String name = infSurname+", "+infName;
		String contact = infEmail+", "+infTelf;
		
		NotifCalendarDTO modelNot = new NotifCalendarDTO();
		modelNot.setLocale(locale);
		
		modelNot.setNocDesEmail(contact);
		modelNot.setNocDesName(name);
	    modelNot.setNocLocation(infAddress);
	    
	    String title = messageSourceApp.getMessage("web.mail.info.title", null, locale);
		title += " " + name;
		
		String content = "<div>" + title + "</div>" + messageSourceApp.getMessage("web.mail.info.text", null, locale);
		content = generatorVelocity.generateContent(modelNot, content).toString();
		
		if (infComment!= null && !infComment.equals("")){
			content += "<p>"+infComment+"</p>";
		}

		modelNot.setNocSummary(title);
	    modelNot.setNocContent(content);
		
		sendMail ( arg0, arg1, modelNot, "web.mail.info");
	
		arg1.sendRedirect("/home?actionLoad=emailok");
	
	}

	
}
