package edu.uci.cs297p.arfurniture.seller;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;

import edu.uci.cs297p.arfurniture.R;

public class SceneViewActivity extends AppCompatActivity {

    private SceneView mSceneView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sceneview);
        mSceneView = findViewById(R.id.scene_view);

        Uri uri = getIntent().getExtras().getParcelable("uri");
        createScene(uri);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mSceneView.resume();
        } catch (CameraNotAvailableException e) {
            Log.e("SceneViewActivity", e.getMessage());
        }
    }

    private void createScene(Uri uri) {
        ModelRenderable.builder()
                .setSource(getApplicationContext(), uri)
                .build()
                .thenAccept(this::onRenderableLoaded)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_LONG);
                    toast.show();
                    return null;
                });
    }

    private void onRenderableLoaded(Renderable model) {
        Node modelNode = new Node();
        modelNode.setRenderable(model);
        mSceneView.getScene().addChild(modelNode);
        modelNode.setLocalPosition(new Vector3(0, 0, -2));
    }
}
