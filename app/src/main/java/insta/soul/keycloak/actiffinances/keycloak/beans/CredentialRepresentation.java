package insta.soul.keycloak.actiffinances.keycloak.beans;

import com.google.gson.annotations.SerializedName;

public class CredentialRepresentation {

    @SerializedName("type")
    private String type;
    @SerializedName("value")
    private String value;
    @SerializedName("temporary")
    private boolean temporary;
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }


}
