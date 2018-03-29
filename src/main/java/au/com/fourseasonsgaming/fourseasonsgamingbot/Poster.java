package au.com.fourseasonsgaming.fourseasonsgamingbot;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import au.com.fourseasonsgaming.fourseasonsgamingbot.util.WebPage;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;

public class Poster {
	
	private final JDA jda;
	private final URL url;
	private final Color embedColor;
	
	{
		this.embedColor = new Color(0, 144, 208);
	}

	public Poster(JDA jda, URL url) {
		this.jda = jda;
		this.url = url;
	}
	
	public void post() {
		try {
			String content = WebPage.download(url);
			JSONObject json = new JSONObject(content);
//			System.out.println(json);
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(this.embedColor);
			for (String key : json.keySet()) {
				String value = json.getString(key);
//				System.out.println(value);
				String[] split = value.split("->");
				embed.addField(split[0], split[1], false);
			}
			
			getPostChannel().sendMessage(embed.build()).queue(m -> {System.out.println("Sent message.");});
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
