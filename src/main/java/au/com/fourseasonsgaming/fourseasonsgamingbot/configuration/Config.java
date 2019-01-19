package au.com.fourseasonsgaming.fourseasonsgamingbot.configuration;

import au.com.fourseasonsgaming.fourseasonsgamingbot.models.RoleMapping;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Config {

    @SerializedName("role-mappings")
    private final RoleMapping[] roleMappings;
    @SerializedName("delete-messages-after-seconds")
    private final int deleteMessagesAfterSeconds;

    public Config(RoleMapping[] roleMappings, int deleteMessagesAfterSeconds) {
        this.roleMappings = roleMappings;
        this.deleteMessagesAfterSeconds = deleteMessagesAfterSeconds;
    }

    public List<RoleMapping> getRoleMappings() {
        return Collections.unmodifiableList(Arrays.asList(roleMappings));
    }

    public int getDeleteMessagesAfterSeconds() {
        return this.deleteMessagesAfterSeconds;
    }

    public static Config defaultFactory() {
        RoleMapping[] roleMappings = {new RoleMapping("", "")};
        int deleteMessagesAfterSeconds = 5;

        Config config = new Config(roleMappings, deleteMessagesAfterSeconds);

        return config;
    }
}
