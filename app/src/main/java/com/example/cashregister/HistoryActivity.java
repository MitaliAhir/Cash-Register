package com.example.cashregister;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView historyRecyclerView;
    private ArrayList<PurchaseHistory> purchaseHistoryList;
    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        historyRecyclerView = findViewById(R.id.historyRecyclerView);

        // Sample purchase history data
        purchaseHistoryList = new ArrayList<>();
        purchaseHistoryList.add(new PurchaseHistory("Product 1", 5, 50.0, new Date()));
        purchaseHistoryList.add(new PurchaseHistory("Product 2", 2, 30.0, new Date()));

        // Set up RecyclerView
        historyAdapter = new HistoryAdapter(this, purchaseHistoryList, purchaseHistory -> {
            // When an item is clicked, navigate to the PurchaseDetailActivity
            Intent intent = new Intent(HistoryActivity.this, PurchaseDetailActivity.class);
            intent.putExtra("product_name", purchaseHistory.getProductName());
            intent.putExtra("quantity", purchaseHistory.getQuantity());
            intent.putExtra("total_price", purchaseHistory.getTotalPrice());
            intent.putExtra("purchase_date", purchaseHistory.getPurchaseDate().toString());
            startActivity(intent);
        });

        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(historyAdapter);

    }
    // Method to update the list when returning from the Detail Page
    @Override
    protected void onResume() {
        super.onResume();
        // You can update the list from a database, shared preferences, or any other persistent storage.
        // If you made any changes to purchaseHistoryList, call notifyDataSetChanged on the adapter
        historyAdapter.notifyDataSetChanged();
    }
}