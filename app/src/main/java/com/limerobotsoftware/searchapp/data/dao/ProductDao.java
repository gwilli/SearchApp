package com.limerobotsoftware.searchapp.data.dao;

import com.limerobotsoftware.searchapp.data.entity.Product;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product[] products);

    @Delete
    void delete(Product[] products);

    @Query("delete from product")
    void deleteAll();

    @Query("select * from product where name like :query or brand_name like :query or ingredients like :query or upc like :query")
    LiveData<List<Product>> searchProducts(String query);

    @Query("select product.* from product join product_fts on product.'rowid' = product_fts.'rowid' where product_fts match :query")
    LiveData<List<Product>> betterSearchProducts(String query);
}
