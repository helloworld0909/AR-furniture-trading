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

import edu.uci.cs297p.arfurniture.ARActivity;
import edu.uci.cs297p.arfurniture.R;
import edu.uci.cs297p.arfurniture.item.Item;

public class PostItemFragment extends DialogFragment {

    private final Context mContext;
    private View mRootView;
    @Item.Category
    private final int mCategory;

    private Bundle arBundle = new Bundle();

    public static final int REQUEST_CODE = 0;
    public static final String SCALE_KEY = "scale";
    public static final String COLOR_KEY = "color";

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
        ModelAdapter modelAdapter = new ModelAdapter(mContext, mCategory, uri -> {
            Bundle args = new Bundle();
            args.putParcelable("uri", uri);
            Intent intent = new Intent(mContext, SceneViewActivity.class);
            intent.putExtras(args);
            startActivity(intent);
        });
        modelGallery.setAdapter(modelAdapter);

        Button arButton = mRootView.findViewById(R.id.ar_support_button);
        arButton.setOnClickListener(clickedView -> {
            Intent intent = new Intent(mContext, ARActivity.class);
            intent.putExtra(ARActivity.FLAG_KEY, ARActivity.SELLER);
            intent.putExtra(ARActivity.URI_KEY, modelAdapter.getSelectedUri());
            startActivityForResult(intent, REQUEST_CODE);
        });

        Button submitButton = mRootView.findViewById(R.id.submit_button);
        submitButton.setOnClickListener((clickedView) -> {
            arBundle.putString("modelName", modelAdapter.getSelectedModelName());
            submit();
        });
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

        if (arBundle.containsKey(SCALE_KEY)) {
            args.putString("modelName", arBundle.getString("modelName"));
            args.putFloatArray(SCALE_KEY, arBundle.getFloatArray(SCALE_KEY));
        }

        if (arBundle.containsKey(COLOR_KEY)) {
            args.putInt(COLOR_KEY, arBundle.getInt(COLOR_KEY));
        }

        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        PostItemListener listener = (PostItemListener) getTargetFragment();
        listener.onSubmit(args);
        dismiss();
    }

    private void setArVisibility(final int visibility) {
        mRootView.findViewById(R.id.model_gallery).setVisibility(visibility);
        mRootView.findViewById(R.id.ar_support_button).setVisibility(visibility);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (data.hasExtra(SCALE_KEY)) {
                float[] scale = data.getFloatArrayExtra(SCALE_KEY);
                arBundle.putFloatArray(SCALE_KEY, scale);
            }

            if (data.hasExtra(COLOR_KEY)) {
                arBundle.putInt(COLOR_KEY, data.getIntExtra(COLOR_KEY, 0));
            }
        }
    }
}
