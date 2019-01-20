package au.com.fourseasonsgaming.fourseasonsgamingbot.commands;

import au.com.fourseasonsgaming.fourseasonsgamingbot.implementation.DeleteMessage;
import au.com.fourseasonsgaming.fourseasonsgamingbot.models.RoleMapping;
import com.tazzie02.tazbotdiscordlib.Command;
import com.tazzie02.tazbotdiscordlib.MessageCallback;
import com.tazzie02.tazbotdiscordlib.SendMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeaveCommand implements Command {

    private final List<RoleMapping> roleMappings;
    private final MessageCallback messageCallback;

    public LeaveCommand(List<RoleMapping> roleMappings, MessageCallback messageCallback) {
        this.roleMappings = roleMappings;
        this.messageCallback = messageCallback;
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        if (e.getGuild() == null) {
            SendMessage.sendMessage(e, "Error: This command cannot be run in a private channel.");
            return;
        }

        messageCallback.callback(e.getMessage());

        if (!PermissionUtil.checkPermission(e.getGuild().getSelfMember(), Permission.MANAGE_PERMISSIONS)) {
            SendMessage.sendMessage(e, "Error: Bot must have Manage Roles permission. Contact the bot administrator.", messageCallback);
            return;
        }

        if (args.length == 0) {
            SendMessage.sendMessage(e, getRemovableRolesString(e.getMember()), messageCallback);
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

                for (Role memberRole : e.getMember().getRoles()) {
                    if (memberRole.getId().equalsIgnoreCase(mapping.getRoleId())) {
                        e.getGuild().getController().removeRolesFromMember(e.getMember(), role).queue();
                        SendMessage.sendMessage(e, String.format("Removed role '%s' (%s) from %s.", trigger, role.getName(), e.getMember().getEffectiveName()), messageCallback);
                        return;
                    }
                }

                SendMessage.sendMessage(e, "Error: Cannot leave a role which is not assigned.", messageCallback);
                return;
            }
        }

        SendMessage.sendMessage(e, String.format("Error: Could not find trigger '%s'. Use this command without arguments to see available triggers.", trigger), messageCallback);
    }

    private String getRemovableRolesString(Member member) {
        List<String> currentRoleIds = new ArrayList<>();
        member.getRoles().forEach(role -> currentRoleIds.add(role.getId()));

        List<String> triggers = new ArrayList<>();
        for (RoleMapping mapping : roleMappings) {
            if (currentRoleIds.contains(mapping.getRoleId())) {
                triggers.add(mapping.getTrigger());
            }
        }

        String triggersString = String.format("Triggers to leave: %s", String.join(", ", triggers));
        return triggersString;
    }

    @Override
    public CommandAccess getAccess() {
        return CommandAccess.ALL;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("leave");
    }

    @Override
    public String getDescription() {
        return "Leave a role.";
    }

    @Override
    public String getName() {
        return "Leave Command";
    }

    @Override
    public String getDetails() {
        return "leave - List current roles.\n" +
                "leave [role] - Leave the role.";
    }

    @Override
    public boolean isHidden() {
        return false;
    }
}
