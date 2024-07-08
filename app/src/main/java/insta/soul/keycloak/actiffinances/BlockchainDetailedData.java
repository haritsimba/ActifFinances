package insta.soul.keycloak.actiffinances;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import insta.soul.keycloak.actiffinances.binance.actions.GetCadleStick;
import insta.soul.keycloak.actiffinances.binance.beans.Candlestick;
import insta.soul.keycloak.actiffinances.binance.enumerations.GetCandleStickStatus;
import insta.soul.keycloak.actiffinances.binance.services.DataMarketService;
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
    private ShapeableImageView bdaPrevious;
    CoinMarket infoSurLeBlockChain;
    View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blockchain_detailed_data);
        mainView = findViewById(R.id.main);
        //lineChart
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
        bdaPrevious = findViewById(R.id.bda_back_btn);

        // Configuration initiale du graphique
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(true);

        //cacher les elements
        mainView.setVisibility(View.GONE);
        // Connexion à l'API WebSocket de Binance
        client = new OkHttpClient();

        //animation
        LoadingAnimation loader = new LoadingAnimation(this);
        loader.show();
        String symbol = getIntent().getStringExtra("symbol") != null ? getIntent().getStringExtra("symbol").toLowerCase()  : "btcusdt" ;
        new CoingeckoService().getCoinckoInfo(symbol, new GetCoinMarketBySymbol.GetCoinMarketBySymbolCallback() {
            @Override
            public void onSucces(GetCoinMarketInfoStatus status, CoinMarket coinMarket) {

                infoSurLeBlockChain = coinMarket;
                bdaAbout.setText(coinMarket.getName());
                bdaNiveauRecord.setText(String.valueOf(coinMarket.getAth()));
                bdaPlusBasPrix.setText(String.valueOf(coinMarket.getAtl()));
                bdaCapitalization.setText(String.valueOf(coinMarket.getMarketCap()));
                bdaApprovisionnement.setText(String.valueOf(coinMarket.getCirculatingSupply()));
                bdaMontantTotal.setText(String.valueOf(coinMarket.getTotalSupply()));
                bdaName.setText(coinMarket.getName());
                bdaSymbol.setText(coinMarket.getSymbol().toUpperCase());
                mainView.setVisibility(View.VISIBLE);
                updateChart();

                loader.cancel();
            }
            @Override
            public void onFaillure(Throwable throwable) {
                Log.e("Error", Objects.requireNonNull(throwable.getMessage()));
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (loader.isShowing()){
                    loader.cancel();
                }
                finish();
            }
        });
        bdaPrevious.setOnClickListener(v -> {
            if (loader.isShowing()) {
                loader.cancel();
            }
            finish();
        });

    }


    void updateChart() {
        String binanceSymbol = infoSurLeBlockChain.getSymbol().toUpperCase().concat("USDT");
        new DataMarketService().getCandleStickOneY(binanceSymbol, new GetCadleStick.GetCandleStickCallback() {
            @Override
            public void onSucces(GetCandleStickStatus status, List<Candlestick> candlesticks) {

                float minPrice = Float.MAX_VALUE;
                float maxPrice = Float.MIN_VALUE;
                List<Entry> entries = new ArrayList<>();
                for (Candlestick candlestick : candlesticks) {
                    float closePrice = Float.parseFloat(candlestick.getClose());
                    entries.add(new Entry(candlestick.getCloseTime(), closePrice));
                    if (closePrice < minPrice) {
                        minPrice = closePrice;
                    }
                    if (closePrice > maxPrice) {
                        maxPrice = closePrice;
                    }
                }
                LineDataSet dataSet = new LineDataSet(entries, "Variation de prix de " + infoSurLeBlockChain.getName() + " en une année");

                // Configurer le LineDataSet pour afficher uniquement les lignes sans points
                dataSet.setDrawCircles(false);
                dataSet.setDrawValues(false);
                dataSet.setLineWidth(2f); // Largeur de ligne

                LineData lineData = new LineData(dataSet);
                lineChart.setData(lineData);

                // Ajouter les lignes de limite pour les prix max et min
                YAxis leftAxis = lineChart.getAxisLeft();
                leftAxis.removeAllLimitLines(); // Supprimer les lignes de limite existantes
                LimitLine llMax = new LimitLine(maxPrice, "Max (" + maxPrice + " $)");
                llMax.setLineColor(Color.RED);
                llMax.setLineWidth(2f);
                llMax.setTextColor(Color.RED);
                llMax.setTextSize(12f);

                LimitLine llMin = new LimitLine(minPrice, "Min (" + minPrice + " $)");
                llMin.setLineColor(Color.BLUE);
                llMin.setLineWidth(2f);
                llMin.setTextColor(Color.BLUE);
                llMin.setTextSize(12f);

                leftAxis.addLimitLine(llMax);
                leftAxis.addLimitLine(llMin);

                leftAxis.setDrawLimitLinesBehindData(true);

                // Configurer l'axe des abscisses (X)
                XAxis xAxis = lineChart.getXAxis();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    xAxis.setValueFormatter(new ValueFormatter() {
                        private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.FRANCE);

                        @Override
                        public String getFormattedValue(float value) {
                            return dateFormat.format(new Date((long) value));
                        }
                    });
                }
                xAxis.setLabelRotationAngle(-45); // Rotation des labels
                xAxis.setGranularity(1f); // Intervalle minimal entre les labels
                xAxis.setGranularityEnabled(true);

                // Configurer l'axe des ordonnées (Y)
                YAxis rightAxis = lineChart.getAxisRight();
                rightAxis.setEnabled(false); // Désactiver l'axe droit

                lineChart.invalidate();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

}