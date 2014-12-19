package br.ufrj.cc.aleph.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class StorageHelper {

	private static final Logger LOGGER = Logger.getLogger( StorageHelper.class );

	public static String commonPath;

	public static String alephPath;

	@PostConstruct
	public void setPaths() {

		alephPath = System.getProperty( "aleph.path" );
		commonPath = System.getProperty( "common.path" );

	}

	public static String getNextPath( String md5Email ) {

		return String.valueOf( Calendar.getInstance().getTimeInMillis() );
	}

	public static String generateTemplate( final String userName,
			final String userEmail, final int numFiles, final String folder ) {

		try {

			FileWriter outFile = new FileWriter( folder + "/script.sh" );
			PrintWriter out = new PrintWriter( outFile );

			out.print( "string=\"cd('" + alephPath + "\')." );
			out.print( " [aleph]." );
			out.print( " cd('" + folder + "')." );

			// Adicionando leitura de todos os arquivos na execução do script
			for ( int i = 0; i < numFiles; i++ ) {

				out.print( " read_all(arq" + i + ")." );
				out.print( " induce." );
			}

			out.println( " halt.\"" );
			out.println( " yap <<< $string > " + folder + "/result.out" );
			out.close();

		} catch ( IOException e ) {

			System.out
					.println( "Erro na classe StorageHelper no método generateTemplate" );

			System.out.println( "Erro: " + e.getMessage() );
			e.printStackTrace();

		}

		return folder + "/script.sh";

	}

	public static String generateFileRdf( final String folder,
			final String UUID, final String target, final String mode )
			throws Exception {

		try {

			LOGGER.info( "{" + UUID + "} -> Gerando o arquivo RDF na pasta: "
					+ folder );

			FileWriter prolog = new FileWriter( folder + "/prolog.pl" );
			PrintWriter outProlog = new PrintWriter( prolog );

			outProlog.println( "ini:- open('" + folder
					+ "/result.out',write,_,[alias(escrita)])," );
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
			outProlog.println( "write(escrita,'')," );
			outProlog.println( "write(escrita,Y)," );
			outProlog.println( "write(escrita,'(')," );
			outProlog.println( "write(escrita,X)," );
			outProlog.println( "write(escrita,',')," );
			outProlog.println( "write(escrita,Z)," );
			outProlog.println( "write(escrita,')')," );
			outProlog.println( "write(escrita,'.')," );
			outProlog.println( "nl(escrita)." );

			outProlog.close();

			FileWriter outFile = new FileWriter( folder + "/script-prolog.sh" );
			PrintWriter out = new PrintWriter( outFile );

			out.print( "string=\"['" + folder + "/prolog.pl']." );
			out.print( " ini." );
			out.println( " \"" );
			out.println( " prolog <<< $string" );
			out.close();
			outFile.close();

			LOGGER.info( "{" + UUID
					+ "} -> Executando o script: gerador de resultado do rdf" );

			ProcessBuilder pb = new ProcessBuilder( "/bin/bash", folder
					+ "/script-prolog.sh" );

			Process p = pb.start();

			synchronized ( p ) {

				p.waitFor();
			}

			BufferedReader br = new BufferedReader( new FileReader( folder
					+ "/result.out" ) );

			String sCurrentLine;

			String regularExpression = "(http[s]?://|ftp://)?((www\\.)?[a-zA-Z0-9-\\.]+\\.(com|org|net|mil|edu|ca|co.uk|com.au|gov|br)|(localhost))[a-zA-Z0-9/-]*#";

			String regularExpressionUrl = "(http[s]?://|ftp://)?((www\\.)?[a-zA-Z0-9-\\.]+\\.(com|org|net|mil|edu|ca|co.uk|com.au|gov|br|de)|(localhost))[a-zA-Z0-9-\\./-]*";

			String regularExpressionNumber = "[0-9-\\.]+_[a-zA-Z]+";

			String regularExpressionStringNumber = "[a-zA-Z_]+[0-9-\\.]+";

			FileWriter result = new FileWriter( folder + "/result-fixed.b" );
			PrintWriter outResult = new PrintWriter( result );

			Map<String, List<String>> mapLines = new HashMap<String, List<String>>();

			while ( ( sCurrentLine = br.readLine() ) != null ) {

				sCurrentLine = sCurrentLine.toLowerCase();

				sCurrentLine = sCurrentLine.replaceAll( regularExpression, "" );

				sCurrentLine = sCurrentLine.replaceAll( "%20", "_" );

				sCurrentLine = sCurrentLine.replaceAll( " ", "_" );

				Pattern pattern = Pattern.compile( regularExpressionNumber );

				Matcher m = pattern.matcher( sCurrentLine );

				while ( m.find() ) {

					String newValue = m.group( 0 )
							.replaceAll( "_[a-zA-Z]+", "" );
					sCurrentLine = sCurrentLine
							.replace( m.group( 0 ), newValue );
				}

				pattern = Pattern.compile( regularExpressionStringNumber );

				m = pattern.matcher( sCurrentLine );

				while ( m.find() ) {

					String newValue = m.group( 0 )
							.replaceAll( "_[0-9\\.]+", "" );
					sCurrentLine = sCurrentLine
							.replace( m.group( 0 ), newValue );
				}

				pattern = Pattern.compile( regularExpressionUrl );

				m = pattern.matcher( sCurrentLine );

				while ( m.find() ) {

					String[] values = m.group( 0 ).split( "/" );

					sCurrentLine = sCurrentLine.replaceAll( m.group( 0 ),
							values[values.length - 1] );
				}

				sCurrentLine = sCurrentLine.replaceAll( "\\:", "" );

				sCurrentLine = sCurrentLine.replaceAll( "\\.", "" );

				sCurrentLine = sCurrentLine.replaceAll( "\\#", "_" );

				sCurrentLine += ".";

				String[] values = sCurrentLine.split( "\\(" );

				if ( mapLines.containsKey( values[0] ) ) {

					mapLines.get( values[0] ).add( sCurrentLine );
				} else {

					List<String> newList = new ArrayList<String>();
					newList.add( sCurrentLine );
					mapLines.put( values[0], newList );
				}

			}

			outResult.println( ":- set(minpos,2)." );
			outResult.println( ":- modeh(*, " + target + "(+" + mode + "))." );
			outResult.println( ":- modeb(*, " + target + "(+" + mode + "))." );
			outResult.println( ":- modeb(*, " + target + "(-" + mode + "))." );

			for ( Entry<String, List<String>> entry : mapLines.entrySet() ) {

				outResult.println( ":- determination(" + target + "/1, "
						+ entry.getKey() + "/2)." );

			}

			for ( Entry<String, List<String>> entry : mapLines.entrySet() ) {

				for ( String line : entry.getValue() ) {

					outResult.println( line );
				}
			}

			br.close();
			outResult.close();
			result.close();

			if ( !target.isEmpty() && mapLines.containsKey( target ) ) {

				FileWriter targetResult = new FileWriter( folder
						+ "/result-target.out" );
				PrintWriter targetOutResult = new PrintWriter( targetResult );

				for ( String line : mapLines.get( target ) ) {

					targetOutResult.println( line );
				}

				targetResult.close();
				targetOutResult.close();
			}

		} catch ( IOException e ) {

			LOGGER.error( "{" + UUID
					+ "} -> Erro de input ao gerar arquivo rdf: "
					+ e.getMessage() );
			throw new Exception( e );

		} catch ( InterruptedException e ) {

			LOGGER.error( "{" + UUID
					+ "} -> Erro de execução da thread do script rdf: "
					+ e.getMessage() );
			throw new Exception( e );
		} catch ( Exception e ) {
			LOGGER.error( "{" + UUID
					+ "} -> Erro de execução da thread do script rdf: "
					+ e.getMessage() );
			throw new Exception( e );
		}

		return folder + "/script.sh";
	}

	public static void main( String[] args ) {

		String regularExpressionUrl = "(http[s]?://|ftp://)?((www\\.)?[a-zA-Z0-9-\\.]+\\.(com|org|net|mil|edu|ca|co.uk|com.au|gov|br)|(localhost))[a-zA-Z0-9/-]*#";

		String value = "http://localhost/foo#hasRank(http://localhost/foo#card0,http://localhost/foo#king).";

		value = value.replaceAll( regularExpressionUrl, "" );

		System.out.println( value );

	}

	public static String generateTemplateRdf( String name, String email,
			String folder ) {

		try {

			FileWriter outFile = new FileWriter( folder + "/script.sh" );
			PrintWriter out = new PrintWriter( outFile );

			out.print( "string=\"cd('" + alephPath + "\')." );
			out.print( " [aleph]." );
			out.print( " cd('" + folder + "')." );

			out.print( " read_all('result-fixed')." );
			out.print( " induce." );

			out.println( " halt.\"" );
			out.println( " yap <<< $string > " + folder + "/result.out" );
			out.close();

		} catch ( IOException e ) {

			System.out
					.println( "Erro na classe StorageHelper no método generateTemplate" );

			System.out.println( "Erro: " + e.getMessage() );
			e.printStackTrace();

		}

		return folder + "/script.sh";
	}
}
