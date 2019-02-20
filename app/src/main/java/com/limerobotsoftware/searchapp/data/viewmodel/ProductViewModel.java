package com.limerobotsoftware.searchapp.data.viewmodel;

import android.app.Application;

import com.limerobotsoftware.searchapp.data.ProductRepository;
import com.limerobotsoftware.searchapp.data.entity.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepository productRepository;
    private LiveData<List<Product>> products;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
    }

    public void searchProduct(String searchString) {
        productRepository.searchProducts(searchString);
        products = productRepository.getProducts();
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }
}
