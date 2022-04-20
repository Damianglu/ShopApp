package com.example.shoppingapp.di;

import android.content.Context;

import androidx.room.Room;

import com.example.shoppingapp.room.ProductDao;
import com.example.shoppingapp.room.ProductDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ProductModule {

    @Singleton
    @Provides
    ProductDatabase getProductDatabase(@ApplicationContext Context context){
        return Room.databaseBuilder(context,ProductDatabase.class,"products.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    ProductDao provideDao(ProductDatabase productDatabase){
        return productDatabase.getProductsDao();
    }
}
