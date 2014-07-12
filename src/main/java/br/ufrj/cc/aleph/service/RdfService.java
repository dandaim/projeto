package br.ufrj.cc.aleph.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import br.ufrj.cc.aleph.controller.form.TripletsForm;

@Service
public class RdfService {

	public void generateRdfFile( TripletsForm tripletsForm ) {

		if ( tripletsForm.getOption().equals( "url" ) ) {

			generateRdfFromUrl( tripletsForm );

		} else {

			generateRdfFromFile( tripletsForm );
		}
	}

	public void generateRdfFromUrl( TripletsForm tripletsForm ) {

	}

	public void generateRdfFromFile( TripletsForm tripletsForm ) {

		try {

			InputStream file = tripletsForm.getFile().getInputStream();

			file.close();

		} catch ( IOException e ) {

			e.printStackTrace();
		}

	}
}
