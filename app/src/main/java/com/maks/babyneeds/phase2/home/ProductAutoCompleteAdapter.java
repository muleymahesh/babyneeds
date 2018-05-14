package com.maks.babyneeds.phase2.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.gson.Gson;
import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Activity.SearchResultsActivity;
import com.maks.babyneeds.Utility.Constants;
import com.maks.model.Product;
import com.maks.model.ProductDTO;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by maks on 5/4/18.
 */

public class ProductAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private List<Product> resultList = new ArrayList<Product>();

    public ProductAutoCompleteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Product getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.simple_dropdown_item_2line, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.text1)).setText(getItem(position).getProduct_name());
        ((TextView) convertView.findViewById(R.id.text2)).setText(getItem(position).getShort_desc());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Product> books = findProducts(mContext, constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = books;
                    filterResults.count = books.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<Product>) results.values;

                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    /**
     * Returns a search result for the given book title.
     */
    private List<Product> findProducts(Context context, String bookTitle) {

        try {

            Response response = null;
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Log.e("request","{\"method\":\"search_product\",\"query\":\""+bookTitle+"\"}");
        RequestBody body = RequestBody.create(JSON, "{\"method\":\"search_product\",\"query\":\""+bookTitle+"\"}");
        Request request = new Request.Builder()
                .url (Constants.WS_URL)
                .post(body)
                .build();

            response = client.newCall(request).execute();

            ProductDTO arr = new Gson().fromJson(response.body().string(), ProductDTO.class);

            Collections.sort(arr.getData(), new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    String query = bookTitle.toString();
                    String arr[] = query.split(" ");
                    String PREFIX = arr[0];
                    if (o1.getProduct_name().contains(PREFIX) && o2.getProduct_name().contains(PREFIX)) return o1.getProduct_name().compareTo(o2.getProduct_name());
                    if (o1.getProduct_name().contains(PREFIX) && !o2.getProduct_name().contains(PREFIX)) return -1;
                    if (!o1.getProduct_name().contains(PREFIX) && o2.getProduct_name().contains(PREFIX)) return 1;
                    return 0;
                }
            });
            return arr.getData();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

}
