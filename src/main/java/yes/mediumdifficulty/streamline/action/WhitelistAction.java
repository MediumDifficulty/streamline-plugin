package yes.mediumdifficulty.streamline.action;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import yes.mediumdifficulty.streamline.Streamline;
import yes.mediumdifficulty.streamline.utils.Uuid;

import java.util.UUID;

public class WhitelistAction extends AbstractAction {
    @Override
    public String handle(String payload) {
        JavaPlugin plugin = JavaPlugin.getPlugin(Streamline.class);

        JSONObject payloadJson = new JSONObject(payload);

        String operation = payloadJson.getString("operation");

        Server server = Bukkit.getServer();

        
        switch (operation) {
            case "add" -> {
                plugin.getLogger().info(payloadJson.toString());
                String uuid = payloadJson.getString("uuid");
                String fullUuid = Uuid.trimmedToFull(uuid);
                
                server.getOfflinePlayer(UUID.fromString(fullUuid)).setWhitelisted(true);
                return payload;
            }
            case "remove" -> {
                String uuid = payloadJson.getString("uuid");
                String fullUuid = Uuid.trimmedToFull(uuid);
                
                server.getOfflinePlayer(UUID.fromString(fullUuid)).setWhitelisted(false);
                return payload;
            }
            case "list" -> {
                return JSONObject.valueToString(server.getWhitelistedPlayers()
                        .stream()
                        .map(OfflinePlayer::getUniqueId)
                        .map(uuid -> Uuid.fullToTrimmed(uuid.toString()))
                        .toArray());
            }
        }

        return payload;
    }

    @Override
    public String getName() {
        return "whitelist";
    }
}
