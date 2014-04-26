package br.ufrj.cc.aleph.helper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class StorageHelper {
	
	public static String commonPath = "/home/daniel/pacote/";
	
	public static String alephPath = "/home/daniel/uw_aleph/";
	
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
			
			//Adicionando leitura de todos os arquivos na execução do script
			for( int i = 0; i < numFiles; i++ ) {
				
				out.print( " read_all(arqb" + i +", arqpos" + i + ", arqneg" + i + ")." );
				out.print( " induce." );
			}			
			
			out.println( " halt.\"" );
			out.println(" yap <<< $string");
			//out.println(" exit 0" );
			out.close();
						
//			File file = new File( pathFolder + "/script.sh" );
//			file.setExecutable( true );
//			file.setReadable( true );
			
		} catch ( IOException e ) {
			
			e.printStackTrace();
			
		}
		
		return folder + "/script.sh";
		
	}
	
}
