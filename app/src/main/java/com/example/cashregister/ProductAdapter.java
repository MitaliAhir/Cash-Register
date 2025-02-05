package com.example.cashregister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {
    private final OnProductClickListener listener;
    private List<Product> productList; // Make this mutable

    public ProductAdapter(Context context, List<Product> productList, OnProductClickListener listener) {
        super(context, 0, productList);
        this.productList = productList;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Check if the recycled view (convertView) is null
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_product, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current product object
        Product currentProduct = productList.get(position);

        // Bind the data to the views in the layout
        bindData(holder, currentProduct);

        // Set up the click listener to notify when a product is selected
        setupClickListener(convertView, position);

        // Return the modified view
        return convertView;
    }

    private void bindData(ViewHolder holder, Product product) {
        holder.nameTextView.setText(product.getName());
        holder.quantityTextView.setText("Qty: " + product.getQuantity());
        holder.priceTextView.setText("$" + product.getPrice());
    }

    private void setupClickListener(View convertView, int position) {
        convertView.setOnClickListener(v -> listener.onProductClick(position));
    }

    // ViewHolder class to hold references to the views in the item layout
    private static class ViewHolder {
        TextView nameTextView;
        TextView quantityTextView;
        TextView priceTextView;

        ViewHolder(View itemView) {
            nameTextView = itemView.findViewById(R.id.product_name);
            quantityTextView = itemView.findViewById(R.id.product_quantity);
            priceTextView = itemView.findViewById(R.id.product_price);
        }
    }

    // Interface to handle product click events
    public interface OnProductClickListener {
        void onProductClick(int position);
    }

}