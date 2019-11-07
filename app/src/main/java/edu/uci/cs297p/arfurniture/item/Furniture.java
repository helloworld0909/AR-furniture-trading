//package edu.uci.cs297p.arfurniture.item;
//
//import androidx.annotation.IntDef;
//
//import java.io.Serializable;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.sql.Timestamp;
//import java.util.List;
//
//public class Furniture implements ARSupportItem, Serializable {
//
//    private @Category
//    int category;
//    private boolean previewable;
//    private String modelName;
//    private Float[] sizeVector;
//    private int color;
//
//    @IntDef({TABLE, CHAIR, BED, SOFA, STORAGE, OTHER})
//    @Retention(RetentionPolicy.SOURCE)
//    public @interface Category {
//    }
//
//    public static final int TABLE = 0;
//    public static final int CHAIR = 1;
//    public static final int BED = 2;
//    public static final int SOFA = 3;
//    public static final int STORAGE = 4;
//    public static final int OTHER = Integer.MAX_VALUE;
//
//    public Furniture(long itemId, String name, String description, float price, long sellerId, Timestamp addedTimestamp, List<String> imageUrls, @Category int category) {
//        super(itemId, name, description, price, sellerId, addedTimestamp, imageUrls);
//        this.previewable = false;
//        this.category = category;
//    }
//
//    public Furniture(long itemId, String name, String description, float price, long sellerId, Timestamp addedTimestamp, List<String> imageUrls, @Category int category, String modelName, float length, float width, float height, int color) {
//        this(itemId, name, description, price, sellerId, addedTimestamp, imageUrls, category);
//        setARData(modelName, length, width, height, color);
//    }
//
//    @Override
//    public boolean isPreviewable() {
//        return previewable;
//    }
//
//    public void setARData(String modelName, float length, float width, float height, int color) {
//        this.modelName = modelName;
//        this.sizeVector = new Float[]{length, width, height};
//        this.color = color;
//        this.previewable = true;
//    }
//
//    @Override
//    public String getModelName() {
//        return modelName;
//    }
//
//    @Override
//    public Float[] getSizeVector() {
//        return sizeVector;
//    }
//}
