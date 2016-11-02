
package com.maks.babyneeds.adapter;

/**
 * Created by maks on 7/2/16.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maks.babyneeds.Activity.MainActivity;
import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Activity.ServicesCategoryActivity;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.Utility.Utils;
import com.maks.model.Category;
import com.maks.model.ServicesCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Belal on 11/9/2015.
 */
public class ServicesCatgoryAdapter extends RecyclerView.Adapter<ServicesCatgoryAdapter.ViewHolder> {

   // private CategoryActivity context;
   private ServicesCategoryActivity context;
    OnItemClickListener mItemClickListener;
    //List of Category
    List<ServicesCategory> Category;
    Activity activity;

    public ServicesCatgoryAdapter(List<ServicesCategory> Category, ServicesCategoryActivity context){
        super();
        //Getting all the Category
        this.Category = Category;
        this.context = context;
        activity = (Activity)context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_category_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ServicesCategory category =  Category.get(position);
        holder.textViewName.setText(category.getCategory());
    }

    @Override
    public int getItemCount() {
        return Category.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewName;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewName = (TextView) itemView.findViewById(R.id.txtServiceCategory);
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
