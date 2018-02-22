
package com.maks.babyneeds.phase2.home;

/**
 * Created by maks on 7/2/16.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.Utility.Utils;
import com.maks.babyneeds.phase2.categories.CategoryFragment;
import com.maks.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

import ss.com.bannerslider.views.BannerSlider;

/**
 * Created by Belal on 11/9/2015.
 */
public class CatgoryAdapter extends RecyclerView.Adapter<CatgoryAdapter.ViewHolder> {

   // private CategoryActivity context;
    OnItemClickListener mItemClickListener;
    //List of Category
    List<Category> Category;
    HomeFragment context;

    public CatgoryAdapter(List<Category> Category, HomeFragment context){
        super();
        //Getting all the Category
        this.Category = Category;
        this.context =context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_card, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);

            return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            Category category = Category.get(position);
            Picasso.with(context.getContext()).load(Constants.PRODUCT_IMG_PATH + category.getCat_img()).resize(400, 220).centerCrop().into(holder.imageView);
            holder.textViewName.setText(category.getCat_name());

    }

    @Override
    public int getItemCount() {

        return Category.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        public TextView textViewName;
        public  BannerSlider bannerSlider;
        public ViewHolder(View itemView) {
            super(itemView);

                imageView = (ImageView) itemView.findViewById(R.id.img);
                textViewName = (TextView) itemView.findViewById(R.id.title1);
                if(textViewName!=null){
                    itemView.setOnClickListener(this);
                    textViewName.setTypeface(Utils.setLatoFontBold(context.getActivity()));
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
