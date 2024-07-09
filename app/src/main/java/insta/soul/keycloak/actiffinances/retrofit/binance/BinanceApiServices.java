package insta.soul.keycloak.actiffinances.retrofit.binance;

import java.util.List;

import insta.soul.keycloak.actiffinances.binance.beans.Candlestick;
import insta.soul.keycloak.actiffinances.binance.beans.MarketDataTicker;
import insta.soul.keycloak.actiffinances.binance.enumerations.BinanceDataTrickerTypes;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BinanceApiServices {

    @GET("/api/v3/ticker/24hr")
    Call<List<MarketDataTicker>> get24hMarketDataTickers(
            @Query("symbols") String symbols,
            @Query("type")BinanceDataTrickerTypes type
            );
    @GET("/api/v3/ticker/24hr")
    Call<MarketDataTicker> get24hMarketDataTicker(
            @Query("symbol") String symbol,
            @Query("type")BinanceDataTrickerTypes type
    );
    @GET("/api/v3/klines")
    Call<List<Candlestick>> getKlines(
            @Query("symbol") String symbol,
            @Query("interval") String interval,
            @Query("startTime") long startTime,
            @Query("endTime") long endTime
    );
}
