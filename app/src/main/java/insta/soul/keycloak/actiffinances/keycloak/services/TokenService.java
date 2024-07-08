package insta.soul.keycloak.actiffinances.keycloak.services;

import insta.soul.keycloak.actiffinances.keycloak.tokens.AdminAccessToken;

public class TokenService {
    private static AdminAccessToken accessToken;

    private static TokenService instance;

    public static TokenService getInstance() {
        if(instance == null){
            instance = new TokenService();
        }
        return instance;
    }

    public void setAccessToken(AdminAccessToken accessToken) {
        TokenService.accessToken = accessToken;
    }

    public AdminAccessToken getAccessToken() {
        return accessToken;
    }
}
