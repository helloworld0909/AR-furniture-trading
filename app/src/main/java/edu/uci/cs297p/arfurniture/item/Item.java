package edu.uci.cs297p.arfurniture.item;

import androidx.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class Item implements Serializable {
    private String itemId;
    private String name;
    private String description;
    private String modelName;
    private String price;
    private List<String> imageUrls;
    private boolean previewable;

    @Category
    private int category;

    @IntDef({TABLE, CHAIR, BED, SOFA, STORAGE, OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Category {
    }

    public static final int TABLE = 0;
    public static final int CHAIR = 1;
    public static final int BED = 2;
    public static final int SOFA = 3;
    public static final int STORAGE = 4;
    public static final int OTHER = 5;

    private static final String[] CATEGORY_STRINGS = new String[]{"table", "chair", "bed", "sofa", "storage", "other"};

    public Item(String itemId, String name, String description, String price, @Category int category) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
        previewable = true;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public String getModelName() {
        return modelName;
    }

    @Category
    public int getCategory() {
        return category;
    }

    public boolean isPreviewable() {
        return previewable;
    }

    public static String categoryToStr(@Category int category) {
        return CATEGORY_STRINGS[category];
    }
}
