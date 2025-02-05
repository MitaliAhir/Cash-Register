package com.example.cashregister;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class RestockActivity extends AppCompatActivity {

    private static final int NO_PRODUCT_SELECTED = -1;
    private ListView productListView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private EditText editTextNewQuantity;
    private Button btnOk, btnCancel;
    private int selectedProductIndex = NO_PRODUCT_SELECTED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restock);

        setupUI();
        loadProductList();
        setupAdapter();
        setupListeners();
    }

    private void setupUI() {
        productListView = findViewById(R.id.productListView);
        editTextNewQuantity = findViewById(R.id.editTextNewQuantity);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void loadProductList() {
        // Load product list from SharedPreferences
        productList = SharedPrefsHelper.loadProductList(this);
    }

    private void setupAdapter() {
        // Set up the adapter with a listener for item selection
        productAdapter = new ProductAdapter(this, productList, position -> {
            // Mark the selected product index when clicked
            selectedProductIndex = position;
            Toast.makeText(RestockActivity.this, "Selected: " + productList.get(position).getName(), Toast.LENGTH_SHORT).show();
        });
        // Set the adapter to the ListView
        productListView.setAdapter(productAdapter);
    }

    private void setupListeners() {
        // Handle OK button click
        btnOk.setOnClickListener(v -> handleOkButtonClick());

        btnCancel.setOnClickListener(v -> finish());
    }

    private void handleOkButtonClick() {
        String newQuantityStr = editTextNewQuantity.getText().toString();
        if (!isValidInput(newQuantityStr)) {
            return;
        }
        int newQuantity = Integer.parseInt(newQuantityStr);
        updateProductQuantity(newQuantity);

    }

    private boolean isValidInput(String newQuantityStr) {
        if (selectedProductIndex == NO_PRODUCT_SELECTED) {
            Toast.makeText(RestockActivity.this, "Please select a product to restock.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (newQuantityStr.isEmpty()) {
            Toast.makeText(RestockActivity.this, "Please enter a quantity.", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            int newQuantity = Integer.parseInt(newQuantityStr);
            if (newQuantity <= 0) {
                Toast.makeText(RestockActivity.this, "Please enter a quantity greater than zero.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(RestockActivity.this, "Please enter a valid number for the quantity.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateProductQuantity(int newQuantity) {
        Product selectedProduct = productList.get(selectedProductIndex);
        selectedProduct.setQuantity(selectedProduct.getQuantity() + newQuantity);
        SharedPrefsHelper.saveProductList(RestockActivity.this, productList);
        productAdapter.notifyDataSetChanged();
        Toast.makeText(RestockActivity.this, "Product restocked successfully", Toast.LENGTH_SHORT).show();
    }
}