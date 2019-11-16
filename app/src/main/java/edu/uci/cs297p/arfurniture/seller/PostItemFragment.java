package edu.uci.cs297p.arfurniture.seller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import edu.uci.cs297p.arfurniture.R;
import edu.uci.cs297p.arfurniture.item.Item;

public class PostItemFragment extends DialogFragment {

    private final Context mContext;
    private View mRootView;
    @Item.Category
    private final int mCategory;

    public PostItemFragment(Context context, @Item.Category int category) {
        mContext = context;
        mCategory = category;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRootView = view;

        Button button = mRootView.findViewById(R.id.submit_button);
        button.setOnClickListener((clickedView) -> submit());

        setArVisibility(View.GONE);

        CheckBox checkBox = mRootView.findViewById(R.id.ar_support_checkbox);
        checkBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (isChecked) {
                setArVisibility(View.VISIBLE);
            } else {
                setArVisibility(View.GONE);
            }
        });

        RecyclerView modelGallery = mRootView.findViewById(R.id.model_gallery);
        modelGallery.setAdapter(new ModelAdapter(mContext, mCategory, uri -> {
            Bundle args = new Bundle();
            args.putParcelable("uri", uri);
            Intent intent = new Intent(mContext, SceneViewActivity.class);
            intent.putExtras(args);
            startActivity(intent);
        }));
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    // Call this method to send the data back to the parent fragment
    public void submit() {

        Bundle args = new Bundle();
        args.putString("name", ((TextView) mRootView.findViewById(R.id.item_name_text)).getText().toString());
        args.putString("description", ((TextView) mRootView.findViewById(R.id.item_description_text)).getText().toString());
        args.putString("price", ((TextView) mRootView.findViewById(R.id.item_price_text)).getText().toString());

        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        PostItemListener listener = (PostItemListener) getTargetFragment();
        listener.onSubmit(args);
        dismiss();
    }

    private void setArVisibility(final int visibility) {
        mRootView.findViewById(R.id.model_gallery).setVisibility(visibility);
        mRootView.findViewById(R.id.ar_support_button).setVisibility(visibility);
    }

}
