package yes.mediumdifficulty.streamline.action;

import org.bukkit.plugin.java.JavaPlugin;
import yes.mediumdifficulty.streamline.ActionManager;
import yes.mediumdifficulty.streamline.Streamline;

public abstract class AbstractAction {
    public void register() {
        JavaPlugin plugin = JavaPlugin.getPlugin(Streamline.class);
        ActionManager.addAction(this);
        plugin.getLogger().info(String.format("Registered action: %s", this.getName()));
    }

    public abstract String handle(String payload);
    public abstract String getName();
}
