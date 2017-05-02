package bio.knowledge.server.utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class UrlDecoder {
	private final static String ENCODING = "UTF-8";
	
	public static String decode(String urlEncodedString) {
		if (urlEncodedString == null) { return null; }
		
		try {
			return URLDecoder.decode(urlEncodedString, ENCODING);
		} catch (UnsupportedEncodingException e) {
			// It should not be possible for this exception to be thrown
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
}
