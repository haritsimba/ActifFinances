package insta.soul.keycloak.actiffinances.binance.beans;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class MarketDataTicker {
    // Getters and Setters
    @SerializedName("symbol")
    private String symbol;

    @Setter
    @SerializedName("priceChange")
    private String priceChange;

    @SerializedName("priceChangePercent")
    private String priceChangePercent;

    @SerializedName("weightedAvgPrice")
    private String weightedAvgPrice;

    @SerializedName("prevClosePrice")
    private String prevClosePrice;

    @SerializedName("lastPrice")
    private String lastPrice;

    @SerializedName("lastQty")
    private String lastQty;

    @SerializedName("bidPrice")
    private String bidPrice;

    @SerializedName("bidQty")
    private String bidQty;

    @SerializedName("askPrice")
    private String askPrice;

    @SerializedName("askQty")
    private String askQty;

    @SerializedName("openPrice")
    private String openPrice;

    @SerializedName("highPrice")
    private String highPrice;

    @SerializedName("lowPrice")
    private String lowPrice;

    @SerializedName("volume")
    private String volume;

    @SerializedName("quoteVolume")
    private String quoteVolume;

    @SerializedName("openTime")
    private Long openTime;

    @SerializedName("closeTime")
    private Long closeTime;

    @SerializedName("firstId")
    private Long firstId;

    @SerializedName("lastId")
    private Long lastId;

    @SerializedName("count")
    private Long count;

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setPriceChangePercent(String priceChangePercent) {
        this.priceChangePercent = priceChangePercent;
    }

    public void setWeightedAvgPrice(String weightedAvgPrice) {
        this.weightedAvgPrice = weightedAvgPrice;
    }

    public void setPrevClosePrice(String prevClosePrice) {
        this.prevClosePrice = prevClosePrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public void setLastQty(String lastQty) {
        this.lastQty = lastQty;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public void setBidQty(String bidQty) {
        this.bidQty = bidQty;
    }

    public void setAskPrice(String askPrice) {
        this.askPrice = askPrice;
    }

    public void setAskQty(String askQty) {
        this.askQty = askQty;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public void setQuoteVolume(String quoteVolume) {
        this.quoteVolume = quoteVolume;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }

    public void setCloseTime(long closeTime) {
        this.closeTime = closeTime;
    }

    public void setFirstId(Long firstId) {
        this.firstId = firstId;
    }

    public void setLastId(Long lastId) {
        this.lastId = lastId;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
