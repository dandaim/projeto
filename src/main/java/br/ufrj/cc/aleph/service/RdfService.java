package br.ufrj.cc.aleph.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.stereotype.Service;

import br.ufrj.cc.aleph.controller.form.TripletsForm;
import br.ufrj.cc.aleph.helper.Md5Helper;
import br.ufrj.cc.aleph.helper.StorageHelper;

@Service
public class RdfService {

	public void generateRdfFile( TripletsForm tripletsForm ) {

		String pathFolder = StorageHelper.commonPath
				+ Md5Helper.md5( tripletsForm.getEmail() );

		String nextFolder = "/"
				+ StorageHelper.getNextPath( Md5Helper.md5( tripletsForm
						.getEmail() ) );

		pathFolder += nextFolder;

		File file = new File( pathFolder );
		file.mkdir();

		if ( tripletsForm.getOption().equals( "url" ) ) {

			generateRdfFromUrl( tripletsForm, pathFolder );

		} else {

			generateRdfFromFile( tripletsForm, pathFolder );
		}
	}

	public void generateRdfFromUrl( TripletsForm tripletsForm, String pathFolder ) {

		try {

			URL website = new URL( tripletsForm.getUrl() );

			URLConnection connection = website.openConnection();
			InputStream file = connection.getInputStream();

			saveRdf( pathFolder, file );

		} catch ( MalformedURLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void saveRdf( String pathFolder, InputStream file )
			throws IOException {

		File arquivo = new File( pathFolder + "/file.rdf" );

		arquivo.createNewFile();

		FileWriter fw = new FileWriter( arquivo.getAbsolutePath() );
		BufferedWriter bw = new BufferedWriter( fw );

		bw.write( PrologService.getStringFromInputStream( file ) );

		file.close();
		bw.close();

		StorageHelper.generateFileRdf( pathFolder );
	}

	public void generateRdfFromFile( TripletsForm tripletsForm,
			String pathFolder ) {

		try {

			InputStream file = tripletsForm.getFile().getInputStream();

			saveRdf( pathFolder, file );

		} catch ( IOException e ) {

			e.printStackTrace();
		}

	}
}
