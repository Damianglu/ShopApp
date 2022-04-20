package com.example.shoppingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.models.ReviewsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsHolder> {

    private List<ReviewsModel> modelList;
    public ReviewsAdapter(List<ReviewsModel> modelList){
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ReviewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_rows_layout,parent,false);
        return new ReviewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsHolder holder, int position) {
        ReviewsModel reviewsModel = modelList.get(position);
        holder.fullName.setText("By " + reviewsModel.getFullName());
        holder.ratingBar.setRating(Float.parseFloat(reviewsModel.getRating()));
        holder.review.setText(reviewsModel.getReview());
        Picasso.get().load(reviewsModel.getImage()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ReviewsHolder extends  RecyclerView.ViewHolder{
        private TextView fullName;
        private RatingBar ratingBar;
        private ImageView productImage;
        private TextView review;
        public ReviewsHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            productImage = itemView.findViewById(R.id.productImage);
            review = itemView.findViewById(R.id.review);
        }
    }
}
