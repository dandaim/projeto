package br.ufrj.cc.aleph.service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.ufrj.cc.aleph.domain.UserRequest;
import br.ufrj.cc.aleph.helper.MailContentEnum;
import br.ufrj.cc.aleph.helper.MailHelper;
import br.ufrj.cc.aleph.helper.StorageHelper;

public class ThreadHelper implements Runnable {

	private static ThreadHelper instance = new ThreadHelper();

	public ThreadHelper() {

	}

	public static ThreadHelper getInstance() {

		return instance;
	}

	public static Queue<UserRequest> userRequestQueue = new LinkedList<UserRequest>();
	private static Semaphore semaforo = new Semaphore( 1 );

	public void run() {

		String userName = null;
		String userEmail = null;

		try {

			semaforo.acquire();

			UserRequest userRequest = userRequestQueue.poll();

			userName = userRequest.getName();
			userEmail = userRequest.getEmail();

			ApplicationContext context = new ClassPathXmlApplicationContext(
					"classpath:spring/mail.xml" );

			MailHelper mailHelper = ( MailHelper ) context
					.getBean( "mailHelper" );

			mailHelper.sendEmail( userRequest.getEmail(),
					"DAHELE - Início do processamento",
					MailContentEnum.INIT.getMsg(), userRequest.getName() );

			String templatePath = "";

			if ( userRequest.isAleph() ) {
				templatePath = StorageHelper.generateTemplate(
						userRequest.getName(), userRequest.getEmail(),
						userRequest.getNumFiles(), userRequest.getFolder() );
			} else {

				templatePath = StorageHelper.generateTemplateRdf(
						userRequest.getName(), userRequest.getEmail(),
						userRequest.getFolder() );
			}

			ProcessBuilder pb = new ProcessBuilder( "/bin/bash", templatePath );

			Process p = pb.start();

			synchronized ( p ) {

				p.waitFor();
			}

			semaforo.release();

			File resultOut = new File( userRequest.getFolder() + "/result.out" );
			File resultRs = new File( userRequest.getFolder() + "/Rs" );

			mailHelper.sendEmail( userRequest.getEmail(),
					"DAHELE - Resultados da Requisição",
					MailContentEnum.END.getMsg(), userRequest.getName(),
					resultOut, resultRs );

		} catch ( IOException e ) {

			System.out.println( "Erro na classe ThreadHelper" );
			System.out.println( "Erro: " + e.getMessage() );
			e.printStackTrace();
		} catch ( InterruptedException e ) {

			System.out.println( "Erro na classe ThreadHelper" );
			System.out.println( "Erro: " + e.getMessage() );
			e.printStackTrace();
		} catch ( Exception e ) {

			System.out.println( "Erro na classe ThreadHelper" );
			System.out.println( "Erro: " + e.getMessage() );
			e.printStackTrace();

			ApplicationContext context = new ClassPathXmlApplicationContext(
					"classpath:spring/mail.xml" );

			MailHelper mailHelper = ( MailHelper ) context
					.getBean( "mailHelper" );

			mailHelper.sendEmail( userEmail, "DAHELE - Erro na requisição",
					MailContentEnum.ERROR.getMsg(), userName );
		}
	}
}
