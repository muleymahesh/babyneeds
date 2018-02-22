package com.maks.babyneeds.adapter;

/**
 * Created by maks on 3/19/2017.
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maks.babyneeds.Activity.MyOrderDetailsActivity;
import com.maks.babyneeds.Activity.R;
import com.maks.model.OrderPojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Deva on 08/03/2017.
 */

public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.ViewHolder> {

    // private CategoryActivity context;
    Context context;
    OnItemClickListener mItemClickListener;
    //List of Category
    List<OrderPojo> Category;
    Activity activity;

    public MyOrderListAdapter(List<OrderPojo> Category, Context context) {
        super();
        this.context = context;
        //Getting all the Category
        this.Category = Category;
        activity = (Activity) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_my_orders, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final OrderPojo category = Category.get(position);

        holder.txtOrderDate.setText("Delivery date - "+category.getDate()+"\n"+category.getOrder_status());
        holder.txtOrderPrice.setText("Rs. " + category.getAmount() +"\n"+category.getShippingType());
        holder.txtOrderId.setText("Order Id - "+category.getOId());
        holder.layoutMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyOrderDetailsActivity.class);
                intent.putExtra("OrderList", (Serializable) category.getDetails());
                intent.putExtra("TotalAmount", category.getAmount());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Category.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //public ImageView imageView;
        public TextView txtOrderDate, txtOrderId, txtOrderPrice;
        public LinearLayout layoutMyOrder;

        public ViewHolder(View itemView) {
            super(itemView);
            layoutMyOrder = (LinearLayout) itemView.findViewById(R.id.layoutMyOrder);
            txtOrderDate = (TextView) itemView.findViewById(R.id.txtMyOrderDate);
            txtOrderId = (TextView) itemView.findViewById(R.id.txtMyOrderOrderId);
            txtOrderPrice = (TextView) itemView.findViewById(R.id.txtMyOrderRupee);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MyOrderDetailsActivity.class);
                }
            });
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
