package yes.mediumdifficulty.streamline;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
    public static FileConfiguration config;

    public static void init(JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }
}
