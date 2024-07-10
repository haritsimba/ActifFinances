package insta.soul.keycloak.actiffinances.coingecko.services;

import insta.soul.keycloak.actiffinances.coingecko.actions.GetCoinMarketBySymbol;
import insta.soul.keycloak.actiffinances.coingecko.actions.GetFeed;
import insta.soul.keycloak.actiffinances.retrofit.coingecko.CoingeckoApiServices;
import insta.soul.keycloak.actiffinances.retrofit.coingecko.RetrofitCoingeckoClientInstance;
import lombok.AllArgsConstructor;


public class CoingeckoService {


    private final CoingeckoApiServices service;


    public CoingeckoService() {
         service = RetrofitCoingeckoClientInstance.getRetrofitInstance().create(CoingeckoApiServices.class);

    }

    public void getCoinckoInfo(String symbol, GetCoinMarketBySymbol.GetCoinMarketBySymbolCallback callback){
        GetCoinMarketBySymbol getCoinMarketBySymbol = new GetCoinMarketBySymbol(symbol,this.service,callback);
        getCoinMarketBySymbol.getCoinMarketInfo();
    }
    public void getFeed(GetFeed.GetFeedCallback feedCallback){
        for (int i = 1; i <5 ; i++) {
            GetFeed getFeed= new GetFeed(service,feedCallback,i);
            getFeed.execute();
        }

    }
}
