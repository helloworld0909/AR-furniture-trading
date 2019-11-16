package edu.uci.cs297p.arfurniture.ar;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class ItemNode extends TransformableNode implements Node.OnTapListener {


    private View menuView;
    private final Context context;

    public ItemNode(Context context, View menuView, TransformationSystem transformationSystem) {
        super(transformationSystem);
        this.context = context;
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
            Toast.makeText(context, "Scale: " + scale.toString(), Toast.LENGTH_LONG).show();
        });

        Button cancel_button = menuView.findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(view -> {
            getParent().removeChild(this);
            menuView.setVisibility(View.GONE);
        });
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);
    }

    private void showColorPicker() {
        new ColorPickerDialog.Builder(context)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(context.getString(android.R.string.ok),
                        new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {

                                Toast.makeText(context, "Color: " + envelope.toString(), Toast.LENGTH_LONG).show();

                                MaterialFactory.makeOpaqueWithColor(context, new Color(envelope.getColor())).thenAccept(material -> {
                                    //TODO: Allow user to adjust other material properties
                                    getRenderable().setMaterial(material);
                                });

                            }
                        })
                .setNegativeButton(context.getString(android.R.string.cancel),
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
