package edu.uci.cs297p.arfurniture;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import edu.uci.cs297p.arfurniture.ar.ItemNode;
import edu.uci.cs297p.arfurniture.item.Item;


public class ARActivity extends AppCompatActivity {
    private static final String TAG = ARActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ModelRenderable modelRenderable;
    private View mMenuView;


    @IntDef({BUYER, SELLER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flag {
    }

    public static final int BUYER = 0;
    public static final int SELLER = 1;

    public static final String ITEM_KEY = "item";
    public static final String URI_KEY = "uri";
    public static final String FLAG_KEY = "flag";

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.ar);
        mMenuView = findViewById(R.id.item_menu);
        mMenuView.setVisibility(View.GONE);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        // Launch buyer AR by default
        @Flag int flag = getIntent().getIntExtra(FLAG_KEY, BUYER);
        if (flag == SELLER) {
            setupSeller();
        } else {
            setupBuyer();
        }
    }

    private void setupSeller() {

        Uri uri = getIntent().getParcelableExtra(URI_KEY);

        ModelRenderable.builder()
                .setSource(this, uri)
                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load renderable " + uri.getLastPathSegment(), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (modelRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable and add it to the anchor.

                    ItemNode node = new ItemNode(this, mMenuView, arFragment.getTransformationSystem());
                    node.setParent(anchorNode);
                    node.setRenderable(modelRenderable);
                    node.select();

                    mMenuView.setVisibility(View.VISIBLE);
                });
    }

    private void setupBuyer() {
        Item item = (Item) getIntent().getSerializableExtra(ITEM_KEY);
        if (item.getModelName() == null) {
            throw new IllegalArgumentException("Item does not support AR");
        }

        String modelName = item.getModelName();

        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(this, Uri.parse("file:///android_asset/" + Item.categoryToStr(item.getCategory()) + "/" + modelName))
                .build()
                .thenAccept(renderable -> {
                    modelRenderable = renderable;
                    if (item.getColor() != null) {
                        int color = item.getColor();
                        MaterialFactory.makeOpaqueWithColor(this, new Color(color)).thenAccept(material -> modelRenderable.setMaterial(material));
                    }
                })
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load renderable " + modelName, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (modelRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable and add it to the anchor.

                    TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
                    node.setParent(anchorNode);
                    node.setRenderable(modelRenderable);

                    List<Double> scale = item.getScale();
                    if (scale != null) {
                        node.setLocalScale(new Vector3(scale.get(0).floatValue(), scale.get(1).floatValue(), scale.get(2).floatValue()));
                    }
                    node.getScaleController().setEnabled(false);
                    node.select();
                });
    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
}
