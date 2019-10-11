package edu.uci.cs297p.arfurniture.item;

import com.google.ar.sceneform.math.Vector3;

public interface ARSupportItem {
    boolean isPreviewable();

    String getModelName();

    Vector3 getSizeVector();
}
