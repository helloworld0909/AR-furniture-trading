package edu.uci.cs297p.arfurniture.buyer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import edu.uci.cs297p.arfurniture.ARActivity;
import edu.uci.cs297p.arfurniture.R;
import edu.uci.cs297p.arfurniture.item.Item;
import edu.uci.cs297p.arfurniture.seller.PostItemFragment;


public class ItemAdapter extends RecyclerView.Adapter implements SwipeRefreshLayout.OnRefreshListener {

    private final Context mContext;
    private List<Item> mDataSet;
    private OnItemClickListener mListener;
    private OnRefreshEventListener mRefreshedListener;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemViewHolder(CardView view) {
            super(view);
        }

        public void bind(Item item, OnItemClickListener listener) {
            ((TextView) itemView.findViewById(R.id.item_name)).setText(item.getName());
            ((TextView) itemView.findViewById(R.id.item_description)).setText(item.getDescription());
            ((TextView) itemView.findViewById(R.id.item_price)).setText(item.getPrice());

            itemView.setOnClickListener((View v) -> listener.onItemClick(item));
        }
    }

    public ItemAdapter(Context context, List<Item> data, OnItemClickListener listener, OnRefreshEventListener refreshedListener) {
        mContext = context;
        mDataSet = data;
        mListener = listener;
        mRefreshedListener = refreshedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView itemCardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        return new ItemViewHolder(itemCardView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Item item = mDataSet.get(position);
        ((ItemViewHolder) viewHolder).bind(item, mListener);

        ImageButton arButton = viewHolder.itemView.findViewById(R.id.ar_button);

        if (item.isPreviewable()) {
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

    private Intent createIntent(Item item) {
        Bundle params = new Bundle();
        params.putInt(ARActivity.FLAG_KEY, ARActivity.BUYER);
        params.putSerializable(ARActivity.ITEM_KEY, item);
        Intent intent = new Intent(mContext, ARActivity.class);
        intent.putExtras(params);
        return intent;
    }

    public void refresh() {
        refresh(null);
    }

    public void refresh(@Item.Category Integer category) {
        Log.d("ItemAdapter", "refresh");

        mRefreshedListener.onRefreshStart();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a reference to the cities collection
        CollectionReference itemsRef = db.collection("items");

        mDataSet.clear();

        // Create a query against the collection.
        Task<QuerySnapshot> query;
        if (category != null) {
            query = itemsRef.whereEqualTo("category", category).get();
        } else {
            query = itemsRef.get();
        }
        query.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(getClass().getName(), document.getId() + " => " + document.getData());
                    mDataSet.add(constructItem(document));
                }
                notifyDataSetChanged();
                mRefreshedListener.onRefreshEnd();
            } else {
                Log.e(getClass().getName(), "Error getting documents: ", task.getException());
                mRefreshedListener.onRefreshEnd();
            }
        });
    }

    private Item constructItem(QueryDocumentSnapshot document) {
        Item item = new Item(document.getId(), document.get("name").toString(),
                document.get("description").toString(), document.get("price").toString(), Integer.valueOf(document.get("category").toString()));
        if (document.contains("imageURLs")) {
            item.setImageUrls((List<String>) document.get("imageURLs"));
        }
        if (document.contains("modelName")) {
            item.setModelName(document.get("modelName").toString());
        }

        if (document.contains(PostItemFragment.SCALE_KEY)) {
            final List<Double> scale = (List<Double>) document.get(PostItemFragment.SCALE_KEY);
            if (scale != null && scale.size() == 3) {
                item.setScale(scale);
            } else {
                throw new IllegalArgumentException("Scale data is wrong");
            }
        }

        if (document.contains(PostItemFragment.COLOR_KEY)) {
            final long color = (long) document.get(PostItemFragment.COLOR_KEY);
            item.setColor((int) color);
        }

        return item;
    }

    @Override
    public void onRefresh() {
        refresh();
    }
}

interface OnItemClickListener {
    void onItemClick(Item item);
}

interface OnRefreshEventListener {
    void onRefreshStart();

    void onRefreshEnd();
}
