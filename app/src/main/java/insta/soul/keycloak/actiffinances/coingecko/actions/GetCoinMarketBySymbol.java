package insta.soul.keycloak.actiffinances.coingecko.actions;

import android.util.Log;

import java.util.List;
import java.util.Objects;

import insta.soul.keycloak.actiffinances.coingecko.GetCoinMarketInfoStatus;
import insta.soul.keycloak.actiffinances.coingecko.beans.Coin;
import insta.soul.keycloak.actiffinances.coingecko.beans.CoinMarket;
import insta.soul.keycloak.actiffinances.retrofit.coingecko.CoingeckoApiServices;
import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AllArgsConstructor
public class GetCoinMarketBySymbol {
    private String symbol;

    public GetCoinMarketBySymbol(String symbol, CoingeckoApiServices services, GetCoinMarketBySymbolCallback callback) {
        this.symbol = symbol;
        this.services = services;
        this.callback = callback;
    }

    private CoingeckoApiServices services;
    private GetCoinMarketBySymbolCallback callback;

    public void getCoinMarketInfo(){
        Call<List<Coin>> call = services.getCoinList();
        call.enqueue(new Callback<List<Coin>>() {
            @Override
            public void onResponse(Call<List<Coin>> call, Response<List<Coin>> response) {
                if(response.isSuccessful()){
                    List<Coin> coinList = response.body();
                    for (Coin coin: coinList) {
                    if (Objects.equals(coin.getSymbol(), symbol)){
                            GetCoinMarketInfoByIdAndCurrency getCoinMarketInfoByIdAndCurrency = new GetCoinMarketInfoByIdAndCurrency(coin.getId(), "usd", services, new GetCoinMarketInfoByIdAndCurrency.GetCoinMarketInfoByIdAndCurrencyCallback() {
                                @Override
                                public void onSucces(GetCoinMarketInfoStatus status, List<CoinMarket> coinMarkets) {
                                    callback.onSucces(GetCoinMarketInfoStatus.SUCCES,coinMarkets.get(0));
                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    callback.onFaillure(throwable);
                                }
                            });
                            getCoinMarketInfoByIdAndCurrency.getCoinMarketInfo();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Coin>> call, Throwable throwable) {
                callback.onFaillure(throwable);
            }
        });
    }

    public interface GetCoinMarketBySymbolCallback{
        void onSucces(GetCoinMarketInfoStatus status,CoinMarket coinMarket);
        void onFaillure(Throwable throwable);
    }
}
