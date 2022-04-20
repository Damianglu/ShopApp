package com.example.shoppingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

public class ProductModel implements Parcelable {
    String productImage;
    String productName;
    String productDescription;
    String productPrice;
    String productQuantity;
    String productShippingCost;
    String size;
    String productID;
    int selectedPosition;


    ProductModel(){

    }

    public ProductModel(String productImage, String productName, String productDescription, String productPrice, String productQuantity, String productShippingCost, String size, String productID, int selectedPosition) {
        this.productImage = productImage;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productShippingCost = productShippingCost;
        this.size = size;
        this.productID = productID;
        this.selectedPosition = selectedPosition;
    }

    protected ProductModel(Parcel in) {
        productImage = in.readString();
        productName = in.readString();
        productDescription = in.readString();
        productPrice = in.readString();
        productQuantity = in.readString();
        productShippingCost = in.readString();
        size = in.readString();
        productID = in.readString();
        selectedPosition = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productImage);
        dest.writeString(productName);
        dest.writeString(productDescription);
        dest.writeString(productPrice);
        dest.writeString(productQuantity);
        dest.writeString(productShippingCost);
        dest.writeString(size);
        dest.writeString(productID);
        dest.writeInt(selectedPosition);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductShippingCost() {
        return productShippingCost;
    }

    public void setProductShippingCost(String productShippingCost) {
        this.productShippingCost = productShippingCost;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}

