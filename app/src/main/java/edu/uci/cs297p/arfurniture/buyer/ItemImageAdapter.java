package edu.uci.cs297p.arfurniture.buyer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.uci.cs297p.arfurniture.R;

public class ItemImageAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mImageUrls;

    public ItemImageAdapter(Context context, List<String> imageUrls) {
        mContext = context;
        mImageUrls = imageUrls;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.i("lz", "Load " + position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image, container, false);
        ImageView imageView = view.findViewById(R.id.image);
        Picasso.with(mContext).load(mImageUrls.get(position)).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return o == view;
    }
}
