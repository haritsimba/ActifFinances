package insta.soul.keycloak.actiffinances.keycloak.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StandardUserRepresentation implements UserRepresenation{
        @SerializedName("id")
        private String id;
        @SerializedName("username")
        private String username;
        @SerializedName("email")
        private String email;
        @SerializedName("firstName")
        private String firstName;
        @SerializedName("lastName")
        private String lastName;
        @SerializedName("enabled")
        private boolean enabled;
        @SerializedName("requiredActions")
        private List<String> requiredActions;
        @SerializedName("credentials")

        private List<CredentialRepresentation> credentials;


        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getFirstName() {
                return firstName;
        }

        public void setFirstName(String firstName) {
                this.firstName = firstName;
        }

        public String getLastName() {
                return lastName;
        }

        public void setLastName(String lastName) {
                this.lastName = lastName;
        }

        public boolean isEnabled() {
                return enabled;
        }

        public void setEnabled(boolean enabled) {
                this.enabled = enabled;
        }

        public List<CredentialRepresentation> getCredentials() {
                return credentials;
        }

        public void setCredentials(List<CredentialRepresentation> credentials) {
                this.credentials = credentials;
        }

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public List<String> getRequiredActions() {
                return requiredActions;
        }

        public void setRequiredActions(List<String> requiredActions) {
                this.requiredActions = requiredActions;
        }
}
