package com.example.cashregister;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView selectedProductTextView, quantityTextView, totalTextView;
    private Button buyButton;
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
        productListView = findViewById(R.id.listViewProducts);

        // Add products to the product list
        productList.add(new Product("Product A", 10, 200));
        productList.add(new Product("Product B", 27, 100));
        productList.add(new Product("Product C", 38, 300));
        productList.add(new Product("Product D", 40, 400));
        productList.add(new Product("Product E", 56, 500));

        // Use the custom ProductAdapter to bind product data to the ListView
        ProductAdapter adapter = new ProductAdapter(this, productList);
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
                    if (selectedProduct != null && desiredQuantity < selectedProduct.quantity) {
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
    }

    // Set up same listener for all number buttons
    private void setUpButtonListeners(int... buttonIds) {
        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(v -> {
                Button button = (Button) v;
                Product selectedProduct = getProductByName(selectedProductName);
                // Update the quantity for the selected product
                if (!selectedProductName.isEmpty() && selectedProduct != null) {
                    //String text = quantityTextView.getDisplay().toString();
                    desiredQuantity = desiredQuantity + Integer.parseInt(button.getText().toString());
                    quantityTextView.setText(String.valueOf(desiredQuantity));
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
        selectedProductTextView.setText("Product Type");
        quantityTextView.setText("0");
        totalTextView.setText("Total: $0.00");
        desiredQuantity = 0;
        selectedProductName = "";
    }
}

//Edge Cases: You may want to add input validation to ensure that the quantity cannot be zero or negative.
//Quantity Increment/Decrement: Instead of typing in a quantity, you might allow the user to increment/decrement the quantity using + and - buttons.
//Multiple Product Selections: If you want to allow the user to add multiple products to the cart, you'll need to store selected products and quantities in a list and update the total accordingly.