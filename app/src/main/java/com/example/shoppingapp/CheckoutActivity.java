package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.adapters.ReviewsAdapter;
import com.example.shoppingapp.models.CheckoutModel;
import com.example.shoppingapp.models.ProductModel;
import com.example.shoppingapp.models.ReviewsModel;
import com.example.shoppingapp.mvvm.ProductViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;


@AndroidEntryPoint
public class CheckoutActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReviewsAdapter reviewsAdapter;
    private List<ReviewsModel> reviewsModelList;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private CompositeDisposable compositeDisposable;
    private ProductModel productModel;
    ProductViewModel productViewModel;
    private int totalQuantity;
    Double totalPrice = 0.0;
    private ImageView productImage,addQuantity,substractQuantity;
    private TextView productName,productDescription,productPrice,productShippingCost,quantityTxt;
    private Button checkoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        productImage  = findViewById(R.id.imageView);
        addQuantity = findViewById(R.id.addQuantity);
        substractQuantity  = findViewById(R.id.removeQuantity);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        productShippingCost  = findViewById(R.id.productShippingCost);
        checkoutBtn = findViewById(R.id.checkout);
        quantityTxt = findViewById(R.id.quantityTxt);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        compositeDisposable = new CompositeDisposable();
        recyclerView  = findViewById(R.id.reviewsRecycler);
        reviewsModelList = new ArrayList<>();
        Intent intent = getIntent();
        if(intent != null){
            productModel = (ProductModel) intent.getParcelableExtra("data");
            productName.setText(productModel.getProductName());
            productDescription.setText(productModel.getProductDescription());
            productPrice.setText("Price : " + productModel.getProductPrice() + "€");
            productShippingCost.setText("Shipping : " +productModel.getProductShippingCost() + "€");
            Picasso.get().load(productModel.getProductImage()).into(productImage);

        }
        showReviews();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        addQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalQuantity++;
                quantityTxt.setText("Quantity : " + totalQuantity);
                totalPrice += Double.parseDouble(productModel.getProductPrice()) + Double.parseDouble(productModel.getProductShippingCost());
            }
        });
        substractQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalQuantity > 0){
                    totalQuantity--;
                    quantityTxt.setText("Quantity" + totalQuantity);
                    totalPrice = totalPrice -  Double.parseDouble(productModel.getProductPrice()) + Double.parseDouble(productModel.getProductShippingCost());
                }
            }
        });
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalQuantity == 0){
                    Toast.makeText(CheckoutActivity.this,"Please set a quantity",Toast.LENGTH_SHORT).show();
                } else {
                    CheckoutModel checkoutModel =  new CheckoutModel(productModel.getProductImage(), productModel.getProductName(),productModel.getProductDescription(),
                            productModel.getProductPrice(),productModel.getProductQuantity(),
                            productModel.getProductShippingCost(),totalPrice.toString(),String.valueOf(totalQuantity),productModel.getProductID());

                   productViewModel
                            .insertNewProduct(checkoutModel)
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                    compositeDisposable.add(d);
                                }

                                @Override
                                public void onComplete() {
                                    Toast.makeText(CheckoutActivity.this,"Product Successfully Added",Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(CheckoutActivity.this,PurchaseActivity.class);
                                    startActivity(intent1);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {

                                }
                            });
                }
            }
        });
    }

    private void showReviews(){
        firebaseFirestore
                .collection("Reviews")
                .document(productModel.getProductID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            ReviewsModel reviewsModel = documentSnapshot.toObject(ReviewsModel.class);
                            reviewsModelList.add(reviewsModel);
                            reviewsAdapter = new ReviewsAdapter(reviewsModelList);
                            recyclerView.setAdapter(reviewsAdapter);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}