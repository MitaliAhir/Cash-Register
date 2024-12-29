package com.example.cashregister;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private  TextView selectedProductTextView, quantityTextView, totalTextView;
    private Button buyButton;
    private ListView productListView;
    private Product selectedProduct;
    private ArrayList<Product> productList = new ArrayList<>();


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

        //Initialize UI components
        selectedProductTextView = findViewById(R.id.product_name);
        quantityTextView = findViewById(R.id.quantity);
        totalTextView = findViewById(R.id.total);
        buyButton = findViewById(R.id.btnBuy);
        productListView = findViewById(R.id.listViewProducts);
        // Add products to the product list
        productList.add(new Product("Product A", 10, 42.99));
        productList.add(new Product("Product B", 27, 69.87));
        productList.add(new Product("Product C", 38, 75.65));
        productList.add(new Product("Product D", 40, 97.99));
        productList.add(new Product("Product E", 56, 35.90));

        // Use the custom ProductAdapter to bind product data to the ListView
        ProductAdapter adapter = new ProductAdapter(this, productList);
        productListView.setAdapter(adapter);

        // Set an item click listener for selecting a product
        productListView.setOnItemClickListener((parentView, selectedItemView, position, id) -> {
                    // Get the selected product
                    selectedProduct = productList.get(position);
            // Update the UI with the selected product's details
            selectedProductTextView.setText("Selected Product: " + selectedProduct.getName());
            quantityTextView.setText("Quantity: " + selectedProduct.getQuantity());

            // Update the total
            updateTotal();
        });
        setUpButtonListeners(R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9);
        findViewById(R.id.btnC).setOnClickListener(v -> clear());
    }

    // Set up same listener for all number buttons
    private void setUpButtonListeners(int... buttonIds) {
        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(v -> {
                Button button = (Button) v;
                int quantity = Integer.parseInt(button.getText().toString());
                // Update the quantity for the selected product
                if (selectedProduct != null) {
                    selectedProduct.setQuantity(quantity);
                    quantityTextView.setText("Quantity: " + quantity);

                    // Update the total price
                    updateTotal();
                }
            });
        }
    }

    void updateTotal(){
        if (selectedProduct != null) {
            double totalPrice = selectedProduct.getTotalPrice();
            totalTextView.setText("Total: " + totalPrice);
        }
    }

    private void clear() {
        quantityTextView.setText("Quantity: 1");
        totalTextView.setText("Total: $0.00");
    }
}

//Edge Cases: You may want to add input validation to ensure that the quantity cannot be zero or negative.
//Quantity Increment/Decrement: Instead of typing in a quantity, you might allow the user to increment/decrement the quantity using + and - buttons.
//Multiple Product Selections: If you want to allow the user to add multiple products to the cart, you'll need to store selected products and quantities in a list and update the total accordingly.