
package com.maks.babyneeds.adapter;

/**
 * Created by maks on 7/2/16.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Activity.ReviewActivity;
import com.maks.babyneeds.Activity.ServicesCategoryActivity;
import com.maks.model.Review;
import com.maks.model.ServicesCategory;

import java.util.List;

/**
 * Created by Belal on 11/9/2015.
 */
public class ProductReviewsAdapter extends RecyclerView.Adapter<ProductReviewsAdapter.ViewHolder> {

   // private CategoryActivity context;
   private ReviewActivity context;
    OnItemClickListener mItemClickListener;
    //List of Category
    List<Review> reviewList;
    ReviewActivity activity;

    public ProductReviewsAdapter(List<Review> reviewList, ReviewActivity context){
        super();
        //Getting all the reviewList
        this.reviewList = reviewList;
        this.context = context;
        activity = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Review review =  reviewList.get(position);
        holder.txtDate.setText(review.getReviewDate());
        holder.txtTitle.setText(review.getTitle());
        holder.txtRating.setText(review.getRating()+"/5");
        holder.txtName.setText(review.getReviewerName());
        holder.txtReview.setText(review.getReview());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtRating,txtTitle,txtReview,txtDate,txtName;
        public RatingBar rbRating;
        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
                    txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
                    txtDate = (TextView) itemView.findViewById(R.id.txtDate);
                    txtReview = (TextView) itemView.findViewById(R.id.txtReview);
                    txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtRating = (TextView) itemView.findViewById(R.id.txtRating);
            rbRating = (RatingBar) itemView.findViewById(R.id.rbRating);
          }

        @Override
        public void onClick(View v) {
            context.onItemClick(v,getPosition());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
