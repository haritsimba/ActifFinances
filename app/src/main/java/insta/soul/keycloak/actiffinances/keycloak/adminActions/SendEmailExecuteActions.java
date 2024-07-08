package insta.soul.keycloak.actiffinances.keycloak.adminActions;

import java.util.List;

import insta.soul.keycloak.actiffinances.keycloak.enumerations.ActionStatus;
import insta.soul.keycloak.actiffinances.retrofit.keycloak.KeycloakApiServices;
import insta.soul.keycloak.actiffinances.keycloak.tokens.AdminAccessToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendEmailExecuteActions implements ActionTokenExpected{

    KeycloakApiServices keycloakApiServices;
    SendEmailExecuteActionsCallback callback;

    public SendEmailExecuteActions(String realm, KeycloakApiServices keycloakApiServices, String id, List<String> actions, SendEmailExecuteActionsCallback callback) {
        this.keycloakApiServices = keycloakApiServices;
        this.callback = callback;
        this.realm = realm;
        this.actions = actions;
        this.id = id;
    }

    String realm;
    List<String> actions;
    String id;


    @Override
    public void doAction(AdminAccessToken adminToken) {
        Call<Void> call = keycloakApiServices.executeActionsEmail(adminToken.useAccessToken(),realm,id,actions);
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
        if (response.isSuccessful()){
            callback.oneEmailExecuteActionSent(ActionStatus.SendEmailExecuteActionsStatus.EMAIL_SENT);
        } else if (response.code() == 401) {
            callback.oneEmailExecuteActionSent(ActionStatus.SendEmailExecuteActionsStatus.ACTION_NOT_AUTHORIZED);
        }
    }

    private void handleFailure(Throwable throwable) {
        callback.oneEmailExecuteActionSent(ActionStatus.SendEmailExecuteActionsStatus.ERROR);
    }

    /**
     * Callback
     */
    public interface SendEmailExecuteActionsCallback{
        void oneEmailExecuteActionSent(ActionStatus.SendEmailExecuteActionsStatus actionsStatus);
    }
}
