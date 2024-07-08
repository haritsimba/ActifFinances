package insta.soul.keycloak.actiffinances.keycloak.adminActions;

import android.util.Log;

import androidx.annotation.NonNull;

import insta.soul.keycloak.actiffinances.keycloak.enumerations.ActionStatus;
import insta.soul.keycloak.actiffinances.keycloak.tokens.AdminAccessToken;
import insta.soul.keycloak.actiffinances.retrofit.keycloak.KeycloakApiServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendEmailVerification implements ActionTokenExpected {

    private final String id;
    private final String realm;
    private final KeycloakApiServices keycloakApiServices;
    private final SendVerificationEmailCallback callback;

    public SendEmailVerification(String realm, KeycloakApiServices keycloakApiServices, String id, SendVerificationEmailCallback callback){
        this.realm = realm;
        this.keycloakApiServices = keycloakApiServices;
        this.id = id;
        this.callback = callback;
    }

    //
    @Override
    public void doAction(AdminAccessToken adminAccessToken) {

        try {
            Call<Void> call = keycloakApiServices.sendVerifyEmail(adminAccessToken.useAccessToken(), this.realm, id);
            call.enqueue(new Callback<Void>() {
                //action
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    handleResponse(response);
                }

                //erreur
                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                    handleFailure(throwable);
                }
            });
        } catch (Exception e) {
            Log.e("Erreur", "message : "+e.getMessage());
        }
    }

    /**
     * Si la requette est executée
     * @param response
     */
    public void handleResponse(Response<Void> response){
        if(response.isSuccessful()){
            callback.oneEmailSent(ActionStatus.SendEmailVerificationStatus.EMAIL_SENT);
            Log.d("email status","email sent");
        }else if (response.code() == 404){
            callback.oneEmailSent(ActionStatus.SendEmailVerificationStatus.LINK_NOT_FOUND);
        } else if (response.code() == 401) {
            callback.oneEmailSent(ActionStatus.SendEmailVerificationStatus.ACTION_NOT_AUTHORIZED);

        }
    }

    /**
     * Enternal Error
     * @param throwable
     */
    public void handleFailure(Throwable throwable) {
        callback.oneEmailSent(ActionStatus.SendEmailVerificationStatus.ERROR);
        Log.e("Request Execution Error (Email verification sending)","Erreur La requette ne peut être executé");
    }

    /**
     * Callback
     */
    public interface SendVerificationEmailCallback{
        void oneEmailSent(ActionStatus.SendEmailVerificationStatus status);
    }
}
