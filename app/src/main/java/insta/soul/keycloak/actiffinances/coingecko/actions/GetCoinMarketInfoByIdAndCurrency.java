package insta.soul.keycloak.actiffinances.coingecko.actions;

import java.util.List;

import insta.soul.keycloak.actiffinances.coingecko.GetCoinMarketInfoStatus;
import insta.soul.keycloak.actiffinances.coingecko.beans.CoinMarket;
import insta.soul.keycloak.actiffinances.retrofit.coingecko.CoingeckoApiServices;
import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AllArgsConstructor
public class GetCoinMarketInfoByIdAndCurrency {
    private String id;
    private String currecy;
    private CoingeckoApiServices services;
    private GetCoinMarketInfoByIdAndCurrencyCallback callback;



    void getCoinMarketInfo(){
        Call<List<CoinMarket>> call = services.getCoinMarkets(currecy,id);
        call.enqueue(new Callback<List<CoinMarket>>() {
            @Override
            public void onResponse(Call<List<CoinMarket>> call, Response<List<CoinMarket>> response) {
                if (response.isSuccessful()){
                    List<CoinMarket> coinMarkets = response.body();
                    callback.onSucces(GetCoinMarketInfoStatus.SUCCES,coinMarkets);
                }
            }

            @Override
            public void onFailure(Call<List<CoinMarket>> call, Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    public interface GetCoinMarketInfoByIdAndCurrencyCallback{
        void onSucces(GetCoinMarketInfoStatus status,List<CoinMarket> coinMarkets);
        void onFailure(Throwable throwable);
    }

}
