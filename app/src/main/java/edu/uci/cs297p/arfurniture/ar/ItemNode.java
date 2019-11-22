package edu.uci.cs297p.arfurniture.ar;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import edu.uci.cs297p.arfurniture.R;
import edu.uci.cs297p.arfurniture.seller.PostItemFragment;

public class ItemNode extends TransformableNode implements Node.OnTapListener {


    private View menuView;

    private final AppCompatActivity activity;

    private Integer color;

    public ItemNode(AppCompatActivity activity, View menuView, TransformationSystem transformationSystem) {
        super(transformationSystem);
        this.activity = activity;
        this.menuView = menuView;
    }

    @Override
    public void onActivate() {
        super.onActivate();

        if (getScene() == null) {
            throw new IllegalStateException("Scene is null!");
        }

        Button color_button = menuView.findViewById(R.id.color_button);
        color_button.setOnClickListener(view -> showColorPicker());

        Button ok_button = menuView.findViewById(R.id.ok_button);
        ok_button.setOnClickListener(view -> {
            Vector3 scale = getTranslationController().getTransformableNode().getLocalScale();
            Toast.makeText(activity, "Scale: " + scale.toString(), Toast.LENGTH_LONG).show();

            Intent intentWithResult = new Intent();
            intentWithResult.putExtra(PostItemFragment.SCALE_KEY, new float[]{scale.x, scale.y, scale.z});
            if (color != null) {
                intentWithResult.putExtra(PostItemFragment.COLOR_KEY, color);
            }
            activity.setResult(PostItemFragment.AR_REQUEST_CODE, intentWithResult);
            activity.finish();
        });

        Button cancel_button = menuView.findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(view -> {
            getParent().removeChild(this);
            color = null;
            menuView.setVisibility(View.GONE);
        });
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);
    }

    private void showColorPicker() {
        new ColorPickerDialog.Builder(activity)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(activity.getString(android.R.string.ok),
                        new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {

                                Toast.makeText(activity, "Color: " + envelope.toString(), Toast.LENGTH_LONG).show();

                                MaterialFactory.makeOpaqueWithColor(activity, new Color(envelope.getColor())).thenAccept(material -> {
                                    //TODO: Allow user to adjust other material properties
                                    getRenderable().setMaterial(material);
                                });

                                color = envelope.getColor();

                            }
                        })
                .setNegativeButton(activity.getString(android.R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                .attachAlphaSlideBar(true) // default is true. If false, do not show the AlphaSlideBar.
                .attachBrightnessSlideBar(true)  // default is true. If false, do not show the BrightnessSlideBar.
                .show();
    }
}
