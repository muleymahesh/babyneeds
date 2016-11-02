package com.maks.babyneeds.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.adapter.CatgoryAdapter;
import com.maks.babyneeds.adapter.DrawerListAdapter;
import com.maks.model.BannerPojo;
import com.maks.model.BannerPojo;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    List<BannerPojo> arr = new ArrayList<>();
    CustomPagerAdapter adapter;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        adapter = new CustomPagerAdapter(this);
        viewPager.setAdapter(adapter);

    }

    class BannerPojoTask extends AsyncTask<String, Void,String> {

        ProgressDialog pd;

        public BannerPojoTask(Context context) {
            this.pd = pd;
            pd= new ProgressDialog(context);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... ulr) {
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Log.e("request",ulr[1]);
            RequestBody body = RequestBody.create(JSON, ulr[1]);
            Request request = new Request.Builder()
                    .url (ulr[0])
                    .post(body)
                    .build();

            try {
                response = client.newCall(request).execute();

                return response.body().string();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(pd!=null && pd.isShowing()){
                pd.dismiss();
            }
            if(s!=null){
                try {
                    Log.e("request",s);
                    parseData(new JSONObject(s).getJSONArray("data"));

                }catch(Exception e)
                {e.printStackTrace();}
            }
        }
    }

    //This method will parse json data
    private void parseData(JSONArray array){
        for(int i = 0; i<array.length(); i++) {
            BannerPojo banner = new BannerPojo();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                //   banner.setCat_img(json.getString(Constants.TAG_CAT_IMG));
                banner.setImagePath(json.getString("image_path"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            arr.add(banner);
        }

        //Finally initializing our adapter
        adapter = new CustomPagerAdapter(HomeActivity.this);

        //Adding adapter to recyclerview
        viewPager.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,MyCartActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;

        public CustomPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(mContext);

            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.pager_item,null);

            collection.addView(layout);

            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return arr.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }
}
