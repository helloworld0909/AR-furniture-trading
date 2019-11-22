package edu.uci.cs297p.arfurniture.buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import edu.uci.cs297p.arfurniture.R;
import edu.uci.cs297p.arfurniture.item.Item;


public class BuyerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buyer, viewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SwipeRefreshLayout swipeLayout = view.findViewById(R.id.swipe_container);

        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light);

        RecyclerView itemRecyclerView = view.findViewById(R.id.item_list);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        itemRecyclerView.setLayoutManager(layoutManager);

        ItemAdapter itemAdapter = new ItemAdapter(getContext(), new ArrayList<>(),
                (Item item) -> {
                    Fragment itemFragment = new ItemFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("item", item);
                    itemFragment.setArguments(args);

                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .add(R.id.content_container, itemFragment)
                            .addToBackStack(null)
                            .commit();
                },

                new OnRefreshEventListener() {
                    @Override
                    public void onRefreshStart() {
                        swipeLayout.setRefreshing(true);
                    }

                    @Override
                    public void onRefreshEnd() {
                        swipeLayout.setRefreshing(false);
                    }
                }
        );

        swipeLayout.setOnRefreshListener(itemAdapter);
        itemRecyclerView.setAdapter(itemAdapter);
        itemAdapter.refresh();
    }
}