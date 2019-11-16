package edu.uci.cs297p.arfurniture.seller;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.uci.cs297p.arfurniture.R;
import edu.uci.cs297p.arfurniture.item.Item;

public class ModelAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Uri> mUriList = new ArrayList<>();
    private OnModelClickListener mListener;

    public ModelAdapter(Context context, @Item.Category int category, OnModelClickListener listener) {
        mContext = context;
        mListener = listener;
        String categoryStr = Item.categoryToStr(category);
        Log.d("ModelAdapter", category + categoryStr);
        try {
            String[] modelNames = context.getResources().getAssets().list(categoryStr);
            if (modelNames != null) {
                for (String modelName : modelNames) {
                    if (modelName != null && !modelName.isEmpty()) {
                        mUriList.add(Uri.parse("file:///android_asset/" + categoryStr + "/" + modelName));
                    }
                }
            }
        } catch (IOException e) {
            Toast.makeText(mContext, "Unable to load models in category " + categoryStr, Toast.LENGTH_LONG).show();
        }
        Log.d("ModelAdapter", mUriList.toString());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(mContext).inflate(R.layout.model_cardview, parent, false);

        return new RecyclerView.ViewHolder(cardView) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Uri uri = mUriList.get(position);
        ((TextView) holder.itemView.findViewById(R.id.model_name)).setText(uri.toString());
        holder.itemView.setOnClickListener(view -> mListener.onModelClick(uri));
    }

    @Override
    public int getItemCount() {
        return mUriList.size();
    }
}

interface OnModelClickListener {
    void onModelClick(Uri uri);
}
