package com.example.shoppingapp.mvvm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.shoppingapp.models.CheckoutModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class ProductViewModel extends ViewModel {
    private ProductRepository productRepository;

    @Inject
    ProductViewModel(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public @NonNull Flowable<List<CheckoutModel>> getProducts(){
        return productRepository
                .getProducts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Completable insertNewProduct(CheckoutModel checkoutModel){
        return productRepository.
                insertNewProduct(checkoutModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Completable deletePerOrder(CheckoutModel checkoutModel){
        return productRepository
                 .deletePerOrder(checkoutModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Completable deleteAllProducts(){
        return productRepository
                .deleteAllProducts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Completable updateProduct(Long id,String totalQuantity,String totalPrice){
        return productRepository
                .updateItem(id,totalQuantity,totalPrice)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
