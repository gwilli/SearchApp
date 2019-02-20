package com.limerobotsoftware.searchapp.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "product")
public class Product {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "upc")
    private String upc;
    @ColumnInfo(name = "brand_name")
    private String brandName;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "size")
    private String size;
    @ColumnInfo(name = "ingredients")
    private String ingredients;
    @ColumnInfo(name = "image_url")
    private String imageUrl;

    public Product(@NonNull String upc, String brandName, String name, String size, String ingredients, String imageUrl) {
        this.upc = upc;
        this.brandName = brandName;
        this.name = name;
        this.size = size;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
    }

    @NonNull
    public String getUpc() {
        return upc;
    }

    public void setUpc(@NonNull String upc) {
        this.upc = upc;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Product{" +
                "upc='" + upc + '\'' +
                ", brandName='" + brandName + '\'' +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
