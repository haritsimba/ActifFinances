package insta.soul.keycloak.actiffinances.keycloak.enumerations;

public interface ActionStatus {
    public enum CreationStatus{
        CREATED,
        CONFLICT,
        NOT_ALLOWED, ERROR
    }
    public enum UserLoginStatus{
        CONNECTED,
        INVALID_CREDENTIALS,
        ERROR, ACCOUNT_NOT_CONFIGURED
    }
    public enum RefreshLoginStatus{
        CONNECTED,
        EXPIRED_TOKEN
    }
    public enum SendEmailVerificationStatus{
        EMAIL_SENT,
        ERROR,
        LINK_NOT_FOUND,
        ACTION_NOT_AUTHORIZED
    }
    public enum GetUserStreamStatus{
        USER_RETURNED,
        USER_NOT_FOUND,
        ACTION_NOT_AUTHORIZED,
        ERROR
    }
    public enum SendEmailExecuteActionsStatus{
        EMAIL_SENT,
        ACTION_NOT_AUTHORIZED,
        ERROR
    }
    public enum UserLoginRefreshStatus {
        CONNECTED,
        REFRESH_EXPIRED,
        ERROR,
        NULL_REFRESH_TOKEN
    }
    public enum UserLogoutStatus{
        SUCCESS,
        ERROR
    }
}
