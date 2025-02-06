package com.example.cashregister;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cashregister.Adapter.ProductAdapter;
import com.example.cashregister.model.Product;

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
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restock);

        setupUI();
        setupData();
        setupListeners();
    }

    private void setupUI() {
        toolbar = findViewById(R.id.toolbar);
        productListView = findViewById(R.id.productListView);
        editTextNewQuantity = findViewById(R.id.editTextNewQuantity);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupData() {
        productList = SharedPrefsHelper.loadProductList(this);
        productAdapter = new ProductAdapter(this, productList, position -> {
            selectedProductIndex = position;
            showToast("Selected: " + productList.get(position).getName());
        });
        productListView.setAdapter(productAdapter);
    }

    private void setupListeners() {
        btnOk.setOnClickListener(v -> handleOkButtonClick());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void handleOkButtonClick() {
        String quantityString = editTextNewQuantity.getText().toString();
        if (!isValidInput(quantityString)) {
            return;
        }
        int quantity = Integer.parseInt(quantityString);
        updateProductQuantity(quantity);
        setResult(Activity.RESULT_OK);
        finish();
    }

    private boolean isValidInput(String quantityString) {
        if (!isProductSelected()) return false;
        if (!isQuantityEntered(quantityString)) return false;
        return isQuantityPositive(quantityString);
    }

    private boolean isProductSelected() {
        if (selectedProductIndex == NO_PRODUCT_SELECTED) {
            showToast("Please select a product to restock.");
            return false;
        }
        return true;
    }

    private boolean isQuantityEntered(String quantityString) {
        if (quantityString.isEmpty()) {
            showToast("Please enter a quantity.");
            return false;
        }
        return true;
    }

    private boolean isQuantityPositive(String quantityString) {
        try {
            int quantity = Integer.parseInt(quantityString);
            if (quantity <= 0) {
                showToast("Please enter a quantity greater than zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            showToast("Please enter a valid number for the quantity.");
            return false;
        }
        return true;
    }

    private void updateProductQuantity(int quantity) {
        Product selectedProduct = productList.get(selectedProductIndex);
        selectedProduct.setQuantity(selectedProduct.getQuantity() + quantity);
        SharedPrefsHelper.saveProductList(RestockActivity.this, productList);
        updateUI();
    }

    private void updateUI() {
        productAdapter.notifyDataSetChanged();
        showToast("Product restocked successfully");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}