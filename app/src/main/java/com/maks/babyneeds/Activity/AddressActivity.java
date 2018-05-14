package com.maks.babyneeds.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.maks.babyneeds.SQLite.SQLiteUtil;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.Utility.Utils;
import com.maks.model.Address;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressActivity extends AppCompatActivity {


    @BindView(R.id.txtFname) EditText txtFName;
    @BindView(R.id.txtLname) EditText txtLName;
    @BindView(R.id.txtLandmark) EditText txtCountry;
    @BindView(R.id.txtAddr) EditText txtAddr;
    @BindView(R.id.txtPhone) EditText txtMobile;
//    @BindView(R.id.txtAddr) EditText txtState;
    @BindView(R.id.btnSave) Button orderBtn;
    @BindView(R.id.spinner) Spinner spnArea;
    @BindView(R.id.spnPincode) Spinner spnPincode;
    @BindView(R.id.spnTimeslot) Spinner spnTimeSlot;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private DatePickerDialog fromDatePickerDialog;
//    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    @BindView(R.id.txtDate) TextView txtDate;

    String amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        initView();
        amount= getIntent().getStringExtra("amount");
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtFName.getWindowToken(), 0);

        initToolbar();
    }

    private void initView() {

        Address address = new SQLiteUtil().getAddress(this);

        txtFName.setText(address.getFname());
        txtMobile.setText(address.getPhone());
        txtAddr.setText(address.getAddr());
        txtCountry.setText(address.getLandmark());

        spnArea.setSelection(Arrays.asList(Constants.inclusions).indexOf(address.getArea()));
        spnPincode.setSelection(Arrays.asList(Constants.pincodes).indexOf(address.getZipcode()));
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        spnArea.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, Constants.inclusions));
        spnPincode.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, Constants.pincodes));
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == txtDate) {
                    fromDatePickerDialog.show();
                }
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
    public void validate(){


        final String fName = txtFName.getText().toString();
        final String lname = txtLName.getText().toString();
//        final String email = txtemail.getText().toString();
        final String landmark = txtCountry.getText().toString();
        final String area = spnArea.getSelectedItem().toString();
        final String mobile = txtMobile.getText().toString();
        final String addr = txtAddr.getText().toString();
        final String zipcode = spnPincode.getSelectedItem().toString();

        if(fName.isEmpty()){
            txtFName.requestFocus();
            txtFName.setError("Name required");
            return;
        }
//        if(lname.isEmpty()){
//            txtLName.requestFocus();
//            txtLName.setError("Last name required");
//            return;
//        }

        if(addr.isEmpty()){
            txtAddr.requestFocus();
            txtAddr.setError("Address details required");
            return;
        }
        if(mobile.isEmpty()){
            txtMobile.requestFocus();
            txtMobile.setError("Mobile name required");

            return;
        }if(mobile.length()<10){
            txtMobile.requestFocus();
            txtMobile.setError("Check mobile number");

            return;
        }
        if(txtDate.getText().toString().isEmpty()){
            txtDate.setError("Required field");
            txtDate.requestFocus();
            return;
        }

        Address address = new Address();
        address.setFname(fName);
        address.setLname(lname);
//        address.setEmail();
        address.setPhone(mobile);
        address.setArea(area);
        address.setAddr(addr);
        address.setLandmark(landmark);
        address.setZipcode(zipcode);

        try {

            new SQLiteUtil().addAddress(address, this);
            Toast.makeText(AddressActivity.this, "Address added!!!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,PlaceOrderActivity.class);
            intent.putExtra("date",txtDate.getText().toString());
            intent.putExtra("slot",spnTimeSlot.getSelectedItem().toString());
            intent.putExtra("amount",amount);

            startActivity(intent);
            finish();
        }catch (Exception e){

            Toast.makeText(AddressActivity.this, "Error occured!", Toast.LENGTH_SHORT).show();

        }

    }


    private void setDateTimeField() {

        final Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE,30);

                if (newDate.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY){
                    Toast.makeText(AddressActivity.this, "We have weekly off on Tuesday", Toast.LENGTH_SHORT).show();
                    txtDate.setText("");
                }else
                if (newDate.get(Calendar.DAY_OF_YEAR)>(cal.get(Calendar.DAY_OF_YEAR)+10)){
                    Toast.makeText(AddressActivity.this, "Please select delivery date within next ten days.", Toast.LENGTH_SHORT).show();
                    txtDate.setText("");
                }else
                if(newDate.after(newCalendar) && newDate.get(Calendar.DAY_OF_YEAR)==cal.get(Calendar.DAY_OF_YEAR)){
                    txtDate.setText(dateFormatter.format(newDate.getTime()));

                    if(cal.get(Calendar.HOUR_OF_DAY)<9){
                        spnTimeSlot.setAdapter(new ArrayAdapter<String>(AddressActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"9 - 11 AM","11 - 1 PM","1 - 3 PM","3 - 5 PM","5 - 7 PM"}));
/*                        spnTimeSlot.setAdapter(new ArrayAdapter<String>(AddressActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"9 - 11 AM","11 - 1 PM","1 - 3 PM","3 - 5 PM"}));*/
                    }else
                    if(cal.get(Calendar.HOUR_OF_DAY)<11){
                        spnTimeSlot.setAdapter(new ArrayAdapter<String>(AddressActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"11 - 1 PM","1 - 3 PM","3 - 5 PM","5 - 7 PM"}));
                        /*spnTimeSlot.setAdapter(new ArrayAdapter<String>(AddressActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"11 - 1 PM","1 - 3 PM","3 - 5 PM"}));*/
                    }else
                    if(cal.get(Calendar.HOUR_OF_DAY)<13){
                        spnTimeSlot.setAdapter(new ArrayAdapter<String>(AddressActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"1 - 3 PM","3 - 5 PM","5 - 7 PM"}));
                        /*spnTimeSlot.setAdapter(new ArrayAdapter<String>(AddressActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"1 - 3 PM","3 - 5 PM"})); */
                    }else
                    if(cal.get(Calendar.HOUR_OF_DAY)<15){
                        spnTimeSlot.setAdapter(new ArrayAdapter<String>(AddressActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"3 - 5 PM","5 - 7 PM"}));
                      /*  spnTimeSlot.setAdapter(new ArrayAdapter<String>(AddressActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"3 - 5 PM"})); */
                    }else
                    if(cal.get(Calendar.HOUR_OF_DAY)<17){
                        spnTimeSlot.setAdapter(new ArrayAdapter<String>(AddressActivity.this,android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"5 - 7 PM"}));
                    }else{
                        Toast.makeText(AddressActivity.this, "Please select future dates for delivery.", Toast.LENGTH_SHORT).show();
                        txtDate.setText("");
                    }

                }else
                if(newDate.after(newCalendar) && newDate.get(Calendar.DAY_OF_YEAR)>cal.get(Calendar.DAY_OF_YEAR)) {
                    txtDate.setText(dateFormatter.format(newDate.getTime()));
                    spnTimeSlot.setAdapter(new ArrayAdapter<String>(AddressActivity.this,android.R.layout.simple_spinner_dropdown_item,
                            new String[]{"9 - 11 AM","11 - 1 PM","1 - 3 PM","3 - 5 PM","5 - 7 PM"}));
                    /*spnTimeSlot.setAdapter(new ArrayAdapter<String>(AddressActivity.this,android.R.layout.simple_spinner_dropdown_item,
                            new String[]{"9 - 11 AM","11 - 1 PM","1 - 3 PM","3 - 5 PM"}));*/

                }
                else{
                    Toast.makeText(AddressActivity.this, "Please select future dates for delivery.", Toast.LENGTH_SHORT).show();
                    txtDate.setText("");
                }

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
}
