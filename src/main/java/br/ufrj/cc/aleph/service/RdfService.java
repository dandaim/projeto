package br.ufrj.cc.aleph.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrj.cc.aleph.controller.form.TripletsForm;
import br.ufrj.cc.aleph.domain.UserRequest;
import br.ufrj.cc.aleph.helper.MailContentEnum;
import br.ufrj.cc.aleph.helper.MailHelper;
import br.ufrj.cc.aleph.helper.Md5Helper;
import br.ufrj.cc.aleph.helper.StorageHelper;

@Service
public class RdfService {

	private static final Logger LOGGER = Logger.getLogger( RdfService.class );

	public static Runnable r = ThreadHelper.getInstance();

	@Autowired
	private MailHelper mailHelper;

	public void generateRdfFile( TripletsForm tripletsForm, final String UUID )
			throws Exception {

		String pathFolder = StorageHelper.commonPath
				+ Md5Helper.md5( tripletsForm.getEmail() );

		String nextFolder = "/"
				+ StorageHelper.getNextPath( Md5Helper.md5( tripletsForm
						.getEmail() ) );

		pathFolder += nextFolder;

		LOGGER.info( "{" + UUID + "} -> Pasta a ser criada: " + pathFolder );

		File file = new File( pathFolder );
		file.mkdirs();

		String mode = this.saveExamplesFile( tripletsForm, pathFolder, UUID );

		if ( tripletsForm.getOption().equals( "url" ) ) {

			LOGGER.info( "{" + UUID
					+ "} -> Usuário optou por gerar rdf a partir da url: "
					+ tripletsForm.getUrl() );

			generateRdfFromUrl( tripletsForm, pathFolder, UUID, mode );

		} else {

			LOGGER.info( "{" + UUID
					+ "} -> Usuário optou por gerar rdf a partir do arquivo" );

			generateRdfFromFile( tripletsForm, pathFolder, UUID, mode );
		}

		UserRequest userRequest = new UserRequest( tripletsForm.getEmail(),
				tripletsForm.getName(), 0, pathFolder, false );

		LOGGER.info( "{" + UUID + "} -> Adicionando a requisição na fila: " );

		ThreadHelper.userRequestQueue.add( userRequest );

		LOGGER.info( "{" + UUID + "} -> Enviando email para o usuário: "
				+ tripletsForm.getEmail() );

		mailHelper.sendEmail( tripletsForm.getEmail(),
				"DAHELE - Envio de requisição",
				MailContentEnum.REQUEST.getMsg(), tripletsForm.getName() );

		LOGGER.info( "{" + UUID + "} -> Iniciando a thread da requisição." );

		Thread thr = new Thread( r );
		thr.start();
	}

	private String saveExamplesFile( TripletsForm tripletsForm,
			String pathFolder, String UUID ) {

		String mode = "";

		try {

			LOGGER.info( "{"
					+ UUID
					+ "} -> Lendo arquivo de exemplos para gerar o as arquivos positivos e negativos " );

			BufferedReader br = new BufferedReader( new InputStreamReader(
					tripletsForm.getExamples().getInputStream() ) );

			String sCurrentLine = null;

			String regularExpression = "kb:([^\"]*)\"";

			Pattern p = Pattern.compile( regularExpression );
			Matcher m = null;

			boolean positives = false;
			boolean negatives = false;

			List<String> positiveList = new ArrayList<String>();
			List<String> negativeList = new ArrayList<String>();

			while ( ( sCurrentLine = br.readLine() ) != null ) {

				if ( positives ) {

					if ( sCurrentLine.contains( "}" ) ) {
						positives = false;
					} else {
						m = p.matcher( sCurrentLine );

						if ( m.find() ) {
							positiveList.add( tripletsForm.getTarget() + "("
									+ m.group( 1 ) + ")." );
						}
					}
				} else if ( negatives ) {

					if ( sCurrentLine.contains( "}" ) ) {

						negatives = false;

					} else {
						m = p.matcher( sCurrentLine );

						if ( m.find() ) {

							negativeList.add( tripletsForm.getTarget() + "("
									+ m.group( 1 ) + ")." );
						}
					}
				}

				if ( sCurrentLine.contains( "positiveExamples" ) ) {
					positives = true;
				}

				if ( sCurrentLine.contains( "negativeExamples" ) ) {
					negatives = true;
				}
			}

			mode = this.generatePositiveFile( positiveList, pathFolder );
			this.generateNegativeFile( negativeList, pathFolder );

		} catch ( IOException e ) {

			LOGGER.error( "Erro ao gerar arquivos positivos e negativos" );
		}

		return mode;
	}

	private void generateNegativeFile( List<String> negativeList,
			String pathFolder ) {

		try {

			FileWriter result = new FileWriter( pathFolder + "/result-fixed.n" );
			PrintWriter outResult = new PrintWriter( result );

			for ( String string : negativeList ) {

				outResult.println( string );
			}

			result.close();
			outResult.close();

		} catch ( IOException e ) {

			LOGGER.error( "Error generating negative files" );
		}

	}

	private String generatePositiveFile( List<String> positiveList,
			String pathFolder ) {

		String predicate = this.getPredicate( positiveList.get( 0 ) );

		try {
			FileWriter result = new FileWriter( pathFolder + "/result-fixed.f" );
			PrintWriter outResult = new PrintWriter( result );

			for ( String string : positiveList ) {

				outResult.println( string );
			}

			result.close();
			outResult.close();

		} catch ( IOException e ) {

			LOGGER.error( "Error generating positive files" );
		}

		return predicate;
	}

	private String getPredicate( String positiveClause ) {

		return positiveClause.replaceAll( "[^A-Za-z]", "" );
	}

	public void generateRdfFromUrl( TripletsForm tripletsForm,
			String pathFolder, final String UUID, final String mode )
			throws Exception {

		try {

			LOGGER.info( "{" + UUID + "} -> Pegando dados da página " );

			URL website = new URL( tripletsForm.getUrl() );

			URLConnection connection = website.openConnection();
			InputStream file = connection.getInputStream();

			saveRdf( pathFolder, file, UUID, tripletsForm.getTarget(), mode );

		} catch ( MalformedURLException e ) {

			LOGGER.error( "{" + UUID + "} -> URL mal escrita: "
					+ e.getMessage() );
			throw new Exception( e );

		} catch ( IOException e ) {

			LOGGER.error( "{" + UUID + "} -> Erro ao ler conteúdo da URL." );
			throw new Exception( e );
		}

	}

	private void saveRdf( String pathFolder, InputStream file,
			final String UUID, final String target, final String mode )
			throws Exception {

		LOGGER.info( "{" + UUID + "} -> Salvando arquivo rdf. " );

		File arquivo = new File( pathFolder + "/file.rdf" );

		arquivo.createNewFile();

		FileWriter fw = new FileWriter( arquivo.getAbsolutePath() );
		BufferedWriter bw = new BufferedWriter( fw );

		bw.write( PrologService.getStringFromInputStream( file, UUID ) );

		file.close();
		bw.close();

		StorageHelper.generateFileRdf( pathFolder, UUID, target, mode );
	}

	public void generateRdfFromFile( TripletsForm tripletsForm,
			String pathFolder, final String UUID, final String mode )
			throws Exception {

		try {

			InputStream file = tripletsForm.getFile().getInputStream();

			saveRdf( pathFolder, file, UUID, tripletsForm.getTarget(), mode );

		} catch ( IOException e ) {

			LOGGER.error( "{" + UUID + "} -> Erro ao salvar arquivo RDF. " );
			throw new Exception( e );
		}

	}
}
