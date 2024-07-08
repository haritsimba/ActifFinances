package insta.soul.keycloak.actiffinances.keycloak.adminActions;

import insta.soul.keycloak.actiffinances.keycloak.enumerations.ActionStatus;
import insta.soul.keycloak.actiffinances.retrofit.keycloak.KeycloakApiServices;
import insta.soul.keycloak.actiffinances.keycloak.services.SessionStorage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLogout {
    private final String realm;
    private final String clientId;
    private final String refreshToken;
    private final KeycloakApiServices keycloakApiServices;
    private final UserLogoutCallback callback;

    public UserLogout(String realm, String clientId, KeycloakApiServices keycloakApiServices, String refreshToken, UserLogoutCallback callback){
        this.realm = realm;
        this.clientId = clientId;
        this.refreshToken = refreshToken;
        this.keycloakApiServices = keycloakApiServices;
        this.callback = callback;
    }
    public void doLogout(){
        Call<Void> call = keycloakApiServices.logout(this.realm,this.clientId,this.refreshToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                handleResponse(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                handleFailure(throwable);
            }
        });
    }

    private void handleResponse(Response<Void> response) {
        if(response.isSuccessful()){
            SessionStorage.getInstance().removeUser();
            SessionStorage.getInstance().removeAccessToken();
            callback.oneUserLogout(ActionStatus.UserLogoutStatus.SUCCESS);
        }
    }

    private void handleFailure(Throwable throwable) {
            callback.oneUserLogout(ActionStatus.UserLogoutStatus.ERROR);
    }

    public interface UserLogoutCallback{
        void oneUserLogout(ActionStatus.UserLogoutStatus logoutStatus);
    }
}
