package insta.soul.keycloak.actiffinances.binance.services;

import java.util.List;

import insta.soul.keycloak.actiffinances.binance.actions.OdDataMarketGetter;
import insta.soul.keycloak.actiffinances.binance.enumerations.BinanceDataTrickerTypes;
import insta.soul.keycloak.actiffinances.retrofit.binance.BinanceApiServices;
import insta.soul.keycloak.actiffinances.retrofit.binance.RetrofitBinanceClientInstance;

public class DataMarketService {
    BinanceApiServices binanceApiServices;

    public DataMarketService(){
         binanceApiServices = RetrofitBinanceClientInstance.getRetrofitInstance().create(BinanceApiServices.class);
    }

    public void getAll24hMarketData(List<String> symbols, BinanceDataTrickerTypes types, OdDataMarketGetter.OdDataMarketListGetterCallback callback){
        OdDataMarketGetter odDataMarketGetter = new OdDataMarketGetter(binanceApiServices,symbols, types,callback);
        odDataMarketGetter.get24hMarketDataTrickers();
    }
}
