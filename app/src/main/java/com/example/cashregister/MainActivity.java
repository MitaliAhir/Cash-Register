package com.example.cashregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView selectedProductTextView, quantityTextView, totalTextView;
    private Button buyButton, managerButton;
    private ListView productListView;
    private String selectedProductName = "";
    private int desiredQuantity = 0;
    private  ArrayList<Product> productList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //UI components initialization
        selectedProductTextView = findViewById(R.id.product_name);
        quantityTextView = findViewById(R.id.quantity);
        totalTextView = findViewById(R.id.total);
        buyButton = findViewById(R.id.btnBuy);
        managerButton = findViewById(R.id.btnManager);
        productListView = findViewById(R.id.listViewProducts);

       loadProductListFromPreferences();
        SharedPrefsHelper.loadProductList(this);

        // Use the custom ProductAdapter to bind product data to the ListView
        ProductAdapter adapter = new ProductAdapter(this, productList, position -> {
            // Get the selected product
            Product selectedProduct = productList.get(position);
            selectedProductName = selectedProduct.getName();
            // Update the UI with the selected product's details
            selectedProductTextView.setText("Selected Product: " + selectedProductName);
        });
        productListView.setAdapter(adapter);

        // Set an item click listener for selecting a product
        productListView.setOnItemClickListener((parentView, selectedItemView, position, id) -> {
            // Get the selected product
            Product selectedProduct = productList.get(position);
            selectedProductName = selectedProduct.getName();
            // Update the UI with the selected product's details
            selectedProductTextView.setText("Selected Product: " + selectedProductName);
        });
        setUpButtonListeners(R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9);
        findViewById(R.id.btnC).setOnClickListener(v -> clear());

        // Set on click listener for buy button
        buyButton.setOnClickListener(v -> {
            if(desiredQuantity > 0 && !selectedProductName.isEmpty()){
                Product selectedProduct = getProductByName(selectedProductName);
                    if (selectedProduct != null && desiredQuantity <= selectedProduct.getQuantity()) {
                        // Deduct quantity from stock
                        selectedProduct.setQuantity(selectedProduct.getQuantity() - desiredQuantity);
                        updateTotal(selectedProduct);
                        clear();
                        Toast.makeText(this, "Thank you for your purchase.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Sorry not enough quantity in stock.", Toast.LENGTH_SHORT).show();
                    }
            }else {
                Toast.makeText(this, "Please select a quantity.", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigate to Manager panel
        managerButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ManagerActivity.class);
            startActivity(intent);
        });
    }

    // Set up same listener for all number buttons
    private void setUpButtonListeners(int... buttonIds) {
        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(v -> {
                Button button = (Button) v;
                Product selectedProduct = getProductByName(selectedProductName);
                // Update the quantity for the selected product
                if (!selectedProductName.isEmpty() && selectedProduct != null) {
                    String btnText = quantityTextView.getDisplay().toString();
                    //desiredQuantity = desiredQuantity + Integer.parseInt(button.getText().toString());
                    if(!btnText.isEmpty()){
                        quantityTextView.append(button.getText().toString());
                        desiredQuantity = Integer.parseInt(quantityTextView.getText().toString());
                    }else{
                        quantityTextView.setText(button.getText().toString());
                        desiredQuantity = Integer.parseInt(quantityTextView.getText().toString());
                    }
                    updateTotal(selectedProduct);
                }else {
                    Toast.makeText(this, "Please select the product you want to purchase.", Toast.LENGTH_SHORT).show();
                }

            });
        }
    }

    // Get product by name
    private Product getProductByName(String name) {
        for (Product product : productList) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    void updateTotal(Product product){
            double totalPrice = product.getTotalPrice(desiredQuantity);
            totalTextView.setText("Total: " + String.format("%.2f", totalPrice));
    }

    private void clear() {
        selectedProductTextView.setText("");
        quantityTextView.setText("");
        totalTextView.setText("");
        desiredQuantity = 0;
        selectedProductName = "";
    }
    // Load the product list from SharedPreferences
    private void loadProductListFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("POSApp", MODE_PRIVATE);
        String json = sharedPreferences.getString("productList", null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Product>>(){}.getType();
            productList = gson.fromJson(json, type);  // Deserialize the JSON back to a list
        } else {
            // Default products if no data is found in SharedPreferences
            productList.add(new Product("Product A", 10, 200.67));
            productList.add(new Product("Product B", 22, 95.99));
            productList.add(new Product("Product C", 38, 57.99));
            productList.add(new Product("Product D", 40, 42.88));
            productList.add(new Product("Product E", 56, 49.99));
        }
        // Once the list is updated (or loaded from SharedPreferences), refresh the ListView
        ProductAdapter adapter = new ProductAdapter(this, productList, position -> {
            // Handle item click
        });
        productListView.setAdapter(adapter);  // Re-set the adapter to reflect the changes
        adapter.notifyDataSetChanged();  // This will refresh the ListView with the updated list
    }

}