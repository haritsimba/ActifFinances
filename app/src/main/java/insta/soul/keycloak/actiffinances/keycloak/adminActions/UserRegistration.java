package insta.soul.keycloak.actiffinances.keycloak.adminActions;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import insta.soul.keycloak.actiffinances.keycloak.enumerations.ActionStatus;
import insta.soul.keycloak.actiffinances.keycloak.tokens.AdminAccessToken;
import insta.soul.keycloak.actiffinances.retrofit.keycloak.KeycloakApiServices;
import insta.soul.keycloak.actiffinances.keycloak.beans.StandardUserRepresentation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistration implements ActionTokenExpected {
    private final KeycloakApiServices keycloakApiServices;
    private final StandardUserRepresentation userRepresentation;
    private final String realm;
    private final UserRegistrationCallback callback;

    public UserRegistration(String realm, StandardUserRepresentation standardUserRepresentation, KeycloakApiServices keycloakApiServices, UserRegistrationCallback callback) {
        this.userRepresentation = standardUserRepresentation;
        this.realm = realm;
        this.callback = callback;
        this.keycloakApiServices = keycloakApiServices;

        //Mode d'authentification de l'utilisateurList<CredentialRepresentation> credentialRepresentations = new ArrayList<CredentialRepresentation>();
        userRepresentation.getCredentials().get(0).setType("password");
        userRepresentation.getCredentials().get(0).setTemporary(false);

        //Les actions que requis pour un utilsateur
        List<String> userRequiredActions = new ArrayList<String>();
        String emailVerificationRequired = "VERIFY_EMAIL";
        String configureOtpRequired = "CONFIGURE_TOTP";
        userRequiredActions.add(emailVerificationRequired);
        userRequiredActions.add(configureOtpRequired);


        userRepresentation.setRequiredActions(userRequiredActions);
        userRepresentation.setEnabled(true);
    }

    @Override
    public void doAction(AdminAccessToken adminAccessToken) {
        try {
            Call<Void> call = keycloakApiServices.createUser(adminAccessToken.useAccessToken(), this.realm, userRepresentation);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    handleResponse(response);
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                    handleFailure(throwable);
                }
            });
        } catch (Exception e) {
            Log.e("UserService", "Exception in doAdminAction", e);
        }
    }


    private void handleResponse(Response<Void> response) {
        String locationHeader = response.headers().get("Location");
        String userId;
        assert locationHeader != null;
        userId = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);

        if (response.isSuccessful()) {
            callback.oneRegistered(ActionStatus.CreationStatus.CREATED, userId);
        } else {
            switch (response.code()) {
                case 409:
                    callback.oneRegistered(ActionStatus.CreationStatus.CONFLICT,userId);
                    break;
                case 401:
                    callback.oneRegistered(ActionStatus.CreationStatus.NOT_ALLOWED,userId);
                    break;
                default:
                    callback.oneRegistered(ActionStatus.CreationStatus.ERROR,userId);
                    break;
            }
        }
    }

    /**
     * Si la requette n'a pas été executé
     * @param throwable
     */
    public void handleFailure(Throwable throwable) {
        callback.oneRegistered(ActionStatus.CreationStatus.ERROR,"null");
    }

    public interface UserRegistrationCallback {
        void oneRegistered(ActionStatus.CreationStatus creationStatus,String userId);
    }
}