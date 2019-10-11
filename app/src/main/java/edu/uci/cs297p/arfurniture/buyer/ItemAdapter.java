package edu.uci.cs297p.arfurniture.buyer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import edu.uci.cs297p.arfurniture.ARActivity;
import edu.uci.cs297p.arfurniture.R;
import edu.uci.cs297p.arfurniture.item.ARSupportItem;
import edu.uci.cs297p.arfurniture.item.BaseItem;


public class ItemAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private List<BaseItem> mDataSet;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemViewHolder(CardView view) {
            super(view);
        }
    }

    public ItemAdapter(Context context, List<BaseItem> data) {
        mContext = context;
        mDataSet = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView itemCardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        return new ItemViewHolder(itemCardView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        BaseItem item = mDataSet.get(position);
        ((TextView) viewHolder.itemView.findViewById(R.id.item_name)).setText(item.getName());
        ((TextView) viewHolder.itemView.findViewById(R.id.item_description)).setText(item.getDescription());
        ((TextView) viewHolder.itemView.findViewById(R.id.item_price)).setText(item.getStringPrice());

        ImageButton arButton = viewHolder.itemView.findViewById(R.id.ar_button);

        if (item instanceof ARSupportItem && ((ARSupportItem) item).isPreviewable()) {
            arButton.setEnabled(true);
            arButton.setVisibility(View.VISIBLE);
            arButton.setOnClickListener((View view) -> {
                Intent myIntent = createIntent(((ARSupportItem) item).getModelName());
                mContext.startActivity(myIntent);
            });
        } else {
            arButton.setEnabled(false);
            arButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private Intent createIntent(String modelName) {
        Bundle params = new Bundle();
        params.putString(ARActivity.MODEL_NAME_KEY, modelName);
        Intent intent = new Intent(mContext, ARActivity.class);
        intent.putExtras(params);
        return intent;
    }
}
