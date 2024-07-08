package insta.soul.keycloak.actiffinances.retrofit.binance;

public class BinanceConfig {
    private static String baseUrl;
    private static String apiKey;
    private static String apiKeySecret;
    private static BinanceConfig instance;

    public static BinanceConfig getInstance() {
        if (instance == null) {
            instance = new BinanceConfig();
            baseUrl = "https://api3.binance.com";
        }
        return instance;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
