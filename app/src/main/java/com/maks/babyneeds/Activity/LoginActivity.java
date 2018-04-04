package com.maks.babyneeds.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.Utility.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private TextView link_signup, link_forgotpass;
    private static final int FB_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private static final String TAG = "FacebookLogin";
    private GoogleSignInClient mGoogleSignInClient;
    private static final int G_SIGN_IN = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail= (EditText) findViewById(R.id.editTextEmail);
        link_signup= (TextView) findViewById(R.id.link_signup);
        link_forgotpass= (TextView) findViewById(R.id.link_forgotpass);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(this);
        link_signup.setOnClickListener(this);
        link_forgotpass.setOnClickListener(this);


        // [START initialize_fblogin]
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == G_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                Toast.makeText(LoginActivity.this, "Google sign in failed", Toast.LENGTH_SHORT).show();
//                updateUI(null);
                // [END_EXCLUDE]
            }
        }

    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        AppPreferences app = new AppPreferences(LoginActivity.this);
        Log.e("email", user.getEmail());
        app.setEmail(user.getEmail());
        app.setLogin(true);
        app.setFname(user.getDisplayName());
        LoginActivity.this.finish();


    }

    ProgressDialog pd ;
    private void showProgressDialog() {
        pd = new ProgressDialog(this);
        pd.show();
    }
    private void hideProgressDialog() {
        if(pd!=null && pd.isShowing()){
            pd.dismiss();
        }
    }

    private void login(){

        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();

        new HttpAsyncTask().execute("{\"method\":\"login\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}");

    }
    private void startSignIn() {
        // Build FirebaseUI sign in intent. For documentation on this operation and all
        // possible customization see: https://github.com/firebase/firebaseui-android
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent , G_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            if(validate()) {
                login();
            }else{
                Toast.makeText(LoginActivity.this, "Username/password should not be empty.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v==link_signup){
            registerMe();
        }
        else if(v==link_forgotpass){
            forgotPassword();
        }
        if( R.id.sign_in_button==v.getId())
        {
            startSignIn();
        }

    }

    private void forgotPassword() {
        Intent intent=new Intent(getApplicationContext(),ForgotPassActivity.class);
        startActivity(intent);

    }

    private void registerMe() {
        Intent intent=new Intent(getApplicationContext(),RegistrationActivity.class);
        startActivity(intent);

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        ProgressDialog d;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            d = new ProgressDialog(LoginActivity.this);
            d.setMessage("please wait...");
            d.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            return HttpUtils.requestWebService(Constants.WS_URL, "POST",urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(d!=null && d.isShowing()){
                d.dismiss();
            }

if(result!=null) {
    try {
        Log.e("", result);
        JSONObject json = new JSONObject(result);
        if (json.getString("result").equalsIgnoreCase("success")) {
            AppPreferences app = new AppPreferences(LoginActivity.this);
            Log.e("email", json.getString("user_email"));
            app.setEmail(json.getString("user_email"));
            app.setLogin(true);
            Log.e("name", json.getString("fname"));
            app.setFname(json.getString("fname"));
            app.setLogin(true);

//            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
//            LoginActivity.this.startActivity(mainIntent);
            LoginActivity.this.finish();

        } else {
            Toast.makeText(LoginActivity.this, "" + json.getString("responseMessage"), Toast.LENGTH_SHORT).show();
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
}
        }
    }

    private boolean validate(){
        if(editTextEmail.getText().toString().trim().equals(""))
            return false;
        else if(editTextPassword.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }
}
