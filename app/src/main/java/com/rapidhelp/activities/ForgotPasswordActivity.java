package com.rapidhelp.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.rapidhelp.R;
import com.rapidhelp.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends NetworkBaseActivity implements View.OnClickListener{

    private EditText editTextEmail;
    private Button buttonSendLink;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
    }

    private void initViews(){
        editTextEmail = findViewById(R.id.edit_user_id);
        tvLogin = findViewById(R.id.btn_back);
        buttonSendLink = findViewById(R.id.btn_send_link);
        buttonSendLink.setOnClickListener(this);

        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == buttonSendLink){
            attemptSendLink();
        }else if(view == tvLogin){
            finish();
        }

    }

    private void attemptSendLink(){
        String email = editTextEmail.getText().toString();
        View focus = null;
        boolean cancel = false;
       if(TextUtils.isEmpty(email)){
            editTextEmail.setError("Please enter User ID.");
            focus=editTextEmail;
            cancel=true;
        }/* else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()){
            editTextEmail.setError(getResources().getString(R.string.valid_email));
            focus=editTextEmail;
            cancel=true;
        }*/

        if(cancel){
            focus.requestFocus();
            return;
        }else {
            progressDialog.setMessage("Loading...");
            Map<String,String> params=new HashMap<>();
            params.put("login_name",email);
            String url=getResources().getString(R.string.url)+"/merchants/appforgetpassword";
            showProgress(true);
            jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"appforgetpassword");
        }
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if(apiName.equals("appforgetpassword")){
                if(response.getString("success").equals("true")||response.getString("success").equals(true)){
                    JSONArray dataArray=response.getJSONArray("data");
                    if(dataArray.length() > 0){
                        JSONObject dataObject = dataArray.getJSONObject(0);
                        DialogAndToast.showDialog(dataObject.getString("message"),ForgotPasswordActivity.this);
                    }else{
                        DialogAndToast.showDialog(getResources().getString(R.string.connection_error),ForgotPasswordActivity.this);
                    }

                }else {
                    DialogAndToast.showDialog(getResources().getString(R.string.connection_error),ForgotPasswordActivity.this);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),ForgotPasswordActivity.this);
        }
    }

}
