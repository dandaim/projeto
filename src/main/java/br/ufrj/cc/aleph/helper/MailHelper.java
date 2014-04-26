package br.ufrj.cc.aleph.helper;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class MailHelper {
	
	public static String EMAIL_FROM = "dandaim@gmail.com";
	
	private MailSender mailSender;
	
	public void setMailSender( final MailSender mailSender ) {
		
		this.mailSender = mailSender;
	}
	
	public void sendEmail( final String to, final String subject, final String msg, final String userName ) {
		
		SimpleMailMessage message =  new SimpleMailMessage();
		
		message.setFrom( EMAIL_FROM );
		message.setTo( to );
		message.setSubject( subject );
		message.setText( String.format( msg , userName ) );
		
		mailSender.send( message );
		
	}
	
}
