package com.example.shoppingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.adapters.OrdersAdapter;
import com.example.shoppingapp.adapters.ShoppersOrdersAdapter;
import com.example.shoppingapp.models.CheckoutModel;
import com.example.shoppingapp.shopper.ShopperProductsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopperOrderActivity extends AppCompatActivity {
    private ShoppersOrdersAdapter ordersAdapter;
    private RecyclerView recyclerView;
    private List<CheckoutModel> list;
    private List<String> orderDates;
    private List<String> orderIDs;
    private Toolbar toolbar;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        orderDates = new ArrayList<>();
        orderIDs = new ArrayList<>();
        list = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        showOrders();

    }

    private void showOrders() {
        firebaseFirestore
                .collection("Orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String orderDate = getFormattedDate(document.get("time").toString());
                            String orderID = document.get("orderID").toString();
                            List<Map<String, Object>> items = (List<Map<String, Object>>) document.get("items");
                            for (int i = 0; i < items.size(); i++) {
                                HashMap<String, Object> map = (HashMap<String, Object>) items.get(i);
                                Long id = (Long) map.get("id");
                                String productImage = (String) map.get("productImage");
                                String productName = (String) map.get("productName");
                                String productDescription = (String) map.get("productDescription");
                                String productPrice = (String) map.get("productPrice");
                                String productQuantity = (String) map.get("productQuantity");
                                String productShippingCost = (String) map.get("productShippingCost");
                                String totalPrice = (String) map.get("totalPrice");
                                String totalQuantity = (String) map.get("totalQuantity");
                                String productID = (String) map.get("productID");

                                CheckoutModel checkoutModel = new CheckoutModel(productImage, productName, productDescription, productPrice, productQuantity, productShippingCost,
                                        totalPrice, totalQuantity, productID);
                                list.add(checkoutModel);
                            }
                            orderIDs.add(orderID);
                            orderDates.add(orderDate);
                            ordersAdapter = new ShoppersOrdersAdapter(list, orderDates, orderIDs);
                            recyclerView.setAdapter(ordersAdapter);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private String getFormattedDate(String time) {
        Long date = Long.parseLong(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return DateFormat.format("dd-MM-yyyy ,HH:mm", calendar).toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ShopperOrderActivity.this, ShopperProductsActivity.class);
        startActivity(intent);
        finish();
    }


}