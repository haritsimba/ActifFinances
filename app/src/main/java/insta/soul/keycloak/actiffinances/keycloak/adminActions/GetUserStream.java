package insta.soul.keycloak.actiffinances.keycloak.adminActions;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import insta.soul.keycloak.actiffinances.keycloak.enumerations.ActionStatus;
import insta.soul.keycloak.actiffinances.keycloak.tokens.AdminAccessToken;
import insta.soul.keycloak.actiffinances.keycloak.beans.UserStreamRepresentation;
import insta.soul.keycloak.actiffinances.retrofit.keycloak.KeycloakApiServices;
import insta.soul.keycloak.actiffinances.keycloak.beans.StandardUserRepresentation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUserStream implements ActionTokenExpected{
    StandardUserRepresentation userRepresentation;
    String realm;
    KeycloakApiServices keycloakApiServices;
    GetUserStreamCallback callback;

    public GetUserStream(String realm, KeycloakApiServices keycloakApiServices, StandardUserRepresentation userRepresentation, GetUserStreamCallback callback){
        this.userRepresentation = userRepresentation;
        this.realm = realm;
        this.keycloakApiServices = keycloakApiServices;
        this.callback = callback;
    }

    /**
     * Execution de la requette de recuperation de le l'information de l'utilisateur
     * @param adminAccessToken
     */
    @Override
    public void doAction(AdminAccessToken adminAccessToken){
        Call<List<UserStreamRepresentation>> call = keycloakApiServices.getUserInfo(adminAccessToken.useAccessToken(),realm,userRepresentation.getUsername());
        Log.d("userRepresenation",userRepresentation.getUsername());
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

    /**
     * callback
     */
    public interface GetUserStreamCallback{
        void oneGetUserStream(ActionStatus.GetUserStreamStatus status, UserStreamRepresentation userStreamRepresentation);
    }

}
