package edu.uci.cs297p.arfurniture.seller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import edu.uci.cs297p.arfurniture.R;
import edu.uci.cs297p.arfurniture.item.Item;

public class SellerFragment extends Fragment implements PostItemListener {

    private Bundle mArgs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seller, viewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mArgs = new Bundle();

        GridLayout gridLayout = view.findViewById(R.id.seller_category_grid);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            CardView cardView = (CardView) gridLayout.getChildAt(i);
            @Item.Category final int category = i;
            cardView.setOnClickListener(clickedView -> {
                mArgs.putInt("category", category);
                showPostItemDialog();
            });

        }
    }

    private void showPostItemDialog() {
        PostItemFragment postItemFragment = new PostItemFragment();
        // SETS the target fragment for use later when sending results
        postItemFragment.setTargetFragment(SellerFragment.this, 300);
        postItemFragment.show(getFragmentManager(), "fragment_edit_name");
    }

    @Override
    public void onSubmit(Bundle args) {
        mArgs.putAll(args);
        Toast.makeText(getContext(), mArgs.toString(), Toast.LENGTH_LONG).show();

        // Post item to the backend
        // TODO: Add imageURLs and AR model attributes
        Map<String, Object> itemData = new HashMap<>();
        Set<String> ks = mArgs.keySet();
        Iterator<String> iterator = ks.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            itemData.put(key, mArgs.get(key));
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items")
                .add(itemData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TEST", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TEST", "Error adding document", e);
                    }
                });

    }
}

// Defines the listener interface
interface PostItemListener {
    void onSubmit(Bundle args);
}
