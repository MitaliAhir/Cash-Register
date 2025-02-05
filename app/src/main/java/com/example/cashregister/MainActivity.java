package com.example.cashregister;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView selectedProductTextView, quantityTextView, totalTextView;
    private Button buyButton, managerButton;
    private ListView productListView;
    private String selectedProductName = "";
    private int desiredQuantity = 0;
    private ArrayList<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter; // To reference and update the adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        selectedProductTextView = findViewById(R.id.product_name);
        quantityTextView = findViewById(R.id.quantity);
        totalTextView = findViewById(R.id.total);
        buyButton = findViewById(R.id.btnBuy);
        managerButton = findViewById(R.id.btnManager);
        productListView = findViewById(R.id.listViewProducts);

        // Load product list from SharedPreferences using SharedPrefsHelper
        productList = SharedPrefsHelper.loadProductList(this);

        // Set up the custom adapter for the product list
        productAdapter = new ProductAdapter(this, productList, position -> {
            // Get the selected product
            Product selectedProduct = productList.get(position);
            selectedProductName = selectedProduct.getName();
            // Update the UI with the selected product's details
            selectedProductTextView.setText("Selected Product: " + selectedProductName);
        });

        productListView.setAdapter(productAdapter);

        // Set an item click listener for selecting a product
        productListView.setOnItemClickListener((parentView, selectedItemView, position, id) -> {
            // Get the selected product
            Product selectedProduct = productList.get(position);
            selectedProductName = selectedProduct.getName();
            // Update the UI with the selected product's details
            selectedProductTextView.setText("Selected Product: " + selectedProductName);
        });

        // Set up number buttons (0 to 9)
        setUpButtonListeners(R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9);
        findViewById(R.id.btnC).setOnClickListener(v -> clear());

        // Set onClickListener for Buy button
        buyButton.setOnClickListener(v -> {
            if (desiredQuantity > 0 && !selectedProductName.isEmpty()) {
                Product selectedProduct = getProductByName(selectedProductName);
                if (selectedProduct != null && desiredQuantity <= selectedProduct.getQuantity()) {
                    // Deduct quantity from stock
                    selectedProduct.setQuantity(selectedProduct.getQuantity() - desiredQuantity);
                    // Save updated product list to SharedPreferences
                    SharedPrefsHelper.saveProductList(this, productList);
                    updateTotal(selectedProduct);
                    productAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the ListView
                    // Save purchase to SharedPreferences
                    savePurchase(selectedProductName, desiredQuantity, selectedProduct.getPrice() * desiredQuantity);
                    clear();
                    Toast.makeText(this, "Thank you for your purchase.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Sorry, not enough quantity in stock.", Toast.LENGTH_SHORT).show();
                }
            } else {
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
                    String btnText = quantityTextView.getText().toString();
                    if (!btnText.isEmpty()) {
                        quantityTextView.append(button.getText().toString());
                        desiredQuantity = Integer.parseInt(quantityTextView.getText().toString());
                    } else {
                        quantityTextView.setText(button.getText().toString());
                        desiredQuantity = Integer.parseInt(quantityTextView.getText().toString());
                    }
                    updateTotal(selectedProduct);
                } else {
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

    // Update the total price for the selected product
    void updateTotal(Product product) {
        double totalPrice = product.getTotalPrice(desiredQuantity);
        totalTextView.setText("Total: " + String.format("%.2f", totalPrice));
    }

    // Clear the UI
    private void clear() {
        selectedProductTextView.setText("");
        quantityTextView.setText("");
        totalTextView.setText("");
        desiredQuantity = 0;
        selectedProductName = "";
    }

    private void savePurchase(String productName, int quantity, double totalPrice) {
        PurchaseHistory purchase = new PurchaseHistory(productName, quantity, totalPrice, new Date());
        SharedPrefsHelper.savePurchase(this, purchase);
    }
}
