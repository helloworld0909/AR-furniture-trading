package edu.uci.cs297p.arfurniture.seller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.uci.cs297p.arfurniture.R;

public class PostItemFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_item, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = view.findViewById(R.id.submit_button);
        button.setOnClickListener((clickedView) -> submit(view));
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    // Call this method to send the data back to the parent fragment
    public void submit(View rootView) {

        Bundle args = new Bundle();
        args.putString("name", ((TextView) rootView.findViewById(R.id.item_name_text)).getText().toString());
        args.putString("description", ((TextView) rootView.findViewById(R.id.item_description_text)).getText().toString());
        args.putString("price", ((TextView) rootView.findViewById(R.id.item_price_text)).getText().toString());

        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        PostItemListener listener = (PostItemListener) getTargetFragment();
        listener.onSubmit(args);
        dismiss();
    }

}
