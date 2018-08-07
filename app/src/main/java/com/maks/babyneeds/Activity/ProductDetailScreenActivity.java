package com.maks.babyneeds.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maks.babyneeds.SQLite.SQLiteUtil;
import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.Utility.Utils;
import com.maks.babyneeds.Utility.ZoomableImageView;
import com.maks.model.BannerPojo;
import com.maks.model.Product;
import com.maks.model.ProductDTO;
import com.maks.model.ProductImage;
import com.maks.model.ShoppingCart;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.events.OnBannerClickListener;
import ss.com.bannerslider.views.BannerSlider;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductDetailScreenActivity extends AppCompatActivity {
    private Product product;
    private ImageView imgViewq, btnPlus, btnMinus;
    private TextView txtName, txtShortDesc, txtLongDesc, mrp, offer, size, brand;
    private TextView txtQuantity, stock, expiry, review , rating;
    RatingBar ratingBar;
    Button btnAddToCart, add_to_fav,writereview;
    private Toolbar toolbar;
    LinearLayout llreview;
    BannerSlider viewPager;
    boolean isFav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_detail_screen);

        setFont();
        initToolbar();
        initView();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ProductDetailTask().execute(Constants.WS_URL,"{\"method\":\"get_product_detail\",\"p_id\":\""+getIntentData()+"\", \"email\":\""+ new AppPreferences(ProductDetailScreenActivity.this).getEmail()+"\"}");
    }




    private void parseData(String array){

        try {
            ProductDTO arr = new Gson().fromJson(array.toString(), ProductDTO.class);
            product = arr.getData().get(0);
            isFav = product.getIsFav().equals("true") ? true : false;
            setListeners();
            setView();

        }catch (Exception e){e.printStackTrace();}//Finally initializig our adapter

    }


    class ProductDetailTask extends AsyncTask<String, Void,String>{
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(ProductDetailScreenActivity.this);
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
                    parseData(s);//new JSONObject(s).getJSONArray("data")

                }catch(Exception e)
                {e.printStackTrace();}
            }
        }
    }

    private void setListeners() {


        writereview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDetailScreenActivity.this,WriteReviewActivity.class);
                i.putExtra("product",product);
                startActivity(i);

            }
        });
        llreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ProductDetailScreenActivity.this,ReviewActivity.class);
                i.putExtra("product",product);
                startActivity(i);

            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!txtQuantity.getText().toString().isEmpty() || !txtQuantity.getText().toString().equals("0")) {
                    txtQuantity.setText("" + (Integer.parseInt(txtQuantity.getText().toString()) - 1));

                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtQuantity.setText("" + (Integer.parseInt(txtQuantity.getText().toString()) + 1));
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShoppingCart cart = new ShoppingCart();
                cart.setProduct_id(product.getP_id());
                cart.setProduct(product);
                cart.setQuantity(txtQuantity.getText().toString());

                if (!cart.getQuantity().isEmpty() && !txtQuantity.getText().toString().equals("0")) {
                    if (Integer.parseInt(product.getStock()) < Integer.parseInt(cart.getQuantity())) {
                        Toast.makeText(ProductDetailScreenActivity.this, "Ordered quantity exceeds available stock!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SQLiteUtil dbUtil = new SQLiteUtil();
                    dbUtil.insert(cart, ProductDetailScreenActivity.this);


                    Intent intent = new Intent(getApplicationContext(), MyCartActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    txtQuantity.setError("add quantity!!!");
                    Toast.makeText(ProductDetailScreenActivity.this, "Enter quantity to purchase.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        if (!isFav) {
            add_to_fav.setBackgroundResource(R.drawable.fav_unselected);
        } else {
            add_to_fav.setBackgroundResource(R.drawable.fav_selected);
        }


        add_to_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new AppPreferences(ProductDetailScreenActivity.this).isLogin()) {

                    if (!isFav) {
                        new AddToFavTask(ProductDetailScreenActivity.this).execute(Constants.WS_URL, "{\"method\":\"add_fav\",\"user_id\":\"" + new AppPreferences(ProductDetailScreenActivity.this).getEmail() + "\",\"p_id\":\"" + product.getP_id() + "\"}");
                    } else {
                        new RemoveFromFavTask(ProductDetailScreenActivity.this).execute(Constants.WS_URL, "{\"method\":\"delete_fav\",\"user_id\":\"" + new AppPreferences(ProductDetailScreenActivity.this).getEmail() + "\",\"p_id\":\"" + product.getP_id() + "\"}");
                    }
                } else {
                    Intent intent4 = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent4);

                }
            }
        });
    }

    private void initView() {
        viewPager = (BannerSlider) findViewById(R.id.viewpager);
        viewPager.setOnBannerClickListener(new OnBannerClickListener() {

            @Override
            public void onClick(int position) {
                Intent intent =new Intent(ProductDetailScreenActivity.this,ImageZoomActivity.class);
                intent.putExtra("url",Constants.PRODUCT_IMG_PATH+product.getImgs().get(position).getImg_url());
                startActivity(intent);

            }
        });
//        imgView=(ImageView)findViewById(R.id.img);
        btnPlus = (ImageView) findViewById(R.id.btnPlus);
        btnMinus = (ImageView) findViewById(R.id.btnMinus);
        txtName = (TextView) findViewById(R.id.title1);
        txtShortDesc = (TextView) findViewById(R.id.short_desc);
        txtLongDesc = (TextView) findViewById(R.id.long_desc);
        offer = (TextView) findViewById(R.id.offer);
        brand = (TextView) findViewById(R.id.txtBrand);
        size = (TextView) findViewById(R.id.txtSize);
        mrp = (TextView) findViewById(R.id.txtMrp);
        stock = (TextView) findViewById(R.id.txtStock);
        expiry = (TextView) findViewById(R.id.txtExpiry);
        review = (TextView) findViewById(R.id.txtNumReview);
        rating = (TextView) findViewById(R.id.txtRating);
        llreview = (LinearLayout) findViewById(R.id.llreview);
        add_to_fav = (Button) findViewById(R.id.add_to_fav);
        writereview = (Button) findViewById(R.id.writeReview);
        txtQuantity = (TextView) findViewById(R.id.quantity);
        btnAddToCart = (Button) findViewById(R.id.addToCartButton);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private void setFont() {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto_Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Product Detail");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }

    private void setView() {

        try {


                List<Banner> banners=new ArrayList<>();
                //add banner using image url
                for (ProductImage b: product.getImgs()
                        ) {
                    RemoteBanner banner = new RemoteBanner(Constants.PRODUCT_IMG_PATH+b.getImg_url());
                    banner.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    banners.add(banner);
                }

                viewPager.setBanners(banners);
        } catch (Exception e) {
            Log.e("", Constants.PRODUCT_IMG_PATH + product.getImgs().toString());
            e.printStackTrace();
        }

        txtName.setText(product.getProduct_name());
        txtShortDesc.setText(product.getShort_desc());
        txtLongDesc.setText(product.getShort_desc() + "\n" + product.getLong_desc());
        brand.setText("Brand          : " + product.getBrand_name());
        size.setText("Size           : " + product.getSize());
        if (product.getSize().contains("NA")) {
            size.setVisibility(View.GONE);
        }
//
//        SpannableString spannable = new SpannableString("Rs. "+product.getMrp()+" Rs. "+Utils.discountPrice(product.getMrp(),product.getPer_discount()));
//        spannable.setSpan(new StrikethroughSpan(),0,product.getMrp().length()+3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        mrp.setText("Price : ");
//        mrp.append(spannable);

        if (product.getOffer_name().equalsIgnoreCase("no offer")) {
            offer.setVisibility(View.INVISIBLE);
            mrp.setText("Price           : Rs. " + product.getMrp());

        } else {
            offer.setText(product.getPer_discount() + "%");

            try {

                SpannableString spannable = new SpannableString("Rs. " + product.getMrp() + " Rs. " + Utils.discountPrice(product.getMrp(), product.getPer_discount()));
                spannable.setSpan(new StrikethroughSpan(), 0, product.getMrp().length() + 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mrp.setText("Price           : ");
                mrp.append(spannable);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //offer.setText(""+product.getOffer_name());

        if (product.getStock().equals("0")) {

            stock.setText("In stock      : Out of stock \n call on +9891850708 to arrange to for this item");
            btnMinus.setVisibility(View.INVISIBLE);
            btnPlus.setVisibility(View.INVISIBLE);
            txtQuantity.setVisibility(View.INVISIBLE);

        }else if (product.getStock().equals("1")) {

            stock.setText("In stock   : Only one left in stock. \n call on +9891850708 for more order");

        } else {
            stock.setText("In stock   : Available ");
            stock.setVisibility(View.VISIBLE);
        }
        if (product.getExpiry_date().trim().equalsIgnoreCase("NA")) {
            expiry.setVisibility(View.GONE);
        }
        else{
            expiry.setText("Expiry date : " + product.getExpiry_date());

            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(500); //You can manage the blinking time with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            expiry.startAnimation(anim);
        }

//        if(product.getFav.equals("NA") )  expiry.setVisibility(View.GONE);
//        else expiry.setText("Expiry date : "+product.getExpiry_date());

//        offer.setText(""+(product.getPer_discount())+"%");

        review.setText("("+product.getReviews()+")");
        try {
            ratingBar.setRating(Math.round(Float.valueOf(product.getAvgRating())));
            rating.setText(""+Math.round(Float.valueOf(product.getAvgRating()))+"/5");
        }catch (Exception e){e.printStackTrace();}
    }

    public String getIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Product product = (Product) bundle.getSerializable("product");
        return product.getP_id();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings: {
                startActivity(new Intent(this, MyCartActivity.class));
                return true;
            }

        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }


    class AddToFavTask extends AsyncTask<String, Void, String> {

        ProgressDialog pd;

        public AddToFavTask(Context context) {
            this.pd = pd;
            pd = new ProgressDialog(context);

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
            Log.e("request", ulr[1]);
            RequestBody body = RequestBody.create(JSON, ulr[1]);
            Request request = new Request.Builder()
                    .url(ulr[0])
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
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (s != null) {
                try {
                    Log.e("response", s);
                    Toast.makeText(ProductDetailScreenActivity.this, "" + new JSONObject(s).getString("responseMessage"), Toast.LENGTH_SHORT).show();
                    if (s.contains("already")) {

                        isFav = true;
                        add_to_fav.setBackgroundResource(R.drawable.fav_selected);
                    } else {
                        isFav = true;
                        add_to_fav.setBackgroundResource(R.drawable.fav_selected);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    class RemoveFromFavTask extends AsyncTask<String, Void, String> {

        ProgressDialog pd;

        public RemoveFromFavTask(Context context) {
            this.pd = pd;
            pd = new ProgressDialog(context);

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
            Log.e("request", ulr[1]);
            RequestBody body = RequestBody.create(JSON, ulr[1]);
            Request request = new Request.Builder()
                    .url(ulr[0])
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
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (s != null) {
                try {
                    Log.e("response", s);
                    Toast.makeText(ProductDetailScreenActivity.this, "" + new JSONObject(s).getString("responseMessage"), Toast.LENGTH_SHORT).show();
                    if (s.contains("success")) {
                        isFav = false;
                        add_to_fav.setBackgroundResource(R.drawable.fav_unselected);
                    } else {
                        isFav = false;
                        add_to_fav.setBackgroundResource(R.drawable.fav_unselected);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
