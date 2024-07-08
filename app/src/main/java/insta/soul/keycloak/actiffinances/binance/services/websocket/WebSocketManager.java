package insta.soul.keycloak.actiffinances.binance.services.websocket;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketManager {
    private final OkHttpClient client;
    private WebSocket webSocket;
    private final WebSocketListener listener;

    public WebSocketManager(WebSocketListener listener) {
        this.client = new OkHttpClient();
        this.listener = listener;
    }

    public void startWebSocket(String url) {
        Request request = new Request.Builder().url(url).build();
        webSocket = client.newWebSocket(request, listener);
        Log.d("websocket", "webscoket started");
    }

    public void closeWebSocket() {
        if (webSocket != null) {
            webSocket.close(1000, "Closing");
        }
    }
    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }
}