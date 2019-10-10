package edu.uci.cs297p.arfurniture.buyer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.uci.cs297p.arfurniture.ARActivity;
import edu.uci.cs297p.arfurniture.R;


public class BuyerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.buyer, viewGroup, false);

        Button button1 = rootView.findViewById(R.id.button1);
        button1.setOnClickListener((View view) -> {
            Intent myIntent = createIntent("chair_0");
            startActivity(myIntent);
        });

        Button button2 = rootView.findViewById(R.id.button2);
        button2.setOnClickListener((View view) -> {
            Intent myIntent = createIntent("table_0");
            startActivity(myIntent);
        });

        return rootView;
    }

    private Intent createIntent(String modelName) {
        Bundle params = new Bundle();
        params.putString(ARActivity.MODEL_NAME_KEY, modelName);
        Intent intent = new Intent(getActivity(), ARActivity.class);
        intent.putExtras(params);
        return intent;
    }
}