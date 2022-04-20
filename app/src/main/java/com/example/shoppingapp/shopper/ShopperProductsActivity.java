package com.example.shoppingapp.shopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoppingapp.CheckoutActivity;
import com.example.shoppingapp.AdminOrderActivity;
import com.example.shoppingapp.PurchaseActivity;
import com.example.shoppingapp.R;
import com.example.shoppingapp.ShopperOrderActivity;
import com.example.shoppingapp.models.ProductModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class ShopperProductsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter productsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopper_products);

        toolbar = findViewById(R.id.toolbar);
        recyclerView  = findViewById(R.id.recyclerView);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        displayShopperProducts();
    }


    private void displayShopperProducts() {
        Query query = firebaseFirestore
                .collection("Products")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("allProducts")
                .orderBy("productName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ProductModel> allProducts = new FirestoreRecyclerOptions.Builder<ProductModel>().setQuery(query, ProductModel.class).build();

        productsAdapter =new FirestoreRecyclerAdapter<ProductModel, ProductsViewHolder>(allProducts) {
            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder propertyViewHolder, int i, @NonNull ProductModel productModel) {
                propertyViewHolder.productName.setText(productModel.getProductName());
                propertyViewHolder.productPrice.setText("Price : " + productModel.getProductPrice() + "€");
                propertyViewHolder.productShippingCost.setText("Shipping : " + productModel.getProductShippingCost() + "€");
                Picasso.get().load(productModel.getProductImage()).into(propertyViewHolder.productImage);
                propertyViewHolder.productLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(propertyViewHolder.itemView.getContext(), CheckoutActivity.class);
                        intent.putExtra("data",productModel);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopperr_rows_layout,parent, false);
                return new ProductsViewHolder(view);
            }
        };
        recyclerView.setAdapter(productsAdapter);
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        private CardView productLayout;
        private TextView productName;
        private TextView productPrice;
        private TextView productQuantity;
        private TextView productShippingCost;
        private ImageView productImage;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productShippingCost = itemView.findViewById(R.id.productShippingCost);
            productImage = itemView.findViewById(R.id.productImage);
            productLayout = itemView.findViewById(R.id.productLayout);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        productsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (productsAdapter != null) {
            productsAdapter.startListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shopper_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cart:{
                Intent intent = new Intent(this, PurchaseActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.orders: {
                Intent intent = new Intent(this, ShopperOrderActivity.class);
                startActivity(intent);
            }
        }
        return true;
    }
}