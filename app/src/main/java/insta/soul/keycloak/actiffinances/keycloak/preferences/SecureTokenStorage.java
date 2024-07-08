package insta.soul.keycloak.actiffinances.keycloak.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.security.crypto.EncryptedSharedPreferences;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.crypto.KeyGenerator;

public class SecureTokenStorage {

    private static final String SHARED_PREF_NAME = "secure_tokens";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private static final String KEY_ALIAS = "refresh_token_key";

    private final EncryptedSharedPreferences sharedPreferences;

    public SecureTokenStorage(Context context) throws GeneralSecurityException, IOException {
        createKeyIfNotExists(context);

        sharedPreferences = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(
                SHARED_PREF_NAME,
                KEY_ALIAS,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    private void createKeyIfNotExists(Context context) throws GeneralSecurityException, IOException {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build());
            keyGenerator.generateKey();
        }
    }

    public void saveRefreshToken(String refreshToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(REFRESH_TOKEN_KEY, refreshToken);
        editor.apply();
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null);
    }

    public void clearTokens() {
        sharedPreferences.edit().remove(REFRESH_TOKEN_KEY).apply();
    }

}
