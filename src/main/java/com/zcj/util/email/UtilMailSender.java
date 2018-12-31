package com.zcj.util.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class UtilMailSender {

	private static Session getSession(String host, int port, String username, String pasword) {
		MailAuthenticator authenticator = null;
		authenticator = new MailAuthenticator(username, pasword);

		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", true);

		Session session = Session.getInstance(properties, authenticator);
		return session;
	}

	public static void sendText(String host, int port, String username, String password, String from, String to, String subject,
			String content) throws MessagingException {
		Message mailMessage = new MimeMessage(getSession(host, port, username, password));
		mailMessage.setFrom(new InternetAddress(from));
		mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		mailMessage.setSubject(subject);
		mailMessage.setSentDate(new Date());
		mailMessage.setText(content);
		Transport.send(mailMessage);
	}

	public static void sendHtml(String host, int port, String username, String password, String from, String to, String subject,
			String content) throws MessagingException {
		Message mailMessage = new MimeMessage(getSession(host, port, username, password));
		mailMessage.setFrom(new InternetAddress(from));
		mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		mailMessage.setSubject(subject);
		mailMessage.setSentDate(new Date());

		Multipart mainPart = new MimeMultipart();
		BodyPart html = new MimeBodyPart();
		html.setContent(content, "text/html; charset=utf-8");
		mainPart.addBodyPart(html);
		mailMessage.setContent(mainPart);

		Transport.send(mailMessage);
	}

}