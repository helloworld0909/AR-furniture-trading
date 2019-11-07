package edu.uci.cs297p.arfurniture.item;

import java.io.Serializable;
import java.util.List;

public class Item implements Serializable, ARSupportItem {
    private String itemId;
    private String name;
    private String description;
    private String modelName;
    private String price;
    private List<String> imageUrls;

    public Item(String itemId, String name, String description, String modelName, String price, List<String> imageUrls) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrls = imageUrls;
        this.modelName = modelName;
    }

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() { return price; }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    @Override
    public boolean isPreviewable() {
        return true;
    }

    public String getModelName() { return modelName; }

    @Override
    public Float[] getSizeVector() {
        return new Float[0];
    }
}
