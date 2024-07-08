package insta.soul.keycloak.actiffinances.binance.actions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import insta.soul.keycloak.actiffinances.binance.beans.Candlestick;

public class CandlestickDeserializer implements JsonDeserializer<List<Candlestick>> {
    @Override
    public List<Candlestick> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Candlestick> candlesticks = new ArrayList<>();
        for (JsonElement element : json.getAsJsonArray()) {
            long openTime = element.getAsJsonArray().get(0).getAsLong();
            String open = element.getAsJsonArray().get(1).getAsString();
            String high = element.getAsJsonArray().get(2).getAsString();
            String low = element.getAsJsonArray().get(3).getAsString();
            String close = element.getAsJsonArray().get(4).getAsString();
            String volume = element.getAsJsonArray().get(5).getAsString();
            long closeTime = element.getAsJsonArray().get(6).getAsLong();
            String quoteAssetVolume = element.getAsJsonArray().get(7).getAsString();
            int numberOfTrades = element.getAsJsonArray().get(8).getAsInt();
            String takerBuyBaseAssetVolume = element.getAsJsonArray().get(9).getAsString();
            String takerBuyQuoteAssetVolume = element.getAsJsonArray().get(10).getAsString();
            String ignore = element.getAsJsonArray().get(11).getAsString();

            Candlestick candlestick = new Candlestick(openTime, open, high, low, close, volume,
                    closeTime, quoteAssetVolume, numberOfTrades, takerBuyBaseAssetVolume,
                    takerBuyQuoteAssetVolume, ignore);
            candlesticks.add(candlestick);
        }
        return candlesticks;
    }
}

