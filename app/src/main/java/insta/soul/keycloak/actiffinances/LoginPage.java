package insta.soul.keycloak.actiffinances;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.security.GeneralSecurityException;

import insta.soul.keycloak.actiffinances.keycloak.adminActions.GetUserByUserameAndPassword;
import insta.soul.keycloak.actiffinances.keycloak.adminActions.GetUserStream;
import insta.soul.keycloak.actiffinances.keycloak.adminActions.SendEmailExecuteActions;
import insta.soul.keycloak.actiffinances.keycloak.adminActions.SendEmailVerification;
import insta.soul.keycloak.actiffinances.keycloak.adminActions.UserLogin;
import insta.soul.keycloak.actiffinances.keycloak.enumerations.ActionStatus;
import insta.soul.keycloak.actiffinances.keycloak.preferences.SecureTokenStorage;
import insta.soul.keycloak.actiffinances.keycloak.services.SessionStorage;
import insta.soul.keycloak.actiffinances.keycloak.services.UserService;
import insta.soul.keycloak.actiffinances.keycloak.tokens.AdminAccessToken;
import insta.soul.keycloak.actiffinances.keycloak.tokens.TokenDecoder;
import insta.soul.keycloak.actiffinances.keycloak.beans.StandardUserRepresentation;
import insta.soul.keycloak.actiffinances.keycloak.beans.UserStreamRepresentation;

public class LoginPage extends AppCompatActivity implements GetUserByUserameAndPassword.GetUserByUsernamesAndPasswordsCallback, SendEmailVerification.SendVerificationEmailCallback, SendEmailExecuteActions.SendEmailExecuteActionsCallback, UserLogin.UserLoginCallback, GetUserStream.GetUserStreamCallback {

    EditText loginUsername;
    EditText loginPassword;
    EditText loginOtp;
    Button loginConnectBtn;
    LoadingAnimation loadingAnimation;

    UserService userService;

    @SuppressLint({"CutPasteId", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //recuperation des elements
        loginUsername = findViewById(R.id.login_username_edit);
        loginPassword = findViewById(R.id.login_password_edit);
        loginConnectBtn = findViewById(R.id.login_connect_btn);
        loginOtp = findViewById(R.id.login_otp_edit);
        //initialisation du service de l'utilsateur
        userService = new UserService();
        loadingAnimation = new LoadingAnimation(this);

        //connexion
        loginConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
                loadingAnimation.show();
            }
        });
        //boutton retour
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

    }

    /**
     * connexion
     */
    private void doLogin(){
        String username = String.valueOf(loginUsername.getText());
        String password = String.valueOf(loginPassword.getText());
        String otp = String.valueOf(loginOtp.getText());

        userService.loginWithUsernamePasswordOtp(username,password,otp,LoginPage.this);
    }

    /**
     * actions suivant la status de connexion
     * @param status
     * @param token
     */
    @Override
    public void oneUserLogin(ActionStatus.UserLoginStatus status, AdminAccessToken token) {
        //si l'utilisateur est connecté
        if (status == ActionStatus.UserLoginStatus.CONNECTED){
            try{
                //memorisation du refresh token
                SecureTokenStorage secureTokenStorage = new SecureTokenStorage(getApplicationContext());
                secureTokenStorage.saveRefreshToken(token.getRefreshToken());

                //recuperation de l'information sur l'utilisateur
                TokenDecoder tokenDecoder = new TokenDecoder();
                String username = tokenDecoder.getUsernameFromToken(token.getAccessToken());
                StandardUserRepresentation userRepresentation = new StandardUserRepresentation();
                userRepresentation.setUsername(username);

                userService.getUserStream(userRepresentation, new GetUserStream.GetUserStreamCallback() {
                    @Override
                    public void oneGetUserStream(ActionStatus.GetUserStreamStatus status, UserStreamRepresentation userStreamRepresentation) {
                        //information de l'utilisateur recuperé
                        if(status == ActionStatus.GetUserStreamStatus.USER_RETURNED){
                            SessionStorage.getInstance().setUser(userStreamRepresentation);

                            Log.d("startActivity",userStreamRepresentation.toString());
                            //demmarage de l'activité profile
                            Intent intent = new Intent(getApplicationContext(),ProfilePage.class);
                            startActivity(intent);
                            loadingAnimation.cancel();
                            finish();
                        }
                    }
                });

            } catch (GeneralSecurityException | IOException e) {
                loadingAnimation.cancel();
                throw new RuntimeException(e);
            }
        }
        //si les information de connexion sont invalides
        else if (status == ActionStatus.UserLoginStatus.INVALID_CREDENTIALS){
            Toast.makeText(LoginPage.this,"Les informations de connexion sont incorrects",Toast.LENGTH_SHORT).show();
            loginPassword.setText("");
            loginOtp.setText("");
            loadingAnimation.cancel();
            loginPassword.requestFocus();
        }
    }

    //si l'utilisateur n'a pas cofiguré son compte (verification de l'email ou configuration de l'otp)
    @Override
    public void oneAccountNotFullySetup(ActionStatus.UserLoginStatus status, String username) {
        if (status == ActionStatus.UserLoginStatus.ACCOUNT_NOT_CONFIGURED){
            Toast.makeText(LoginPage.this,"Veuillez configurer votre compte avant de continuer",Toast.LENGTH_SHORT).show();
            StandardUserRepresentation userRepresentation = new StandardUserRepresentation();
            userRepresentation.setUsername(username);
            //recuperation de l'id de l'utilisateur pour l'envoie de l'email de configuration
            userService.getUserByUsernameAndPassword(username,"",LoginPage.this);
        }
    }

    @Override
    public void oneGetUserStream(ActionStatus.GetUserStreamStatus status, UserStreamRepresentation userStreamRepresentation) {
        if(status == ActionStatus.GetUserStreamStatus.USER_RETURNED){
            if(userStreamRepresentation.isEmailVerified() && userStreamRepresentation.isTotp()){

            } else if (!userStreamRepresentation.isTotp() && !userStreamRepresentation.isEmailVerified()) {
                userService.sendEmailRegistrationRequiredActions(userStreamRepresentation.getId(),LoginPage.this);
            } else if (!userStreamRepresentation.isTotp()) {
                userService.sendEmailConfigureOtp(userStreamRepresentation.getId(),LoginPage.this);
            } else if (!userStreamRepresentation.isEmailVerified()) {
                userService.sendEmailVerification(userStreamRepresentation.getId(),LoginPage.this);
            }
        }else if (status == ActionStatus.GetUserStreamStatus.USER_NOT_FOUND){
            Toast.makeText(LoginPage.this,"Les informations de connexion sont incorrects",Toast.LENGTH_SHORT).show();
            loginPassword.setText("");
            loginPassword.requestFocus();
            loadingAnimation.cancel();
        }
    }
    @Override
    public void oneEmailSent(ActionStatus.SendEmailVerificationStatus status) {
        if (status == ActionStatus.SendEmailVerificationStatus.EMAIL_SENT){
            Toast.makeText(LoginPage.this,"Veuillez consulter votre boite mail pour confirmer votre email",Toast.LENGTH_SHORT).show();
            loadingAnimation.cancel();
        }
    }

    @Override
    public void oneEmailExecuteActionSent(ActionStatus.SendEmailExecuteActionsStatus actionsStatus) {
        if(actionsStatus == ActionStatus.SendEmailExecuteActionsStatus.EMAIL_SENT){
            Toast.makeText(LoginPage.this,"Veuillez consulter votre boite mail pour configurer votre compte",Toast.LENGTH_SHORT).show();
            loadingAnimation.cancel();
        }
    }
}