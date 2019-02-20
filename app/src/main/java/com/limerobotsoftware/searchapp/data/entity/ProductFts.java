package com.limerobotsoftware.searchapp.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;

@Fts4(contentEntity = Product.class)
@Entity(tableName = "product_fts")
public class ProductFts {

    @ColumnInfo(name = "upc")
    private String upc;
    @ColumnInfo(name = "brand_name")
    private String brandName;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "ingredients")
    private String ingredients;

    public ProductFts(String upc, String brandName, String name, String ingredients) {
        this.upc = upc;
        this.brandName = brandName;
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
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

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
