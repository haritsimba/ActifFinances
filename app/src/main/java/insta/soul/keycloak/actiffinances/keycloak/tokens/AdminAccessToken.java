package insta.soul.keycloak.actiffinances.keycloak.tokens;

import com.google.gson.annotations.SerializedName;


public class AdminAccessToken implements AccessToken{
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private Integer expiresIn;
    @SerializedName("refresh_expires_in")
    private Integer refreshExpiresIn;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("not-before-policy")
    private Integer notBeforePolicy;
    @SerializedName("session_state")
    private String sessionState;
    @SerializedName("scope")
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }
    public String useAccessToken(){
        return "Bearer "+accessToken;
    }
    public String getRefreshToken(){
        return refreshToken;
    }

}
