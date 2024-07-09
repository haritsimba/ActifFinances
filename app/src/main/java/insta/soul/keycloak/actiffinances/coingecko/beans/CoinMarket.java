package insta.soul.keycloak.actiffinances.coingecko.beans;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CoinMarket {
    @SerializedName("id")
    private String id;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("current_price")
    private BigDecimal currentPrice;
    @SerializedName("market_cap")
    private double marketCap;
    @SerializedName("market_cap_rank")
    private Integer marketCapRank = 0;
    @SerializedName("total_volume")
    private double totalVolume;
    @SerializedName("high_24h")
    private double high24h;
    @SerializedName("low_24h")
    private double low24h;
    @SerializedName("price_change_24h")
    private double priceChange24h;
    @SerializedName("price_change_percentage_24h")
    private double priceChangePercentage24h;
    @SerializedName("circulating_supply")
    private double circulatingSupply;
    @SerializedName("total_supply")
    private double totalSupply;
    @SerializedName("max_supply")
    private double maxSupply;
    @SerializedName("ath")
    private BigDecimal ath;
    @SerializedName("ath_change_percentage")
    private double athChangePercentage;
    @SerializedName("ath_date")
    private String athDate;
    @SerializedName("atl")
    private double atl;
    @SerializedName("atl_change_percentage")
    private double atlChangePercentage;
    @SerializedName("atl_date")
    private String atlDate;
    @SerializedName("roi")
    private Object roi;
    @SerializedName("last_updated")
    private String lastUpdated;
}
