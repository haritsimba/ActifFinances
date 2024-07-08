package insta.soul.keycloak.actiffinances.keycloak.services;

import insta.soul.keycloak.actiffinances.keycloak.tokens.AdminAccessToken;
import insta.soul.keycloak.actiffinances.keycloak.beans.UserStreamRepresentation;

public class SessionStorage {
    private static AdminAccessToken accessToken;
    private static UserStreamRepresentation user;
    private static SessionStorage instance;

    public static SessionStorage getInstance() {
        if (instance == null) {
            instance = new SessionStorage();
        }
        return instance;
    }

    public void setAccessToken(AdminAccessToken accessToken) {
        SessionStorage.accessToken = accessToken;
    }

    public AdminAccessToken getAccessToken() {
        return accessToken;
    }
    public void removeAccessToken(){
        accessToken = null;
    }

    public void setUser(UserStreamRepresentation user) {
        SessionStorage.user = user;
    }

    public UserStreamRepresentation getUser() {
        return user;
    }
    public void removeUser(){
        user = null;
    }
}
