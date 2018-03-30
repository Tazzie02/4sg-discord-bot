package au.com.fourseasonsgaming.fourseasonsgamingbot;

import java.io.IOException;
import java.net.URL;

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
	
	private final Logger logger = LoggerFactory.getLogger(Poster.class);
	
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
			logger.debug("{}", json);
			
			JSONArray updates = json.getJSONArray("updates");
			if (updates.length() == 0) {
				System.out.println("[POSTER]: No updates.");
				return;
			}
			
			String output = "";
			for (int i = 0; i < updates.length(); i++) {
				output += formatArticle(updates.getJSONObject(i)) + '\n'; 
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
		String articleType = json.getString("Article_Type");
		String articleGame = json.getString("Article_Game");
		String postedBy = json.getString("Posted_By");
		String articleTitle = json.getString("Article_Title");
		String articleUrl = json.getString("Article_URL");
		
		String formatted = String.format("New %s posted by %s - %s - <%s>", articleType, postedBy, articleTitle, articleUrl); 
		return formatted;
	}
	
	private TextChannel getPostChannel() {
		return jda.getTextChannelById(System.getenv("FEED_CHANNEL_ID"));
	}
	
}
