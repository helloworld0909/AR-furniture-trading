package edu.uci.cs297p.arfurniture.buyer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.uci.cs297p.arfurniture.R;
import edu.uci.cs297p.arfurniture.item.Item;


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

//        List<BaseItem> hardcodedDataSet = Arrays.asList(
//                new Furniture(0L, "A beautiful table",
//                        "This table is very good! This table is very good! This table is very good!",
//                        49.99F, 0L, new Timestamp(0), new ArrayList<>(), Furniture.TABLE, "table_0", 1F, 0.5F, 0.5F, Color.YELLOW),
//                new Furniture(1L, "A pretty chair",
//                        "This chair is very good! This chair is very good! This chair is very good!",
//                        99.99F, 1L, new Timestamp(0), Arrays.asList(
//                        "https://m.media-amazon.com/images/I/71kHxfeqNaL._AC_UL320_ML3_.jpg",
//                        "https://images-na.ssl-images-amazon.com/images/I/41lbJSuAzbL._SL500_.jpg"), Furniture.CHAIR, "chair_0", 0.5F, 0.5F, 1F, Color.BLACK),
//                new Furniture(1L, "A giant sofa",
//                        "This sofa cannot be previewed, so the button is missing",
//                        199.99F, 1L, new Timestamp(0), new ArrayList<>(), Furniture.SOFA)
//        );
        List<Item> dataSet = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a reference to the cities collection
        CollectionReference itemsRef = db.collection("items");

        // Create a query against the collection.
        Task<QuerySnapshot> query = itemsRef.get();
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TEST", document.getId() + " => " + document.getData());
                        dataSet.add(new Item(document.getId(), document.get("name").toString(), document.get("description").toString(),
                                document.get("modelName").toString(), document.get("price").toString(), new ArrayList<>()));
                    }

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
                } else {
                    Log.d("TEST", "Error getting documents: ", task.getException());
                }
            }
        });

        return rootView;
    }
}