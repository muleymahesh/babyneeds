
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
import com.maks.model.AgeGroup;
import com.squareup.picasso.Picasso;

import java.util.List;

import ss.com.bannerslider.views.BannerSlider;

/**
 * Created by Belal on 11/9/2015.
 */
public class AgeGroupAdapter extends RecyclerView.Adapter<AgeGroupAdapter.ViewHolder> {

   // private AgeGroupActivity context;
    OnItemClickListener mItemClickListener;
    //List of AgeGroup
    List<AgeGroup> AgeGroup;
    HomeFragment context;

    public AgeGroupAdapter(List<AgeGroup> AgeGroup, HomeFragment context){
        super();
        //Getting all the AgeGroup
        this.AgeGroup = AgeGroup;
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
            AgeGroup category = AgeGroup.get(position);
            Picasso.with(context.getContext()).load(Constants.PRODUCT_IMG_PATH + category.getImg_url()).resize(400, 220).centerCrop().into(holder.imageView);
            holder.textViewName.setText(category.getName());
            holder.textViewName.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {

        return AgeGroup.size();
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
