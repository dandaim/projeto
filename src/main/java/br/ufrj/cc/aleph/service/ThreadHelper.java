package br.ufrj.cc.aleph.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

		try {

			semaforo.acquire();

			UserRequest userRequest = userRequestQueue.poll();

			ApplicationContext context = new ClassPathXmlApplicationContext(
					"classpath:spring/mail.xml" );

			MailHelper mailHelper = ( MailHelper ) context
					.getBean( "mailHelper" );

			mailHelper.sendEmail( userRequest.getEmail(), "teste",
					MailContentEnum.INIT.getMsg(), userRequest.getName() );

			String templatePath = StorageHelper.generateTemplate(
					userRequest.getName(), userRequest.getEmail(),
					userRequest.getNumFiles(), userRequest.getFolder() );

			ProcessBuilder pb = new ProcessBuilder( "/bin/bash", templatePath );

			Process p = pb.start();

			synchronized ( p ) {

				p.waitFor();
			}

			semaforo.release();

			mailHelper.sendEmail( userRequest.getEmail(), "teste",
					MailContentEnum.END.getMsg(), userRequest.getName() );

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
		}
	}

	public static void main( String[] args ) throws IOException,
			InterruptedException {

		String template = "/home/daniel/pacote/92d4b920096df6a8544c04e4e2ad63ac/script.sh";

		ProcessBuilder pb = new ProcessBuilder( "/bin/bash", template );

		Process p = pb.start();

		System.out.println( p.waitFor() );

		InputStream stderr = p.getErrorStream();
		BufferedReader brCleanUp = new BufferedReader( new InputStreamReader(
				stderr ) );
		String line;

		while ( ( line = brCleanUp.readLine() ) != null ) {
			System.out.println( "[Stderr] " + line );
		}

		// String[] cmd = { "/bin/bash",
		// "/home/daniel/pacote/92d4b920096df6a8544c04e4e2ad63ac/script.sh" };
		//
		// Process process = Runtime.getRuntime().exec(cmd);
		//
		// System.out.println( process.waitFor() );

	}

}
