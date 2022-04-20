package com.example.shoppingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "products")
public class CheckoutModel implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @NotNull
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

    protected CheckoutModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        productImage = in.readString();
        productName = in.readString();
        productDescription = in.readString();
        productPrice = in.readString();
        productQuantity = in.readString();
        productShippingCost = in.readString();
        totalPrice = in.readString();
        totalQuantity = in.readString();
        productID = in.readString();
    }

    public static final Creator<CheckoutModel> CREATOR = new Creator<CheckoutModel>() {
        @Override
        public CheckoutModel createFromParcel(Parcel in) {
            return new CheckoutModel(in);
        }

        @Override
        public CheckoutModel[] newArray(int size) {
            return new CheckoutModel[size];
        }
    };

    @NotNull
    public Long getId() {
        return id;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public CheckoutModel(String productImage, String productName, String productDescription, String productPrice, String productQuantity, String productShippingCost, String totalPrice, String totalQuantity, String productID) {
        this.productImage = productImage;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productShippingCost = productShippingCost;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.productID = productID;
    }

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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(productImage);
        parcel.writeString(productName);
        parcel.writeString(productDescription);
        parcel.writeString(productPrice);
        parcel.writeString(productQuantity);
        parcel.writeString(productShippingCost);
        parcel.writeString(totalPrice);
        parcel.writeString(totalQuantity);
        parcel.writeString(productID);
    }
}
