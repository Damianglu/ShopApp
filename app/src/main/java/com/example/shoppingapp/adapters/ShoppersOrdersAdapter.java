package com.example.shoppingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.models.CheckoutModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShoppersOrdersAdapter extends RecyclerView.Adapter<ShoppersOrdersAdapter.SubOrderViewHolder> {

    private List<String> orderDates;
    private List<String> orderIDs;
    List<CheckoutModel> list;

    public ShoppersOrdersAdapter(List<CheckoutModel> list,List<String> orderDates,List<String> orderIDs){
        this.list = list;
        this.orderDates = orderDates;
        this.orderIDs = orderIDs;
    }


    @NonNull
    @Override
    public SubOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopper_order_row_layout, parent, false);
        return new ShoppersOrdersAdapter.SubOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubOrderViewHolder holder, int position) {
        CheckoutModel checkoutModel = list.get(position);
        holder.productName.setText(checkoutModel.getProductName());
        holder.orderId.setText("ORDER ID #" + orderIDs.get(position));
        holder.orderDate.setText("Placed on : " + orderDates.get(position));
        holder.productPrice.setText("Total Price : " + checkoutModel.getTotalPrice().substring(0,checkoutModel.getTotalPrice().indexOf("."))+ "€");
        holder.productQuantity.setText("Quantity : " + checkoutModel.getTotalQuantity());
        Picasso.get().load(checkoutModel.getProductImage()).into(holder.productImage);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SubOrderViewHolder extends RecyclerView.ViewHolder{
        private TextView productName;
        private TextView productPrice;
        private ImageView productImage;
        private TextView productQuantity;
        private TextView orderId;
        private TextView orderDate;
        private ImageView deleteOrder;
        public SubOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productImage = itemView.findViewById(R.id.productImage);
            orderId = itemView.findViewById(R.id.orderID);
            orderDate = itemView.findViewById(R.id.orderDate);
            deleteOrder = itemView.findViewById(R.id.deleteOrder);

        }
    }

}
