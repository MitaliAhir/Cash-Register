package com.example.cashregister;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ManagerActivity extends AppCompatActivity {
    private Button historyButton, restockButton;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);
        historyButton = findViewById(R.id.btnHistory);
        restockButton = findViewById(R.id.btnRestock);

        // Navigate to PurchaseHistory screen
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // Navigate to Restock screen
        restockButton.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerActivity.this, RestockActivity.class);
            startActivity(intent);
        });
        setupToolbar();
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