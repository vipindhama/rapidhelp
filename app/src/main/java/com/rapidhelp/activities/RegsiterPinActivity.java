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

public class RegsiterPinActivity extends NetworkBaseActivity {

    private EditText etAmount,etTransactionHash;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsiter_pin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

    }

    private void init(){
        etAmount =  findViewById(R.id.edit_amount);
        etTransactionHash =  findViewById(R.id.edit_transaction_hash);
        btnSubmit =  findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ConnectionDetector.isNetworkAvailable(RegsiterPinActivity.this)){
                    requestPin();
                }else{
                    DialogAndToast.showDialog(getResources().getString(R.string.no_internet),RegsiterPinActivity.this);
                }
            }
        });
    }

    private void requestPin(){
        String amount = etAmount.getText().toString();
        String transHash = etTransactionHash.getText().toString();

        boolean cancel = false;
        View focus = null;

        if(TextUtils.isEmpty(amount)){
            cancel = true;
            etAmount.setError("Please enter amount");
            focus = etAmount;
        }

        if(TextUtils.isEmpty(transHash)){
            cancel = true;
            etTransactionHash.setError("Please enter transaction hash");
            focus = etTransactionHash;
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
            params.put("transaction_hash",transHash);
            String url=getResources().getString(R.string.url)+"/merchants/apprequestregistrationpins";
            showProgress(true);
            jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"apprequestregistrationpins");
        }
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if(apiName.equals("apprequestregistrationpins")){
                if(response.getString("success").equals("true")){
                    showMyDialog(response.getJSONArray("data").getJSONObject(0).getString("message"));
                }else {
                    DialogAndToast.showDialog(response.getString("message"),RegsiterPinActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),RegsiterPinActivity.this);
        }
    }

    @Override
    public void onDialogPositiveClicked(){
        etAmount.setError(null);
        etAmount.setText("");
        etTransactionHash.setError(null);
        etTransactionHash.setText("");
    }

}
