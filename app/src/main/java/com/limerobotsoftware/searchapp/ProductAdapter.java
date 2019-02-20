package com.limerobotsoftware.searchapp;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.limerobotsoftware.searchapp.data.entity.Product;
import com.limerobotsoftware.searchapp.databinding.ItemProductListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private Context context;

    public ProductAdapter(Context context) {
        this.context = context;
        products = new ArrayList<>();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_product_list, parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.product = product;
        String name = product.getBrandName() == null ? product.getName() : product.getBrandName() + " " + product.getName();
        holder.binding.productName.setText(name);
        holder.binding.productSize.setText(product.getSize());
        holder.binding.productUpc.setText(product.getUpc());
        Picasso.with(context).load(product.getImageUrl()).placeholder(R.drawable.ic_no_photo).noFade().into(holder.binding.productImage);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public Product product;
        final ItemProductListBinding binding;

        public ProductViewHolder(ItemProductListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
