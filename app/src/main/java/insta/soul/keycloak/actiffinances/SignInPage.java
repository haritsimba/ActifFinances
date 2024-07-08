package insta.soul.keycloak.actiffinances;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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

import java.util.ArrayList;
import java.util.List;

import insta.soul.keycloak.actiffinances.keycloak.adminActions.SendEmailExecuteActions;
import insta.soul.keycloak.actiffinances.keycloak.adminActions.UserRegistration;
import insta.soul.keycloak.actiffinances.keycloak.enumerations.ActionStatus;
import insta.soul.keycloak.actiffinances.keycloak.services.UserService;
import insta.soul.keycloak.actiffinances.keycloak.beans.CredentialRepresentation;
import insta.soul.keycloak.actiffinances.keycloak.beans.StandardUserRepresentation;


public class SignInPage extends AppCompatActivity implements UserRegistration.UserRegistrationCallback, SendEmailExecuteActions.SendEmailExecuteActionsCallback {
    EditText signInUsername;
    EditText signInPassword;
    EditText signInConfirmPassword;
    EditText signInEmail;
    EditText signInFirstName;
    Button signInRegisterBtn;

    StandardUserRepresentation standardUserRepresentation;

    UserService userService;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //userServices

        //recuperation des elements
        signInUsername = findViewById(R.id.si_username_edit);
        signInPassword = findViewById(R.id.si_password_edit);
        signInEmail = findViewById(R.id.si_email_edit);
        signInFirstName = findViewById(R.id.si_nom_edit);
        signInConfirmPassword = findViewById(R.id.si_confirm_password_edit);
        signInRegisterBtn = findViewById(R.id.si_register_btn);

        //
        userService = new UserService();
        //
        signInRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    register();
            }
        });

        //back button action
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

    public void register(){
        String username = String.valueOf(signInUsername.getText());
        String email = String.valueOf(signInEmail.getText());
        String nom = String.valueOf(signInFirstName.getText());
        String password = String.valueOf(signInPassword.getText());
        String confirmPassword = String.valueOf(signInConfirmPassword.getText());

        if(username.isEmpty()){
            signInUsername.setError("Le nom d'utilisateur est obligatoire");
            signInUsername.requestFocus();
            Log.e("SignInPage","Le nom d'utilisateur est obligatoire");
            return;}
        if(username.contains(" ")){
            signInUsername.setError("Le nom d'utilisateur ne doit pas contenir d'espace");
            signInUsername.setText("");
            signInUsername.requestFocus();
            Log.e("SignInPage","Le nom d'utilisateur espace");
            return;
        }
        if(nom.isEmpty()){
            signInFirstName.setError("Le nom est obligatoire");
            signInFirstName.requestFocus();
            Log.e("SignInPage","Le nom est obligatoire");
            return;
        }
       if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
           signInEmail.setError("L'email n'est pas valide");
           signInEmail.requestFocus();
           Log.e("SignInPage","L'email n'est pas valide");
           return;
       }
        if(!password.equals(confirmPassword)){
            signInConfirmPassword.setError("Les mots de passe ne correspondent pas");
            signInConfirmPassword.setText("");
            signInConfirmPassword.requestFocus();
            Log.e("SignInPage","Les mots de passe ne correspondent pas");
            return;
        }
        StandardUserRepresentation userRepresentation = new StandardUserRepresentation();
        userRepresentation.setUsername(username);
        userRepresentation.setEmail(email);
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(password);

        List<CredentialRepresentation> credentialRepresentations = new ArrayList<CredentialRepresentation>();
        credentialRepresentations.add(credentialRepresentation);
        userRepresentation.setCredentials(credentialRepresentations);
        userRepresentation.setEnabled(true);
        userRepresentation.setFirstName(nom);

        this.standardUserRepresentation = userRepresentation;

        userService.registration(userRepresentation,SignInPage.this);
    }

    @Override
    public void oneRegistered(ActionStatus.CreationStatus creationStatus,String userId) {
            if (creationStatus == ActionStatus.CreationStatus.CREATED) {
                userService.sendEmailRegistrationRequiredActions(userId,SignInPage.this);
                Toast.makeText(SignInPage.this,"Veuillez verifié votre boite mail",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),LoginPage.class);
                startActivity(intent);
                finish();
            }
    }

    @Override
    public void oneEmailExecuteActionSent(ActionStatus.SendEmailExecuteActionsStatus actionsStatus) {
        if(actionsStatus == ActionStatus.SendEmailExecuteActionsStatus.EMAIL_SENT){
            Log.d("Email required Actions","Email Envoyé");
        }
    }
}