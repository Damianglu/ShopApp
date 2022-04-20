package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.adapters.PurchaseAdapter;
import com.example.shoppingapp.models.CheckoutModel;
import com.example.shoppingapp.mvvm.ProductViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

@AndroidEntryPoint
public class PurchaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private TextView totalToPay;
    private Button payBtn;
    private String clientName;
    private String creditNumber;
    private List<CheckoutModel> list;
    private Double itemsTotal = 0.0;
    private CompositeDisposable compositeDisposable;
    private Toolbar toolbar;
    protected RecyclerView recyclerView;
    ProductViewModel productViewModel;
    private PurchaseAdapter purchaseAdapter;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        payBtn  = findViewById(R.id.payBtn);
        totalToPay  = findViewById(R.id.totalToPay);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance(Utils.DATABASE_URL);
        firebaseFirestore = FirebaseFirestore.getInstance();

        list = new ArrayList<>();
        compositeDisposable = new CompositeDisposable();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        getProfileInfo();
        getOrders();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(list.isEmpty()){
                    progressDialog.dismiss();
                } else {
                    for (int i = 0; i <  list.size(); i++) {
                        itemsTotal += Double.parseDouble(list.get(i).getTotalPrice());
                        totalToPay.setText("Total To Pay : " + itemsTotal + "€");
                        progressDialog.dismiss();
                    }
                }
            }
        },1000);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(purchaseAdapter.getItemCount() <= 0){
                    Toast.makeText(PurchaseActivity.this,"No items added",Toast.LENGTH_SHORT).show();
                } else {
                    // process payment for items
                    showPaymentsDialog();
                }
            }
        });

    }

    private void showPaymentsDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.payment_dialog_layout,null);
        Button processPayment = view.findViewById(R.id.processPayment);
        EditText editFullName = view.findViewById(R.id.editFullName);
        EditText editCreditNumber = view.findViewById(R.id.editCreditNumberr);

        AlertDialog.Builder builder  = new AlertDialog.Builder(this)
                .setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        processPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uniqueID = String.valueOf(Calendar.getInstance().getTimeInMillis());
                String fullName  = editFullName.getText().toString();
                String creditNumber = editCreditNumber.getText().toString();
                if(TextUtils.isEmpty(fullName)){
                    Toast.makeText(PurchaseActivity.this,"Please provide a full name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(creditNumber)){
                    Toast.makeText(PurchaseActivity.this,"Please provide a credit card number",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Processing Payment..");
                progressDialog.show();

                String id = String.valueOf(Calendar.getInstance().getTimeInMillis());
                Map<String,Object> paymentMap = new HashMap<>();
                paymentMap.put("orderID",id);
                paymentMap.put("time",id);
                paymentMap.put("total", String.valueOf(itemsTotal).substring(0,String.valueOf(itemsTotal).indexOf(".")));
                paymentMap.put("items",list);

                firebaseFirestore
                        .collection("Orders")
                        .document(id)
                        .set(paymentMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(PurchaseActivity.this,"Order was successfully placed",Toast.LENGTH_SHORT).show();
                                    productViewModel
                                            .deleteAllProducts()
                                            .subscribe(new CompletableObserver() {
                                                @Override
                                                public void onSubscribe(@NonNull Disposable d) {
                                                    compositeDisposable.add(d);
                                                }

                                                @Override
                                                public void onComplete() {
                                                    dialog.dismiss();
                                                    progressDialog.dismiss();
                                                    Intent intent = new Intent(PurchaseActivity.this, AdminOrderActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                                @Override
                                                public void onError(@NonNull Throwable e) {

                                                }
                                            });

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception e) {
                                dialog.dismiss();
                                progressDialog.dismiss();
                            }
                        });
            }
        });
    }


    private void getProfileInfo(){
        firebaseDatabase
                .getReference()
                .child("Users")
                .child(firebaseAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            clientName = snapshot.child("fullName").getValue(String.class);
                            creditNumber = snapshot.child("creditNumber").getValue(String.class);
                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                    }
                });

    }

    private void getOrders() {
        productViewModel
                .getProducts()
                .toObservable()
                .subscribe(new Observer<List<CheckoutModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<CheckoutModel> checkoutModels) {
                        list = checkoutModels;
                        purchaseAdapter = new PurchaseAdapter(list);
                        recyclerView.setAdapter(purchaseAdapter);
                        purchaseAdapter.onAdapter(new PurchaseAdapter.onAdapterListener() {
                            @Override
                            public void deleteProduct(CheckoutModel checkoutModel) {
                                // delete per product
                                productViewModel
                                        .deletePerOrder(checkoutModel)
                                        .subscribe(new CompletableObserver() {
                                            @Override
                                            public void onSubscribe(@NonNull Disposable d) {
                                                compositeDisposable.add(d);
                                            }

                                            @Override
                                            public void onComplete() {
                                               if(itemsTotal == 0.0){
                                                   totalToPay.setText("Total To Pay : 0€");
                                               } else {
                                                   itemsTotal -= Double.parseDouble(checkoutModel.getTotalPrice());
                                                   totalToPay.setText("Total To Pay : " + itemsTotal + "€");
                                                   Toast.makeText(PurchaseActivity.this,"Item Successfully Deleted", Toast.LENGTH_SHORT).show();
                                               }
                                            }

                                            @Override
                                            public void onError(@NonNull Throwable e) {

                                            }
                                        });
                            }

                            @Override
                            public void addReview(CheckoutModel checkoutModel) {
                               addNewReview(checkoutModel.getProductID(),checkoutModel.getProductImage());
                            }

                            @Override
                            public void updateProduct(CheckoutModel checkoutModel) {
                                Intent intent = new Intent(PurchaseActivity.this, CartUpdateActivity.class);
                                intent.putExtra("data",checkoutModel);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void addNewReview(String productID,String productImage) {
        View view = LayoutInflater.from(this).inflate(R.layout.review_layout,null);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        EditText reviewEdit = view.findViewById(R.id.review);
        Button submitBtn = view.findViewById(R.id.submit);

        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setView(view);
        Dialog dialog = alert.create();
        dialog.show();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String review = reviewEdit.getText().toString();
                if(TextUtils.isEmpty(review)){
                    Toast.makeText(PurchaseActivity.this,"Please submit a review",Toast.LENGTH_SHORT).show();
                }

                Map<String,Object> map = new HashMap<>();
                map.put("fullName",clientName);
                map.put("review",review);
                map.put("rating",String.valueOf(ratingBar.getRating()));
                map.put("image",productImage);

                firebaseFirestore
                        .collection("Reviews")
                        .document(productID)
                        .set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    dialog.dismiss();
                                    Toast.makeText(PurchaseActivity.this,"Review Submitted Successfully",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception e) {

                            }
                        });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.orders_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteAll: {
                if(list.isEmpty()){
                    Toast.makeText(PurchaseActivity.this,"No Items Added",Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Deleting Items")
                            .setMessage("Are you sure you want to delete all items!")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    productViewModel
                                            .deleteAllProducts()
                                            .subscribe(new CompletableObserver() {
                                                @Override
                                                public void onSubscribe(@NonNull Disposable d) {
                                                    compositeDisposable.add(d);
                                                }

                                                @Override
                                                public void onComplete() {
                                                    Toast.makeText(PurchaseActivity.this,"All items successfully deleted",
                                                            Toast.LENGTH_SHORT).show();
                                                    totalToPay.setText("Total To Pay : 0€");
                                                }

                                                @Override
                                                public void onError(@NonNull Throwable e) {

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
                            .create()
                            .show();
                }

            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}