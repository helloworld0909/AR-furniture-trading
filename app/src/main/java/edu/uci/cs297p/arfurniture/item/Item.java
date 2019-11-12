package edu.uci.cs297p.arfurniture.item;

import java.io.Serializable;
import java.util.List;

public class Item implements Serializable{
    private String itemId;
    private String category;
    private String title;
    private String description;
    private String modelName;
    private String price;
    private List<String> imageUrls;
    private boolean previewable;

    public Item(String title, String description, String price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
        previewable = true;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setCategory(String category) { this.category = category; }

    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getName() { return title; }

    public String getDescription() { return description; }

    public String getPrice() { return price; }

    public List<String> getImageUrls() { return imageUrls; }

    public String getModelName() {return modelName; }

    public boolean isPreviewable() { return previewable; }
}
