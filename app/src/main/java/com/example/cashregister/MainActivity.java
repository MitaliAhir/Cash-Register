package com.example.cashregister;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cashregister.Adapter.ProductAdapter;
import com.example.cashregister.model.Product;
import com.example.cashregister.model.PurchaseHistory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView selectedProductTextView, quantityTextView, totalTextView;
    private Button buyButton, managerButton;
    private ListView productListView;
    private String selectedProductName = "";
    private int desiredQuantity = 0;
    private List<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
        loadProductList();
        setupProductAdapter();
        setupClickListeners();
    }

    private void setupUI() {
        selectedProductTextView = findViewById(R.id.product_name);
        quantityTextView = findViewById(R.id.quantity);
        totalTextView = findViewById(R.id.total);
        buyButton = findViewById(R.id.btnBuy);
        managerButton = findViewById(R.id.btnManager);
        productListView = findViewById(R.id.listViewProducts);
    }

    private void loadProductList() {
        productList = SharedPrefsHelper.loadProductList(this);
    }

    private void setupProductAdapter() {
        productAdapter = new ProductAdapter(this, productList, position -> {
            Product selectedProduct = productList.get(position);
            selectedProductName = selectedProduct.getName();
            updateSelectedProductUI();
        });
        productListView.setAdapter(productAdapter);
    }

    private void setupClickListeners() {
        productListView.setOnItemClickListener((parentView, selectedItemView, position, id) -> {
            Product selectedProduct = productList.get(position);
            selectedProductName = selectedProduct.getName();
            updateSelectedProductUI();
        });

        setUpNumberButtonListeners(R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9);
        findViewById(R.id.btnC).setOnClickListener(v -> clearUI());

        buyButton.setOnClickListener(v -> handleBuyButtonClick());

        managerButton.setOnClickListener(view -> navigateToManagerActivity());
    }

    private void updateSelectedProductUI() {
        selectedProductTextView.setText("Selected Product: " + selectedProductName);
    }

    private void setUpNumberButtonListeners(int... buttonIds) {
        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(v -> {
                Button button = (Button) v;
                handleNumberButtonClick(button.getText().toString());
            });
        }
    }

    private void handleNumberButtonClick(String number) {
        Product selectedProduct = getProductByName(selectedProductName);
        if (selectedProductName.isEmpty() || selectedProduct == null) {
            Toast.makeText(this, "Please select the product you want to purchase.", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentQuantityText = quantityTextView.getText().toString();
        if (currentQuantityText.isEmpty()) {
            quantityTextView.setText(number);
        } else {
            quantityTextView.append(number);
        }
        desiredQuantity = Integer.parseInt(quantityTextView.getText().toString());
        updateTotal(selectedProduct);
    }

    private void handleBuyButtonClick() {
        if (desiredQuantity <= 0 || selectedProductName.isEmpty()) {
            Toast.makeText(this, "Please select a product and a quantity.", Toast.LENGTH_SHORT).show();
            return;
        }

        Product selectedProduct = getProductByName(selectedProductName);
        if (selectedProduct == null || desiredQuantity > selectedProduct.getQuantity()) {
            Toast.makeText(this, "Sorry, not enough quantity in stock.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Deduct quantity from stock
        selectedProduct.setQuantity(selectedProduct.getQuantity() - desiredQuantity);
        // Save updated product list to SharedPreferences
        SharedPrefsHelper.saveProductList(this, productList);
        updateTotal(selectedProduct);
        productAdapter.notifyDataSetChanged();
        // Save purchase to SharedPreferences
        savePurchase(selectedProductName, desiredQuantity, selectedProduct.getPrice() * desiredQuantity);
        clearUI();
        Toast.makeText(this, "Thank you for your purchase.", Toast.LENGTH_SHORT).show();
    }

    private void navigateToManagerActivity() {
        Intent intent = new Intent(MainActivity.this, ManagerActivity.class);
        startActivity(intent);
    }

    private Product getProductByName(String name) {
        for (Product product : productList) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    private void updateTotal(Product product) {
        double totalPrice = product.getTotalPrice(desiredQuantity);
        totalTextView.setText("Total: " + String.format("%.2f", totalPrice));
    }

    private void clearUI() {
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