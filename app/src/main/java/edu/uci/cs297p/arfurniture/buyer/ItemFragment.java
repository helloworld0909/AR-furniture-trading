package edu.uci.cs297p.arfurniture.buyer;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import edu.uci.cs297p.arfurniture.ARActivity;
import edu.uci.cs297p.arfurniture.R;
import edu.uci.cs297p.arfurniture.item.Item;
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

        Bundle args = getArguments();
        Item item = (Item) args.getSerializable("item");

        ((TextView) rootView.findViewById(R.id.item_name)).setText(item.getName());

        ((TextView) rootView.findViewById(R.id.item_description)).setText(item.getDescription());

        ((TextView) rootView.findViewById(R.id.item_price)).setText(item.getPrice());

        ImageButton arButton = rootView.findViewById(R.id.ar_button);
        if (item.isPreviewable()) {
            arButton.setEnabled(true);
            arButton.setVisibility(View.VISIBLE);
            arButton.setOnClickListener((View view) -> {
                Bundle params = new Bundle();
                params.putSerializable(ARActivity.ITEM_KEY, item);
                Intent intent = new Intent(getContext(), ARActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            });
        } else {
            arButton.setEnabled(false);
            arButton.setVisibility(View.GONE);
        }

        ItemImageBuyerAdapter adapter = new ItemImageBuyerAdapter(getContext(), item.getImageUrls());

        ViewPager viewPager = rootView.findViewById(R.id.image_list);
        viewPager.setAdapter(adapter);

        CircleIndicator circleIndicator = rootView.findViewById(R.id.circle);
        circleIndicator.setViewPager(viewPager);

        return rootView;
    }

}
