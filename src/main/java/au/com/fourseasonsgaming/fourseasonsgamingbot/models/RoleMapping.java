package au.com.fourseasonsgaming.fourseasonsgamingbot.models;

public class RoleMapping {
    private String trigger;
    private String roleId;

    public RoleMapping(String trigger, String roleId) {
        this.trigger = trigger;
        this.roleId = roleId;
    }

    public String getTrigger() {
        return trigger;
    }

    public String getRoleId() {
        return roleId;
    }

}
