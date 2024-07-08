package insta.soul.keycloak.actiffinances.keycloak.adminActions;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import insta.soul.keycloak.actiffinances.keycloak.enumerations.ActionStatus;
import insta.soul.keycloak.actiffinances.keycloak.tokens.AdminAccessToken;
import insta.soul.keycloak.actiffinances.retrofit.keycloak.KeycloakApiServices;
import insta.soul.keycloak.actiffinances.keycloak.beans.UserStreamRepresentation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUserByUserameAndPassword implements ActionTokenExpected{

    private final String realm;
    private final KeycloakApiServices keycloakApiServices;
    private final String password;
    private final String username;
    GetUserByUsernamesAndPasswordsCallback callback;

    public GetUserByUserameAndPassword(String realm, KeycloakApiServices keycloakApiServices, String username, String password, GetUserByUsernamesAndPasswordsCallback callback){
        this.realm = realm;
        this.keycloakApiServices = keycloakApiServices;
        this.username = username;
        this.password = password;
        this.callback = callback;
    }
    @Override
    public void doAction(AdminAccessToken adminToken) {
        Call<List<UserStreamRepresentation>> call = keycloakApiServices.getUserByUsernameAndPassword(adminToken.useAccessToken(),realm,username,password);
        call.enqueue(new Callback<List<UserStreamRepresentation>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserStreamRepresentation>> call, @NonNull Response<List<UserStreamRepresentation>> response) {
                handleResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<List<UserStreamRepresentation>> call, @NonNull Throwable throwable) {
                handleFailure(throwable);
            }
        });
    }

    /**
     * la requette à été executé
     * @param response
     */
    private void handleResponse(Response<List<UserStreamRepresentation>> response) {
        if(response.isSuccessful() && response.body() != null){
            List<UserStreamRepresentation> users = response.body();
            if(!users.isEmpty()){
                UserStreamRepresentation user = users.get(0);
                callback.oneGetUserStream(ActionStatus.GetUserStreamStatus.USER_RETURNED,user);
            }else {
                callback.oneGetUserStream(ActionStatus.GetUserStreamStatus.USER_NOT_FOUND,null);
            }
        }else if(response.code() == 401){
            callback.oneGetUserStream(ActionStatus.GetUserStreamStatus.ACTION_NOT_AUTHORIZED,null);
        }
    }

    /**
     * La requette n'a pas pu etre executé
     * @param throwable
     */
    private void handleFailure(Throwable throwable) {
        callback.oneGetUserStream(ActionStatus.GetUserStreamStatus.ERROR,null);
        Log.e("cannot execute (get user stream)","impossible d'executer la requette");
    }

    public interface GetUserByUsernamesAndPasswordsCallback{
        void oneGetUserStream(ActionStatus.GetUserStreamStatus status, UserStreamRepresentation userStreamRepresentation);
    }
}
