package com.example.cashregister;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cashregister.Adapter.HistoryAdapter;
import com.example.cashregister.model.PurchaseHistory;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView historyRecyclerView;
    private ArrayList<PurchaseHistory> purchaseHistoryList;
    private HistoryAdapter historyAdapter;
    private Toolbar toolbar;

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
        toolbar = findViewById(R.id.toolbar);

        // Load purchase history from SharedPreferences
        purchaseHistoryList = SharedPrefsHelper.loadPurchaseHistory(this);

        // Set up RecyclerView
        historyAdapter = new HistoryAdapter(this, purchaseHistoryList, purchaseHistory -> {
            // When an item is clicked, navigate to the PurchaseDetailActivity
            Intent intent = new Intent(HistoryActivity.this, PurchaseDetailActivity.class);
            intent.putExtra("purchaseDetails", purchaseHistory);
            // Add the flag to remove HistoryActivity from the back stack
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });

        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(historyAdapter);
        setupToolbar();
    }

    // Method to update the list when returning from the Detail Page
    @Override
    protected void onResume() {
        super.onResume();
        historyAdapter.notifyDataSetChanged();
    }
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the Up button (back navigation)
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}