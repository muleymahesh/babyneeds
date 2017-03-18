package com.maks.babyneeds.Activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.Utility.ConnectionDetector;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.adapter.CardArrayAdapter;
import com.maks.babyneeds.adapter.CatgoryAdapter;
import com.maks.babyneeds.adapter.DrawerListAdapter;
import com.maks.babyneeds.adapter.NewProductAdapter;
import com.maks.babyneeds.adapter.ProductAdapter;
import com.maks.model.BannerPojo;
import com.maks.model.Category;
import com.maks.model.HomepageDTO;
import com.maks.model.Product;
import com.maks.model.Service;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CatgoryAdapter.OnItemClickListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ListView cardListView;

    //Creating a List of BannerPojo
    List<BannerPojo> bannerList = new ArrayList<>();
    CustomPagerAdapter adapter;
    ViewPager viewPager;

    boolean isPagerLoaded = false;
    //private List<Product> listCategory = new ArrayList<>();
    private List<Category> listCategory = new ArrayList<>();
    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter productAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initToolbar();
        initDrawer();
        getData();

//        productAdapter = new NewProductAdapter(listCategory, MainActivity.this);
    productAdapter  = new CatgoryAdapter(listCategory,this);
        //Adding adapter to recyclerview
        recyclerView.setAdapter(productAdapter);
    }

    private void getData(){
        if(new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
            new BannerPojoTask(this).execute(Constants.WS_URL,"{\"method\":\"get_all_banner\"}");
        }else{
            Toast.makeText(MainActivity.this, "You are offline!.", Toast.LENGTH_SHORT).show();
        }
    }


    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;

        public CustomPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ImageView img  = (ImageView)  inflater.inflate(R.layout.pager_item, collection, false);
            Picasso.with(MainActivity.this).load(Constants.PRODUCT_IMG_PATH+bannerList.get(position).getImagePath()).placeholder(R.drawable.baby_bg).error(R.drawable.baby_bg).into(img);

            collection.addView(img);
            return img;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return bannerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

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
                    Log.e("response",s);
                    bannerList.clear();
                    parseData(s);

                }catch(Exception e)
                {e.printStackTrace();}
            }
        }
    }

    //This method will parse json data
    private void parseData(String array){

        bannerList.clear();
        listCategory.clear();
        HomepageDTO dto = new Gson().fromJson(array,HomepageDTO.class);
               bannerList.addAll(dto.getData());
        listCategory.addAll(dto.getNew_data());
        //Finally initializing our adapter
        adapter = new CustomPagerAdapter(MainActivity.this);
        viewPager.setAdapter(adapter);
        
        handler.postDelayed(runnable,4000);
        isPagerLoaded = true;
        productAdapter.notifyDataSetChanged();


    }

    private void initDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,MyCartActivity.class));
            return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        ComponentName cn = new ComponentName(this, SearchResultsActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        return true;
    }

    private void initView() {

        LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.header_main,null);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);

        TextView txtDraweruname = (TextView)findViewById(R.id.txtDrawerUserName);
        TextView txtDrawerEmail = (TextView)findViewById(R.id.txtDrawerEmail);
        AppPreferences app = new AppPreferences(this);
        txtDrawerEmail.setText(app.getEmail());
        txtDraweruname.setText(app.getFname());

        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);


        leftDrawerList.setAdapter(new DrawerListAdapter(this, mDrawerItemArray));
        leftDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }
     
    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("Baby Needs");
            setSupportActionBar(toolbar);
        }


    }

    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            navigateTo(position);
        }
    }



    String[] mDrawerItemArray = {"Home",
                                "New arrivals",
                                "Shop by Brands",
                                "My Shopping Cart",
                                "My Wishlist",
                                "My Orders",
                                "Services",
                                "About us",
                                "Feedback",
                                "Change password"};



    private void navigateTo(int position) {
        //drawerLayout.closeDrawer(mDrawerList);
        drawerLayout.closeDrawer(Gravity.LEFT);
        switch (position) {

            case 0://for Dashboard
                break;

            case 1://for new arrivals page
            {
                Intent intent2 = new Intent(getApplicationContext(), WhatsNewActivity.class);
                startActivity(intent2);
                break;
            }

            case 2://for Brand list
            {
                Intent intent32 = new Intent(getApplicationContext(), BrandListActivity.class);
                startActivity(intent32);
                break;
            }
            case 3://for shopping cart
            {
                Intent intent3 = new Intent(getApplicationContext(), MyCartActivity.class);
                startActivity(intent3);
                break;
            }
            case 4://for orders
            {

                if (new AppPreferences(MainActivity.this).isLogin()) {

                    Intent intent3 = new Intent(getApplicationContext(), ProductListActivity.class);
                    intent3.putExtra("getfav","getfav");
                    startActivity(intent3);

                } else {
                    Intent intent4 = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent4);
                }
                break;
            }
            case 5: {
                if (new AppPreferences(MainActivity.this).isLogin()) {
                    Intent intent4 = new Intent(getApplicationContext(), MyOrdersActivity.class);
                    startActivity(intent4);
                } else {
                    Intent intent4 = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent4);
                }
                break;

            }
            case 6://for About us
            {
                Intent intent7 = new Intent(getApplicationContext(), ServicesCategoryActivity.class);
                startActivity(intent7);
                break;


            }
            case 7://for Feedback screen
            {
                Intent intent5 = new Intent(getApplicationContext(), AboutUsActivity.class);
                startActivity(intent5);
                break;

            }
            case 8://for Service catogory list
            {
                Intent intent6 = new Intent(getApplicationContext(), FeedbackActivity.class);
                startActivity(intent6);
                break;
            }

            case 9://for Service catogory list
            {
                if (new AppPreferences(MainActivity.this).isLogin()) {
                    Intent intent4 = new Intent(getApplicationContext(), PasswordResetActivity.class);
                    startActivity(intent4);
                } else {
                    Intent intent4 = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent4);
                }
                break;
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
//        Product product = listCategory.get(position);
//        Intent intent=new Intent(this,ProductDetailScreenActivity.class);
//        intent.putExtra("product", product);
//        startActivity(intent);

        Intent i  = new Intent(this, ProductListActivity.class);
        i.putExtra("cat_id",listCategory.get(position).getCat_id());
        i.putExtra("cat_name",listCategory.get(position).getCat_name());
        startActivity(i);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isPagerLoaded){
            handler.postDelayed(runnable,4000);
            if(bannerList.isEmpty()){
                getData();

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    Handler handler =new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
try {
    int position = viewPager.getCurrentItem();
    if (position < bannerList.size() - 2) {
        viewPager.setCurrentItem(position + 1);
    } else {
        viewPager.setCurrentItem(0);
    }
}catch(Exception e){e.printStackTrace();}

            handler.postDelayed(runnable,8000);

        }
    };
}
