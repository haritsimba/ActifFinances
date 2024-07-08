package insta.soul.keycloak.actiffinances.binance.beans;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BlockchainListJsonParser extends Service {

    private static final String TAG = "JsonProcessingService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                processJson();
            }
        }).start();
        return START_STICKY;
    }

    private void processJson() {
        Gson gson = new Gson();

        try {
            // Lire le fichier JSON depuis les assets
            InputStream inputStream = getAssets().open("cryptoList2.json");

            // Désérialiser le fichier JSON en une liste d'objets TradingPair
            List<CryptoList> CryptoLists = gson.fromJson(new InputStreamReader(inputStream), new TypeToken<List<CryptoList>>(){}.getType());
            BlockchainList.getInstance().setCryptoLists(CryptoLists);
            List<CryptoList> newlist = new ArrayList<>();
            List<List<CryptoList>> cryptoListsOptimized = new ArrayList<>();
            int count = 0;
            for (CryptoList cryptoList : BlockchainList.getInstance().getCryptoLists()){
                if(count<250 && BlockchainList.getInstance().getCryptoLists().indexOf(cryptoList) != BlockchainList.getInstance().getCryptoLists().size()-1){
                    newlist.add(cryptoList);
                    count++;
                }else{
                    cryptoListsOptimized.add(newlist);
                    newlist = new ArrayList<>();
                    count = 0;
                }
            }
            BlockchainList.getInstance().setCryptoListsOptimized(cryptoListsOptimized);
            Log.d("test", String.valueOf(BlockchainList.getInstance().getCryptoListsOptimized().size()));
        } catch (IOException e) {
            Log.e("Erreur", "Erreur lors du chargement du fichier JSON", e);
        }
    }
}
