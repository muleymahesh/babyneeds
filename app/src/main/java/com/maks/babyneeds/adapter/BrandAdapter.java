package com.maks.babyneeds.adapter;

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

import com.maks.babyneeds.Activity.BrandListActivity;
import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.Utility.Utils;
import com.maks.model.Brand;
import com.maks.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Belal on 11/9/2015.
 */
public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {

    private BrandListActivity context;
    OnItemClickListener mItemClickListener;
    //List of Category
    List<Brand> Category;
    Activity activity;

    public BrandAdapter(List<Brand> Category, BrandListActivity context){
        super();
        //Getting all the Category
        this.Category = Category;
        this.context = context;
        activity = (Activity)context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_brand_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Brand product = Category.get(position);

        if (product.getBrandImg() != null && !product.getBrandImg().isEmpty()){
            Picasso.with(context).load(Constants.PRODUCT_IMG_PATH + product.getBrandImg()).resize(300, 300).centerInside().placeholder(R.drawable.baby_bg).error(R.drawable.baby_bg).into(holder.imageView);
        }else{
            holder.imageView.setImageResource(R.drawable.baby_bg);
        }

        holder.textViewName.setText(product.getName());
    }

    @Override
    public int getItemCount() {
        return Category.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        public TextView textViewName;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            textViewName = (TextView) itemView.findViewById(R.id.title1);

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
