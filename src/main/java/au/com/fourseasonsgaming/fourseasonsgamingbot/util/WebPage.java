package au.com.fourseasonsgaming.fourseasonsgamingbot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class WebPage {
	
	public static String download(URL url) throws IOException {
		try (InputStreamReader is = new InputStreamReader(url.openStream());
				BufferedReader br = new BufferedReader(is)) {
			
			String content = "";
			String line;
			while ((line = br.readLine()) != null) {
				content += line;
			}
			
			return content;
		}
	}

}
