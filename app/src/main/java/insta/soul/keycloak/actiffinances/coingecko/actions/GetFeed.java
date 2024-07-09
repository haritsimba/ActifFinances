package insta.soul.keycloak.actiffinances.coingecko.actions;

import insta.soul.keycloak.actiffinances.coingecko.beans.FeedContainer;
import insta.soul.keycloak.actiffinances.retrofit.coingecko.CoingeckoApiServices;
import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AllArgsConstructor
public class GetFeed {
    private CoingeckoApiServices services;
    private GetFeedCallback callback;
    void execute(){

        Call<FeedContainer> call= services.getFeeds();
        call.enqueue(new Callback<FeedContainer>() {
            @Override
            public void onResponse(Call<FeedContainer> call, Response<FeedContainer> response) {
                if(response .isSuccessful()){
                    FeedContainer feedContainer= response.body();
                    callback.onSucces(feedContainer);
                }
            }

            @Override
            public void onFailure(Call<FeedContainer> call, Throwable throwable) {
                callback.onFailError(throwable);
            }
        });
    }

    public interface GetFeedCallback{
        void onSucces(FeedContainer feedContainer);
        void onFailError(Throwable throwable);

    }

}