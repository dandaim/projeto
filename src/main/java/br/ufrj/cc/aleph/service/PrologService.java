package br.ufrj.cc.aleph.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import br.ufrj.cc.aleph.controller.form.BeaconForm;
import br.ufrj.cc.aleph.domain.UserRequest;
import br.ufrj.cc.aleph.helper.MailContentEnum;
import br.ufrj.cc.aleph.helper.MailHelper;
import br.ufrj.cc.aleph.helper.Md5Helper;
import br.ufrj.cc.aleph.helper.StorageHelper;

@Service
public class PrologService {

	private static final Logger LOGGER = Logger.getLogger( PrologService.class );

	public static Runnable r = ThreadHelper.getInstance();

	@Autowired
	private MailHelper mailHelper;

	public void executeShellScript( final BeaconForm beaconForm, String UUID )
			throws Exception {

		String pathFolder = StorageHelper.commonPath
				+ Md5Helper.md5( beaconForm.getEmail() );

		String nextFolder = "/"
				+ StorageHelper.getNextPath( Md5Helper.md5( beaconForm
						.getEmail() ) );

		pathFolder += nextFolder;

		File file = new File( pathFolder );

		int files = 0;

		try {

			LOGGER.info( "{" + UUID + "} -> Pasta a ser criada: " + pathFolder );

			LOGGER.info( "{" + UUID
					+ "} -> Vou criar a pasta caso não exista: " + pathFolder );

			file.mkdirs();

			for ( int i = 0; i < beaconForm.getArqpos().length; i++ ) {

				if ( beaconForm.getArqpos()[i].getSize() > 0 ) {

					files++;

					generateFileB( beaconForm, pathFolder, i, UUID );

					generateFoldPos( beaconForm, beaconForm.getArqpos()[i],
							pathFolder, i, UUID );

					if ( beaconForm.getArqneg() != null ) {
						generateFoldNeg( beaconForm, beaconForm.getArqneg()[i],
								pathFolder, i, UUID );
					}

				}
			}

			if ( beaconForm.getArqopt() != null ) {

				generateArqOpt( beaconForm, pathFolder, UUID );
			}

			UserRequest userRequest = new UserRequest( beaconForm.getEmail(),
					beaconForm.getName(), files, pathFolder, true );

			LOGGER.info( "{" + UUID + "} -> Adicionando a requisição na fila: " );

			ThreadHelper.userRequestQueue.add( userRequest );

		} catch ( IOException e ) {

			LOGGER.error( "{" + UUID + "} -> Erro gerando arquivos: "
					+ e.getMessage() );
			throw new Exception( e );

		}

		LOGGER.info( "{" + UUID + "} -> Enviando email para o usuário: "
				+ beaconForm.getEmail() );

		mailHelper.sendEmail( beaconForm.getEmail(),
				"DAHELE - Envio de requisição",
				MailContentEnum.REQUEST.getMsg(), beaconForm.getName() );

		LOGGER.info( "{" + UUID + "} -> Iniciando a thread da requisição." );

		Thread thr = new Thread( r );
		thr.start();
	}

	public void sendUserRequestEmail() {

	}

	private void generateFileB( final BeaconForm beaconForm,
			final String pathFolder, final int index, final String UUID )
			throws IOException {

		LOGGER.info( "{" + UUID + "} -> Criando arquivo B: " + beaconForm );

		File arquivo = new File( pathFolder + "/"
				+ beaconForm.getArqb().getName() + index + ".b" );
		arquivo.createNewFile();

		FileWriter fw = new FileWriter( arquivo.getAbsolutePath() );
		BufferedWriter bw = new BufferedWriter( fw );

		String conteudo = "";

		InputStream input = beaconForm.getArqb().getInputStream();

		conteudo += getStringFromInputStream( input, UUID );
		conteudo += "\n:- set(test_pos,'arqpos" + index + ".f').";
		conteudo += "\n:- set(test_neg,'arqneg" + index + ".n').";

		input.close();

		bw.write( conteudo );
		bw.close();
		fw.close();
	}

	private void generateArqOpt( final BeaconForm beaconForm,
			final String pathFolder, final String UUID ) throws IOException {

		LOGGER.info( "{" + UUID + "} -> Criando arquivos opcionais: "
				+ beaconForm );

		for ( CommonsMultipartFile file : beaconForm.getArqopt() ) {

			if ( file.getSize() > 0 ) {
				File arquivo = new File( pathFolder + "/"
						+ file.getOriginalFilename() );
				arquivo.createNewFile();

				file.transferTo( arquivo );
			}

		}
	}

	private void generateFoldPos( final BeaconForm beaconForm,
			final CommonsMultipartFile file, final String pathFolder,
			final int index, final String UUID ) throws IOException {

		LOGGER.info( "{" + UUID + "} -> Criando folds positivos: " + beaconForm );

		File arquivo = new File( pathFolder + "/"
				+ beaconForm.getArqpos()[index].getName() + index + ".f" );
		arquivo.createNewFile();

		FileWriter fw = new FileWriter( arquivo.getAbsolutePath() );
		BufferedWriter bw = new BufferedWriter( fw );

		String conteudo = "";

		for ( int j = 0; j < beaconForm.getArqpos().length; j++ ) {

			if ( beaconForm.getArqpos()[j].getSize() > 0 ) {

				if ( index != j ) {

					InputStream input = beaconForm.getArqpos()[j]
							.getInputStream();

					conteudo += getStringFromInputStream( input, UUID );

					input.close();
				}
			}
		}

		if ( conteudo.length() == 0 ) {

			InputStream input = beaconForm.getArqpos()[index].getInputStream();

			conteudo += getStringFromInputStream( input, UUID );

			input.close();
		}

		bw.write( conteudo );
		bw.close();
		fw.close();
	}

	private void generateFoldNeg( final BeaconForm beaconForm,
			final CommonsMultipartFile file, final String pathFolder,
			final int index, final String UUID ) throws IOException {

		LOGGER.info( "{" + UUID + "} -> Criando folds negativos: " + beaconForm );

		File arquivo = new File( pathFolder + "/"
				+ beaconForm.getArqneg()[index].getName() + index + ".n" );
		arquivo.createNewFile();

		FileWriter fw = new FileWriter( arquivo.getAbsolutePath() );
		BufferedWriter bw = new BufferedWriter( fw );

		String conteudo = "";

		for ( int j = 0; j < beaconForm.getArqneg().length; j++ ) {

			if ( beaconForm.getArqneg()[j].getSize() > 0 ) {

				if ( index != j ) {

					InputStream input = beaconForm.getArqneg()[j]
							.getInputStream();

					conteudo += getStringFromInputStream( input, UUID );

					input.close();
				}
			}

		}

		if ( conteudo.length() == 0 ) {

			InputStream input = beaconForm.getArqpos()[index].getInputStream();

			conteudo += getStringFromInputStream( input, UUID );

			input.close();
		}

		bw.write( conteudo );
		bw.close();
		fw.close();
	}

	public static String getStringFromInputStream( final InputStream is,
			final String UUID ) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader( new InputStreamReader( is ) );
			while ( ( line = br.readLine() ) != null ) {

				sb.append( line + "\n" );
			}

		} catch ( IOException e ) {

			LOGGER.error( "{" + UUID + "} -> erro lendo arquivo." );

		} finally {
			if ( br != null ) {
				try {
					br.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
}
