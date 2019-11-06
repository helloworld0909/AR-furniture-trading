package edu.uci.cs297p.arfurniture.item;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public abstract class BaseItem implements Serializable {
    private long itemId;
    private String name;
    private String description;
    private float price;
    private long sellerId;
    private Timestamp addedTimestamp;
    private List<String> imageUrls;

    public BaseItem(long itemId, String name, String description, float price, long sellerId, Timestamp addedTimestamp, List<String> imageUrls) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.sellerId = sellerId;
        this.addedTimestamp = addedTimestamp;
        this.imageUrls = imageUrls;
    }

    public long getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public long getSellerId() {
        return sellerId;
    }

    public Timestamp getAddedTimestamp() {
        return addedTimestamp;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    @NonNull
    @Override
    public String toString() {
        return itemId + "_" + name + "_" + sellerId;
    }

    public static String formatPrice(Locale locale, float price) {
        return NumberFormat.getCurrencyInstance(locale).format(price);
    }

    public String getStringPrice() {
        return formatPrice(Locale.US, price);
    }
}
