package edu.uci.cs297p.arfurniture.seller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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

        //TODO: Post item to the backend
    }
}

// Defines the listener interface
interface PostItemListener {
    void onSubmit(Bundle args);
}
