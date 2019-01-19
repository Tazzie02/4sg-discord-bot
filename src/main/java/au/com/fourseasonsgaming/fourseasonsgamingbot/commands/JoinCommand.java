package au.com.fourseasonsgaming.fourseasonsgamingbot.commands;

import au.com.fourseasonsgaming.fourseasonsgamingbot.implementation.DeleteMessage;
import au.com.fourseasonsgaming.fourseasonsgamingbot.models.RoleMapping;
import com.tazzie02.tazbotdiscordlib.Command;
import com.tazzie02.tazbotdiscordlib.MessageCallback;
import com.tazzie02.tazbotdiscordlib.SendMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinCommand implements Command {

    private final List<RoleMapping> roleMappings;
    private final MessageCallback messageCallback;

    public JoinCommand(List<RoleMapping> roleMappings, MessageCallback messageCallback) {
        this.roleMappings = roleMappings;
        this.messageCallback = messageCallback;
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        messageCallback.callback(e.getMessage());

        if (!PermissionUtil.checkPermission(e.getGuild().getSelfMember(), Permission.MANAGE_PERMISSIONS)) {
            SendMessage.sendMessage(e, "Error: Bot must have Manage Roles permission. Contact the bot administrator.", messageCallback);
            return;
        }

        if (args.length == 0) {
            SendMessage.sendMessage(e, getAvailableTriggersString(), messageCallback);
            return;
        }

        String trigger = String.join(" ", args);
        for (RoleMapping mapping : roleMappings) {
            if (trigger.equalsIgnoreCase(mapping.getTrigger())) {
                if (mapping.getRoleId().isEmpty()) {
                    SendMessage.sendMessage(e, "Error: Role ID is empty in configuration. Contact the bot administrator.", messageCallback);
                    return;
                }

                Role role = e.getGuild().getRoleById(mapping.getRoleId());
                if (role == null) {
                    SendMessage.sendMessage(e, "Error: Could not find role with ID specified in configuration. Contact the bot administrator.", messageCallback);
                    return;
                }

                e.getGuild().getController().addRolesToMember(e.getMember(), role).queue();
                SendMessage.sendMessage(e, String.format("Added role '%s' (%s) to %s.", trigger, role.getName(), e.getMember().getEffectiveName()), messageCallback);
                return;
            }
        }

        SendMessage.sendMessage(e, String.format("Error: Could not find trigger '%s'. Use this command without arguments to see available triggers.", trigger), messageCallback);
    }

    private String getAvailableTriggersString() {
        List<String> triggers = new ArrayList<>();
        roleMappings.stream().forEach(mapping -> triggers.add(mapping.getTrigger()));

        String triggersString = String.format("Available triggers to join: %s", String.join(", ", triggers));
        return triggersString;
    }

    @Override
    public CommandAccess getAccess() {
        return CommandAccess.ALL;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("join");
    }

    @Override
    public String getDescription() {
        return "Join a role.";
    }

    @Override
    public String getName() {
        return "Join Command";
    }

    @Override
    public String getDetails() {
        return "join - List roles available to join.\n" +
                "join <role> - Join the role.";
    }

    @Override
    public boolean isHidden() {
        return false;
    }
}
