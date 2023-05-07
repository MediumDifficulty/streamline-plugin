package yes.mediumdifficulty.streamline;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

public class RequestHandler implements HttpHandler {
    private void closeWithError(HttpExchange exchange, int errCode, @Nullable Map<String, List<String>> headerFields) throws IOException {
        String res = String.format("{\"err\":%s}", errCode);

        if (headerFields != null) {
            exchange.getResponseHeaders().putAll(headerFields);
        }
        exchange.sendResponseHeaders(errCode, res.length());

        OutputStream output = exchange.getResponseBody();
        output.write(res.getBytes(StandardCharsets.UTF_8));
        output.close();
    }

    @Override
    public void handle(HttpExchange exchange) {
        JavaPlugin plugin = JavaPlugin.getPlugin(Streamline.class);
        Logger logger = plugin.getLogger();

        try {
            handleInner(exchange);
        } catch(Exception err) {
            try {
                closeWithError(exchange, 400, null);
            } catch(Exception e) {
                logger.warning("Failed to send error to client");
            }
        }
    }

    private void handleInner(HttpExchange exchange) throws IOException {
        JavaPlugin plugin = JavaPlugin.getPlugin(Streamline.class);
        Logger logger = plugin.getLogger();

        if (!exchange.getRequestMethod().equals("POST")) {
            logger.warning("Incoming request not POST type");
            closeWithError(exchange, 405, Map.of("Allow", List.of("POST")));
            return;
        }


        DecodedJWT jwt;
        try {
            Algorithm algorithm = Algorithm.HMAC256(Objects.requireNonNull(ConfigManager.config.getString("secret")));
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();

            jwt = verifier.verify(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
        } catch (JWTVerificationException exception) {
            logger.warning("Could not verify incoming jwt");
            closeWithError(exchange, 403, null);
            return;
        }

        logger.info("Successfully verified JWT");

        String payload = new String(Base64.getDecoder().decode(jwt.getPayload().getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        JSONObject payloadJSON = new JSONObject(payload);

        ActionManager.handleActionAsync(payloadJSON.getString("action"), payloadJSON.getString("payload"), result -> {
            try {
                exchange.sendResponseHeaders(200, result.length());
                OutputStream output = exchange.getResponseBody();
                output.write(result.getBytes());
                output.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
