package insta.soul.keycloak.actiffinances.listadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import insta.soul.keycloak.actiffinances.BlockchainDetailedData;
import insta.soul.keycloak.actiffinances.ProfileMarcheFragment;
import insta.soul.keycloak.actiffinances.R;
import insta.soul.keycloak.actiffinances.listmodels.MarketDataItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class DataMarketListAdapter extends RecyclerView.Adapter<DataMarketListAdapter.MyViewHolder> {
    private List<MarketDataItem> items = new ArrayList<>();
    @Getter
    @Setter
    private boolean search = false;
    private int missingIcon = 0;
    private ProfileMarcheFragment profileMarcheFragment;

    public DataMarketListAdapter(ProfileMarcheFragment profileMarcheFragment){
        this.profileMarcheFragment = profileMarcheFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_data_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MarketDataItem item = items.get(position);
        holder.symbolTextView.setText(item.getBcSymbol());
        holder.priceTextView.setText(String.valueOf(item.getBcPrice()));
        holder.priceChangeTextView.setText(String.valueOf(item.getBcPriceChange()).concat(" %"));
        holder.bcNameTextView.setText(item.getBcName());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DecimalFormat decimalFormat = new DecimalFormat("#.########");
            holder.priceTextView.setText(decimalFormat.format(item.getBcPrice()));
            holder.priceChangeTextView.setText(decimalFormat.format(item.getBcPriceChange()).concat(" %"));
        }

        if(item.getBcPriceChange()<0){
            holder.priceChangeTextView.setTextColor(holder.itemView.getResources().getColor(R.color.red));
        }else{
            holder.priceChangeTextView.setTextColor(holder.itemView.getResources().getColor(R.color.green));
        }
        String iconName = item.getBcSymbol().toLowerCase();
        int iconResId = holder.itemView.getResources().getIdentifier(iconName, "drawable", holder.itemView.getContext().getPackageName());

        if (iconResId == 0) {
            missingIcon++;
            Log.d("Missing Icon", "Missing icon: " + iconName + " count "+missingIcon);
           // Log.d("Icon not found",iconName);
            // Use default BTC icon if not found
            iconResId = R.drawable.btc;  // Assure that you have a default icon named btc.png in drawable
        }

        holder.bcIconImageView.setImageResource(iconResId);holder.bcIconImageView.setImageResource(iconResId);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), BlockchainDetailedData.class);
                intent.putExtra("symbol", item.getBcSymbol());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {return items.size();
    }

    public int updateItem(String symbol, double price,double priceChange) {
        if(this.profileMarcheFragment.getLoadingAnimation().isShowing()){
            this.profileMarcheFragment.getLoadingAnimation().cancel();
            profileMarcheFragment.getView().setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < items.size(); i++) {
            MarketDataItem item = items.get(i);
            if (item.getBcSymbol().equals(symbol)) {
                item.setBcPrice(price);
                item.setBcPriceChange(priceChange);
                return i;
            }
        }
        return -1;
    }

    /**
     * ajout d'un item
     * @param item
     */
    public void addItem(MarketDataItem item) {
        Log.d("notif","Additem");
        if(!search){
            items.add(item);
            notifyItemInserted(items.size() - 1);
            if(this.profileMarcheFragment.getLoadingAnimation().isShowing()){
                this.profileMarcheFragment.getLoadingAnimation().cancel();
                profileMarcheFragment.getView().setVisibility(View.VISIBLE);
            }
            Log.e("notif","Item added");
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchItem(String query) {
        search = true;
        List<MarketDataItem> newList = new ArrayList<>();
        if(!query.trim().isEmpty()){
            for (MarketDataItem item:items) {
                if(item.getBcSymbol().toLowerCase().contains(query.toLowerCase().trim()) || item.getBcName().toLowerCase().contains(query.toLowerCase().trim())){
                    newList.add(item);
                }
            }
            items = newList;
            notifyDataSetChanged();
        }
         // Notifier les changements
    }

    public void resetSearch() {
        search = false;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView symbolTextView;
        TextView priceTextView;
        TextView priceChangeTextView;
        ShapeableImageView bcIconImageView;
        TextView bcNameTextView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            symbolTextView = itemView.findViewById(R.id.bc_symbol);
            priceTextView = itemView.findViewById(R.id.bc_price);
            priceChangeTextView = itemView.findViewById(R.id.bc_price_change);
            bcIconImageView = itemView.findViewById(R.id.bc_icon);
            bcNameTextView = itemView.findViewById(R.id.bc_name);
        }
    }
}