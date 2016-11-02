package com.maks.babyneeds.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.maks.babyneeds.SQLite.SQLiteUtil;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.Utility.Utils;
import com.maks.model.Address;

public class AddressActivity extends AppCompatActivity {


    private EditText txtFName,txtLName, txtCountry,txtAddr,txtMobile,txtState;
    Button orderBtn;
    Spinner spnArea,spnPincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

initView();
    }

    private void initView() {

        txtFName=(EditText)findViewById(R.id.txtFname);
        txtLName=(EditText)findViewById(R.id.txtLname);
//        txtemail=(EditText)findViewById(R.id.txtEmail);
        txtMobile=(EditText)findViewById(R.id.txtPhone);
        txtAddr=(EditText)findViewById(R.id.txtAddr);
        txtState=(EditText)findViewById(R.id.txtAddr);
        txtCountry=(EditText)findViewById(R.id.txtLandmark);
        spnPincode=(Spinner)findViewById(R.id.txtZipcode);
        orderBtn=(Button)findViewById(R.id.btnSave);
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        spnArea=(Spinner)findViewById(R.id.spinner);
        spnArea.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, Constants.inclusions));
        spnPincode.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, Constants.pincodes));

    }

    private void setFonts() {
//        txtemail.setTypeface(Utils.setLatoFontBold(this));
        txtFName.setTypeface(Utils.setLatoFontBold(this));
        txtLName.setTypeface(Utils.setLatoFontBold(this));
        txtState.setTypeface(Utils.setLatoFontBold(this));
        txtCountry.setTypeface(Utils.setLatoFontBold(this));
//        txtPincode.setTypeface(Utils.setLatoFontBold(this));
        txtMobile.setTypeface(Utils.setLatoFontBold(this));
        orderBtn.setTypeface(Utils.setLatoFontBold(this));
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
            txtFName.setError("First name required");
            return;
        }
        if(lname.isEmpty()){
            txtLName.requestFocus();
            txtLName.setError("Last name required");
            return;
        }

        if(addr.isEmpty()){
            txtAddr.requestFocus();
            txtAddr.setError("Address details required");
            return;
        }
        if(mobile.isEmpty()){
            txtMobile.requestFocus();
            txtMobile.setError("Mobile name required");

            return;
        }
        /*if(zipcode.isEmpty() ){
            txtPincode.requestFocus();
            txtPincode.setError("Pincode required");

            return;
        }
        if(!TextUtils.isDigitsOnly(zipcode) || TextUtils.isEmpty(zipcode)){
            txtPincode.requestFocus();
            txtPincode.setError("Invalid Pincode");

            return;
        }*/

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
            finish();
        }catch (Exception e){

            Toast.makeText(AddressActivity.this, "Error occured!", Toast.LENGTH_SHORT).show();

        }

    }
}
