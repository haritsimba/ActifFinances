package insta.soul.keycloak.actiffinances.retrofit.keycloak;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitKeycloakClientInstace {
    private static Retrofit retrofit;
    public static String REALM = KeycloakConfig.getInstance().getRealmName();
    public static String BASE_URL = KeycloakConfig.getInstance().getBaseUrl();
    public static String CLIENT_ID = KeycloakConfig.getInstance().getClientId();

    public static Retrofit getRetrofitInstance(){
        if(retrofit == null){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
