package yes.mediumdifficulty.streamline;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public final class Streamline extends JavaPlugin {
    HttpServer httpServer;

    @Override
    public void onEnable() {
        // Plugin startup logic
        ConfigManager.init(this);
        ActionManager.init();

        try {
            httpServer = HttpServer.create(new InetSocketAddress(ConfigManager.config.getInt("port")), 0);
            httpServer.createContext("/", new RequestHandler());
            httpServer.setExecutor(null);

            this.getLogger().info("Started http server!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Bukkit.getServer().getScheduler().runTaskAsynchronously(this, () -> httpServer.start());

        this.getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        httpServer.stop(1);
    }
}
