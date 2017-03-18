package com.maks.babyneeds.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.maks.babyneeds.SQLite.SQLiteUtil;
import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.Utility.Utils;
import com.maks.model.Address;
import com.maks.model.CartList;
import com.maks.model.ShoppingCart;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PlaceOrderActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtDate;
    Button orderBtn,btnAddr;

    Spinner spnTimeSlot,spnPaymentType;
    ArrayList<ShoppingCart> list;
    String amount,selected_date;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;


    ArrayList<Address> addresses = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        initToolbar();
        initView();
        setFonts();

        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
       list= CartList.getInstance().getArrayListCart();
        Intent intent=getIntent();
        Bundle bundle = intent.getExtras();
       amount= bundle.getString("amount");
        addresses.clear();
        addresses.addAll(new SQLiteUtil().getAddressList(this));
        ListView addrlist = (ListView)findViewById(R.id.listView);
        MyAdapter adapter = new MyAdapter(this,addresses);
        addrlist.setAdapter(adapter);

    }


    class MyAdapter extends ArrayAdapter<Address>{

        ArrayList<Address> arr;
        public MyAdapter(Context context,ArrayList<Address> arr) {
            super(context,R.layout.addr_item);
            this.arr=arr;
        }

        @Override
        public int getCount() {
            return arr.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView= getLayoutInflater().inflate(R.layout.addr_item,null);

            }
            Address address = arr.get(position);
            String add = address.getFname()+ " "+address.getLname()+ ",\n"+address.getAddr()+", "+address.getArea()+", "+" pincode "+address.getZipcode()+",\n"+address.getPhone();

            TextView txtaddr = (TextView)convertView.findViewById(R.id.textView3);
            txtaddr.setText(add);
//txtaddr.setTypeface(Utils.setLatoFontBold(MyOrdersActivity.this));
            return convertView;
        }
    }

    private void setFonts() {
      //  orderBtn.setTypeface(Utils.setLatoFontBold(this));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        orderBtn=(Button)findViewById(R.id.orderBtn);
        spnTimeSlot =(Spinner)findViewById(R.id.spnTimeslot);
        spnPaymentType =(Spinner)findViewById(R.id.spnPaymentType);

        spnTimeSlot.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,
                new String[]{"9 - 11 AM","11 - 1 PM","1 - 3 PM","3 - 5 PM","5 - 7 PM"}));
       /* spnTimeSlot.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,
                new String[]{"9 - 11 AM","11 - 1 PM","1 - 3 PM","3 - 5 PM"}));*/

        btnAddr=(Button)findViewById(R.id.btnAddr);
        txtDate = (TextView)findViewById(R.id.txtDate);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == txtDate) {
                    fromDatePickerDialog.show();
                }
            }
        });
        btnAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlaceOrderActivity.this,AddressActivity.class));
            }
        });


        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
   setDateTimeField();
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Delivery address");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Delivery Address");

        }
    }

    private void setDateTimeField() {

        final Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                Calendar cal = Calendar.getInstance();

                if (newDate.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY){
                    Toast.makeText(PlaceOrderActivity.this, "We have weekly off on Tuesday", Toast.LENGTH_SHORT).show();
                    txtDate.setText("");
                }else
                if (newDate.get(Calendar.DAY_OF_YEAR)>(cal.get(Calendar.DAY_OF_YEAR)+10)){
                    Toast.makeText(PlaceOrderActivity.this, "Please select delivery date within next ten days.", Toast.LENGTH_SHORT).show();
                    txtDate.setText("");
                }else
                if(newDate.after(newCalendar) && newDate.get(Calendar.DAY_OF_YEAR)==cal.get(Calendar.DAY_OF_YEAR)){
                    txtDate.setText(dateFormatter.format(newDate.getTime()));

                    if(cal.get(Calendar.HOUR_OF_DAY)<9){
                        spnTimeSlot.setAdapter(new ArrayAdapter<String>(PlaceOrderActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"9 - 11 AM","11 - 1 PM","1 - 3 PM","3 - 5 PM","5 - 7 PM"}));
/*                        spnTimeSlot.setAdapter(new ArrayAdapter<String>(PlaceOrderActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"9 - 11 AM","11 - 1 PM","1 - 3 PM","3 - 5 PM"}));*/
                    }else
                    if(cal.get(Calendar.HOUR_OF_DAY)<11){
                        spnTimeSlot.setAdapter(new ArrayAdapter<String>(PlaceOrderActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"11 - 1 PM","1 - 3 PM","3 - 5 PM","5 - 7 PM"}));
                        /*spnTimeSlot.setAdapter(new ArrayAdapter<String>(PlaceOrderActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"11 - 1 PM","1 - 3 PM","3 - 5 PM"}));*/
                    }else
                    if(cal.get(Calendar.HOUR_OF_DAY)<13){
                        spnTimeSlot.setAdapter(new ArrayAdapter<String>(PlaceOrderActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"1 - 3 PM","3 - 5 PM","5 - 7 PM"}));
                        /*spnTimeSlot.setAdapter(new ArrayAdapter<String>(PlaceOrderActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"1 - 3 PM","3 - 5 PM"})); */
                    }else
                    if(cal.get(Calendar.HOUR_OF_DAY)<15){
                        spnTimeSlot.setAdapter(new ArrayAdapter<String>(PlaceOrderActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"3 - 5 PM","5 - 7 PM"}));
                      /*  spnTimeSlot.setAdapter(new ArrayAdapter<String>(PlaceOrderActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"3 - 5 PM"})); */
                    }else
                    if(cal.get(Calendar.HOUR_OF_DAY)<17){
                        spnTimeSlot.setAdapter(new ArrayAdapter<String>(PlaceOrderActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"5 - 7 PM"}));
                    }else{
                        Toast.makeText(PlaceOrderActivity.this, "Please select future dates for delivery.", Toast.LENGTH_SHORT).show();
                        txtDate.setText("");
                    }

                }else
                if(newDate.after(newCalendar) && newDate.get(Calendar.DAY_OF_YEAR)>cal.get(Calendar.DAY_OF_YEAR)) {
                    txtDate.setText(dateFormatter.format(newDate.getTime()));
                    spnTimeSlot.setAdapter(new ArrayAdapter<String>(PlaceOrderActivity.this,android.R.layout.simple_spinner_dropdown_item,
                            new String[]{"9 - 11 AM","11 - 1 PM","1 - 3 PM","3 - 5 PM","b"}));
                    /*spnTimeSlot.setAdapter(new ArrayAdapter<String>(PlaceOrderActivity.this,android.R.layout.simple_spinner_dropdown_item,
                            new String[]{"9 - 11 AM","11 - 1 PM","1 - 3 PM","3 - 5 PM"}));*/

                }
                else{
                    Toast.makeText(PlaceOrderActivity.this, "Please select future dates for delivery.", Toast.LENGTH_SHORT).show();
                    txtDate.setText("");
                }

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(PlaceOrderActivity.this);
            pd.setMessage("Loading...");
            pd.show();
        }


        @Override
        protected String doInBackground(String... ulr) {
            Response response = null;

            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

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
                Log.e("response", s);
                try {
                    JSONObject json = new JSONObject(s);
                    if(json.getString("result").equalsIgnoreCase("success")){
                        new SQLiteUtil().emptyCart(PlaceOrderActivity.this);

                        AlertDialog.Builder alert = new AlertDialog.Builder(PlaceOrderActivity.this);
                        alert.setMessage("Your order placed successfully");
                        alert.setTitle("Thank you !");
                        alert.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PlaceOrderActivity.this.finish();

                            }
                        });
                        alert.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }        // onPostExecute displays the results of the AsyncTask.

    }

    public static String POST(String data){


//        return HttpUtils.requestWebService(Constants.WS_URL, "POST", data);

        Response response = null;
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, data);
        Request request = new Request.Builder()
                .url (Constants.WS_URL)
                .post(body)
                .build();

        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    private void setListeners() {
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(addresses==null || addresses.isEmpty()){
                    Toast.makeText(PlaceOrderActivity.this, "Enter delivery address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(txtDate.getText().toString().isEmpty()){
                    txtDate.setError("Required field");
                    txtDate.requestFocus();
                    return;
                }

                if(spnTimeSlot.getSelectedItem().toString().equals("9 - 11 AM")){

                }

                String data = "\"[";
                String p_id="",qty="";
                for(int i=0;i<list.size();i++){
                    p_id+=list.get(i).getProduct_id()+",";
                    qty+=list.get(i).getQuantity()+",";
                }

                data+="]\"";
                String req="{\"method\":\"add_oder\",\"first_name\":\""+addresses.get(0).getFname()+"\",\"last_name\":\""+addresses.get(0).getLname()+"\"," +
                        "\"gender\":\"Male\",\"email\":\""+new AppPreferences(PlaceOrderActivity.this).getEmail()+"\",\"amount\":\""+amount+
                        "\",\"shipping_type\":\""+spnPaymentType.getSelectedItem().toString()+"\",\"street\":\""+addresses.get(0).getArea()+"\",\"city\":\""+addresses.get(0).getAddr()+"\",\"state\":\""+addresses.get(0).getLandmark()+"\",\"country\":\"India\",\"zipcode\":\""+addresses.get(0).getZipcode()+
                        "\",\"phone\":\""+addresses.get(0).getPhone()+"\",\"order_detail\":\"Delivery Date "+txtDate.getText().toString()+", between "+spnTimeSlot.getSelectedItem().toString()+"\",\"user_id\":\"23\",\"p_id\":\""+p_id+"\",\"qty\":\""+qty+"\"}";


                Log.e("request",req);

                new HttpAsyncTask().execute(Constants.WS_URL,req);


            }
        });
    }


}
