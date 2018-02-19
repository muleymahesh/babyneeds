package com.maks.babyneeds.adapter;

/**
 * Created by maks on 7/2/16.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Activity.NewArrivalActivity;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.Utility.Utils;
import com.maks.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Belal on 11/9/2015.
 */
public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.ViewHolder> {

    private NewArrivalActivity context;
    OnItemClickListener mItemClickListener;
    //List of Category
    List<Product> Category;
    Activity activity;

    public NewProductAdapter(List<Product> Category, NewArrivalActivity context){
        super();
        //Getting all the Category
        this.Category = Category;
        this.context = context;
        activity = (Activity)context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_product_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Product product =  Category.get(position);
        Log.e("",product.getImgs().get(0).getImg_url());
        Picasso.with(context).load(Constants.PRODUCT_IMG_PATH+product.getImgs().get(0).getImg_url()).centerInside().resize(300,300).placeholder(R.drawable.baby_bg).into(holder.imageView);
        holder.textViewName.setText(product.getProduct_name());

        holder.textDisc.setText(product.getOffer_name());

        if(product.getOffer_name().equalsIgnoreCase("no offer") ){
            holder.textPrice.setText("Rs. " +product.getMrp());
            holder.textDisc.setVisibility(View.INVISIBLE);
        } else {
            holder.textDisc.setVisibility(View.VISIBLE);
            try {

                SpannableString spannable = new SpannableString("Rs. " + product.getMrp() + " Rs. " + Utils.discountPrice(product.getMrp(), product.getPer_discount()));
                spannable.setSpan(new StrikethroughSpan(), 0, product.getMrp().length() + 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.textPrice.setText(spannable);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public int getItemCount() {
        return Category.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        public TextView textViewName;
        public TextView textPrice;
        public TextView textDisc;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            textViewName = (TextView) itemView.findViewById(R.id.title1);
            textDisc = (TextView) itemView.findViewById(R.id.discount);
            textPrice = (TextView) itemView.findViewById(R.id.price);
//            textViewName.setTypeface(Utils.setLatoFontBold(activity));

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
