package au.com.fourseasonsgaming.fourseasonsgamingbot.commands;

import com.tazzie02.tazbotdiscordlib.Command;
import com.tazzie02.tazbotdiscordlib.SendMessage;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class RolesCommand implements Command {

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        String output = String.format("List of roles and IDs in %s:\n", e.getGuild().getName());
        for (Role role : e.getGuild().getRoles()) {
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
        return "roles - output list of roles.";
    }

    @Override
    public boolean isHidden() {
        return false;
    }
}
