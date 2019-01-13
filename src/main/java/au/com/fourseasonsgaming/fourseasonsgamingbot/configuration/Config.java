package au.com.fourseasonsgaming.fourseasonsgamingbot.configuration;

import au.com.fourseasonsgaming.fourseasonsgamingbot.models.RoleMapping;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Config {

    @SerializedName("role-mappings")
    private final RoleMapping[] roleMappings;

    public Config(RoleMapping[] roleMappings) {
        this.roleMappings = roleMappings;
    }

    public List<RoleMapping> getRoleMappings() {
        return Collections.unmodifiableList(Arrays.asList(roleMappings));
    }

    public static Config defaultFactory() {
        RoleMapping[] roleMappings = {new RoleMapping("", "")};
        Config config = new Config(roleMappings);

        return config;
    }
}
