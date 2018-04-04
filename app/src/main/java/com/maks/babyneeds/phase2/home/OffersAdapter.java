
package com.maks.babyneeds.phase2.home;

/**
 * Created by maks on 7/2/16.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.Utility.Utils;
import com.maks.model.BannerPojo;
import com.maks.model.Category;
import com.maks.model.Offer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

/**
 * Created by Belal on 11/9/2015.
 */
public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    // private CategoryActivity context;
    OnItemClickListener mItemClickListener;
    //List of Category
    List<Offer> offersList;
    Context context;

    public OffersAdapter(List<Offer> offersList, Context context){
        super();
        //Getting all the offersList
        this.offersList = offersList;
        this.context =context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_offer, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Offer offer = offersList.get(position);
        Picasso.with(context).load(Constants.PRODUCT_IMG_PATH + offer.getOfferImg()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {

        return offersList.size()>5 ? 5 : offersList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        public  BannerSlider bannerSlider;
        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.img);
            if(imageView !=null){
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                itemView.setOnClickListener(this);
            }

        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener!=null){
                mItemClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
