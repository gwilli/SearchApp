package com.limerobotsoftware.searchapp.data;

import android.content.Context;

import com.limerobotsoftware.searchapp.data.dao.ProductDao;
import com.limerobotsoftware.searchapp.data.entity.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class ProductRepository {
    private ProductDao productDao;
    private LiveData<List<Product>> products;

    public ProductRepository(@NonNull Context context) {
        MyRoomDatabase database = MyRoomDatabase.getDatabase(context);
        productDao = database.productDao();
        products = productDao.searchProducts("");
    }

    public void searchProducts(String searchString) {
        //products = productDao.searchProducts("%" + searchString + "%");
        products = productDao.betterSearchProducts(searchString + "*");
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }
}
