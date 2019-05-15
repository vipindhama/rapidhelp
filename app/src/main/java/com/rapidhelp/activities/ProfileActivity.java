package com.rapidhelp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.rapidhelp.R;
import com.rapidhelp.utilities.ConnectionDetector;
import com.rapidhelp.utilities.Constants;
import com.rapidhelp.utilities.DialogAndToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends NetworkBaseActivity {

    private EditText edit_full_name,edit_email,edit_phone,edit_address,edit_paytm_number,edit_google_pay_number,
            edit_phone_pe_number,edit_bank_name,edit_bank_account_number,edit_bank_account_name,edit_ifsc_code,
            edit_bank_branch;

    private Button buttonUpdate;

    private String address,paytmNumber,googlePayNumber,phonePayNumber,bankName,bankAccountName,accountNumber,ifscCode,bankBranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init(){
        edit_full_name = findViewById(R.id.edit_full_name);
        edit_email = findViewById(R.id.edit_email);
        edit_phone = findViewById(R.id.edit_phone);
        edit_address = findViewById(R.id.edit_address);
        edit_paytm_number = findViewById(R.id.edit_paytm_number);
        edit_google_pay_number = findViewById(R.id.edit_google_pay_number);
        edit_phone_pe_number = findViewById(R.id.edit_phone_pe_number);
        edit_bank_name = findViewById(R.id.edit_bank_name);
        edit_bank_account_number = findViewById(R.id.edit_bank_account_number);
        edit_bank_account_name = findViewById(R.id.edit_bank_account_name);
        edit_ifsc_code = findViewById(R.id.edit_ifsc_code);
        edit_bank_branch = findViewById(R.id.edit_bank_branch);
        buttonUpdate = findViewById(R.id.btn_update);

        edit_full_name.setText(sharedPreferences.getString(Constants.FULL_NAME,""));
        edit_email.setText(sharedPreferences.getString(Constants.EMAIL,""));
        edit_phone.setText(sharedPreferences.getString(Constants.MOBILE_NO,""));
        edit_address.setText(sharedPreferences.getString(Constants.ADDRESS,""));
        edit_paytm_number.setText(sharedPreferences.getString(Constants.PAYTM_NUMBER,""));
        edit_google_pay_number.setText(sharedPreferences.getString(Constants.GOOGLE_PAY_NUMBER,""));
        edit_phone_pe_number.setText(sharedPreferences.getString(Constants.PHONE_PAY_NUMBER,""));
        edit_bank_name.setText(sharedPreferences.getString(Constants.BANK_NAME,""));
        edit_bank_account_number.setText(sharedPreferences.getString(Constants.ACCOUNT_NO,""));
        edit_bank_account_name.setText(sharedPreferences.getString(Constants.BANK_ACCOUNT_NAME,""));
        edit_ifsc_code.setText(sharedPreferences.getString(Constants.IFSC_CODE,""));
        edit_bank_branch.setText(sharedPreferences.getString(Constants.BRANCH_ADRESS,""));

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  if(ConnectionDetector.isNetworkAvailable(ProfileActivity.this)){
                      attemptUpdate();
                  }else{
                      DialogAndToast.showDialog(getResources().getString(R.string.no_internet),ProfileActivity.this);
                  }
            }
        });
    }

    private void attemptUpdate(){
       // String name = edit_full_name.getText().toString();
        address = edit_address.getText().toString();
        paytmNumber = edit_paytm_number.getText().toString();
        googlePayNumber = edit_google_pay_number.getText().toString();
        phonePayNumber = edit_phone_pe_number.getText().toString();
        bankName = edit_bank_name.getText().toString();
        bankAccountName = edit_bank_account_name.getText().toString();
        accountNumber = edit_bank_account_number.getText().toString();
        ifscCode = edit_ifsc_code.getText().toString();
        bankBranch = edit_bank_branch.getText().toString();

        View focus = null;
        boolean cancel = false;

        if(TextUtils.isEmpty(address)){
            address = "";
        }

        if(TextUtils.isEmpty(paytmNumber)){
            paytmNumber = "";
        }

        if(TextUtils.isEmpty(googlePayNumber)){
            googlePayNumber = "";
        }

        if(TextUtils.isEmpty(phonePayNumber)){
            phonePayNumber = "";
        }

        if(TextUtils.isEmpty(bankName)){
            bankName = "";
        }

        if(TextUtils.isEmpty(accountNumber)){
            accountNumber = "";
        }

        if(TextUtils.isEmpty(bankAccountName)){
            bankAccountName = "";
        }

        if(TextUtils.isEmpty(bankBranch)){
            bankBranch = "";
        }

        if(TextUtils.isEmpty(ifscCode)){
            ifscCode = "";
        }

        if(cancel){
            focus.requestFocus();
            return;
        }else{
            progressDialog.setMessage(getResources().getString(R.string.logging_user));
            showProgress(true);
            Map<String,String> params=new HashMap<>();
            params.put("user_token",sharedPreferences.getString(Constants.TOKEN,""));
            params.put("login_name",sharedPreferences.getString(Constants.USER_ID,""));
            params.put("address",address);
            params.put("paytm_no",paytmNumber);
            params.put("google_pay_no",googlePayNumber);
            params.put("phone_pay_no",phonePayNumber);
            params.put("bank_name",bankName);
            params.put("account_no",accountNumber);
            params.put("bank_account_name",bankAccountName);
            params.put("ifsc_code",ifscCode);
            params.put("bank_branch",bankBranch);
            String url=getResources().getString(R.string.url)+"/merchants/appupdateprofile";
            showProgress(true);
            jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"appupdateprofile");
        }

    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if(apiName.equals("login")){
                if(response.getString("success").equals("true")){
                    editor.putString(Constants.ADDRESS,address);
                    editor.putString(Constants.PAYTM_NUMBER,paytmNumber);
                    editor.putString(Constants.GOOGLE_PAY_NUMBER,googlePayNumber);
                    editor.putString(Constants.PHONE_PAY_NUMBER,phonePayNumber);
                    editor.putString(Constants.BANK_NAME,bankName);
                    editor.putString(Constants.ACCOUNT_NO,accountNumber);
                    editor.putString(Constants.BANK_ACCOUNT_NAME,bankAccountName);
                    editor.putString(Constants.IFSC_CODE,ifscCode);
                    editor.putString(Constants.BRANCH_ADRESS,bankBranch);
                    editor.commit();
                    showMyDialog(response.getString("message"));
                }else {
                    DialogAndToast.showDialog(response.getString("message"),ProfileActivity.this);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),ProfileActivity.this);
        }
    }

    @Override
    public void onDialogPositiveClicked(){
        edit_address.setError(null);
        edit_paytm_number.setError(null);
        edit_google_pay_number.setError(null);
        edit_phone_pe_number.setError(null);
        edit_bank_name.setError(null);
        edit_bank_account_number.setError(null);
        edit_bank_account_name.setError(null);
        edit_ifsc_code.setError(null);
        edit_bank_branch.setError(null);
    }

}
