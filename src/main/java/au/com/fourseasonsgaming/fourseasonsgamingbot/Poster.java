package au.com.fourseasonsgaming.fourseasonsgamingbot;

import au.com.fourseasonsgaming.fourseasonsgamingbot.util.WebPage;
import com.tazzie02.tazbotdiscordlib.SendMessage;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Poster {
	
	private final Logger logger;
	
	private final JDA jda;
	private final URL url;
	private static final String UPDATES_KEY;
//	private static final String FORMAT_KEY;
	private static final String POST_CHANNEL_ID_KEY;
	private static final String MESSAGE_KEY;

	{
		logger = LoggerFactory.getLogger(Poster.class);
	}

	static {
        UPDATES_KEY = "Updates";
//        FORMAT_KEY = "Discord_Format";
        POST_CHANNEL_ID_KEY = "Discord_Post_Channel_Id";
        MESSAGE_KEY = "Message";
    }

	public Poster(JDA jda, URL url) {
		this.jda = jda;
		this.url = url;
	}
	
	public void post() throws JSONException {
	    JSONObject jsonRoot = null;
	    try {
            // Get JSON from the body of the web page
            jsonRoot = getJsonFromPageBody(url);
            logger.debug("{}", jsonRoot);
        }
        catch (IOException e) {
	        logger.error("Could not fetch web page.");
	        return;
        }
        catch (JSONException e) {
	        logger.error("Could not parse URL body as JSON.");
	        return;
        }

        // Check for updates
        JSONArray updates = getUpdates(jsonRoot);
        // Something went wrong when getting the updates array
        if (updates == null) {
            return;
        }
        // No updates
        else if (updates.length() == 0) {
            logger.info("[POSTER]: No updates.");
            return;
        }

        Map<String, List<String>> channelMessages = new HashMap<>();
        for (int i = 0; i < updates.length(); i++) {
            JSONObject update = updates.getJSONObject(i);

            // Get the post channel ID
            String postChannelId = getPostChannelId(update);
            // Skip this update when post channel ID key is invalid
            if (postChannelId == null) {
                logger.error("Skipping update due to post channel ID issue.");
                continue;
            }

//          // Get the format
//		    String format = getFormat(update);
//          // Skip this update when format key is invalid
//          if (format == null) {
//              logger.error("Skipping update due to format issue.");
//              continue;
//          }

//          // Format the update
//			String formatted = formatUpdate(format, update);
//          // Skip this update when the formatting can't be generated
//          if (formatted == null) {
//              logger.error("Skipping update due to issue with generating formatting.");
//              continue;
//          }

            // Get the message
            String message = getMessage(update);
            // Skip this update when message key is invalid
            if (message == null) {
                logger.error("Skipping update due to message issue.");
                continue;
            }

            // First time encountering the channel ID, create the message list
            if (!channelMessages.containsKey(postChannelId)) {
                channelMessages.put(postChannelId, new ArrayList<>());
            }

            // Add the formatted messages to the channel's messages
            List<String> messages = channelMessages.get(postChannelId);
//          messages.add(formatted);
            messages.add(message);
        }

        // Loop through each channel to sent messages
        for (Map.Entry<String, List<String>> entry : channelMessages.entrySet()) {
            TextChannel channel = getPostChannel(entry.getKey());
            if (channel == null) {
                logger.error(String.format("Skipping %d updates destined for channel ID %s due to invalid channel ID.", entry.getValue().size(), entry.getKey()));
                continue;
            }
            String output = String.join("\n", entry.getValue());

            SendMessage.sendMessage(channel, output);
        }
	}

	private JSONObject getJsonFromPageBody(URL url) throws IOException {
        String content = WebPage.download(url);
        return new JSONObject(content);
    }

	private JSONArray getUpdates(JSONObject json) {
	    if (!json.has(UPDATES_KEY)) {
	        logger.error(String.format("Could not find the update key (%s) at the root level.", UPDATES_KEY));
	        return null;
        }

        Object updates = json.get(UPDATES_KEY);
	    if (!(updates instanceof JSONArray)) {
	        logger.error(String.format("The type of the update key (%s) key must be a JSON array.", UPDATES_KEY));
	        return null;
        }

        return (JSONArray)updates;
    }

    private String getPostChannelId(JSONObject json) {
        if (!json.has(POST_CHANNEL_ID_KEY)) {
            logger.error(String.format("Could not find post channel ID key (%s) in the update.", POST_CHANNEL_ID_KEY));
            return null;
        }

        return json.getString(POST_CHANNEL_ID_KEY);
    }

//	private String getFormat(JSONObject json) {
//        if (!json.has(FORMAT_KEY)) {
//            logger.error(String.format("Could not find format key (%s) in the update.", FORMAT_KEY));
//            return null;
//        }
//
//        return json.getString(FORMAT_KEY);
//    }

    private TextChannel getPostChannel(String channelId) {
	    try {
            return jda.getTextChannelById(channelId);
        }
        catch (NumberFormatException e) {
	        return null;
        }
    }

    private String getMessage(JSONObject json) {
	    if (!json.has(MESSAGE_KEY)) {
	        logger.error(String.format("Could not find message key (%s) in the update.", MESSAGE_KEY));
	        return null;
        }

        return json.getString(MESSAGE_KEY);
    }

//	private String formatUpdate(String format, JSONObject update) {
//		Iterator<String> keys = json.keys();
//		while (keys.hasNext()) {
//			String key = keys.next();
//			String value =json.getString(key);
//			String keyPlaceholder = Pattern.quote("{$" + key + "}");
//			format = format.replaceAll(keyPlaceholder, value);
//		}
//
//		return format;
//	}

}
