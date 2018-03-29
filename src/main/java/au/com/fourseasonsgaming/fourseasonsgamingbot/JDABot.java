package au.com.fourseasonsgaming.fourseasonsgamingbot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class JDABot {
	
	private final JDA jda;
	
	public JDABot(String token) throws LoginException, InterruptedException {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setAudioEnabled(false);
		builder.setToken(token);
		jda = builder.buildBlocking();
	}
	
	public JDA getJDA() {
		return jda;
	}
	
	

}
