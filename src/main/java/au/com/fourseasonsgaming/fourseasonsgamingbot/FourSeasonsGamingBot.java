package au.com.fourseasonsgaming.fourseasonsgamingbot;

import java.net.MalformedURLException;

import javax.security.auth.login.LoginException;

import org.apache.commons.cli.ParseException;

import au.com.fourseasonsgaming.fourseasonsgamingbot.CommandLineOptions.CommandLineParsed;
import net.dv8tion.jda.core.JDA;

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
		} catch (LoginException | IllegalArgumentException e) {
			System.out.println("The provided bot token is invalid.");
//			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("The bot was interrupted.");
//			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("The URL is not valid.");
//			e.printStackTrace();
		}
		
		/*
		 * hostname
		 * port
		 * database
		 * username
		 * password
		 * bot token
		 * 
		 * channel id
		 * refresh rate
		 */
//		
//		try {
//			JDABot jdaBot = new JDABot(token);
//			jdaBot.getJDA();
//		} catch (LoginException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

}
