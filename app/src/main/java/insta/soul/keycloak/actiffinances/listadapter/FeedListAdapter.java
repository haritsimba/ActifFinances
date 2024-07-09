package insta.soul.keycloak.actiffinances.listadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import insta.soul.keycloak.actiffinances.R;
import insta.soul.keycloak.actiffinances.coingecko.beans.FeedContainer;
import insta.soul.keycloak.actiffinances.listmodels.FeedItem;

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ViewHolder> {
    private List<FeedItem> feedList;
  public FeedListAdapter(List<FeedItem> feedList){
      this.feedList= feedList;


  }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_feed_item,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedItem feedItem = feedList.get(position);
        holder.title.setText(feedItem.getTitle());
        holder.description.setText(feedItem.getDescription());
        holder.pubDate.setText((int) feedItem.getDate());

    }

    @Override
    public int getItemCount() {

      return feedList.size();
    }
    public void addItem(FeedItem feedItem){
      feedList.add(feedItem);
      notifyItemInserted(feedList.size() -1);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, pubDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.feed_name);
            description = itemView.findViewById(R.id.feed_user);
            pubDate = itemView.findViewById(R.id.feed_date);
        }
    }
}

