package com.example.shoppingapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shoppingapp.admin.AdminProductsActivity;
import com.example.shoppingapp.admin.AdminScreen;
import com.example.shoppingapp.R;
import com.example.shoppingapp.shopper.ShopperProductsActivity;
import com.example.shoppingapp.shopper.ShopperScreen;

public class MainPage extends AppCompatActivity {

    Button madminButton, muserButton,addNewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        madminButton = findViewById(R.id.adminButton);
        muserButton = findViewById(R.id.userButton);
        addNewProduct = findViewById(R.id.addNewProduct);

        madminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainPage.this, AdminProductsActivity.class));
            }
        });
        muserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainPage.this, ShopperProductsActivity.class));
            }
        });
        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this,AdminScreen.class);
                startActivity(intent);
            }
        });
    }
}