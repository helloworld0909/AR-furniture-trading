package edu.uci.cs297p.arfurniture.buyer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.uci.cs297p.arfurniture.R;
import edu.uci.cs297p.arfurniture.item.Item;
import edu.uci.cs297p.arfurniture.seller.PostItemFragment;


public class BuyerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buyer, viewGroup, false);

        RecyclerView itemRecyclerView = rootView.findViewById(R.id.item_list);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        itemRecyclerView.setLayoutManager(layoutManager);

        List<Item> dataSet = new ArrayList<>();

        RecyclerView.Adapter adapter = new ItemAdapter(getContext(), dataSet, (Item item) ->
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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a reference to the cities collection
        CollectionReference itemsRef = db.collection("items");

        // Create a query against the collection.
        Task<QuerySnapshot> query = itemsRef.get();
        query.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("TEST", document.getId() + " => " + document.getData());
                    dataSet.add(constructItem(document));
                }
                adapter.notifyDataSetChanged();

            } else {
                Log.d("TEST", "Error getting documents: ", task.getException());
            }
        });

        return rootView;
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
}