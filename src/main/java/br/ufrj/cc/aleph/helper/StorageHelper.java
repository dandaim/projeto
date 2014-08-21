package br.ufrj.cc.aleph.helper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class StorageHelper {

	private static final Logger LOGGER = Logger.getLogger( StorageHelper.class );

	// public static String commonPath = "/home/ec2-user/pacote/";
	public static String commonPath;

	// public static String alephPath = "/home/ec2-user/uw_aleph/";
	public static String alephPath;

	@PostConstruct
	public void setPaths() {

		alephPath = System.getProperty( "aleph.path" );
		commonPath = System.getProperty( "common.path" );

	}

	public static String getNextPath( String md5Email ) {

		return String.valueOf( Calendar.getInstance().getTimeInMillis() );
	}

	public static String generateTemplate( final String userName, final String userEmail, final int numFiles, final String folder ) {

		try {

			FileWriter outFile = new FileWriter( folder + "/script.sh" );
			PrintWriter out = new PrintWriter( outFile );

			out.print( "string=\"cd('" + alephPath + "\')." );
			out.print( " [aleph]." );
			out.print( " cd('" + folder + "')." );

			// Adicionando leitura de todos os arquivos na execução do script
			for ( int i = 0; i < numFiles; i++ ) {

				out.print( " read_all(arqb" + i + ", arqpos" + i + ", arqneg" + i + ")." );
				out.print( " induce." );
			}

			out.println( " halt.\"" );
			out.println( " yap <<< $string > result.out" );
			// out.println(" exit 0" );
			out.close();

		} catch ( IOException e ) {

			System.out.println( "Erro na classe StorageHelper no método generateTemplate" );

			System.out.println( "Erro: " + e.getMessage() );
			e.printStackTrace();

		}

		return folder + "/script.sh";

	}

	public static String generateFileRdf( final String folder, final String UUID ) throws Exception {

		try {

			LOGGER.info( "{" + UUID + "} -> Gerando o arquivo RDF na pasta: " + folder );

			FileWriter prolog = new FileWriter( folder + "/prolog.pl" );
			PrintWriter outProlog = new PrintWriter( prolog );

			outProlog.println( "ini:- open('" + folder + "/result.out',write,_,[alias(escrita)])," );
			outProlog.println( "[library(rdf)]," );
			outProlog.println( "load_rdf('" + folder + "/file.rdf', [H|T])," );
			outProlog.println( "checklist(assert, [H|T])," );
			outProlog.println( "ini1." );
			outProlog.println( "ini:-close(escrita)." );
			outProlog.println( "ini1:-" );
			outProlog.println( "rdf(X,Y,Z)," );
			outProlog.println( "escreve(X,Y,Z)," );
			outProlog.println( "fail." );
			outProlog.println( "escreve(X,Y,Z):-" );
			outProlog.println( "write(escrita,':- ')," );
			outProlog.println( "write(escrita,Y)," );
			outProlog.println( "write(escrita,'(')," );
			outProlog.println( "write(escrita,X)," );
			outProlog.println( "write(escrita,',')," );
			outProlog.println( "write(escrita,Z)," );
			outProlog.println( "write(escrita,')')," );
			outProlog.println( "write(escrita,'.')," );
			outProlog.println( "nl(escrita)." );

			outProlog.close();

			FileWriter outFile = new FileWriter( folder + "/script.sh" );
			PrintWriter out = new PrintWriter( outFile );

			out.print( "string=\"['" + folder + "/prolog.pl']." );
			out.print( " ini." );
			out.println( " \"" );
			out.println( " prolog <<< $string" );
			out.close();

			LOGGER.info( "{" + UUID + "} -> Executando o script: gerador de resultado do rdf" );

			ProcessBuilder pb = new ProcessBuilder( "/bin/bash", folder + "/script.sh" );

			Process p = pb.start();

			synchronized ( p ) {

				p.waitFor();
			}

		} catch ( IOException e ) {

			LOGGER.error( "{" + UUID + "} -> Erro de input ao gerar arquivo rdf: " + e.getMessage() );
			throw new Exception( e );

		} catch ( InterruptedException e ) {

			LOGGER.error( "{" + UUID + "} -> Erro de execução da thread do script rdf: " + e.getMessage() );
			throw new Exception( e );
		}

		return folder + "/script.sh";

	}

}
