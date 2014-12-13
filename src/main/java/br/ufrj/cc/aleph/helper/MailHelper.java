package br.ufrj.cc.aleph.helper;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailHelper {

	public static String EMAIL_FROM = "ufrj.cc.aleph@gmail.com";

	private JavaMailSender mailSender;

	public void setMailSender( final JavaMailSender mailSender ) {

		this.mailSender = mailSender;
	}

	public void sendEmail( final String to, final String subject,
			final String msg, final String userName, File... attachs ) {

		try {

			MimeMessage message = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper( message, true,
					"UTF-8" );

			helper.setFrom( EMAIL_FROM );
			helper.setTo( to );
			helper.setSubject( subject );
			helper.setText( String.format( msg, userName ) );

			if ( attachs != null && attachs.length > 0 ) {

				for ( File file : attachs ) {

					helper.addAttachment( file.getName(), file );
				}
			}

			mailSender.send( message );

		} catch ( Exception e ) {

			e.printStackTrace();
		}

	}
}
