package insta.soul.keycloak.actiffinances.binance.actions;

import java.util.List;

import insta.soul.keycloak.actiffinances.binance.beans.Candlestick;
import insta.soul.keycloak.actiffinances.binance.enumerations.GetCandleStickStatus;
import insta.soul.keycloak.actiffinances.retrofit.binance.BinanceApiServices;
import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AllArgsConstructor
public class GetCadleStick {
    private BinanceApiServices binanceApiServices;
    private String symbol;
    private String interval;
    private long startTime;
    private long endTime;
    private GetCandleStickCallback callback;

    public void execute(){
        Call<List<Candlestick>> call = binanceApiServices.getKlines(symbol,interval,startTime,endTime);
        call.enqueue(new Callback<List<Candlestick>>() {
            @Override
            public void onResponse(Call<List<Candlestick>> call, Response<List<Candlestick>> response) {
                if (response.isSuccessful()){
                    List<Candlestick> candlesticks = response.body();
                    callback.onSucces(GetCandleStickStatus.SUCCES,candlesticks);
                }
            }

            @Override
            public void onFailure(Call<List<Candlestick>> call, Throwable throwable) {
                    callback.onFailure(throwable);
            }
        });
    }

    public interface GetCandleStickCallback{
        void onSucces(GetCandleStickStatus status,List<Candlestick> candlesticks);
        void onFailure(Throwable throwable);
    }

}
