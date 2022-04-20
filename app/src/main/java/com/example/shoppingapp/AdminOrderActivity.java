package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.shoppingapp.adapters.OrdersAdapter;
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

public class AdminOrderActivity extends AppCompatActivity {
    private OrdersAdapter ordersAdapter;
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
                            ordersAdapter = new OrdersAdapter(list, orderDates, orderIDs);
                            recyclerView.setAdapter(ordersAdapter);
                            ordersAdapter.deleteOrder(new OrdersAdapter.onDeleteListener() {
                                @Override
                                public void onDeleted(String orderID, int position) {
                                    firebaseFirestore
                                            .collection("Orders")
                                            .document(orderID)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(AdminOrderActivity.this, "Order Successfully deleted", Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(AdminOrderActivity.this, "Order Successfully deleted", Toast.LENGTH_SHORT).show();
                                                    list.remove(position);
                                                    ordersAdapter.notifyItemRemoved(position);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                }
                            });

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteOrders: {
                new AlertDialog.Builder(this)
                        .setTitle("Deleting Orders")
                        .setMessage("Are you sure you want to delete all orders !")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebaseFirestore
                                        .collection("Orders")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    String orderID = document.get("orderID").toString();
                                                    firebaseFirestore
                                                            .collection("Orders")
                                                            .document(orderID)
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(AdminOrderActivity.this, "All Orders successfully removed",
                                                                            Toast.LENGTH_SHORT).show();
                                                                    ordersAdapter.notifyDataSetChanged();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {

                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();

            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminOrderActivity.this, ShopperProductsActivity.class);
        startActivity(intent);
        finish();
    }


}