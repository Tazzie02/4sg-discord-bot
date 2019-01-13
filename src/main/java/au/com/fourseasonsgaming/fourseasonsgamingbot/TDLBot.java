package au.com.fourseasonsgaming.fourseasonsgamingbot;

import au.com.fourseasonsgaming.fourseasonsgamingbot.commands.*;
import au.com.fourseasonsgaming.fourseasonsgamingbot.configuration.Config;
import com.tazzie02.tazbotdiscordlib.CommandRegistry;
import com.tazzie02.tazbotdiscordlib.TazbotDiscordLib;
import com.tazzie02.tazbotdiscordlib.TazbotDiscordLibBuilder;
import com.tazzie02.tazbotdiscordlib.commands.HelpCommand;
import com.tazzie02.tazbotdiscordlib.commands.PingCommand;
import com.tazzie02.tazbotdiscordlib.commands.ShutdownCommand;
import com.tazzie02.tazbotdiscordlib.filehandling.LocalFiles;
import com.tazzie02.tazbotdiscordlib.impl.MessageLoggerImpl;
import com.tazzie02.tazbotdiscordlib.impl.MessageSenderImpl;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.nio.file.Path;

public class TDLBot {
	
	private final TazbotDiscordLib tdl;
	
	public TDLBot(String token, Path configPath, Config fourSeasonsConfig) throws LoginException, IllegalArgumentException, InterruptedException, RateLimitedException {
		TazbotDiscordLibBuilder builder = new TazbotDiscordLibBuilder(token);
		// Set the location files will be stored
		builder.setFilePath(configPath);

		this.tdl = builder.build();

	    // Create the default MessageSender and add the default MessageLogger to it
	    MessageLoggerImpl logger = new MessageLoggerImpl();
	    MessageSenderImpl sender = new MessageSenderImpl();
	    sender.setMessageSentLogger(logger);
	    tdl.setMessageSender(sender);

	    LocalFiles filesInstance = LocalFiles.getInstance(tdl.getJDA());

	    // Create a CommandRegistry to manage Commands
	    CommandRegistry registry = new CommandRegistry();
	    
	    registry.setCaseSensitiveCommands(false);
		// Set the owners as per the config file
	    registry.setOwners(filesInstance.getConfig());
		// Set the CommandSettings for all as well as guild overrides
	    registry.setDefaultCommandSettings(filesInstance);
	    registry.setGuildCommandSettings(filesInstance);
		// Use the MessageLogger to log received messages
	    registry.setMessageReceivedLogger(logger);
	    
	    registerCommands(registry, fourSeasonsConfig);

	    // Add the CommandRegistry to the TazbotDiscordLib object
	    tdl.addListener(registry);
	}
	
	private void registerCommands(CommandRegistry registry, Config fourSeasonsConfig) {
		// Commands provided by TDL
		registry.registerCommand(new HelpCommand(registry));
	    registry.registerCommand(new PingCommand());
	    registry.registerCommand(new ShutdownCommand());
	    
	    // General commands
	    registry.registerCommand(new AboutCommand());
	    registry.registerCommand(new JoinedCommand());
	    registry.registerCommand(new JoinCommand(fourSeasonsConfig.getRoleMappings()));
		registry.registerCommand(new LeaveCommand(fourSeasonsConfig.getRoleMappings()));
		registry.registerCommand(new RolesCommand());
	}
	
	public JDA getJDA() {
		return tdl.getJDA();
	}

}
