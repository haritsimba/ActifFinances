package insta.soul.keycloak.actiffinances;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;

import insta.soul.keycloak.actiffinances.coingecko.GetCoinMarketInfoStatus;
import insta.soul.keycloak.actiffinances.coingecko.actions.GetCoinMarketBySymbol;
import insta.soul.keycloak.actiffinances.coingecko.beans.CoinMarket;
import insta.soul.keycloak.actiffinances.coingecko.services.CoingeckoService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class BlockchainDetailedData extends AppCompatActivity {


 /**   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blockchain_detailed_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
 **/
    private LineChart lineChart;
    private OkHttpClient client;
    private TextView bdaCapitalization;
    private TextView bdaPrice;
    private TextView bdaPriceChangePercent;
    private TextView bdaName;
    private TextView bdaAbout;
    private TextView bdaApprovisionnement;
    private TextView bdaApprovisionnementMax;
    private TextView bdaMontantTotal;
    private TextView bdaNiveauRecord;
    private TextView bdaPlusBasPrix;
    private TextView bdaRange;
    private TextView bdaSymbol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blockchain_detailed_data);
        lineChart = findViewById(R.id.bda_linechart);
        bdaCapitalization = findViewById(R.id.bda_capitalization_value);
        bdaPrice = findViewById(R.id.bda_price_txt);
        bdaPriceChangePercent = findViewById(R.id.bda_price_change_txt);
        bdaName = findViewById(R.id.bda_name_txt);
        bdaAbout = findViewById(R.id.bda_about_name);
        bdaApprovisionnement = findViewById(R.id.bda_approvisionnement_value);
      //  bdaApprovisionnementMax = findViewById(R.id.bda_approvisonnement_max_value);
        bdaMontantTotal = findViewById(R.id.bda_montant_total_value);
        bdaNiveauRecord = findViewById(R.id.bda_niveau_record_value);
        bdaPlusBasPrix = findViewById(R.id.bda_plus_bas_prix_value);
        //bdaRange = findViewById(R.id.bda_range_value);
        bdaSymbol = findViewById(R.id.bda_symbol_txt);

        // Configuration initiale du graphique
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(true);

        // Configuration de l'axe X
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Configuration de l'axe Y
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.GRAY);
        lineChart.getAxisRight().setEnabled(false);

        // Ajout des données initiales
        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);
        lineChart.setData(data);

        // Connexion à l'API WebSocket de Binance
        client = new OkHttpClient();
        connectWebSocket();

        String symbol = getIntent().getStringExtra("symbol") != null ? getIntent().getStringExtra("symbol").toLowerCase()  : "btcusdt" ;
        new CoingeckoService().getCoinckoInfo(symbol, new GetCoinMarketBySymbol.GetCoinMarketBySymbolCallback() {
            @Override
            public void onSucces(GetCoinMarketInfoStatus status, CoinMarket coinMarket) {
                bdaAbout.setText(coinMarket.getName());
                bdaNiveauRecord.setText(String.valueOf(coinMarket.getAth()));
                bdaPlusBasPrix.setText(String.valueOf(coinMarket.getAtl()));
                bdaCapitalization.setText(String.valueOf(coinMarket.getMarketCap()));
                bdaApprovisionnement.setText(String.valueOf(coinMarket.getCirculatingSupply()));
                bdaMontantTotal.setText(String.valueOf(coinMarket.getTotalSupply()));
                bdaName.setText(coinMarket.getName());
                bdaSymbol.setText(coinMarket.getSymbol().toUpperCase());
            }
            @Override
            public void onFaillure(Throwable throwable) {
                Log.e("Error", Objects.requireNonNull(throwable.getMessage()));
            }
        });

    }

    private void connectWebSocket() {

        String symbol = getIntent().getStringExtra("symbol") != null ? Objects.requireNonNull(getIntent().getStringExtra("symbol")).toLowerCase() : "btc";
        Request request = new Request.Builder().url("wss://stream.binance.com:9443/ws").build();

        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                String mess = "{\"method\" :\"SUBSCRIBE\", \"params\" : [\""+symbol.concat("usdt@ticker")+"\"]} ";
                webSocket.send(mess);
                Log.d("Warning",mess);
                // WebSocket ouvert
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                Log.d("message", "message");
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(text);
                    double price = jsonObject.getDouble("c");
                    double priceChangePercent = jsonObject.getDouble("P");

                    // Utilisez runOnUiThread pour mettre à jour l'interface utilisateur
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (bdaPrice != null) {
                                bdaPrice.setText(String.valueOf(price).concat(" $"));
                            }
                            if (bdaPriceChangePercent != null) {
                                bdaPriceChangePercent.setText(String.valueOf(priceChangePercent).concat( " %"));
                            }
                            // Ajout de l'entrée dans le graphique
                            addEntry((float) price);
                        }
                    });
                } catch (JSONException e) {
                    Log.e("error", e.getMessage());
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                // Gérer les messages binaires si nécessaire
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                t.printStackTrace();
            }
        };

        client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private void addEntry(float price) {
        LineData data = lineChart.getData();

        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), price), 0);
            data.notifyDataChanged();

            lineChart.notifyDataSetChanged();
            lineChart.setVisibleXRangeMaximum(50);
            lineChart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Variation de prix en temps réel");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.RED);
        set.setLineWidth(2f);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        set.setDrawFilled(true);
        set.setFillColor(Color.RED);
        set.setMode(LineDataSet.Mode.LINEAR);

        return set;
    }
}