package edu.uci.cs297p.arfurniture.buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

    @Item.Category
    private Integer mCategory;

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

        Spinner spinner = view.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.category_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final int idx = parent.getSelectedItemPosition();
                if (idx == 0) {
                    mCategory = null;
                } else {
                    mCategory = idx - 1;
                }
                itemAdapter.refresh(mCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swipeLayout.setOnRefreshListener(itemAdapter);
        itemRecyclerView.setAdapter(itemAdapter);
    }
}