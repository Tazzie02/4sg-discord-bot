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
				.argName("TOKEN")
				.required()
				.build();
		Option url = Option.builder("u")
				.longOpt("url")
				.desc("url to retrieve data (required)")
				.hasArg()
				.argName("URL")
				.required()
				.build();
		
		options.addOption(help);
		options.addOption(token);
		options.addOption(url);
		
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
			return this.commandLine.getOptionValue("token");
		}
		
		public String getUrl() {
			return this.commandLine.getOptionValue("url");
		}
		
	}

}
