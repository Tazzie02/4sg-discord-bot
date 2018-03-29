package au.com.fourseasonsgaming.fourseasonsgamingbot;

import java.net.MalformedURLException;

import javax.security.auth.login.LoginException;

import org.apache.commons.cli.ParseException;

import au.com.fourseasonsgaming.fourseasonsgamingbot.CommandLineOptions.CommandLineParsed;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class FourSeasonsGamingBot {
	
	public static void main(String[] args) {
		CommandLineOptions options = new CommandLineOptions();
		try {
			CommandLineParsed parsed = options.parse(args);
			System.out.println("Bot token set to: " + parsed.getToken());
			System.out.println("Data url set to: " + parsed.getUrl());
			
			JDABot jdaBot = new JDABot(parsed.getToken());
			JDA jda = jdaBot.getJDA();
			
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
