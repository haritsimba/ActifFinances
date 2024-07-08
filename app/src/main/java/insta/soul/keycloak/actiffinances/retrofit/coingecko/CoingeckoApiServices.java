package insta.soul.keycloak.actiffinances.retrofit.coingecko;

import java.util.List;

import insta.soul.keycloak.actiffinances.coingecko.beans.Coin;
import insta.soul.keycloak.actiffinances.coingecko.beans.CoinMarket;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoingeckoApiServices {
    @GET("/api/v3/coins/list")
    Call<List<Coin>> getCoinList();

    @GET("/api/v3/coins/markets")
    Call<List<CoinMarket>> getCoinMarkets(
            @Query("vs_currency") String vsCurrency,
            @Query("ids") String ids
    );
}
