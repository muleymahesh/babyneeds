package com.maks.babyneeds.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.Utility.ConnectionDetector;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.adapter.ProductReviewsAdapter;
import com.maks.babyneeds.phase2.services.FavProductAdapter;
import com.maks.babyneeds.phase2.services.FavoriteFragment;
import com.maks.model.ReviewDTO;

public class ReviewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
    }
    private void getData(){
        if(new ConnectionDetector(getContext()).isConnectingToInternet()) {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            JsonObject json = new JsonObject();
            json.addProperty("method", "get_product_review");
            json.addProperty("p_id", getIntent().getStringExtra("p_id"));

            pd.show();

            Ion.with(getContext())
                    .load(Constants.WS_URL)
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error
                            if(pd!=null && pd.isShowing()){
                                pd.dismiss();
                            }
                            if(e==null){
                                ReviewDTO dto = new Gson().fromJson(result,ReviewDTO.class);

                                listCategory.addAll(dto.getData());
                                if(listCategory.isEmpty()){
//                                       Toast.makeText(getContext(), "No data. Click Heart on product detail to add to Wishlist", Toast.LENGTH_SHORT).show();
                                }
                                adapter = new ProductReviewsAdapter(listCategory,ReviewActivity.this);

                                recyclerView.setAdapter(adapter);
                            }
                        }
                    });

        }else{
            Toast.makeText(getContext(), "You are offline!.", Toast.LENGTH_SHORT).show();
        }
    }
}
