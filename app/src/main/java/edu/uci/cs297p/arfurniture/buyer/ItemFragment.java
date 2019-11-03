package edu.uci.cs297p.arfurniture.buyer;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;

import edu.uci.cs297p.arfurniture.R;
import me.relex.circleindicator.CircleIndicator;


public class ItemFragment extends Fragment {


    public ItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item, viewGroup, false);
        Toolbar toolbar = rootView.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        TextView textView = rootView.findViewById(R.id.item_name);
        textView.setText("Item Name");

        ItemImageAdapter adapter = new ItemImageAdapter(getContext(), Arrays.asList(
                "https://m.media-amazon.com/images/I/71kHxfeqNaL._AC_UL320_ML3_.jpg",
                "https://images-na.ssl-images-amazon.com/images/I/41lbJSuAzbL._SL500_.jpg"));

        ViewPager viewPager = rootView.findViewById(R.id.image_list);
        viewPager.setAdapter(adapter);

        CircleIndicator circleIndicator = rootView.findViewById(R.id.circle);
        circleIndicator.setViewPager(viewPager);

        return rootView;
    }

}
