package au.com.fourseasonsgaming.fourseasonsgamingbot.commands;

import java.util.Arrays;
import java.util.List;

import com.tazzie02.tazbotdiscordlib.Command;
import com.tazzie02.tazbotdiscordlib.SendMessage;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AboutCommand implements Command {
	
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		SendMessage.sendMessage(e.getTextChannel(), getAboutString());
	}
	
	private String getAboutString() {
		String s = "**4seasonsgaming Discord Bot**\n"
				+ "Created by tazzie#3859";
		
		return s;
	}

	@Override
	public CommandAccess getAccess() {
		return CommandAccess.ALL;
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("about");
	}

	@Override
	public String getDescription() {
		return "Display information about the bot.";
	}

	@Override
	public String getDetails() {
		return "about - display information";
	}

	@Override
	public String getName() {
		return "About Command";
	}

	@Override
	public boolean isHidden() {
		return false;
	}

}
