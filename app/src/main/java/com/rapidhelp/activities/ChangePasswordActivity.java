package com.rapidhelp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends NetworkBaseActivity {

    private EditText etCurrentPassword,etNewPassword,etConfPassword;
    private Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init(){
        etCurrentPassword = findViewById(R.id.edit_current_password);
        etNewPassword = findViewById(R.id.edit_new_password);
        etConfPassword =findViewById(R.id.edit_conf_new_password);
        btnChangePassword =findViewById(R.id.btn_change_password);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ConnectionDetector.isNetworkAvailable(ChangePasswordActivity.this)){
                    changePassword();
                }else{
                    DialogAndToast.showDialog(getResources().getString(R.string.no_internet),ChangePasswordActivity.this);
                }
            }
        });
    }

    private void changePassword(){
        String currentPassword = etCurrentPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();
        String confPassword = etConfPassword.getText().toString();
        boolean cancel = false;
        View focus = null;

        if(TextUtils.isEmpty(currentPassword)){
            cancel = true;
            focus = etCurrentPassword;
            etCurrentPassword.setError("Please enter current password.");
        }


        if(TextUtils.isEmpty(newPassword)){
            cancel = true;
            focus = etNewPassword;
            etNewPassword.setError("Please enter new password.");
        }

        if(!newPassword.equals(confPassword)){
            cancel = true;
            focus = etConfPassword;
            etConfPassword.setError("Passwords are not matching.");
        }
        if (cancel){
            focus.requestFocus();
            return;
        }else {
            progressDialog.setMessage("Changing password...");
            showProgress(true);
            Map<String, String> params = new HashMap<>();
            params.put("login_name", sharedPreferences.getString(Constants.USER_ID,""));
            params.put("user_token", sharedPreferences.getString(Constants.TOKEN,""));
            params.put("password", currentPassword);
            params.put("newpassword", newPassword);
            String url = getResources().getString(R.string.url) + "/merchants/appchangepassword";
            jsonObjectApiRequest(Request.Method.POST, url, new JSONObject(params),"appchangepassword");
        }

    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if(apiName.equals("appchangepassword")){
                if(response.getString("success").equals("true")){
                    JSONArray dataArray=response.getJSONArray("data");
                    if(dataArray.length() > 0){
                        etCurrentPassword.setText("");
                        etCurrentPassword.setText("");
                        etNewPassword.setText("");
                        etNewPassword.setError(null);
                        etConfPassword.setText("");
                        etConfPassword.setError(null);
                        JSONObject dataObject = dataArray.getJSONObject(0);
                        DialogAndToast.showDialog(dataObject.getString("message"),ChangePasswordActivity.this);
                    }else{
                        DialogAndToast.showDialog(getResources().getString(R.string.unable_to_load_data),ChangePasswordActivity.this);
                    }

                }else {
                    DialogAndToast.showDialog(getResources().getString(R.string.unable_to_load_data),ChangePasswordActivity.this);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),ChangePasswordActivity.this);
        }
    }

}
