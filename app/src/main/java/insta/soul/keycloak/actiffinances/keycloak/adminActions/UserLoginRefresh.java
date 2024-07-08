package insta.soul.keycloak.actiffinances.keycloak.adminActions;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.security.GeneralSecurityException;

import insta.soul.keycloak.actiffinances.keycloak.enumerations.ActionStatus;
import insta.soul.keycloak.actiffinances.keycloak.preferences.SecureTokenStorage;
import insta.soul.keycloak.actiffinances.retrofit.keycloak.KeycloakApiServices;
import insta.soul.keycloak.actiffinances.keycloak.services.SessionStorage;
import insta.soul.keycloak.actiffinances.keycloak.tokens.AdminAccessToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginRefresh {
    String realm;
    String client_id;
    KeycloakApiServices keycloakApiServices;
    String refresh_token;
    UserLoginRefreshCallback callback;

    public UserLoginRefresh(String realm, String client_id, KeycloakApiServices keycloakApiServices, String refresh_token, UserLoginRefreshCallback callback) {
        this.realm = realm;
        this.client_id = client_id;
        this.keycloakApiServices = keycloakApiServices;
        this.refresh_token = refresh_token;
        this.callback = callback;
    }

    public void doLogin(){
        if (refresh_token == null){
            callback.oneUserLoginRefresh(ActionStatus.UserLoginRefreshStatus.NULL_REFRESH_TOKEN,null);
            return;
        }
        Call<AdminAccessToken> call = keycloakApiServices.connectViaRefreshToken(realm,client_id,"refresh_token",refresh_token);
        call.enqueue(new Callback<AdminAccessToken>() {
            @Override
            public void onResponse(Call<AdminAccessToken> call, Response<AdminAccessToken> response) {
                handleResponse(response);
            }

            @Override
            public void onFailure(Call<AdminAccessToken> call, Throwable throwable) {
                handleFailure(throwable);
            }
        });
    }

    private void handleResponse(Response<AdminAccessToken> response) {
        if (response.isSuccessful()){
            assert response.body() != null;
            AdminAccessToken accessToken = response.body();
            try {
                SecureTokenStorage secureTokenStorage = new SecureTokenStorage((Context) callback);
                secureTokenStorage.saveRefreshToken(accessToken.getRefreshToken());
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }
            SessionStorage.getInstance().setAccessToken(accessToken);
            callback.oneUserLoginRefresh(ActionStatus.UserLoginRefreshStatus.CONNECTED,accessToken);
        }else if(response.code() == 400){
            Log.e("expired refresh token error", String.valueOf(response.errorBody()));
            callback.oneUserLoginRefresh(ActionStatus.UserLoginRefreshStatus.REFRESH_EXPIRED,null);
        }
    }

    private void handleFailure(Throwable throwable) {
        callback.oneUserLoginRefresh(ActionStatus.UserLoginRefreshStatus.ERROR,null);
    }

    public interface UserLoginRefreshCallback {
        void oneUserLoginRefresh(ActionStatus.UserLoginRefreshStatus status,AdminAccessToken accessToken);
    }
}
