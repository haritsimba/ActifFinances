package insta.soul.keycloak.actiffinances.keycloak.adminActions;

import android.content.Context;

import java.io.IOException;
import java.security.GeneralSecurityException;

import insta.soul.keycloak.actiffinances.keycloak.enumerations.ActionStatus;
import insta.soul.keycloak.actiffinances.keycloak.tokens.AdminAccessToken;
import insta.soul.keycloak.actiffinances.keycloak.preferences.SecureTokenStorage;
import insta.soul.keycloak.actiffinances.retrofit.keycloak.KeycloakApiServices;
import insta.soul.keycloak.actiffinances.keycloak.services.SessionStorage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLogin {
    private String username;
    private String password;
    private String otp;
    private String realm;
    private KeycloakApiServices keycloakApiServices;
    private String client_id;
    UserLoginCallback callback;

    public UserLogin(String realm, String client_id, KeycloakApiServices keycloakApiServices, String username, String password, String otp, UserLoginCallback callback){
        this.username = username;
        this.password = password;
        this.otp = otp;
        this.realm = realm;
        this.keycloakApiServices = keycloakApiServices;
        this.callback = callback;
        this.client_id = client_id;
    }

    /**
     * Execution de la requette de connexion
     */
    public void doLogin(){
        Call<AdminAccessToken> call = keycloakApiServices.doLogin(realm,client_id,"password",username,password,otp);
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

    /**
     * la requette à été executé
     * @param response
     */
    private void handleResponse(Response<AdminAccessToken> response) {
        if(response.isSuccessful() && response.body() != null){
            AdminAccessToken accessToken = response.body();

            //ajout du token dans le shared preferences
            try {
                SecureTokenStorage secureTokenStorage = new SecureTokenStorage((Context) callback);
                secureTokenStorage.saveRefreshToken(accessToken.getRefreshToken());
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }
            SessionStorage.getInstance().setAccessToken(accessToken);
            //execution de la callback
            callback.oneUserLogin(ActionStatus.UserLoginStatus.CONNECTED,accessToken);
        } else if (response.code()==401) {
            callback.oneUserLogin(ActionStatus.UserLoginStatus.INVALID_CREDENTIALS,null);
        } else if (response.code() == 400) {
            callback.oneAccountNotFullySetup(ActionStatus.UserLoginStatus.ACCOUNT_NOT_CONFIGURED,username);
        }
    }

    /**
     * La requette n'a pas pu etre executé
     * @param throwable
     */
    private void handleFailure(Throwable throwable) {
        callback.oneUserLogin(ActionStatus.UserLoginStatus.ERROR,null);
    }

    /**
     * Callback
     */
    public interface UserLoginCallback{
        void oneUserLogin(ActionStatus.UserLoginStatus status,AdminAccessToken token);
        void oneAccountNotFullySetup(ActionStatus.UserLoginStatus status,String username);
    }
}
