package au.com.fourseasonsgaming.fourseasonsgamingbot;

import java.net.MalformedURLException;

import javax.security.auth.login.LoginException;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.fourseasonsgaming.fourseasonsgamingbot.CommandLineOptions.CommandLineParsed;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class FourSeasonsGamingBot {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FourSeasonsGamingBot.class);
	
	public static void main(String[] args) {
		CommandLineOptions options = new CommandLineOptions();
		try {
			CommandLineParsed parsed = options.parse(args);
			LOGGER.info("Bot token set to {}", parsed.getToken());
			LOGGER.info("Data url set to {}", parsed.getUrl());
			
			TDLBot tdlBot = new TDLBot(parsed.getToken());
			JDA jda = tdlBot.getJDA();
			
			UrlRefresher refresher = new UrlRefresher(jda, parsed.getUrl());
			refresher.start();
		}
		catch (ParseException e) {
			System.out.println(e.getMessage());
			options.printHelp();
//			e.printStackTrace();
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RateLimitedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
