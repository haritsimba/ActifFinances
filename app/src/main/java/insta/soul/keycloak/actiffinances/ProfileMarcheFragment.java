package insta.soul.keycloak.actiffinances;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import insta.soul.keycloak.actiffinances.binance.actions.Utils;
import insta.soul.keycloak.actiffinances.binance.beans.BlockchainList;
import insta.soul.keycloak.actiffinances.binance.services.DataMarketService;
import insta.soul.keycloak.actiffinances.binance.services.websocket.listener.DataMarketListener;
import insta.soul.keycloak.actiffinances.binance.services.websocket.WebSocketManager;
import insta.soul.keycloak.actiffinances.listadapter.DataMarketListAdapter;
import okhttp3.OkHttpClient;

public class ProfileMarcheFragment extends Fragment {

    DataMarketService dataMarketService;
    private OkHttpClient client;
    private final List<WebSocketManager> webSocketManagers = new ArrayList<>();
    private DataMarketListener webSocketListener;
    DataMarketListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new DataMarketListAdapter();
        webSocketListener = new DataMarketListener(adapter);
        for (int i = 0; i < BlockchainList.getInstance().getCryptoListsOptimized().size(); i++) {
            webSocketManagers.add(new WebSocketManager(webSocketListener));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the la   yout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_marche, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.market_data_list);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setSupportsChangeAnimations(false);
        recyclerView.setItemAnimator(animator);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        int websocketCounter = 0;
        for (WebSocketManager webSocketManager : webSocketManagers){
            webSocketManager.startWebSocket("wss://stream.binance.com:9443/ws");
            String params = "{\"method\": \"SUBSCRIBE\", \"params\": "+ new Utils().makeWebsocketParams(BlockchainList.getInstance().getCryptoListsOptimized().get(websocketCounter))+"}";
            Log.d("param",params);
            webSocketManager.sendMessage(params);
            websocketCounter++;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (WebSocketManager webSocketManager : webSocketManagers) {
            webSocketManager.closeWebSocket();
        }
        Log.e("ws","close");
    }
}
