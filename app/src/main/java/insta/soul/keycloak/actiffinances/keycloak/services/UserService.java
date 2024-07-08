package insta.soul.keycloak.actiffinances.keycloak.services;

import static insta.soul.keycloak.actiffinances.retrofit.keycloak.RetrofitKeycloakClientInstace.CLIENT_ID;
import static insta.soul.keycloak.actiffinances.retrofit.keycloak.RetrofitKeycloakClientInstace.REALM;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import insta.soul.keycloak.actiffinances.keycloak.adminActions.ActionTokenExpected;
import insta.soul.keycloak.actiffinances.keycloak.adminActions.GetUserByUserameAndPassword;
import insta.soul.keycloak.actiffinances.keycloak.adminActions.GetUserStream;
import insta.soul.keycloak.actiffinances.keycloak.adminActions.SendEmailExecuteActions;
import insta.soul.keycloak.actiffinances.keycloak.adminActions.SendEmailVerification;
import insta.soul.keycloak.actiffinances.keycloak.adminActions.UserLogin;
import insta.soul.keycloak.actiffinances.keycloak.adminActions.UserLoginRefresh;
import insta.soul.keycloak.actiffinances.keycloak.adminActions.UserLogout;
import insta.soul.keycloak.actiffinances.keycloak.adminActions.UserRegistration;
import insta.soul.keycloak.actiffinances.retrofit.keycloak.KeycloakApiServices;
import insta.soul.keycloak.actiffinances.retrofit.keycloak.RetrofitKeycloakClientInstace;
import insta.soul.keycloak.actiffinances.keycloak.tokens.AdminAccessToken;
import insta.soul.keycloak.actiffinances.keycloak.beans.StandardUserRepresentation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserService {
    Retrofit retrofit;
    KeycloakApiServices keycloakApiServices;

    public UserService(){
        retrofit = RetrofitKeycloakClientInstace.getRetrofitInstance();
        keycloakApiServices = retrofit.create(KeycloakApiServices.class);
    }

    //recuperation de token de l'administrateur
    private void doAdminAction(ActionTokenExpected adminActionTokenExpected){
        Call<AdminAccessToken> call = keycloakApiServices.getAdminToken(REALM,CLIENT_ID,"password","eric","eric");
        call.enqueue(new Callback<AdminAccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AdminAccessToken> call, @NonNull Response<AdminAccessToken> response) {
                if(response.isSuccessful() && response.body() != null){
                    AdminAccessToken accessToken = response.body();
                    adminActionTokenExpected.doAction(accessToken);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AdminAccessToken> call, @NonNull Throwable throwable) {
                //erreur
            }
        });
    };

    /**
     * Creation d'un utilisateur
     * @param user
     * @param callback
     */
    public void registration(StandardUserRepresentation user, UserRegistration.UserRegistrationCallback callback){
        UserRegistration userRegistration = new UserRegistration(REALM,user, keycloakApiServices,callback);
        doAdminAction(userRegistration);
    }

    /**
     * Verification de l'email
     * @param id
     * @param callback
     */
    public void sendEmailVerification(String id, SendEmailVerification.SendVerificationEmailCallback callback){
        SendEmailVerification emailVerification = new SendEmailVerification(REALM, keycloakApiServices,id, callback);
        doAdminAction(emailVerification);
    }

    /**
     * verification de l'email et configuration de l'otp
     * @param id
     * @param callback
     */
    public void sendEmailRegistrationRequiredActions(String id, SendEmailExecuteActions.SendEmailExecuteActionsCallback callback){

        List<String> actions = new ArrayList<String>();
        String verifyEmail = "VERIFY_EMAIL";
        String configureOtp = "CONFIGURE_TOTP";
        actions.add(verifyEmail);
        actions.add(configureOtp);

        SendEmailExecuteActions emailExecuteActions = new SendEmailExecuteActions(REALM, keycloakApiServices,id,actions,callback);
        doAdminAction(emailExecuteActions);
    }

    /**
     * Email de cofiguration de l'otp
     * @param id
     * @param callback
     */
    public void sendEmailConfigureOtp(String id, SendEmailExecuteActions.SendEmailExecuteActionsCallback callback){

        List<String> action = new ArrayList<String>();
        action.add("CONFIGURE_TOTP");

        SendEmailExecuteActions emailExecuteAction = new SendEmailExecuteActions(REALM, keycloakApiServices,id,action,callback);
        doAdminAction(emailExecuteAction);
    }

    public void getUserByUsernameAndPassword(String username, String password, GetUserByUserameAndPassword.GetUserByUsernamesAndPasswordsCallback callback){
        GetUserByUserameAndPassword getUserByUserameAndPassword = new GetUserByUserameAndPassword(REALM, keycloakApiServices,username,password,callback);
        doAdminAction(getUserByUserameAndPassword);
    }

    /**
     * Connexion via username, password et otp
     * @param username
     * @param password
     * @param otp
     * @param callback
     */
    public void loginWithUsernamePasswordOtp(String username, String password, String otp, UserLogin.UserLoginCallback callback){
        UserLogin userLogin = new UserLogin(REALM,CLIENT_ID, keycloakApiServices,username,password,otp,callback);
        userLogin.doLogin();
    }


    /**
     *Connexion via refresh token
     * @param refreshToken
     */
    public void connectViaRefreshToken(String refreshToken , UserLoginRefresh.UserLoginRefreshCallback callback){
        UserLoginRefresh userLoginRefresh = new UserLoginRefresh(REALM,CLIENT_ID, keycloakApiServices,refreshToken,callback);
        userLoginRefresh.doLogin();
    }

    /**
     * recuperation de l'inforamtion sur l'utilisateur
     * @param userRepresentation
     * @param callback
     */
    public void getUserStream(StandardUserRepresentation userRepresentation, GetUserStream.GetUserStreamCallback callback){
        GetUserStream getUserStream = new GetUserStream(REALM, keycloakApiServices,userRepresentation,callback);
        doAdminAction(getUserStream);
    }

    public void userLogout(String refreshToken, UserLogout.UserLogoutCallback callback){
        UserLogout userLogout = new UserLogout(REALM,CLIENT_ID, keycloakApiServices,refreshToken,callback);
        userLogout.doLogout();
    }
}
