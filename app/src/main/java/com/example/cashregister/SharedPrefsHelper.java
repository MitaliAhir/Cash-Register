package com.example.cashregister;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefsHelper {
    private static final String PREFS_NAME = "POSApp";
    private static final String PRODUCT_LIST_KEY = "productList";

    // Save product list to SharedPreferences
    public static void saveProductList(Context context, List<Product> productList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(productList); // Convert product list to JSON string
        editor.putString(PRODUCT_LIST_KEY, json);  // Save the JSON string
        editor.apply();  // Commit changes asynchronously
    }
    // Load product list from SharedPreferences
    public static ArrayList<Product> loadProductList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PRODUCT_LIST_KEY, null);

        if (json != null) {
            return gson.fromJson(json, new TypeToken<ArrayList<Product>>(){}.getType()); // Deserialize back into a list
        } else {
            // Return a default list if no data is found in SharedPreferences
            ArrayList<Product> defaultList = new ArrayList<>();
            defaultList.add(new Product("Product A", 10, 200.67));
            defaultList.add(new Product("Product B", 27, 100.0));
            defaultList.add(new Product("Product C", 38, 57.99));
            defaultList.add(new Product("Product D", 40, 42.88));
            defaultList.add(new Product("Product E", 56, 49.99));
            return defaultList;
        }
    }
}
