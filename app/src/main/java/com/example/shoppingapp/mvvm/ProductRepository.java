package com.example.shoppingapp.mvvm;

import androidx.lifecycle.LiveData;

import com.example.shoppingapp.models.CheckoutModel;
import com.example.shoppingapp.models.ProductModel;
import com.example.shoppingapp.room.ProductDao;
import com.example.shoppingapp.room.ProductDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class ProductRepository {
    private ProductDatabase productDatabase;
    @Inject
    ProductRepository(ProductDatabase productDatabase){
        this.productDatabase = productDatabase;
    }

    public Flowable<List<CheckoutModel>> getProducts(){
        return productDatabase.getProductsDao().getProducts();
    }

    public Completable insertNewProduct(CheckoutModel checkoutModel){
        return productDatabase.getProductsDao().insertProduct(checkoutModel);
    }

    public Completable deletePerOrder(CheckoutModel checkoutModel){
        return productDatabase.getProductsDao().deletePerOrder(checkoutModel);
    }

    public Completable deleteAllProducts(){
       return productDatabase.getProductsDao().deleteAllProducts();
    }

    public Completable updateItem(Long id,String totalQuantity,String totalPrice){
        return productDatabase
                .getProductsDao()
                .updateItem(id,totalQuantity,totalPrice);
    }

}
