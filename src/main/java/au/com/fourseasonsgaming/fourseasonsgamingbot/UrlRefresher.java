package au.com.fourseasonsgaming.fourseasonsgamingbot;

import java.net.MalformedURLException;
import java.net.URL;

import net.dv8tion.jda.core.JDA;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlRefresher {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlRefresher.class);
	private final JDA jda;
	private final URL url;
	private final int refreshRate;
	private boolean running;
	
	public UrlRefresher(JDA jda, String url, int refreshRate) throws MalformedURLException {
		this.jda = jda;
		this.url = new URL(url);
		this.refreshRate = refreshRate;
	}
	
	public void start() {
		running = true;
		new Thread(new RefreshLoop()).start();
	}
	
	public void stop() {
		this.running = false;
	}
	
	private class RefreshLoop implements Runnable {
		
		private Poster poster;
		
		{
			poster = new Poster(jda, url);
		}

		@Override
		public void run() {
			while (running) {
                poster.post();

				try {
					Thread.sleep(refreshRate);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
