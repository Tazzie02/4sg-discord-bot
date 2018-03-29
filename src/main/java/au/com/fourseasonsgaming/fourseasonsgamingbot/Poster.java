package au.com.fourseasonsgaming.fourseasonsgamingbot;

import java.io.IOException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.tazzie02.tazbotdiscordlib.SendMessage;

import au.com.fourseasonsgaming.fourseasonsgamingbot.util.WebPage;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;

public class Poster {
	
	private final JDA jda;
	private final URL url;

	public Poster(JDA jda, URL url) {
		this.jda = jda;
		this.url = url;
	}
	
	public void post() {
		try {
			String content = WebPage.download(url);
			JSONObject json = new JSONObject(content);
//			System.out.println(json);
			
			String output = "";
			for (String key : json.keySet()) {
				String value = json.getString(key);
				output += value + '\n';
			}
			
			SendMessage.sendMessage(getPostChannel(), output);
		} catch (IOException e) {
			System.out.println("Could not read webpage.");
//			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("Could not parse JSON.");
		}
	}
	
	private TextChannel getPostChannel() {
		return jda.getTextChannelById(System.getenv("FEED_CHANNEL_ID"));
	}
	
}
