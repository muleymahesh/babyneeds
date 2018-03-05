
package com.maks.babyneeds.phase2.home;

/**
 * Created by maks on 7/2/16.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Utility.Constants;
import com.maks.model.Brand;
import com.maks.model.Offer;
import com.squareup.picasso.Picasso;

import java.util.List;

import ss.com.bannerslider.views.BannerSlider;

/**
 * Created by Belal on 11/9/2015.
 */
public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {

    // private CategoryActivity context;
    OnItemClickListener mItemClickListener;
    //List of Category
    List<Brand> Category;
    HomeFragment context;

    public BrandAdapter(List<Brand> Category, HomeFragment context){
        super();
        //Getting all the Category
        this.Category = Category;
        this.context =context;
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
        Brand offer = Category.get(position);
            Picasso.with(context.getContext()).load(Constants.PRODUCT_IMG_PATH + offer.getBrandImg()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {

        return Category.size()>6 ? 6 :Category.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        public  BannerSlider bannerSlider;
        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.img);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            if(imageView !=null){
                itemView.setOnClickListener(this);
            }

        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener!=null){
                mItemClickListener.onItemClick(v,getPosition());
            }
        }
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener( OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
