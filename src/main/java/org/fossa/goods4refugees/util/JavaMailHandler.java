/**
 * Copyright (c) 2015 Frank Kaddereit, Anne Lachnitt, http://www.fossa.de/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.fossa.goods4refugees.util;

import java.net.URL;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class JavaMailHandler {

	public JavaMailHandler() {
		super();
	}

	public void send(String toAddress, String fromAddress, String subject, String body, String host, final String username, final String password, String agbUrl, String widerrufUrl) throws Exception {
		try {
			InternetAddress from;
			try {
				from = new InternetAddress(fromAddress);
			} catch (AddressException e){
				throw new AddressException("Fehler in der Absende-E-Mail-Adresse: " + e.getMessage());			
			}
			InternetAddress to;
			try {
				to = new InternetAddress(toAddress);
			} catch (AddressException e){
				throw new AddressException("Fehler in der Ziel-E-Mail-Adresse: " + e.getMessage());			
			}

			Properties props = System.getProperties();
			props.put("mail.smtp.host", host);
			Authenticator mailAuth = null;
			if (!username.isEmpty()) {
				props.put("mail.smtp.auth", "true");
				mailAuth = new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				};
			}
			
			Session session = Session.getDefaultInstance(props, mailAuth);  
			
			MimeMessage message = new MimeMessage(session);

			message.setFrom(from);
			message.addRecipient(Message.RecipientType.TO, to);
			message.setSubject(subject);
			message.setSentDate(new Date());

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(body, "text/html;charset=utf-8");

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(htmlPart);

			if (agbUrl != null) {
				MimeBodyPart attachFilePart1 = new MimeBodyPart();
				URL url1 = new URL(agbUrl);
				url1.openConnection().connect();
				DataHandler dataHandler1 = new DataHandler(url1);
				attachFilePart1.setDataHandler(dataHandler1);
				String[] nameParts1 = dataHandler1.getName().split("/");
				attachFilePart1.setFileName(nameParts1[nameParts1.length - 1]);
				mp.addBodyPart(attachFilePart1);
			}
			
			if (widerrufUrl != null) {
				MimeBodyPart attachFilePart2 = new MimeBodyPart();
				URL url2 = new URL(widerrufUrl);
				url2.openConnection().connect();
				DataHandler dataHandler2 = new DataHandler(url2);
				attachFilePart2.setDataHandler(dataHandler2);
				String[] nameParts2 = dataHandler2.getName().split("/");
				attachFilePart2.setFileName(nameParts2[nameParts2.length - 1]);
				mp.addBodyPart(attachFilePart2);
			}
			
			message.setContent(mp);
			
			Transport.send(message);
		} catch (MessagingException e) {
			throw new MessagingException("Fehler beim Versenden der E-Mail: " + e.getMessage());
		}

	}
}
