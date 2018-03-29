package au.com.fourseasonsgaming.fourseasonsgamingbot;

import java.net.MalformedURLException;
import java.net.URL;

import net.dv8tion.jda.core.JDA;

public class UrlRefresher {
	
	private final JDA jda;
	private final URL url;
	private boolean running;
	
	public UrlRefresher(JDA jda, String url) throws MalformedURLException {
		this.jda = jda;
		this.url = new URL(url);
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
					Thread.sleep(getRefreshMs());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		private int getRefreshMs() {
			String seconds = System.getenv("REFRESH_SECONDS");
			return Integer.parseInt(seconds) * 1000;
		}
		
	}

}
