package au.com.fourseasonsgaming.fourseasonsgamingbot;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tazzie02.tazbotdiscordlib.SendMessage;

import au.com.fourseasonsgaming.fourseasonsgamingbot.util.WebPage;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;

public class Poster {
	
	private final Logger logger;
	
	private final JDA jda;
	private final URL url;
	private final String formatKey;
	
	{
		logger = LoggerFactory.getLogger(Poster.class);
		formatKey = "Discord_Format";
	}

	public Poster(JDA jda, URL url) {
		this.jda = jda;
		this.url = url;
	}
	
	public void post() {
		try {
			String content = WebPage.download(url);
			JSONObject json = new JSONObject(content);
			logger.debug("{}", json);
			
			JSONArray updates = json.getJSONArray("updates");
			if (updates.length() == 0) {
				System.out.println("[POSTER]: No updates.");
				return;
			}
			
			String output = "";
			for (int i = 0; i < updates.length(); i++) {
				String formatted = formatArticle(updates.getJSONObject(i));
				if (formatted != null) {
					output += formatted + '\n';
				}
				else {
					logger.error("Could not generate output for article from JSON.");
				}
			}
			
			SendMessage.sendMessage(getPostChannel(), output);
		} catch (IOException e) {
			System.out.println("Could not read webpage.");
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("Could not parse response as JSON.");
			e.printStackTrace();
		}
	}
	
	private String formatArticle(JSONObject json) {
		if (!json.has(formatKey)) {
			System.out.println("Could not find format key.");
			return null;
		}
		
		String format = json.getString(formatKey);
		json.remove(formatKey);
		
		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			String value =json.getString(key);
			String keyPlaceholder = Pattern.quote("{$" + key + "}");
			format = format.replaceAll(keyPlaceholder, value);
		}
		
		return format;
	}
	
	private TextChannel getPostChannel() {
		return jda.getTextChannelById(System.getenv("FEED_CHANNEL_ID"));
	}
	
}
