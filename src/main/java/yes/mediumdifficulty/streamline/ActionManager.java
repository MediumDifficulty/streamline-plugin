package yes.mediumdifficulty.streamline;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import yes.mediumdifficulty.streamline.action.AbstractAction;
import yes.mediumdifficulty.streamline.action.CommandAction;
import yes.mediumdifficulty.streamline.action.WhitelistAction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ActionManager {
    private static final Map<String, AbstractAction> actions = new HashMap<>();

    public static void addAction(AbstractAction action) {
        actions.put(action.getName(), action);
    }

    public static void init() {
        new WhitelistAction().register();
        new CommandAction().register();
    }

    public static boolean handleActionAsync(String actionName, String payload, Consumer<String> callback) {
        JavaPlugin plugin = JavaPlugin.getPlugin(Streamline.class);

        AbstractAction action = actions.get(actionName.toLowerCase());
        if (action != null) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                String result = action.handle(payload);
                plugin.getLogger().info(String.format("Ran action: %s", actionName));

                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> callback.accept(result));
            });
        }


        return action != null;
    }
}
