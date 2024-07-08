package insta.soul.keycloak.actiffinances.keycloak.adminActions;

import insta.soul.keycloak.actiffinances.keycloak.tokens.AdminAccessToken;

public interface ActionTokenExpected {
    void doAction(AdminAccessToken adminToken);

}
