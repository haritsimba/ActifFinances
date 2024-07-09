package insta.soul.keycloak.actiffinances.binance.services.websocket.listener;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import insta.soul.keycloak.actiffinances.binance.actions.Utils;
import insta.soul.keycloak.actiffinances.binance.beans.BlockchainList;
import lombok.Getter;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class BlockchainDetailedDataListener extends WebSocketListener {
    @Getter
    TextView priceChangePercent;
    TextView price;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public BlockchainDetailedDataListener(TextView priceChangePercent, TextView price){
        this.priceChangePercent = priceChangePercent;
        this.price = price;
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
        super.onMessage(webSocket, text);
        try {
            JSONObject jsonObject = new JSONObject(text);
            String symbol = jsonObject.getString("s").substring(0, jsonObject.getString("s").length() - 4);
            double price = jsonObject.getDouble("c");
            double priceChangePercent = jsonObject.getDouble("P");
            String blockChanainName = new Utils().getBlockChainsName(BlockchainList.getInstance().getCryptoLists(), symbol);
            Log.d("item","{ name : " + blockChanainName + ", symbol : " +symbol + "}");
            this.price.setText(String.valueOf(priceChangePercent).concat(" $"));
            this.priceChangePercent.setText(String.valueOf(priceChangePercent).concat(" %"));
        }catch (Exception e){

        }

    }
}
