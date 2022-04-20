package com.example.shoppingapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shoppingapp.models.CheckoutModel;
import com.example.shoppingapp.models.ProductModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM products")
    Flowable<List<CheckoutModel>> getProducts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertProduct(CheckoutModel checkoutModel);

    @Delete
    Completable deletePerOrder(CheckoutModel checkoutModel);

    @Query("DELETE FROM products")
    Completable deleteAllProducts();

    @Query("UPDATE products SET totalQuantity = :totalQuantity , totalPrice= :totalPrice WHERE id LIKE :id ")
    Completable updateItem(Long id,String totalQuantity,String totalPrice);

    /*
    public Long id;
    String productImage;
    String productName;
    String productDescription;
    String productPrice;
    String productQuantity;
    String productShippingCost;
    String totalPrice;
    String totalQuantity;
    String productID;
     */
}
