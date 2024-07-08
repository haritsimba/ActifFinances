package insta.soul.keycloak.actiffinances.binance.services.websocket.listener;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import insta.soul.keycloak.actiffinances.binance.actions.Utils;
import insta.soul.keycloak.actiffinances.binance.beans.BlockchainList;
import insta.soul.keycloak.actiffinances.listadapter.DataMarketListAdapter;
import insta.soul.keycloak.actiffinances.listmodels.MarketDataItem;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import org.json.JSONObject;

public class DataMarketListener extends WebSocketListener {
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final DataMarketListAdapter adapter;

    public DataMarketListener(DataMarketListAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
        try {
            JSONObject jsonObject = new JSONObject(text);
            String symbol = jsonObject.getString("s").substring(0, jsonObject.getString("s").length() - 4);
            double price = jsonObject.getDouble("c");
            double priceChangePercent = jsonObject.getDouble("P");
            String blockChanainName = new Utils().getBlockChainsName(BlockchainList.getInstance().getCryptoLists(), symbol);
            mainHandler.post(() -> {

                int position = adapter.updateItem(symbol, price,priceChangePercent);
                if (position == -1) {
                    adapter.addItem(new MarketDataItem(blockChanainName,symbol,symbol,price,priceChangePercent));
                } else {
                    adapter.notifyItemChanged(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}