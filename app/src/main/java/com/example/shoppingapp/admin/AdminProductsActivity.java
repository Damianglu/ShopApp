package com.example.shoppingapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.AdminOrderActivity;
import com.example.shoppingapp.R;
import com.example.shoppingapp.models.ProductModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class AdminProductsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter productsAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_products);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        toolbar = findViewById(R.id.toolbar);
        recyclerView  = findViewById(R.id.recyclerView);
        setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        displayAdminProducts();

    }

    private void displayAdminProducts() {
        Query query = firebaseFirestore
                .collection("Products")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("allProducts")
                .orderBy("productName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ProductModel> allProducts = new FirestoreRecyclerOptions.Builder<ProductModel>().setQuery(query, ProductModel.class).build();
        productsAdapter = new FirestoreRecyclerAdapter<ProductModel, ProductsViewHolder>(allProducts) {
            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder ProductsViewHolder, int i, @NonNull ProductModel productModel) {
                ProductsViewHolder.productName.setText(productModel.getProductName());
                ProductsViewHolder.productPrice.setText("Price : " + productModel.getProductPrice() + "€");
                ProductsViewHolder.productQuantity.setText("Quantity : " + productModel.getProductQuantity());
                ProductsViewHolder.productShippingCost.setText("Shipping : " + productModel.getProductShippingCost() + "€");
                Picasso.get().load(productModel.getProductImage()).into(ProductsViewHolder.productImage);
                ProductsViewHolder.deleteEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(ProductsViewHolder.itemView.getContext(),ProductsViewHolder.deleteEdit);
                        popupMenu.inflate(R.menu.product_menu);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()){
                                    case R.id.deleteProduct:{
                                        deletePerProduct(productModel.getProductID());
                                    }
                                    break;
                                    case R.id.editProduct: {
                                        Intent intent  = new Intent(ProductsViewHolder.itemView.getContext(),EditProductActivity.class);
                                        intent.putExtra("data",productModel);
                                        startActivity(intent);
                                    }
                                    break;
                                }
                                return true;
                            }
                        });
                        popupMenu.show();
                    }
                });
            }

            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_rows_layout,parent, false);
                return new ProductsViewHolder(view);
            }
        };
        recyclerView.setAdapter(productsAdapter);

    }

    private void deletePerProduct(String productID) {
        firebaseFirestore
                .collection("Products")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("allProducts")
                .document(productID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AdminProductsActivity.this,"Product Successfully Deleted",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView productPrice;
        private TextView productQuantity;
        private TextView productShippingCost;
        private ImageView productImage;
        private ImageView deleteEdit;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productShippingCost = itemView.findViewById(R.id.productShippingCost);
            productImage = itemView.findViewById(R.id.productImage);
            deleteEdit = itemView.findViewById(R.id.deleteEdit);
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
        getMenuInflater().inflate(R.menu.admin_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.orders: {
                Intent intent = new Intent(AdminProductsActivity.this, AdminOrderActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.deleteProducts: {
                getProducts();
            }
        }
        return true;
    }

    private void getProducts(){
        new AlertDialog.Builder(this)
                .setTitle("DELETING PRODUCTS")
                .setMessage("Are you sure you want to delete products !")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseFirestore
                                .collection("Products")
                                .document(firebaseAuth.getCurrentUser().getUid())
                                .collection("allProducts")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for(QueryDocumentSnapshot querySnapshot : task.getResult()){
                                            String productID = querySnapshot.get("productID").toString();
                                            deleteProduct(productID);
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

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

    private void deleteProduct(String productID) {
        firebaseFirestore
                .collection("Products")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("allProducts")
                .document(productID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                     Toast.makeText(AdminProductsActivity.this,"All Products Successfully Deleted",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}

