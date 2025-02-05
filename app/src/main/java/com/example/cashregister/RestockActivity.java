package com.example.cashregister;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RestockActivity extends AppCompatActivity {
    private ListView productListView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private EditText editTextNewQuantity;
    private Button btnOk, btnCancel;
    private int selectedProductIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restock);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productListView = findViewById(R.id.productListView);
        editTextNewQuantity = findViewById(R.id.editTextNewQuantity);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);

        // Load product list from SharedPreferences
        productList = SharedPrefsHelper.loadProductList(this);

        // Set up the adapter with a listener for item selection
        productAdapter = new ProductAdapter(this, productList, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(int position) {
                // Mark the selected product index when clicked
                selectedProductIndex = position;
                Toast.makeText(RestockActivity.this, "Selected: " + productList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        // Set the adapter to the ListView
        productListView.setAdapter(productAdapter);

        // Handle OK button click
        btnOk.setOnClickListener(v -> {
            String newQuantityStr = editTextNewQuantity.getText().toString();
            if (selectedProductIndex != -1 && !newQuantityStr.isEmpty()) {
                int newQuantity = Integer.parseInt(newQuantityStr);
                if (newQuantity > 0) {
                    Product selectedProduct = productList.get(selectedProductIndex);
                    selectedProduct.setQuantity(selectedProduct.getQuantity() + newQuantity);
                    SharedPrefsHelper.saveProductList(RestockActivity.this, productList);
                    productAdapter.notifyDataSetChanged();  // Refresh the ListView
                    Toast.makeText(RestockActivity.this, "Product restocked successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RestockActivity.this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RestockActivity.this, "Please select a product and enter a quantity", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> finish());
    }
    private void loadProductList() {
        // Sample products (replace this with real data loading logic from SharedPreferences)
        productList.add(new Product("Product 1", 10, 25.0));
        productList.add(new Product("Product 2", 20, 15.0));
        productList.add(new Product("Product 3", 30, 50.0));
    }

    private void saveProductList() {
        SharedPreferences sharedPreferences = getSharedPreferences("POSApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Save the updated list to SharedPreferences (you could serialize objects for complex data)
        for (int i = 0; i < productList.size(); i++) {
            editor.putInt("product_" + i + "_quantity", productList.get(i).getQuantity());
        }
        editor.apply();
    }
    private void saveProductListToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("POSApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Store product list
        Gson gson = new Gson();
        String json = gson.toJson(productList); // Serialize the product list to JSON
        editor.putString("productList", json);
        editor.apply();
    }


}