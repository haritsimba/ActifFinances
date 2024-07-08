package insta.soul.keycloak.actiffinances.keycloak.tokens;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

public class TokenDecoder {
    public String decodeToken(String token) {
        String[] parts = token.split("\\.");
        String payload = parts[1];
        byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE);
        return new String(decodedBytes);
    }

    public String getUserIdFromToken(String token) {
        String decodedToken = decodeToken(token);
        try {
            JSONObject jsonObject = new JSONObject(decodedToken);
            return jsonObject.getString("sub");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUsernameFromToken(String token) {
        String decodedToken = decodeToken(token);
        try {
            JSONObject jsonObject = new JSONObject(decodedToken);
            return jsonObject.getString("preferred_username");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
