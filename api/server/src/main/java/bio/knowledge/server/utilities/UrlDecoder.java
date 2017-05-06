package bio.knowledge.server.utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class UrlDecoder {
	private final static String ENCODING = "UTF-8";

	public static String decode(String urlEncodedString) {
		if (urlEncodedString == null) {
			return null;
		}

		try {
			return URLDecoder.decode(urlEncodedString, ENCODING);
		} catch (UnsupportedEncodingException e) {
			// It should not be possible for this exception to be thrown
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public static List<String> decode(List<String> urlEncodedStrings) {
		if (urlEncodedStrings == null) {
			return null;
		}
		
		for (int i = 0; i < urlEncodedStrings.size(); i++) {
			String string = urlEncodedStrings.get(i);
			urlEncodedStrings.set(i, decode(string));
		}

		return urlEncodedStrings;
	}
}
