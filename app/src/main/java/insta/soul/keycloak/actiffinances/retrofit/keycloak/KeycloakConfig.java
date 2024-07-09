package insta.soul.keycloak.actiffinances.retrofit.keycloak;

public class KeycloakConfig {
    private static String baseUrl;
    private static String realmName;
    private static String clientId;
    private static KeycloakConfig instance;

    public static KeycloakConfig getInstance() {
        if(instance==null){
            instance = new KeycloakConfig();
            baseUrl = "http://192.168.1.182:8080";
            realmName = "ActifsFinances";
            clientId = "ActifsFinancesApp";
        }
        return instance;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
    public String getRealmName() {
        return realmName;
    }
    public String getClientId() {
        return clientId;
        }
}
