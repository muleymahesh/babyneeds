package com.maks.babyneeds.phase2.home;

/**
 * Created by maks on 7/2/16.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maks.babyneeds.Activity.ProductListActivity;
import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.Utility.Utils;
import com.maks.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Belal on 11/9/2015.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {


    OnItemClickListener mItemClickListener;
    //List of recommendList
    List<Product> recommendList;
    HomeFragment activity;

    public ProductAdapter(List<Product> recommendList, HomeFragment context){
        super();
        //Getting all the recommendList
        this.recommendList = recommendList;

        activity = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recomended, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Product product =  recommendList.get(position);

if(product.getImgs()!=null && !product.getImgs().isEmpty())
        Picasso.with(activity.getContext()).load(Constants.PRODUCT_IMG_PATH+product.getImgs().get(0).getImg_url()).centerInside().resize(300,300).placeholder(R.drawable.baby_bg).into(holder.imageView);
        holder.textViewName.setText(product.getProduct_name());

        holder.textDisc.setText(product.getOffer_name());

        if(product.getOffer_name().equalsIgnoreCase("no offer") ){
            holder.textPrice.setText("Rs. " +product.getMrp());

            holder.textDisc.setVisibility(View.INVISIBLE);
        } else {
            holder.textDisc.setVisibility(View.VISIBLE);
//            holder.textDisc.setText(product.getPer_discount() + "%");

            try {

                SpannableString spannable = new SpannableString("Rs. " + product.getMrp() + " Rs. " + Utils.discountPrice(product.getMrp(), product.getPer_discount()));
                spannable.setSpan(new StrikethroughSpan(), 0, product.getMrp().length() + 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.textPrice.setText(spannable);

            } catch (Exception e) {
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItemClickListener!=null)mItemClickListener.onItemClick(v,position);
                }
            });

        
    }

    @Override
    public int getItemCount() {
        return recommendList.size()>8 ? 8 : recommendList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        public TextView textViewName;
        public TextView textPrice;
        public TextView textDisc;
        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.img);
            textViewName = (TextView) itemView.findViewById(R.id.title1);
            textDisc = (TextView) itemView.findViewById(R.id.discount);
            textPrice = (TextView) itemView.findViewById(R.id.price);

        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
