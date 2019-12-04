package edu.uci.cs297p.arfurniture.seller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private Bundle mSavedState;

    private int mSelectedPos = RecyclerView.NO_POSITION;

    public ModelAdapter(Context context, @Item.Category int category, OnModelClickListener listener, Bundle savedState) {
        mContext = context;
        mListener = listener;
        mSavedState = savedState;
        String categoryStr = Item.categoryToStr(category);
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
        String filename = uri.getLastPathSegment();
        if (filename != null) {
            ImageView modelImageView = holder.itemView.findViewById(R.id.model_image);
            String name = filename.substring(0, filename.indexOf("."));
            final int resourceId = mContext.getResources().getIdentifier(name, "drawable", mContext.getPackageName());
            modelImageView.setImageDrawable(mContext.getDrawable(resourceId));
        }

        Button previewButton = holder.itemView.findViewById(R.id.model_preview_button);

        previewButton.setOnClickListener(view -> mListener.onModelClick(uri));

        if (mSelectedPos != position) {
            previewButton.setVisibility(View.GONE);
            holder.itemView.setBackground(null);
        }

        holder.itemView.setOnClickListener(view -> {
            previewButton.setVisibility(View.VISIBLE);
            view.setBackgroundResource(R.color.slate_blue);
            mSelectedPos = position;
            notifyDataSetChanged();
            mSavedState.putString("modelName", getSelectedModelName());
        });
    }

    @Override
    public int getItemCount() {
        return mUriList.size();
    }

    @Nullable
    public Uri getSelectedUri() {
        return mSelectedPos == RecyclerView.NO_POSITION ? null : mUriList.get(mSelectedPos);
    }

    @Nullable
    public String getSelectedModelName() {
        return mSelectedPos == RecyclerView.NO_POSITION ? null : mUriList.get(mSelectedPos).getLastPathSegment();
    }
}

interface OnModelClickListener {
    void onModelClick(Uri uri);
}
