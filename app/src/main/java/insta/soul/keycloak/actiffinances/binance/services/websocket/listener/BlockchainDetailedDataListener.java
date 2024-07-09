package insta.soul.keycloak.actiffinances.binance.services.websocket.listener;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import insta.soul.keycloak.actiffinances.BlockchainDetailedData;
import insta.soul.keycloak.actiffinances.binance.actions.Utils;
import insta.soul.keycloak.actiffinances.binance.beans.BlockchainList;
import lombok.Getter;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class BlockchainDetailedDataListener extends WebSocketListener {
    TextView priceChangePercent;
    TextView price;
    BlockchainDetailedData blockchainDetailedData;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public BlockchainDetailedDataListener(TextView priceChangePercent, TextView price, BlockchainDetailedData blockchainDetailedData){
        this.priceChangePercent = priceChangePercent;
        this.price = price;
        this.blockchainDetailedData = blockchainDetailedData;
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
        super.onMessage(webSocket, text);
        try {
            JSONObject jsonObject = new JSONObject(text);
            String symbol = jsonObject.getString("s").substring(0, jsonObject.getString("s").length() - 4);
            double price = jsonObject.getDouble("c");
            double priceChangePercent = jsonObject.getDouble("P");
            this.price.setText(String.valueOf(price));
            this.priceChangePercent.setText(String.valueOf(priceChangePercent));
            blockchainDetailedData.getBdaPrice().setText(String.valueOf(price));
            blockchainDetailedData.getBdaPriceChangePercent().setText(String.valueOf(priceChangePercent));
        }catch (Exception e){

        }

    }
}
