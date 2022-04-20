package com.example.shoppingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.CartUpdateActivity;
import com.example.shoppingapp.CheckoutActivity;
import com.example.shoppingapp.R;
import com.example.shoppingapp.models.CheckoutModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PurchaseAdapter  extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {


    public onAdapterListener adapterListener;
    public interface  onAdapterListener{
        void deleteProduct(CheckoutModel checkoutModel);
        void addReview(CheckoutModel checkoutModel);
        void updateProduct(CheckoutModel checkoutModel);
    }

    public void onAdapter(onAdapterListener listener){
        this.adapterListener = listener;
    }

    private List<CheckoutModel> arrayList;
    public PurchaseAdapter(List<CheckoutModel> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_rows_layout, parent, false);
        return new PurchaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int position) {
        CheckoutModel checkoutModel = arrayList.get(position);
        holder.productName.setText(checkoutModel.getProductName());
        holder.productTotalQuantity.setText("Quantity : " +  checkoutModel.getTotalQuantity());
        holder.productTotalPrice.setText("Total : " + checkoutModel.getTotalPrice() + "€");
        Picasso.get().load(checkoutModel.getProductImage()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapterListener != null){
                    int holderPosition = holder.getAdapterPosition();
                    if(holderPosition != RecyclerView.NO_POSITION){
                        adapterListener.updateProduct(checkoutModel);
                    }
                }
            }
        });

        holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapterListener != null){
                    int holderPosition = holder.getAdapterPosition();
                    if(holderPosition != RecyclerView.NO_POSITION){
                        adapterListener.deleteProduct(checkoutModel);
                    }
                }

            }
        });

        holder.reviewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapterListener != null){
                    int holderPosition = holder.getAdapterPosition();
                    if(holderPosition != RecyclerView.NO_POSITION){
                        adapterListener.addReview(checkoutModel);

                    }
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class PurchaseViewHolder extends RecyclerView.ViewHolder  {
        private ImageView deleteProduct;
        private ImageView reviewProduct;
        private ImageView imageView;
        private TextView productName;
        private TextView productTotalPrice;
        private TextView productTotalQuantity;
        public PurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteProduct = itemView.findViewById(R.id.deleteProduct);
            reviewProduct = itemView.findViewById(R.id.addReview);
            imageView = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productTotalPrice = itemView.findViewById(R.id.productPrice);
            productTotalQuantity = itemView.findViewById(R.id.productQuantity);
        }

    }

}
