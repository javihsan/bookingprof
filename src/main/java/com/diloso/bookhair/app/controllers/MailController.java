package com.diloso.bookhair.app.controllers;

import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.negocio.dto.generator.NotifCalendarDTO;
import com.diloso.bookhair.app.negocio.utils.ApplicationContextProvider;
import com.diloso.bookhair.app.negocio.utils.templates.Generator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value={"/*/mail", "/mail"})
public class MailController {

	protected static final Logger log = Logger.getLogger(MailController.class.getName());
		
	public static final int MAX_RETRYS_EMAIL = 3;
	
	protected static final String DEMO = "Demo Beauty";
	
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
			
			String sender = messageSourceApp.getMessage("mail.sender", null, locale);
			String fileName = messageSourceApp.getMessage(templateKey+".fileName", null, locale);
			String templatePath = messageSourceApp.getMessage(templateKey+".template", null, locale);
			String encode = messageSourceApp.getMessage(templateKey+".encode", null, locale);
						
			String recEmail = modelNot.getNocDesEmail();
			String recipientCC = modelNot.getNocDesEmailCC();
									
			if (recEmail!=null){
				Properties props = new Properties();
				Session mailSession = Session.getDefaultInstance(props, null);    
				MimeMessage msg = new MimeMessage(mailSession);
				msg.setFrom(new InternetAddress(sender));
				msg.addRecipient(Message.RecipientType.TO,
				                 new InternetAddress(recEmail));
				if (recipientCC!= null){
					msg.addRecipient(Message.RecipientType.BCC,	new InternetAddress(recipientCC));
				}			
				msg.setSubject(modelNot.getNocSummary(), "UTF-8");
	
				Multipart multipart = new MimeMultipart();
				
				// Text/html
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(modelNot.getNocContent(),  "text/html; charset=UTF-8");
				multipart.addBodyPart(messageBodyPart);
				
				// Attachment ics
				messageBodyPart = new MimeBodyPart();
			    messageBodyPart.setFileName(fileName);
			    modelNot.setNocOrgEmail(sender);
			    String content = generatorVelocity.generate(modelNot, templatePath).toString();
				DataSource src = new ByteArrayDataSource(content, encode);
				messageBodyPart.setDataHandler(new DataHandler(src));
				//messageBodyPart.attachFile(new File("path/to/file"));
				multipart.addBodyPart(messageBodyPart);
			    
			    msg.setContent(multipart);
		
				Transport.send(msg);
				
				log.info("SendController mail enviado : "+modelNot.getNocSummary());
			}
				
		} catch (SendFailedException e) {
			log.severe(e.getMessage());
			throw e;
		}

	}
	
	@RequestMapping("/operator/invite")
	public void invite(HttpServletRequest arg0, HttpServletResponse arg1, 
			NotifCalendarDTO modelNot) throws Exception {
		
		sendMail ( arg0, arg1, modelNot, "mail.invite");
			
	}
	
	@RequestMapping("/operator/cancel")
	public void cancel(HttpServletRequest arg0, HttpServletResponse arg1, 
			NotifCalendarDTO modelNot) throws Exception {
		
		sendMail ( arg0, arg1, modelNot, "mail.cancel");
			
	}
	
	@RequestMapping("/admin/report")
	public void report(HttpServletRequest arg0, HttpServletResponse arg1, 
			NotifCalendarDTO modelNot) throws Exception {
		
		sendMail ( arg0, arg1, modelNot, "mail.report");
			
	}

	public void setMessageSourceApp(MessageSource messageSourceApp) {
		this.messageSourceApp = messageSourceApp;
	}

	public void setGeneratorVelocity(Generator generatorVelocity) {
		this.generatorVelocity = generatorVelocity;
	}
	
	
}
