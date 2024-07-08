package insta.soul.keycloak.actiffinances.retrofit.keycloak;

import java.util.List;

import insta.soul.keycloak.actiffinances.keycloak.tokens.AdminAccessToken;
import insta.soul.keycloak.actiffinances.keycloak.beans.StandardUserRepresentation;
import insta.soul.keycloak.actiffinances.keycloak.beans.UserStreamRepresentation;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface KeycloakApiServices {

    @FormUrlEncoded
    @POST("/realms/{REALM}/protocol/openid-connect/token")
    Call<AdminAccessToken> getAdminToken(
            @Path("REALM") String realm,
            @Field("client_id") String clientId,
            @Field("grant_type") String grantType,
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/realms/{REALM}/protocol/openid-connect/token")
    Call<AdminAccessToken> doLogin(
            @Path("REALM") String realm,
            @Field("client_id") String clientId,
            @Field("grant_type") String grantType,
            @Field("username") String username,
            @Field("password") String password,
            @Field("totp") String totp
    );

    @FormUrlEncoded
    @POST("/realms/{REALM}/protocol/openid-connect/logout")
    Call<Void> logout(
            @Path("REALM") String realm,
            @Field("client_id") String clientId,
            @Field("refresh_token") String refreshToken
    );
    @FormUrlEncoded
    @POST("/realms/{REALM}/protocol/openid-connect/token")
    Call<AdminAccessToken> connectViaRefreshToken(
            @Path("REALM") String realm,
            @Field("client_id") String clientId,
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken
    );

    @Headers("content-type:application/json")
    @POST("/admin/realms/{REALM}/users")
    Call<Void> createUser(
            @Header("Authorization") String token,
            @Path("REALM") String realm,
            @Body StandardUserRepresentation simpleUser
    );

    @GET("/admin/realms/{REALM}/users")
    Call<List<UserStreamRepresentation>> getUserInfo(
            @Header("Authorization") String token,
            @Path("REALM") String realm,
            @Query("username") String username
    );

    @GET("/admin/realms/{REALM}/users")
    Call<List<UserStreamRepresentation>> getUserByUsernameAndPassword(
            @Header("Authorization") String token,
            @Path("REALM") String realm,
            @Query("username") String username,
            @Query("password") String password
    );

    @PUT("/admin/realms/{REALM}/users/{USER_ID}/send-verify-email")
    Call<Void> sendVerifyEmail(
            @Header("Authorization") String token,
            @Path("REALM") String realm,
            @Path("USER_ID") String userId
    );

    @Headers("content-type:application/json")
    @PUT("/admin/realms/{REALM}/users/{USER_ID}/execute-actions-email")
    Call<Void> executeActionsEmail(
            @Header("Authorization") String token,
            @Path("REALM") String realm,
            @Path("USER_ID") String userId,
            @Body List<String> actions
            );
}
