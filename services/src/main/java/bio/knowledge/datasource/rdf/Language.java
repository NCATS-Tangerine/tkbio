/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-16 Scripps Institute (USA) - Dr. Benjamin Good
 *                       STAR Informatics / Delphinai Corporation (Canada) - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *-------------------------------------------------------------------------------
 */

package bio.knowledge.datasource.rdf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import bio.knowledge.datasource.file.FileUtilService;

/**
 * Language
 *
 * @author rbruskiewich
 *
 */
public class Language {

	private static Boolean _DEBUG = false ;

	private final static  String defaultCode = "en" ;

	public static void debug(Boolean debug) {
		_DEBUG = debug ;
	}

	//private final static String ISO_639_2 = 
	//		"http://www.loc.gov/standards/iso639-2/ISO-639-2_8859-1.txt";

	private static Map<String,String> table = new HashMap<String,String>() ;

	public static Map<String,String> ISO_639_2() {
		if( table.isEmpty() ) {
			init() ;
		}
		return  new HashMap<String,String>(table) ;
	}

	public static String getLanguage(String code) {
		if( table.isEmpty() ) {
			init() ;
		}
		if(table.containsKey(code))
			return table.get(code) ;
		else {
			return null ;
		}
	}

	public static String getDefaultLanguageCode() {
		return defaultCode ;
	}	

	public static String getDefaultLanguage() {
		return getLanguage(defaultCode) ;
	}	

	private static void init() {
		FileUtilService fus = FileUtilService.getDefaultClassPath() ;
		BufferedReader reader = null ;
		try {
			// For some reason, this US Gov link now replies with a HTTP 403 error
			// when accessed by code (but not by web browser) so I read a local cached version 
			// URL isolang = new URL(ISO_639_2);

			InputStream input = fus.getConfigInputStream( "ISO-639-2_8859-1" ) ;
			reader = new BufferedReader ( 
					new InputStreamReader(input, "UTF8") 
					);

			String inputLine;
			while ((inputLine = reader.readLine()) != null) {

				String[] field = inputLine.split("\\|") ;
				String[] englishName = field[3].split("\\;") ;

				if( _DEBUG ) {
					System.out.println(inputLine); 
					System.out.println("alpha-3 (bibliographic) code: "+field[0]) ;
					System.out.println("\talpha-3 (terminologic) code: "+field[1]) ;
					System.out.println("\talpha-2 code:\t"+field[2]) ;
					System.out.println("\tEnglish name:\t"+field[3]) ;
					System.out.println("\tFrench name:\t"+field[4]) ;
					System.out.println() ;
				}
				if(! table.containsKey(field[0])) {
					table.put(field[0],englishName[0]) ;
				}
				if( field[2].length()>0 && 
						!table.containsKey(field[2])
						) {
					table.put(field[2],englishName[0]) ;
				}
			}
			reader.close();

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
