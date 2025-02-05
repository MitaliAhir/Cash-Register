package com.example.cashregister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context context;
    private List<PurchaseHistory> purchaseHistoryList;
    private OnPurchaseClickListener listener;

    public interface OnPurchaseClickListener {
        void onPurchaseClick(PurchaseHistory purchaseHistory);
    }

    public HistoryAdapter(Context context, List<PurchaseHistory> purchaseHistoryList, OnPurchaseClickListener listener) {
        this.context = context;
        this.purchaseHistoryList = purchaseHistoryList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_purchase, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PurchaseHistory purchaseHistory = purchaseHistoryList.get(position);
        holder.productNameTextView.setText(purchaseHistory.getProductName());
        holder.quantityTextView.setText("Qty: " + purchaseHistory.getQuantity());
        holder.totalPriceTextView.setText("$" + purchaseHistory.getTotalPrice());
        holder.itemView.setOnClickListener(v -> listener.onPurchaseClick(purchaseHistory));
    }

    @Override
    public int getItemCount() {
        return purchaseHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        TextView quantityTextView;
        TextView totalPriceTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productName);
            quantityTextView = itemView.findViewById(R.id.quantity);
            totalPriceTextView = itemView.findViewById(R.id.totalPrice);
        }
    }
}
