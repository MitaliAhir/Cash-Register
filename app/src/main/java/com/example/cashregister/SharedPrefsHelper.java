package com.example.cashregister;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefsHelper {
    private static final String PREFS_NAME = "POSApp";
    private static final String PRODUCT_LIST_KEY = "productList";
    private static final String PURCHASE_HISTORY_KEY = "purchaseHistory";
    private static final String TAG = "SharedPrefsHelper";

    // Generic method to save data to SharedPreferences
    private static <T> void saveData(Context context, String key, List<T> data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(key, json);
        editor.apply();
    }

    // Generic method to load data from SharedPreferences
    private static <T> List<T> loadData(Context context, String key, TypeToken<List<T>> typeToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);

        if (json != null) {
            try {
                return gson.fromJson(json, typeToken.getType());
            } catch (JsonSyntaxException e) {
                Log.e(TAG, "Error parsing JSON for key: " + key, e);
                // Handle the error appropriately, e.g., return an empty list or a default list
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    // Save product list to SharedPreferences
    public static void saveProductList(Context context, List<Product> productList) {
        saveData(context, PRODUCT_LIST_KEY, productList);
    }

    // Load product list from SharedPreferences
    public static ArrayList<Product> loadProductList(Context context) {
        List<Product> productList = loadData(context, PRODUCT_LIST_KEY, new TypeToken<List<Product>>() {});
        if (productList.isEmpty()) {
            return createDefaultProductList();
        }
        return new ArrayList<>(productList);
    }

    // Save purchase to SharedPreferences
    public static void savePurchase(Context context, PurchaseHistory purchase) {
        List<PurchaseHistory> purchaseHistoryList = loadPurchaseHistory(context);
        purchaseHistoryList.add(purchase);
        saveData(context, PURCHASE_HISTORY_KEY, purchaseHistoryList);
    }

    // Load purchase history from SharedPreferences
    public static ArrayList<PurchaseHistory> loadPurchaseHistory(Context context) {
        return new ArrayList<>(loadData(context, PURCHASE_HISTORY_KEY, new TypeToken<List<PurchaseHistory>>() {}));
    }

    // Create a default product list
    private static ArrayList<Product> createDefaultProductList() {
        ArrayList<Product> defaultList = new ArrayList<>();
        defaultList.add(new Product("Product A", 10, 200.67));
        defaultList.add(new Product("Product B", 27, 95.99));
        defaultList.add(new Product("Product C", 38, 57.99));
        defaultList.add(new Product("Product D", 40, 42.88));
        defaultList.add(new Product("Product E", 56, 49.99));
        return defaultList;
    }
}