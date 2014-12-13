package br.ufrj.cc.aleph.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import br.ufrj.cc.aleph.helper.GmailAuthenticator;

@Service
public class MailService {

	public void processMail( final String mailTo, final String subject,
			final String messageBody, final String mailFrom )
			throws AddressException, MessagingException {

		// final String smtpHost = this.emailConfig.getSmtp();
		final String smtpHost = "email-smtp.us-west-2.amazonaws.com";
		final String emailAccount = "AKIAIAKA33BN5DAWFAQA";
		final String emailPassword = "AoHlUFSMs5cIXnN+USRf80PUbruOlW3KnhzutCtuEIIm";
		final String smtpPort = "587";
		// final String smtpPort = this.emailConfig.getPort();

		final Properties props = System.getProperties();
		props.put( "mail.smtp.starttls.enable", "true" );
		props.put( "mail.smtp.host", smtpHost );
		props.put( "mail.smtp.user", emailAccount );
		props.put( "mail.smtp.password", emailPassword );
		props.put( "mail.smtp.port", smtpPort );
		props.put( "mail.smtp.auth", "true" );

		final String[] to = { mailTo };

		final Session session = Session.getInstance( props,
				new GmailAuthenticator( emailAccount, emailPassword ) );

		final MimeMessage message = new MimeMessage( session );
		final InternetAddress[] toAddress = new InternetAddress[to.length];

		for ( int i = 0; i < to.length; ++i ) {
			toAddress[i] = new InternetAddress( to[i] );
		}

		for ( int i = 0; i < toAddress.length; ++i ) {
			message.addRecipient( Message.RecipientType.TO, toAddress[i] );
		}

		message.setFrom( new InternetAddress( mailFrom ) );
		message.setHeader( "content-type", "text/html" );
		message.setSubject( subject );
		message.setContent( messageBody, "text/html" );

		final Transport transport = session.getTransport( "smtp" );

		transport.connect( smtpHost, emailAccount, emailPassword );
		transport.sendMessage( message, message.getAllRecipients() );
		transport.close();
	}

}
