package com.rapidhelp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;

import com.rapidhelp.R;
import com.rapidhelp.utilities.ConnectionDetector;
import com.rapidhelp.utilities.Constants;
import com.rapidhelp.utilities.DialogAndToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends NetworkBaseActivity{

    private EditText editTextUserID,editTextPassword;
    private TextView textForgotPassword;
    private Button btnLogin,btnSignUp;
    private String userId,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserID=(EditText)findViewById(R.id.edit_user_id);
        editTextPassword=(EditText)findViewById(R.id.edit_password);
        textForgotPassword=(TextView)findViewById(R.id.text_forgot_password);
        progressDialog.setMessage(getResources().getString(R.string.logging_user));

        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        //dbHelper.dropAndCreateAllTable();
        //createDatabase();



        btnLogin=(Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
               // Intent intent=new Intent(LoginActivity.this,MainActivity.class);
               // startActivity(intent);
            }
        });

        btnSignUp=(Button)findViewById(R.id.btn_register);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void attemptLogin(){
        userId=editTextUserID.getText().toString();
        password=editTextPassword.getText().toString();
        View focus=null;
        boolean cancel=false;

        if(TextUtils.isEmpty(password)){
            editTextPassword.setError(getResources().getString(R.string.password_required));
            focus=editTextPassword;
            cancel=true;
        }

        if(TextUtils.isEmpty(userId)){
            editTextUserID.setError(getResources().getString(R.string.user_required));
            focus=editTextUserID;
            cancel=true;
        }

        if(cancel){
            focus.requestFocus();
            return;
        }else {
            if(ConnectionDetector.isNetworkAvailable(this)) {
                progressDialog.setMessage(getResources().getString(R.string.logging_user));
                showProgress(true);
                /*editor.putString(Constants.FULL_NAME,"Vipin Dhama");
                editor.putString(Constants.USER_ID,userId);
                editor.putString(Constants.MOBILE_NO,"9718181697");
                editor.putString(Constants.LOCATION,"Delhi");
                editor.putBoolean(Constants.IS_LOGGED_IN,true);
                editor.commit();
                //DialogAndToast.showToast("Account created",LoginActivity.this);
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();*/

                Map<String,String> params=new HashMap<>();
                params.put("username",userId);
                params.put("password",password);
                String url=getResources().getString(R.string.url)+"/merchants/login";
                showProgress(true);
                jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"login");

            }else {
                DialogAndToast.showDialog(getResources().getString(R.string.no_internet),this);
            }

        }
    }

    public void volleyRequest(){
        Map<String,String> params=new HashMap<>();
        params.put("user_id",userId);
        params.put("password",password);
        String url=getResources().getString(R.string.url)+"/Users/login";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"login");
    }


    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if(apiName.equals("login")){
                if(response.getString("success").equals("true")){
                    JSONObject dataObject=response.getJSONObject("data");
                    editor.putString(Constants.USER_ID,dataObject.getString("login_name"));
                    editor.putString(Constants.FULL_NAME,dataObject.getString("name"));
                    editor.putString(Constants.EMAIL,dataObject.getString("email"));
                    editor.putString(Constants.MOBILE_NO,dataObject.getString("phone"));
                    editor.putString(Constants.TOKEN,dataObject.getString("user_token"));
                    editor.putString(Constants.TOKEN_EXPIRY_DATE,dataObject.getString("token_expiry_date"));
                    editor.putString(Constants.ADDRESS,dataObject.getString("address"));
                    editor.putString(Constants.PAYTM_NUMBER,dataObject.getString("paytm_no"));
                    editor.putString(Constants.GOOGLE_PAY_NUMBER,dataObject.getString("google_pay_no"));
                    editor.putString(Constants.PHONE_PAY_NUMBER,dataObject.getString("phone_pay_no"));
                    editor.putString(Constants.BANK_NAME,dataObject.getString("bank_name"));
                    editor.putString(Constants.ACCOUNT_NO,dataObject.getString("account_no"));
                    editor.putString(Constants.BANK_ACCOUNT_NAME,dataObject.getString("bank_account_name"));
                    editor.putString(Constants.IFSC_CODE,dataObject.getString("ifsc_code"));
                    editor.putString(Constants.BRANCH_ADRESS,dataObject.getString("bank_branch"));
                    editor.putBoolean(Constants.IS_LOGGED_IN,true);
                    editor.commit();
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    DialogAndToast.showDialog(response.getString("message"),LoginActivity.this);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),LoginActivity.this);
        }
    }

}
