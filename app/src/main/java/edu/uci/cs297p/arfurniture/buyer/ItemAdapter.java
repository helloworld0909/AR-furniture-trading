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
    private OnItemClickListener mListener;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemViewHolder(CardView view) {
            super(view);
        }

        public void bind(BaseItem item, OnItemClickListener listener) {
            ((TextView) itemView.findViewById(R.id.item_name)).setText(item.getName());
            ((TextView) itemView.findViewById(R.id.item_description)).setText(item.getDescription());
            ((TextView) itemView.findViewById(R.id.item_price)).setText(item.getStringPrice());

            itemView.setOnClickListener((View v) -> listener.onItemClick(item));
        }
    }

    public ItemAdapter(Context context, List<BaseItem> data, OnItemClickListener listener) {
        mContext = context;
        mDataSet = data;
        mListener = listener;
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
        ((ItemViewHolder) viewHolder).bind(item, mListener);

        ImageButton arButton = viewHolder.itemView.findViewById(R.id.ar_button);

        if (item instanceof ARSupportItem && ((ARSupportItem) item).isPreviewable()) {
            arButton.setEnabled(true);
            arButton.setVisibility(View.VISIBLE);
            arButton.setOnClickListener((View view) -> {
                Intent myIntent = createIntent(item);
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

    private Intent createIntent(BaseItem item) {
        Bundle params = new Bundle();
        params.putSerializable(ARActivity.ITEM_KEY, item);
        Intent intent = new Intent(mContext, ARActivity.class);
        intent.putExtras(params);
        return intent;
    }
}

interface OnItemClickListener {
    void onItemClick(BaseItem item);
}
