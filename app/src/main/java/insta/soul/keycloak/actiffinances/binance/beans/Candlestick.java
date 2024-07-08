package insta.soul.keycloak.actiffinances.binance.beans;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Candlestick {
    private long openTime;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;
    private long closeTime;
    private String quoteAssetVolume;
    private int numberOfTrades;
    private String takerBuyBaseAssetVolume;
    private String takerBuyQuoteAssetVolume;
    private String ignore;
}
