package yes.mediumdifficulty.streamline.action;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.json.JSONObject;

import java.util.UUID;

public class WhitelistAction extends AbstractAction {
    @Override
    public String handle(String payload) {
        JSONObject payloadJson = new JSONObject(payload);

        String operation = payloadJson.getString("operation");

        Server server = Bukkit.getServer();

        switch (operation) {
            case "add" -> {
                server.getOfflinePlayer(UUID.fromString(payloadJson.getString("uuid"))).setWhitelisted(true);
                return payload;
            }
            case "remove" -> {
                server.getOfflinePlayer(UUID.fromString(payloadJson.getString("uuid"))).setWhitelisted(false);
                return payload;
            }
            case "list" -> {
                return JSONObject.valueToString(server.getWhitelistedPlayers()
                        .stream()
                        .map(OfflinePlayer::getUniqueId)
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
