package insta.soul.keycloak.actiffinances.binance.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Calendar;
import java.util.List;

import insta.soul.keycloak.actiffinances.binance.actions.CandlestickDeserializer;
import insta.soul.keycloak.actiffinances.binance.actions.GetCadleStick;
import insta.soul.keycloak.actiffinances.binance.actions.OdDataMarketGetter;
import insta.soul.keycloak.actiffinances.binance.beans.Candlestick;
import insta.soul.keycloak.actiffinances.binance.enumerations.BinanceDataTrickerTypes;
import insta.soul.keycloak.actiffinances.retrofit.binance.BinanceApiServices;
import insta.soul.keycloak.actiffinances.retrofit.binance.RetrofitBinanceClientInstance;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataMarketService {
    BinanceApiServices binanceApiServices;

    public DataMarketService(){
         binanceApiServices = RetrofitBinanceClientInstance.getRetrofitInstance().create(BinanceApiServices.class);
    }

    public void getAll24hMarketData(List<String> symbols, BinanceDataTrickerTypes types, OdDataMarketGetter.OdDataMarketListGetterCallback callback){
        OdDataMarketGetter odDataMarketGetter = new OdDataMarketGetter(binanceApiServices,symbols, types,callback);
        odDataMarketGetter.get24hMarketDataTrickers();
    }

    public void getCandleStickOneY(String symbol, GetCadleStick.GetCandleStickCallback callback){
        long currentTimeStamp = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<Candlestick>>(){}.getType(), new CandlestickDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.binance.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        BinanceApiServices apiService = retrofit.create(BinanceApiServices.class);

        long oneYearAgoTimestamp = calendar.getTimeInMillis();
        GetCadleStick getCadleStick = new GetCadleStick(apiService,symbol,"1h",oneYearAgoTimestamp,currentTimeStamp,callback);
        getCadleStick.execute();
    }
}
