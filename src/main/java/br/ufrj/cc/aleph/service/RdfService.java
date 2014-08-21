package br.ufrj.cc.aleph.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import br.ufrj.cc.aleph.controller.form.TripletsForm;
import br.ufrj.cc.aleph.helper.Md5Helper;
import br.ufrj.cc.aleph.helper.StorageHelper;

@Service
public class RdfService {

	private static final Logger LOGGER = Logger.getLogger( RdfService.class );

	public void generateRdfFile( TripletsForm tripletsForm, final String UUID ) throws Exception {

		String pathFolder = StorageHelper.commonPath + Md5Helper.md5( tripletsForm.getEmail() );

		String nextFolder = "/" + StorageHelper.getNextPath( Md5Helper.md5( tripletsForm.getEmail() ) );

		pathFolder += nextFolder;

		LOGGER.info( "{" + UUID + "} -> Pasta a ser criada: " + pathFolder );

		File file = new File( pathFolder );
		file.mkdirs();

		if ( tripletsForm.getOption().equals( "url" ) ) {

			LOGGER.info( "{" + UUID + "} -> Usuário optou por gerar rdf a partir da url: " + tripletsForm.getUrl() );

			generateRdfFromUrl( tripletsForm, pathFolder, UUID );

		}
		else {

			LOGGER.info( "{" + UUID + "} -> Usuário optou por gerar rdf a partir do arquivo" );

			generateRdfFromFile( tripletsForm, pathFolder, UUID );
		}
	}

	public void generateRdfFromUrl( TripletsForm tripletsForm, String pathFolder, final String UUID ) throws Exception {

		try {

			LOGGER.info( "{" + UUID + "} -> Pegando dados da página " );

			URL website = new URL( tripletsForm.getUrl() );

			URLConnection connection = website.openConnection();
			InputStream file = connection.getInputStream();

			saveRdf( pathFolder, file, UUID );

		} catch ( MalformedURLException e ) {

			LOGGER.error( "{" + UUID + "} -> URL mal escrita: " + e.getMessage() );
			throw new Exception( e );

		} catch ( IOException e ) {

			LOGGER.error( "{" + UUID + "} -> Erro ao ler conteúdo da URL." );
			throw new Exception( e );
		}

	}

	private void saveRdf( String pathFolder, InputStream file, final String UUID ) throws Exception {

		LOGGER.info( "{" + UUID + "} -> Salvando arquivo rdf. " );

		File arquivo = new File( pathFolder + "/file.rdf" );

		arquivo.createNewFile();

		FileWriter fw = new FileWriter( arquivo.getAbsolutePath() );
		BufferedWriter bw = new BufferedWriter( fw );

		bw.write( PrologService.getStringFromInputStream( file, UUID ) );

		file.close();
		bw.close();

		StorageHelper.generateFileRdf( pathFolder, UUID );
	}

	public void generateRdfFromFile( TripletsForm tripletsForm, String pathFolder, final String UUID ) throws Exception {

		try {

			InputStream file = tripletsForm.getFile().getInputStream();

			saveRdf( pathFolder, file, UUID );

		} catch ( IOException e ) {

			LOGGER.error( "{" + UUID + "} -> Erro ao salvar arquivo RDF. " );
			throw new Exception( e );
		}

	}
}
