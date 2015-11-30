package org.vasttrafik.wso2.carbon.apimgt.portal.api.utils;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.axis2.context.ConfigurationContext;

import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.axis2.engine.AxisConfiguration;

import org.wso2.carbon.core.CarbonConfigurationContextFactory;

public final class MailSender {

	private static Properties transportProperties;
	private static Authenticator authenticator;
	
	public MailSender() {
	}

	public static void send(org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Message im) throws Exception {
		if (transportProperties == null)
			loadTransportProperties();
			
		MimeMessage mime = null;
		
		try {
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(im.getBody());
			MimeMultipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			
			Session session = Session.getInstance(transportProperties, getAuthenticator());
			mime = new MimeMessage(session);
			mime.setFrom(new InternetAddress(im.getFrom()));
			mime.setSubject(im.getSubject());
			mime.setContent(multipart);
		}
		catch (Exception e) {
			throw e;
		}
		
		addRecipients(Message.RecipientType.TO, mime, im.getTo().split(";| "));
		send(mime);
	}
	
	private static Authenticator getAuthenticator() {
		Boolean authenticate = Boolean.valueOf(transportProperties.getProperty("mail.smtp.auth", "true"));
		
		if (authenticate && authenticator == null) {
			String user = transportProperties.getProperty("mail.smtp.user");
			String pass = transportProperties.getProperty("mail.smtp.password");
			authenticator = new SMTPAuthenticator(user, pass);
		}
		
		return authenticator;
	}
	
	private static void addRecipients(Message.RecipientType recipientType, MimeMessage message, Object recipientObject) throws Exception {
		
        try {
            if (recipientObject instanceof String[]) {
                String[] to = (String[]) recipientObject;
                InternetAddress[] recipientAddresses = new InternetAddress[to.length];
				
                for (int i = 0; i < to.length; i++) {
                    recipientAddresses[i] = new InternetAddress(to[i]);
                }
				
                message.addRecipients(recipientType, recipientAddresses);
            } 
			else if (recipientObject instanceof String) {
                message.addRecipient(recipientType, new InternetAddress((String) recipientObject));
            } 
			else {
                throw new Exception("The argument to this function should be an array of email addresses or a single email address");
            }
        } 
		catch (Exception e) {
           throw e;
        }
    }
	
	private static void send(MimeMessage message) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(javax.mail.Session.class.getClassLoader());
		
        try {
            Transport.send(message);
        } 
		catch (Exception e) {
            throw e;
        } 
		finally {
            Thread.currentThread().setContextClassLoader(classLoader);
        }
    }
	
	private static void loadTransportProperties() throws Exception {
	
		transportProperties = new Properties();
	
		try {
			ConfigurationContext configContext = CarbonConfigurationContextFactory.getConfigurationContext();
			AxisConfiguration axisConfig = configContext.getAxisConfiguration();
			TransportOutDescription mailto = axisConfig.getTransportOut("mailto"); 
			ArrayList<Parameter> parameters = mailto.getParameters();
			
            for (Parameter parameter : parameters) {
				String prop = parameter.getName();
				String value = (String)parameter.getValue();
				transportProperties.setProperty(prop, value);
			}
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	private static class SMTPAuthenticator extends Authenticator {

        private String username;
		private String password;

        private SMTPAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }
}