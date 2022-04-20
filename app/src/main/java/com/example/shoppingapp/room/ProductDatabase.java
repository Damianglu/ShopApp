package com.example.shoppingapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.shoppingapp.models.CheckoutModel;
import com.example.shoppingapp.models.ProductModel;

@Database(entities = {CheckoutModel.class},version = 1,exportSchema = false)
public abstract class ProductDatabase extends RoomDatabase {

    abstract public ProductDao getProductsDao();
}
