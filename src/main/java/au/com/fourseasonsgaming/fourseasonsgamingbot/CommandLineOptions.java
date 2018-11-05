package au.com.fourseasonsgaming.fourseasonsgamingbot;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineOptions {
	
	private final Options options;
	private final String tokenArgName = "token";
	private final String urlArgName = "url";
	private final String refreshArgName = "refreshRate";

	public CommandLineOptions() {
		this.options = createOptions();
	}
	
	public CommandLineParsed parse(String[] args) throws ParseException {
		return new CommandLineParsed(options, args);
	}
	
	private Options createOptions() {
		Options options = new Options();
		
		Option help = Option.builder("h")
				.longOpt("help")
				.desc("print this message")
				.build();
		Option token = Option.builder("t")
				.longOpt("token")
				.desc("discord bot token (required)")
				.hasArg()
				.argName(tokenArgName)
				.required()
				.build();
		Option url = Option.builder("u")
				.longOpt("url")
				.desc("url to retrieve data (required)")
				.hasArg()
				.argName(urlArgName)
				.required()
				.build();
		Option refreshRate = Option.builder("r")
				.longOpt("refreshRate")
				.desc("url refresh rate in seconds for retrieving from url (required)")
				.hasArg()
				.argName(refreshArgName)
                .type(Integer.class)
				.required()
				.build();
		
		options.addOption(help);
		options.addOption(token);
		options.addOption(url);
		options.addOption(refreshRate);
		
		return options;
	}
	
	public void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("help", options);
	}
	
	public class CommandLineParsed {
		
		private final CommandLine commandLine;
		
		public CommandLineParsed(Options options, String[] args) throws ParseException {
			CommandLineParser parser = new DefaultParser();
			this.commandLine = parser.parse(options, args);
		}
		
		public String getToken() {
			return this.commandLine.getOptionValue(tokenArgName);
		}
		
		public String getUrl() {
			return this.commandLine.getOptionValue(urlArgName);
		}

		public int getRefreshRate() {
		    System.out.println(this.commandLine.getOptionValue(refreshArgName));
		    int refreshRate = Integer.parseInt(this.commandLine.getOptionValue(refreshArgName));
		    // Convert milliseconds to seconds
			return refreshRate * 1000;
		}

	}

}
