package au.com.fourseasonsgaming.fourseasonsgamingbot.commands;

import au.com.fourseasonsgaming.fourseasonsgamingbot.util.MembersCsv;
import com.tazzie02.tazbotdiscordlib.Command;
import com.tazzie02.tazbotdiscordlib.SendMessage;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class JoinedCommand implements Command {

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        Guild guild;
        if (args.length == 0) {
            guild = e.getGuild();
        }
        else if (args.length == 1) {
            guild = e.getJDA().getGuildById(args[0]);
        }
        else {
            SendMessage.sendMessage(e, "Error: Incorrect number of arguments. Expected none or <serverId>.");
            return;
        }

        if (guild == null) {
            SendMessage.sendMessage(e, "Error: Could not find server with the specified ID.");
            return;
        }

        MembersCsv csv = new MembersCsv(guild.getMembers());

        e.getTextChannel().sendFile(csv.getCsv().getBytes(), guild.getName() + "_members.csv").queue();
    }

    @Override
    public CommandAccess getAccess() {
        return CommandAccess.MODERATOR;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("joined");
    }

    @Override
    public String getDescription() {
        return "Get how long each user with the specified role has been in the specified server.";
    }

    @Override
    public String getName() {
        return "Joined Command";
    }

    @Override
    public String getDetails() {
        return "joined <serverId> <roleId> - Get a list of users with roleId in serverId.";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

}
