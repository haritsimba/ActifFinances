package insta.soul.keycloak.actiffinances;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import insta.soul.keycloak.actiffinances.coingecko.actions.GetFeed;
import insta.soul.keycloak.actiffinances.coingecko.beans.Feed;
import insta.soul.keycloak.actiffinances.coingecko.beans.FeedContainer;
import insta.soul.keycloak.actiffinances.coingecko.services.CoingeckoService;
import insta.soul.keycloak.actiffinances.listadapter.FeedListAdapter;
import insta.soul.keycloak.actiffinances.listmodels.FeedItem;


public class ProfileSquareFragment extends Fragment {

private CoingeckoService coingeckoService;
private FeedListAdapter feedListAdapter;
private RecyclerView recyclerView;
private final  List<FeedItem> feedItem=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_profile_square, container, false);
        // Inflate the layout for this fragment
        coingeckoService=new CoingeckoService();
        feedListAdapter= new FeedListAdapter(feedItem);
        recyclerView = view.findViewById(R.id.body);
        recyclerView.setAdapter(feedListAdapter);
        coingeckoService.getFeed(new GetFeed.GetFeedCallback() {
            @Override
            public void onSucces(FeedContainer feedContainer) {

                for (Feed feed:feedContainer.getData()){

                       FeedItem f = new FeedItem(feed.getTitle(), feed.getDescription(), feed.getUpdatedAt());
                       feedListAdapter.addItem(f);
                    }

            }

            @Override
            public void onFailError(Throwable throwable) {

            }
        });

        return view;

    }

}