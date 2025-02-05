package com.example.cashregister;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PurchaseDetailActivity extends AppCompatActivity {
    private TextView productNameTextView, quantityTextView, totalPriceTextView, purchaseDateTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_purchase_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize TextViews
        productNameTextView = findViewById(R.id.purchasedProductName);
        quantityTextView = findViewById(R.id.purchasedProductQuantity);
        totalPriceTextView = findViewById(R.id.purchasedProductPrice);
        purchaseDateTextView = findViewById(R.id.purchaseDate);
        backButton = findViewById(R.id.backButton);

        // Retrieve PurchaseHistory object from Intent
        PurchaseHistory purchaseHistory = (PurchaseHistory) getIntent().getSerializableExtra("purchaseDetails");


        // Display purchase details
        if (purchaseHistory != null) {
            productNameTextView.setText("Product: " + purchaseHistory.getProductName());
            quantityTextView.setText("Quantity: " + purchaseHistory.getQuantity());
            totalPriceTextView.setText("Total Price: $" + purchaseHistory.getTotalPrice());
            purchaseDateTextView.setText("Date: " + purchaseHistory.getPurchaseDate().toString());
        }

        backButton.setOnClickListener(v -> super.onBackPressed());
    }
}
