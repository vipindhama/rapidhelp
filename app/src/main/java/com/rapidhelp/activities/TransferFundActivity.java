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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TransferFundActivity extends NetworkBaseActivity {

    private EditText etUserId,etAmount;
    private TextView tvAvailableBal,tvUserName;

    private Button btnTransfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_fund);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init(){
        etUserId = findViewById(R.id.edit_user_id);
        etAmount = findViewById(R.id.edit_amount);
        tvAvailableBal = findViewById(R.id.tv_available_bal);
        tvUserName = findViewById(R.id.tv_user_name);
        btnTransfer = findViewById(R.id.btn_transfer);

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ConnectionDetector.isNetworkAvailable(TransferFundActivity.this)){
                    transferFunds();
                }else{
                    DialogAndToast.showDialog(getResources().getString(R.string.no_internet),TransferFundActivity.this);
                }
            }
        });

        etUserId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(!TextUtils.isEmpty(etUserId.getText().toString())){
                        getUserName();
                    }
                }
            }
        });

        if(ConnectionDetector.isNetworkAvailable(TransferFundActivity.this)){
            getAvailableAmount();
        }else{
            DialogAndToast.showDialog(getResources().getString(R.string.no_internet),TransferFundActivity.this);
        }
    }

    private void transferFunds(){
        String userID = etUserId.getText().toString();
        String amount = etAmount.getText().toString();

        boolean cancel = false;
        View focus = null;

        if(TextUtils.isEmpty(amount)){
            cancel = true;
            etAmount.setError("Please enter amount");
            focus = etAmount;
        }

        if(TextUtils.isEmpty(userID)){
            cancel = true;
            etUserId.setError("Please enter user ID");
            focus = etUserId;
        }

        if(cancel){
            focus.requestFocus();
            return;
        }else{
            showProgress(true);
            Map<String,String> params=new HashMap<>();
            params.put("user_token",sharedPreferences.getString(Constants.TOKEN,""));
            params.put("login_name",sharedPreferences.getString(Constants.USER_ID,""));
            params.put("amount",amount);
            params.put("user_id",userID);
            String url=getResources().getString(R.string.url)+"/merchants/appfundstransfer";
            showProgress(true);
            jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"appfundstransfer");
        }
    }

    private void getAvailableAmount(){
        Map<String,String> params=new HashMap<>();
        params.put("user_token",sharedPreferences.getString(Constants.TOKEN,""));
        params.put("login_name",sharedPreferences.getString(Constants.USER_ID,""));
        String url=getResources().getString(R.string.url)+"/merchants/appgetusercurrentbalance";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"appgetusercurrentbalance");
    }

    private void getUserName(){
        Map<String,String> params=new HashMap<>();
        params.put("user_token",sharedPreferences.getString(Constants.TOKEN,""));
        params.put("login_name",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("finduserid",etUserId.getText().toString());
        String url=getResources().getString(R.string.url)+"/merchants/appgetusernamebyuserid";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"appgetusernamebyuserid");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if(apiName.equals("appfundstransfer")){
                if(response.getString("success").equals("true")){
                    showMyDialog(response.getJSONArray("data").getJSONObject(0).getString("message"));
                }else {
                    DialogAndToast.showDialog(response.getString("message"),TransferFundActivity.this);
                }
            }else if(apiName.equals("appgetusercurrentbalance")){
                if(response.getString("success").equals("true")){
                    tvAvailableBal.setText(response.getString("data"));
                }else {
                    DialogAndToast.showDialog(response.getString("message"),TransferFundActivity.this);
                }
            }else if(apiName.equals("appgetusernamebyuserid")){
                if(response.getString("success").equals("true")){
                    tvUserName.setText(response.getString("data"));
                }else {
                    DialogAndToast.showDialog(response.getString("message"),TransferFundActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),TransferFundActivity.this);
        }
    }

    @Override
    public void onDialogPositiveClicked(){
        etAmount.setError(null);
        etAmount.setText("");
        etUserId.setError(null);
        etUserId.setText("");
        tvUserName.setText("");

    }

}
