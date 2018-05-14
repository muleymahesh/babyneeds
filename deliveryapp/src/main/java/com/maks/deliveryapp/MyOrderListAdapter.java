package com.maks.deliveryapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by maks on 6/5/18.
 */

class MyOrderListAdapter extends RecyclerView.Adapter {
    // private CategoryActivity context;
    Context context;
    //List of Category
    List<OrderPojo> Category;
    MainActivity activity;

    public MyOrderListAdapter(List<OrderPojo> Category, MainActivity context) {
        super();
        this.context = context;
        //Getting all the Category
        this.Category = Category;
        activity =  context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_iem_my_orders, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {

        final OrderPojo category = Category.get(position);
        MyViewHolder holder = (MyViewHolder) holder1;
        holder.txtOrderDate.setText(category.getDate().replace("between","time")+"\n Status : "+category.getOrder_status());
        holder.txtOrderPrice.setText("Rs. " + category.getAmount() );
        holder.txtOrderId.setText("Order Id - "+category.getOId());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.txtAddress.setText(Html.fromHtml(category.getAddress(), Html.FROM_HTML_MODE_COMPACT));
        }else
        {
            holder.txtAddress.setText(Html.fromHtml(category.getAddress()));
        }
        if(category.getOrder_status().equalsIgnoreCase("delivered")){
            holder.btnPayment.setVisibility(View.GONE);
            holder.btnDelivered.setVisibility(View.GONE);
            holder.txtOrderDate.setTextColor(Color.RED);
        }else{
            holder.btnPayment.setVisibility(View.VISIBLE);
            holder.btnDelivered.setVisibility(View.VISIBLE);
            holder.txtOrderDate.setTextColor(Color.BLUE);
        }

        holder.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            activity.completeOrder(category.getOId());
            }
        });

        holder.layoutMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, OrderDetailActivity.class);
//                intent.putExtra("order", category);
//
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Category.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        //public ImageView imageView;
        public TextView txtOrderDate, txtOrderId, txtOrderPrice, txtAddress;
        public ConstraintLayout layoutMyOrder;
        Button btnPayment, btnDelivered;

        public MyViewHolder(View itemView) {
            super(itemView);
            layoutMyOrder = (ConstraintLayout) itemView.findViewById(R.id.topPanel);
            txtOrderDate = (TextView) itemView.findViewById(R.id.txtDeliverydate);
            txtOrderId = (TextView) itemView.findViewById(R.id.txtOrderNo);
            txtOrderPrice = (TextView) itemView.findViewById(R.id.txtTotal);
            txtAddress = (TextView) itemView.findViewById(R.id.txtaddress);
            btnDelivered = (Button) itemView.findViewById(R.id.button2);
            btnPayment = (Button) itemView.findViewById(R.id.button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context,OrderDetailActivity.class);
                }
            });
        }
    }

}
