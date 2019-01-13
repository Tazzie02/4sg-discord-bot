package au.com.fourseasonsgaming.fourseasonsgamingbot.commands;

import au.com.fourseasonsgaming.fourseasonsgamingbot.models.RoleMapping;
import com.tazzie02.tazbotdiscordlib.Command;
import com.tazzie02.tazbotdiscordlib.SendMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeaveCommand implements Command {

    private final List<RoleMapping> roleMappings;

    public LeaveCommand(List<RoleMapping> roleMappings) {
        this.roleMappings = roleMappings;
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        if (!PermissionUtil.checkPermission(e.getGuild().getSelfMember(), Permission.MANAGE_PERMISSIONS)) {
            SendMessage.sendMessage(e, "Error: Bot must have Manage Roles permission. Contact the bot administrator.");
            return;
        }

        if (args.length == 0) {
            SendMessage.sendMessage(e, getRemovableRolesString(e.getMember()));
            return;
        }

        String trigger = String.join(" ", args);
        for (RoleMapping mapping : roleMappings) {
            if (trigger.equalsIgnoreCase(mapping.getTrigger())) {
                Role role = e.getGuild().getRoleById(mapping.getRoleId());
                if (role == null) {
                    SendMessage.sendMessage(e, "Error: Could not find role with ID specified in configuration. Contact the bot administrator.");
                    return;
                }

                for (Role memberRole : e.getMember().getRoles()) {
                    if (memberRole.getId().equalsIgnoreCase(mapping.getRoleId())) {
                        e.getGuild().getController().removeRolesFromMember(e.getMember(), role).queue();
                        SendMessage.sendMessage(e, String.format("Removed role '%s' (%s) from %s.", trigger, role.getName(), e.getMember().getEffectiveName()));
                        return;
                    }
                }

                SendMessage.sendMessage(e, "Error: Cannot leave a role which is not assigned.");
                return;
            }
        }

        SendMessage.sendMessage(e, String.format("Error: Could not find trigger '%s'. Use this command without arguments to see available triggers.", trigger));
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
