package com.example.cashregister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> productList;
    private OnProductClickListener listener;

     public ProductAdapter(Context context, List<Product> productList, OnProductClickListener listener) {
        super(context, 0, productList);  // Passing 0 as resource to use the custom layout
        this.context = context;
        this.productList = productList;
         this.listener = listener;
    }

    // This method is called for every item in the ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the recycled view (convertView) is null
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_product, parent, false);
        }

        // Get the current product object
        Product currentProduct = productList.get(position);

        // Bind the data to the views in the layout
        TextView nameTextView = convertView.findViewById(R.id.product_name);
        TextView quantityTextView = convertView.findViewById(R.id.product_quantity);
        TextView priceTextView = convertView.findViewById(R.id.product_price);

        nameTextView.setText(currentProduct.getName());
        quantityTextView.setText("Qty: " + currentProduct.getQuantity());
        priceTextView.setText("$" + currentProduct.getPrice());

        // Set up the click listener to notify when a product is selected
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the listener when an item is clicked
                listener.onProductClick(position);
            }
        });

        // Return the modified view
        return convertView;

    }
    // Interface to handle product click events
    public interface OnProductClickListener {
        void onProductClick(int position);
    }
}
