package bio.knowledge.server.utilities;

public class Utilities {
	public static String[] buildArray(String string) {
		return string != null && !string.isEmpty() ? string.split(" ") : null;
	}
	
}
