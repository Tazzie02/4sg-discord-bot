package au.com.fourseasonsgaming.fourseasonsgamingbot.commands;

import com.tazzie02.tazbotdiscordlib.Command;
import com.tazzie02.tazbotdiscordlib.SendMessage;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class RolesCommand implements Command {

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        Guild guild;
        if (args.length == 0) {
            if (e.getGuild() == null) {
                SendMessage.sendMessage(e, "Error: This command cannot be run in a private channel without specifying a server ID.");
                return;
            }
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

        String output = String.format("List of roles and IDs in %s:\n", guild.getName());
        for (Role role : guild.getRoles()) {
            output += String.format(" - %s (%s)\n", role.getName(), role.getId());
        }

        SendMessage.sendPrivate(e, output);
    }

    @Override
    public CommandAccess getAccess() {
        return CommandAccess.MODERATOR;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("roles");
    }

    @Override
    public String getDescription() {
        return "Output information about the roles in the guild.";
    }

    @Override
    public String getName() {
        return "Roles Command";
    }

    @Override
    public String getDetails() {
        return "roles - output list of roles.\n" +
                "roles [id] - output list of roles in guild with [id].";
    }

    @Override
    public boolean isHidden() {
        return false;
    }
}
