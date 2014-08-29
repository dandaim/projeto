package br.ufrj.cc.aleph.helper;

import java.io.BufferedReader;
import java.io.FileReader;

public class MainHelper {

	public static void main( String[] args ) throws Exception {

		BufferedReader br = new BufferedReader( new FileReader( "/Users/ddaim/Documents/prolog/result2.out" ) );

		String sCurrentLine;

		String regularExpression = "(http[s]?://|ftp://)?(www\\.)?[a-zA-Z0-9-\\.]+\\.(com|org|net|mil|edu|ca|co.uk|com.au|gov|br)[a-zA-Z0-9/-]*#";

		while ( ( sCurrentLine = br.readLine() ) != null ) {

			System.out.println( sCurrentLine.replaceAll( regularExpression, "" ) );
		}

		br.close();
	}
}
