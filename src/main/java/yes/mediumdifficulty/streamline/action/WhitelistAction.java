package yes.mediumdifficulty.streamline.action;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.json.JSONObject;

import yes.mediumdifficulty.streamline.utils.Uuid;

import java.util.UUID;

public class WhitelistAction extends AbstractAction {
    @Override
    public String handle(String payload) {
        JSONObject payloadJson = new JSONObject(payload);

        String operation = payloadJson.getString("operation");

        Server server = Bukkit.getServer();

        
        switch (operation) {
            case "add" -> {
                String fullUuid = Uuid.trimmedToFull(payloadJson.getString("uuid"));
                
                server.getOfflinePlayer(UUID.fromString(fullUuid)).setWhitelisted(true);
                return payload;
            }
            case "remove" -> {
                String fullUuid = Uuid.trimmedToFull(payloadJson.getString("uuid"));
                
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
