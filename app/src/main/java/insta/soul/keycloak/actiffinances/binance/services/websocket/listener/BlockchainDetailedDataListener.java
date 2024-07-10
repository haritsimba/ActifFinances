package insta.soul.keycloak.actiffinances.binance.services.websocket.listener;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import insta.soul.keycloak.actiffinances.BlockchainDetailedData;
import insta.soul.keycloak.actiffinances.R;
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

    @SuppressLint("DefaultLocale")
    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
        super.onMessage(webSocket, text);
        try {
            JSONObject jsonObject = new JSONObject(text);
            String symbol = jsonObject.getString("s").substring(0, jsonObject.getString("s").length() - 4);
            double price = jsonObject.getDouble("c");

            double priceChangePercent = jsonObject.getDouble("P");
            long timestamp = System.currentTimeMillis();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                timestamp = Instant.now().toEpochMilli();
            }
            long finalTimestamp = timestamp;
            mainHandler.post(() -> {
                // Update UI elements on the main thread
                this.price.setText(String.format("%.8f", price).concat(" $"));
                this.price.setTextColor(blockchainDetailedData.getResources().getColor(R.color.blue));
                if (priceChangePercent<0){
                    this.priceChangePercent.setTextColor(blockchainDetailedData.getResources().getColor(R.color.red));
                }else{
                    this.priceChangePercent.setTextColor(blockchainDetailedData.getResources().getColor(R.color.green));
                }
                this.priceChangePercent.setText(String.valueOf(priceChangePercent).concat(" %"));
                blockchainDetailedData.getEntries().add(new Entry(finalTimestamp, (float) price));
                blockchainDetailedData.getLineChart().notifyDataSetChanged();
                blockchainDetailedData.getLineChart().invalidate();
            });
        }catch (Exception e){
            Log.d("Erreur", "onMessage: "+e.getMessage());
        }

    }
}
