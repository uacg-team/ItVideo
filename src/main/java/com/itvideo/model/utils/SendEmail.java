package com.itvideo.model.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.itvideo.model.User;
import com.itvideo.model.exceptions.user.UserException;


public class SendEmail {
	private static String host = "smtp.gmail.com";
	private static String port = "465";
	private static String from = "noreply.itvideo@gmail.com";
	private static String pass = "itvideo12345";

	public static void welcome(User u) throws MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props);
		
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(from));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(u.getEmail()));
			msg.setSubject("Wellcome to www.itvideo.com");
			
			String emailText = String.format(
					"Wellcome to <strong>www.itvideo.com</strong><br>"
					+ "Your username is <strong>%s</strong><br>"
					+ "Please click <a href=\"%s\">here</a> to activate your account "
					+ "<br><br><br><a href=\"www.itvideo.com\">www.itvideo.com</a> ",
					u.getUsername(), "http://localhost:8080/ItVideo/activate/"+u.getUserId()+"/"+u.getActivationToken());
			
			msg.setContent(emailText, "text/html; charset=utf-8");

			Transport transport = session.getTransport("smtps");
			transport.connect(host, from, pass);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static void forgottenPassword(User u) throws MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props);
		
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(from));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(u.getEmail()));
			msg.setSubject("new password for www.itvideo.com");
			
			String emailText = String.format(
					"Your new password for <strong>www.itvideo.com</strong> is: <strong>%s</strong>"
					+ "<br><br><br><a href=\"www.itvideo.com\">www.itvideo.com</a> ",
					u.getPassword());
			
			msg.setContent(emailText, "text/html; charset=utf-8");

			Transport transport = session.getTransport("smtps");
			transport.connect(host, from, pass);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
