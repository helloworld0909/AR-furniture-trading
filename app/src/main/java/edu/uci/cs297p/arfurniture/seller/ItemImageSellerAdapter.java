package edu.uci.cs297p.arfurniture.seller;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uci.cs297p.arfurniture.R;

public class ItemImageSellerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Bitmap> mPictureList;

    public ItemImageSellerAdapter(Context context, List<Bitmap> pictureList) {
        mContext = context;
        mPictureList = pictureList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.item_image_seller, parent, false);
        return new RecyclerView.ViewHolder(imageView) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageView imageView = holder.itemView.findViewById(R.id.item_image);
        imageView.setImageBitmap(mPictureList.get(position));
    }

    @Override
    public int getItemCount() {
        return mPictureList.size();
    }
}
