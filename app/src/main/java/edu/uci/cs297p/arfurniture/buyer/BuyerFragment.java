package edu.uci.cs297p.arfurniture.buyer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uci.cs297p.arfurniture.R;
import edu.uci.cs297p.arfurniture.item.BaseItem;
import edu.uci.cs297p.arfurniture.item.Furniture;


public class BuyerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buyer, viewGroup, false);

        RecyclerView itemRecyclerView = rootView.findViewById(R.id.item_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        itemRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        itemRecyclerView.setLayoutManager(layoutManager);

        List<BaseItem> hardcodedDataSet = Arrays.asList(
                new Furniture(0L, "A beautiful table",
                        "This table is very good! This table is very good! This table is very good!",
                        49.99F, 0L, new Timestamp(0), new ArrayList<>(), Furniture.TABLE, "table_0", 1F, 0.5F, 0.5F, Color.YELLOW),
                new Furniture(1L, "A pretty chair",
                        "This chair is very good! This chair is very good! This chair is very good!",
                        99.99F, 1L, new Timestamp(0), Arrays.asList(
                        "https://m.media-amazon.com/images/I/71kHxfeqNaL._AC_UL320_ML3_.jpg",
                        "https://images-na.ssl-images-amazon.com/images/I/41lbJSuAzbL._SL500_.jpg"), Furniture.CHAIR, "chair_0", 0.5F, 0.5F, 1F, Color.BLACK),
                new Furniture(1L, "A giant sofa",
                        "This sofa cannot be previewed, so the button is missing",
                        199.99F, 1L, new Timestamp(0), new ArrayList<>(), Furniture.SOFA)
        );

        RecyclerView.Adapter adapter = new ItemAdapter(getContext(), hardcodedDataSet, (BaseItem item) ->
        {
            Fragment itemFragment = new ItemFragment();
            Bundle args = new Bundle();
            args.putSerializable("item", item);
            itemFragment.setArguments(args);

            getFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .add(R.id.content_container, itemFragment)
                    .addToBackStack(null)
                    .commit();
        }
        );
        itemRecyclerView.setAdapter(adapter);

        return rootView;
    }
}