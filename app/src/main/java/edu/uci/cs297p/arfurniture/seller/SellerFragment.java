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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                showPostItemDialog(category);
            });

        }
    }

    private void showPostItemDialog(@Item.Category int category) {
        PostItemFragment postItemFragment = new PostItemFragment(getContext(), category);
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
        for (String key : mArgs.keySet()) {
            if (PostItemFragment.SCALE_KEY.equals(key)) {
                float[] data = mArgs.getFloatArray(key);
                if (data != null && data.length == 3) {
                    List<Double> scale = new ArrayList<>();
                    scale.add((double) data[0]);
                    scale.add((double) data[1]);
                    scale.add((double) data[2]);
                    itemData.put(key, scale);
                }
            } else {
                itemData.put(key, mArgs.get(key));
            }
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
