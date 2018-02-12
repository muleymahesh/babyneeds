
package com.maks.babyneeds.phase2.services;

/**
 * Created by maks on 7/2/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Utility.Utils;
import com.maks.model.Service;

import java.util.List;


public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private ServicesFragment context;
    OnItemClickListener mItemClickListener;
    //List of Category
    List<Service> doctors;
    Activity activity;

    public DoctorAdapter(List<Service> doctors, ServicesFragment context){
        super();
        //Getting all the Category
        this.doctors = doctors;
        this.context = context;
        activity = (Activity)context.getActivity();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_doctor_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Service doctor = doctors.get(position);

        //  Picasso.with(context).load(category.getCatImg_url()).resize(400,220).centerCrop().into(holder.imageView);
        holder.textViewName.setText(doctor.getName());
        holder.textViewAddress.setText(doctor.getAddress());
        holder.textViewContact.setText(doctor.getContact());
        if (doctor.getDetail().length() > 50) {
            holder.textViewDetail.setText(doctor.getDetail().substring(0,50)+"...");
        }else{
            holder.textViewDetail.setText(doctor.getDetail());
        }
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewName;
        public TextView textViewAddress;
        public TextView textViewContact;
        public TextView textViewDetail;

        public ImageView callImg;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewName = (TextView) itemView.findViewById(R.id.doc_name);
            textViewAddress = (TextView) itemView.findViewById(R.id.address);
            textViewContact = (TextView) itemView.findViewById(R.id.contact_no);
            textViewDetail = (TextView) itemView.findViewById(R.id.detail);
            callImg=(ImageView)itemView.findViewById(R.id.imgCall);
            textViewName.setTypeface(Utils.setLatoFontBold(activity));
            textViewAddress.setTypeface(Utils.setLatoFontBold(activity));

            callImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+textViewContact.getText().toString()));
//                    activity.startActivity(callIntent);
                }
            });
         //   textViewContact.setTypeface(Utils.setLatoFontBold(activity));
        }

        @Override
        public void onClick(View v) {

//            context.onItemClick(v,getPosition());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
