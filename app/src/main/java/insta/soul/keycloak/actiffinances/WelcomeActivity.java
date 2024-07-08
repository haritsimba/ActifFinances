package insta.soul.keycloak.actiffinances;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.security.GeneralSecurityException;

import insta.soul.keycloak.actiffinances.binance.beans.BlockchainListJsonParser;
import insta.soul.keycloak.actiffinances.keycloak.adminActions.GetUserStream;
import insta.soul.keycloak.actiffinances.keycloak.adminActions.UserLoginRefresh;
import insta.soul.keycloak.actiffinances.keycloak.enumerations.ActionStatus;
import insta.soul.keycloak.actiffinances.keycloak.preferences.SecureTokenStorage;
import insta.soul.keycloak.actiffinances.keycloak.services.SessionStorage;
import insta.soul.keycloak.actiffinances.keycloak.services.UserService;
import insta.soul.keycloak.actiffinances.keycloak.tokens.AdminAccessToken;
import insta.soul.keycloak.actiffinances.keycloak.tokens.TokenDecoder;
import insta.soul.keycloak.actiffinances.keycloak.beans.StandardUserRepresentation;
import insta.soul.keycloak.actiffinances.keycloak.beans.UserStreamRepresentation;

public class WelcomeActivity extends AppCompatActivity implements UserLoginRefresh.UserLoginRefreshCallback, GetUserStream.GetUserStreamCallback {

    String refresh_token;
    UserService userService;
    LoadingAnimation loadingAnimation;
    BlockchainListJsonParser blockchainListJsonParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        Intent intent = new Intent(this, BlockchainListJsonParser.class);
        startService(intent);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //dependences
        userService = new UserService();
        loadingAnimation = new LoadingAnimation(this);
        //boutton retour
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                AlertDialog.Builder popup = new AlertDialog.Builder(WelcomeActivity.this);
                popup.setTitle("");
                popup.setMessage("voulez vous quitter l'application ?");
                popup.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });
                popup.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                popup.show();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        loadingAnimation.show();

        try {
            SecureTokenStorage secureTokenStorage = new SecureTokenStorage(WelcomeActivity.this);
            String refreshToken = secureTokenStorage.getRefreshToken();
            this.refresh_token = refreshToken;
            refreshLogin();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    void refreshLogin(){
        userService.connectViaRefreshToken(refresh_token,WelcomeActivity.this);
    }

    @Override
    public void oneUserLoginRefresh(ActionStatus.UserLoginRefreshStatus status, AdminAccessToken accessToken) {
        if(status == ActionStatus.UserLoginRefreshStatus.CONNECTED){
            TokenDecoder tokenDecoder = new TokenDecoder();
            String username = tokenDecoder.getUsernameFromToken(accessToken.getAccessToken());
            StandardUserRepresentation userRepresentation = new StandardUserRepresentation();
            userRepresentation.setUsername(username);
            userService.getUserStream(userRepresentation,WelcomeActivity.this);
        }else if(status == ActionStatus.UserLoginRefreshStatus.NULL_REFRESH_TOKEN){
            Log.e("refresh token status","null refresh token");
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            loadingAnimation.cancel();
            finish();
        }else if(status == ActionStatus.UserLoginRefreshStatus.REFRESH_EXPIRED){
            Log.e("refresh token status","expired refresh token");
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            loadingAnimation.cancel();
            finish();
        }else if(status == ActionStatus.UserLoginRefreshStatus.ERROR){
            Log.e("Erreur ","Requettte non executée");
        }
    }

    @Override
    public void oneGetUserStream(ActionStatus.GetUserStreamStatus status, UserStreamRepresentation userStreamRepresentation) {
        if(status == ActionStatus.GetUserStreamStatus.USER_RETURNED){
            SessionStorage.getInstance().setUser(userStreamRepresentation);
            Intent intent = new Intent(getApplicationContext(),ProfilePage.class);
            startActivity(intent);
            loadingAnimation.cancel();
            finish();
        }
    }
}