package insta.soul.keycloak.actiffinances;

import android.annotation.SuppressLint;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import insta.soul.keycloak.actiffinances.databinding.ActivityProfilePageBinding;
import insta.soul.keycloak.actiffinances.keycloak.services.SessionStorage;

public class ProfilePage extends AppCompatActivity {

    ActivityProfilePageBinding activityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    /***    if (SessionStorage.getInstance().getAccessToken() == null || SessionStorage.getInstance().getUser() == null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }**/

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityBinding = ActivityProfilePageBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        replaceFragment(new ProfileMainFragment());
        activityBinding.bottomNavigation.setBackground(null);
        activityBinding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.marche) {
                replaceFragment(new ProfileMarcheFragment());
            } else if (item.getItemId() == R.id.square) {
                replaceFragment(new ProfileSquareFragment());
            } else if (item.getItemId() == R.id.decouvrir) {
                replaceFragment(new ProfileDiscoverFragment());
            } else if (item.getItemId() == R.id.portfeuille) {
                replaceFragment(new ProfileWalletFragment());
            }
            return true;
        });
        FloatingActionButton fab = activityBinding.changeBtn;
        fab.setOnClickListener(view -> {
            replaceFragment(new ProfileMainFragment());
        });

        //boutton retour
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                AlertDialog.Builder popup = new AlertDialog.Builder(ProfilePage.this);
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
    }

    private void replaceFragment(Fragment fragment) {
        Log.d("test","bungo");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
