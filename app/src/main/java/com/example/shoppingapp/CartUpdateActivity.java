package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.models.CheckoutModel;
import com.example.shoppingapp.models.ProductModel;
import com.example.shoppingapp.mvvm.ProductViewModel;
import com.squareup.picasso.Picasso;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class CartUpdateActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable;
    ProductViewModel productViewModel;
    private int totalQuantity;
    Double totalPrice = 0.0;
    private CheckoutModel model;
    private ImageView productImage,addQuantity,substractQuantity;
    private TextView productName,productDescription,productPrice,productShippingCost,productQuantity,quantityTxt;
    private Button updateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_update);

        productImage  = findViewById(R.id.imageView);
        addQuantity = findViewById(R.id.addQuantity);
        substractQuantity  = findViewById(R.id.removeQuantity);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        productShippingCost  = findViewById(R.id.productShippingCost);
        productQuantity = findViewById(R.id.productQuantity);
        updateBtn = findViewById(R.id.update);
        quantityTxt = findViewById(R.id.quantityTxt);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        compositeDisposable = new CompositeDisposable();

        Intent intent = getIntent();
        if(intent != null){
            model = (CheckoutModel) intent.getParcelableExtra("data");
            totalQuantity = Integer.parseInt(model.getTotalQuantity());
            totalPrice = Double.parseDouble(model.getTotalPrice());
            productName.setText(model.getProductName());
            productDescription.setText(model.getProductDescription());
            productPrice.setText("Price : " + model.getProductPrice());
            productShippingCost.setText("Shipping : " + model.getProductShippingCost());
            productQuantity.setText("Quantity : " + model.getProductQuantity());
            Picasso.get().load(model.getProductImage()).into(productImage);
            quantityTxt.setText("Quantity : " + totalQuantity);
        }

        addQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalQuantity++;
                quantityTxt.setText("Quantity : " + totalQuantity);
                totalPrice += Double.parseDouble(model.getProductPrice()) + Double.parseDouble(model.getProductShippingCost());
            }
        });
        substractQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalQuantity > 0){
                    totalQuantity--;
                    quantityTxt.setText("Quantity : " + totalQuantity);
                    totalPrice -= Double.parseDouble(model.getProductPrice()) + Double.parseDouble(model.getProductShippingCost());
                }
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalQuantity == 0){
                    Toast.makeText(CartUpdateActivity.this,"Please set a quantity",Toast.LENGTH_SHORT).show();
                } else {
                    productViewModel
                            .updateProduct(model.id,String.valueOf(totalQuantity),String.valueOf(totalPrice))
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                    compositeDisposable.add(d);
                                }

                                @Override
                                public void onComplete() {
                                    Toast.makeText(CartUpdateActivity.this,"Product Successfully Updated",Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(CartUpdateActivity.this,PurchaseActivity.class);
                                    startActivity(intent1);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Log.d("TAG ERROR","ERROR " + e.getMessage());
                                }
                            });

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}