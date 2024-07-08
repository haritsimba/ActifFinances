package insta.soul.keycloak.actiffinances.binance.actions;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

import insta.soul.keycloak.actiffinances.binance.beans.MarketDataTicker;
import insta.soul.keycloak.actiffinances.binance.enumerations.BinanceDataTrickerTypes;
import insta.soul.keycloak.actiffinances.binance.enumerations.OdDataMarketGetterStatus;
import insta.soul.keycloak.actiffinances.retrofit.binance.BinanceApiServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OdDataMarketGetter {
    BinanceApiServices service;
    String symbol;
    List<String> symbols;
    BinanceDataTrickerTypes type;
    OdDataMarketListGetterCallback listCallback;
    OdDataMarketGetterCallback callback;

    public OdDataMarketGetter(BinanceApiServices service, List<String> symbols, BinanceDataTrickerTypes type, OdDataMarketListGetterCallback callback) {
        this.service = service;
        this.symbols = symbols;
        this.type = type;
        this.listCallback = callback;
    }
    public OdDataMarketGetter(BinanceApiServices service, String symbol, BinanceDataTrickerTypes type, OdDataMarketGetterCallback callback){
        this.service = service;
        this.symbol = symbol;
        this.type = type;
        this.callback = callback;
    }

    /**
     * Execution de la requêtte de recuperation des market datas
     */
    public void get24hMarketDataTrickers(){
        String symbols = new Utils().makeSymbol(this.symbols);
        Call<List<MarketDataTicker>> call = service.get24hMarketDataTickers(symbols,type);
        call.enqueue(new Callback<List<MarketDataTicker>>() {
            @Override
            public void onResponse(@NonNull Call<List<MarketDataTicker>> call, @NonNull Response<List<MarketDataTicker>> response) {
                Log.d("respose ","start");
                handleListResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<List<MarketDataTicker>> call, @NonNull Throwable throwable) {
                Log.e("fail", Objects.requireNonNull(throwable.getMessage()));
                handleListFailure(throwable);
            }
        });
    }

    /**
     * Execution de la requêtte de recuperation d'un market data
     */
    public void get24hMarketDataTricker(){
        Call<MarketDataTicker> call = service.get24hMarketDataTicker(symbol,type);
        call.enqueue(new Callback<MarketDataTicker>() {
            @Override
            public void onResponse(@NonNull Call<MarketDataTicker> call, @NonNull Response<MarketDataTicker> response) {
                handleResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<MarketDataTicker> call, @NonNull Throwable throwable) {
                handleListFailure(throwable);
            }
        });
    }

    /**
     * Si la requette renvoie une reponse (liste)
     * @param response
     */
    void handleListResponse(Response<List<MarketDataTicker>> response){
        if (response.isSuccessful()){
            List<MarketDataTicker> marketDataTickerList = response.body();
            listCallback.onSucces(OdDataMarketGetterStatus.SUCCESS, marketDataTickerList);
        } else {
            listCallback.onSucces(OdDataMarketGetterStatus.SUCCESS,null);
        }
    }

    /**
     * Si la requette renvoie une erreur (liste)
     * @param throwable
     */
    void handleListFailure(Throwable throwable){}

    /**
     * Si la requette renvoie une reponse
     * @param response
     */
    void handleResponse(Response<MarketDataTicker> response){
        if (response.isSuccessful()){
            MarketDataTicker marketDataTicker = response.body();
            callback.onSucces(OdDataMarketGetterStatus.SUCCESS, marketDataTicker);
        } else {
            callback.onSucces(OdDataMarketGetterStatus.SUCCESS,null);
        }
    }

    /**
     * Si la requette renvoie une erreur
     * @param throwable
     */
    void handleFailure(Throwable throwable){}
    /**
     * CallBacks (recuperation de la liste des market datas
     */
    public interface OdDataMarketListGetterCallback {
        void onSucces(OdDataMarketGetterStatus status,List<MarketDataTicker> marketDataTickerList);
        void onFail(String message);
    }

    /**
     * CallBacks (recuperation d'un market data)
     */
    public interface OdDataMarketGetterCallback {
        void onSucces(OdDataMarketGetterStatus status, MarketDataTicker marketDataTicker);
        void onFail(String message);
    }
}