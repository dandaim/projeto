package br.ufrj.cc.aleph.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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
	
	public static Runnable r = ThreadHelper.getInstance();
	
	public void executeShellScript( final BeaconForm beaconForm ){		
		
		String pathFolder = StorageHelper.commonPath + Md5Helper.md5( beaconForm.getEmail() );
		
		String nextFolder = "/" + StorageHelper.getNextPath( Md5Helper.md5( beaconForm.getEmail() ) );
		
		pathFolder += nextFolder;
		
		File file = new File( pathFolder );
		
		int files = 0;
		
		try {
			
			if( file.mkdirs() ) {
				
				for ( int i = 0; i < beaconForm.getArqpos().length; i++ ) {
					
					if( beaconForm.getArqpos()[i].getSize() > 0 ) {
						
						files++;
						
						generateFileB( beaconForm, pathFolder, i );
						
						generateFoldPos( beaconForm, beaconForm.getArqpos()[i], pathFolder, i );
						
						if( beaconForm.getArqneg() != null ) {						
							generateFoldNeg( beaconForm, beaconForm.getArqneg()[i], pathFolder, i );
						}
					}					
				}
				
			} else {
				
				for ( int i = 0; i < beaconForm.getArqpos().length; i++ ) {
					
					if( beaconForm.getArqpos()[i].getSize() > 0 ) {
						
						files++;
						
						generateFoldPos( beaconForm, beaconForm.getArqpos()[i], pathFolder, i );
						
						if( beaconForm.getArqneg() != null ) {						
							generateFoldNeg( beaconForm, beaconForm.getArqneg()[i], pathFolder, i );
						}
					}				
				}
			}
			
			ThreadHelper.userRequestQueue.add( new UserRequest( beaconForm.getEmail(), beaconForm.getName(), files, pathFolder ) );
			
		} catch( Exception e ) {
			
		}
		
		
		ApplicationContext context =  new ClassPathXmlApplicationContext("classpath:spring/mail.xml");
		
		MailHelper mailHelper = (MailHelper) context.getBean( "mailHelper" );
		
		//mailHelper.sendEmail( beaconForm.getEmail(), "teste", MailContentEnum.REQUEST.getMsg(), beaconForm.getName() );
		 
		
		Thread thr = new Thread(r);
		thr.start();		
	}
	
	public void sendUserRequestEmail() {
		
	}
	
	private void generateFileB( final BeaconForm beaconForm, final String pathFolder, final int index ) throws IOException {
		
		File arquivo = new File( pathFolder + "/" + beaconForm.getArqb().getName() + index + ".b" );
		arquivo.createNewFile();
		
		 FileWriter fw = new FileWriter( arquivo.getAbsolutePath() );
		 BufferedWriter bw = new BufferedWriter(fw);
		 
		String conteudo = "";
		
		InputStream input =  beaconForm.getArqb().getInputStream();
			
		conteudo += getStringFromInputStream( input );
		conteudo += "\n:- set(test_pos,'arqpos"+ index +".f').";
		conteudo += "\n:- set(test_neg,'arqneg"+ index +".n').";
			
		input.close();
			
		bw.write( conteudo );
		bw.close();
		fw.close();
	}
	
	private void generateFoldPos ( final BeaconForm beaconForm, final CommonsMultipartFile file, final String pathFolder, final int index ) throws IOException {
		
		 File arquivo = new File( pathFolder + "/" + beaconForm.getArqpos()[index].getName() + index + ".f" );
		 arquivo.createNewFile();
		 
		 FileWriter fw = new FileWriter( arquivo.getAbsolutePath() );
		 BufferedWriter bw = new BufferedWriter(fw);
		 
		 String conteudo = "";
		 
		 for( int j = 0; j < beaconForm.getArqpos().length; j++ ) {
			 
			 if( beaconForm.getArqpos()[j].getSize() > 0 ) {
				 
				 if( index != j ) {
						
						InputStream input =  beaconForm.getArqpos()[j].getInputStream();
						
						conteudo += getStringFromInputStream( input );
						
						input.close();
				 }	
			 }			 		 			 
		 }
		
		 bw.write( conteudo );
		 bw.close();
		 fw.close();		
	}
	
	private void generateFoldNeg ( final BeaconForm beaconForm, final CommonsMultipartFile file, final String pathFolder, final int index ) throws IOException {
		
		 File arquivo = new File( pathFolder + "/" + beaconForm.getArqneg()[index].getName() + index + ".n" );
		 arquivo.createNewFile();
		 
		 FileWriter fw = new FileWriter( arquivo.getAbsolutePath() );
		 BufferedWriter bw = new BufferedWriter(fw);
		 
		 String conteudo = "";
		 
		 for( int j = 0; j < beaconForm.getArqneg().length; j++ ) {
			 
			 if( beaconForm.getArqneg()[j].getSize() > 0 ) {
				 
				 if( index != j ) {
						
						InputStream input =  beaconForm.getArqneg()[j].getInputStream();
						
						conteudo += getStringFromInputStream( input );
						
						input.close();
				 }	
			 }			 
			 		 			 
		 }
		
		 bw.write( conteudo );
		 bw.close();
		 fw.close();		
	}
	
	private static String getStringFromInputStream( final InputStream is ) {
		 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append( line + "\n");
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch ( IOException e) {
					e.printStackTrace();
				}
			}
		} 
		return sb.toString(); 
	}	
}
