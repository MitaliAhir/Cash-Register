package com.example.cashregister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends  RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    private Context context;
    private List<PurchaseHistory> purchaseHistoryList;
    private OnItemClickListener onItemClickListener;

    public HistoryAdapter(Context context, List<PurchaseHistory> purchaseHistoryList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.purchaseHistoryList = purchaseHistoryList;
        this.onItemClickListener = onItemClickListener;
    }

    // ViewHolder to hold the item views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        TextView quantityTextView;
        TextView totalPriceTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(android.R.id.text1);
            quantityTextView = itemView.findViewById(android.R.id.text2);
            totalPriceTextView = itemView.findViewById(android.R.id.text2);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item view for each purchase
        View itemView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Bind the data to the views
        PurchaseHistory purchaseHistory = purchaseHistoryList.get(position);
        holder.productNameTextView.setText(purchaseHistory.getProductName());
        holder.quantityTextView.setText("Qty: " + purchaseHistory.getQuantity());
        holder.totalPriceTextView.setText("Total: $" + purchaseHistory.getTotalPrice());

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(purchaseHistory);
            }
        });
    }

    @Override
    public int getItemCount() {
        return purchaseHistoryList.size();
    }

    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(PurchaseHistory purchaseHistory);
    }
}
