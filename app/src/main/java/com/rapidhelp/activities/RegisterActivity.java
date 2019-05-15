package com.rapidhelp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.rapidhelp.R;
import com.rapidhelp.models.Country;
import com.rapidhelp.utilities.ConnectionDetector;
import com.rapidhelp.utilities.Constants;
import com.rapidhelp.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends NetworkBaseActivity{

    private EditText editFullName,editEmail,editMobile,editPassword,editConfPassword,editSponsorId,editTransHash;
    private Button btnRegister;
    private String fullName,email,mobile,password,confPassword;
    private View gold_unselected_circle,gold_selected_circle,platinum_unselected_circle,platinum_selected_circle;
    private TextView tv_gold_joining_amount,tv_gold_provide_help_amount,tv_gold_get_help_amount,tv_first_get_help_days,
            tv_first_get_help_amount,tv_second_get_help_days,tv_second_get_help_amount,tv_third_get_help_days,
            tv_third_get_help_amount,tv_platinum_joining_amount,tv_platinum_provide_help_amount,
            tv_platinum_get_help_amount,tv_plat_first_get_help_days,tv_plat_first_get_help_amount,
            tv_plat_second_get_help_days,tv_plat_second_get_help_amount,tv_plat_third_get_help_days,tv_plat_third_get_help_amount;

    private AppCompatSpinner spinnerCountry;
    private List<String> countryList;
    private List<Country> countryObjectList;
    private ArrayAdapter<String> countryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

    }

    private void init(){
        editFullName=(EditText)findViewById(R.id.edit_full_name);
        editEmail=(EditText)findViewById(R.id.edit_email);
        editMobile=(EditText)findViewById(R.id.edit_mobile);
        editPassword=(EditText)findViewById(R.id.edit_password);
        editConfPassword=(EditText)findViewById(R.id.edit_conf_password);
        editSponsorId=(EditText)findViewById(R.id.edit_sponsor_id);
        editTransHash=(EditText)findViewById(R.id.edit_transaction_hash);

        spinnerCountry = findViewById(R.id.spinner_country);
        tv_gold_joining_amount =findViewById(R.id.tv_gold_joining_amount);
        tv_gold_provide_help_amount = findViewById(R.id.tv_gold_provide_help_amount);
        tv_gold_get_help_amount = findViewById(R.id.tv_gold_get_help_amount);
        tv_first_get_help_days = findViewById(R.id.tv_first_get_help_days);
        tv_first_get_help_amount = findViewById(R.id.tv_first_get_help_amount);
        tv_second_get_help_days = findViewById(R.id.tv_second_get_help_days);
        tv_second_get_help_amount = findViewById(R.id.tv_second_get_help_amount);
        tv_third_get_help_days = findViewById(R.id.tv_third_get_help_days);
        tv_third_get_help_amount = findViewById(R.id.tv_third_get_help_amount);
        tv_platinum_joining_amount = findViewById(R.id.tv_platinum_joining_amount);
        tv_platinum_provide_help_amount =findViewById(R.id.tv_platinum_provide_help_amount);
        tv_platinum_get_help_amount = findViewById(R.id.tv_platinum_get_help_amount);
        tv_plat_first_get_help_days = findViewById(R.id.tv_plat_first_get_help_days);
        tv_plat_first_get_help_amount = findViewById(R.id.tv_plat_first_get_help_amount);
        tv_plat_second_get_help_days = findViewById(R.id.tv_plat_second_get_help_days);
        tv_plat_second_get_help_amount = findViewById(R.id.tv_plat_second_get_help_amount);
        tv_plat_third_get_help_days = findViewById(R.id.tv_plat_third_get_help_days);
        tv_plat_third_get_help_amount = findViewById(R.id.tv_plat_third_get_help_amount);
        gold_unselected_circle = findViewById(R.id.gold_unselected_circle);
        gold_selected_circle = findViewById(R.id.gold_selected_circle);
        platinum_unselected_circle = findViewById(R.id.platinum_unselected_circle);
        platinum_selected_circle = findViewById(R.id.platinum_selected_circle);

        countryObjectList = new ArrayList<>();
        countryList = new ArrayList<>();

        countryAdapter = new ArrayAdapter<String>(this, R.layout.simple_dropdown_list_item, countryList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.blue50));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.white));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey700));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                tv.setPadding(20, 20, 20, 20);
                return view;
            }
        };

        spinnerCountry.setAdapter(countryAdapter);

        gold_unselected_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                platinum_selected_circle.setVisibility(View.GONE);
                platinum_unselected_circle.setVisibility(View.VISIBLE);
                gold_unselected_circle.setVisibility(View.GONE);
                gold_selected_circle.setVisibility(View.VISIBLE);
            }
        });

        platinum_unselected_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                platinum_selected_circle.setVisibility(View.VISIBLE);
                platinum_unselected_circle.setVisibility(View.GONE);
                gold_unselected_circle.setVisibility(View.VISIBLE);
                gold_selected_circle.setVisibility(View.GONE);
            }
        });

        if(ConnectionDetector.isNetworkAvailable(this)){
            getPackages();
            getCountries();
        }else{
            DialogAndToast.showDialog(getResources().getString(R.string.no_internet),this);
        }

        btnRegister=(Button)findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    public void attemptRegister(){
        fullName=editFullName.getText().toString();
        email=editEmail.getText().toString();
        mobile=editMobile.getText().toString();
      //  location=editLocation.getText().toString();
        password=editPassword.getText().toString();
        confPassword=editConfPassword.getText().toString();
        String sponsorId = editSponsorId.getText().toString();
        String transHash = editTransHash.getText().toString();

        String packageSelected = "";
        if(gold_selected_circle.getVisibility() == View.VISIBLE){
            packageSelected = "1";
        }else if(platinum_selected_circle.getVisibility() == View.VISIBLE){
            packageSelected = "2";
        }

        int countryID = 0;
        if(spinnerCountry.getSelectedItemPosition() > 0){
            countryID = countryObjectList.get(spinnerCountry.getSelectedItemPosition()).getId();
        }

       // password="Vipin@12345";
      //  confPassword="Vipin@12345";
        boolean isChecked=true;

        View focus=null;
        boolean cancel=false;

        if(TextUtils.isEmpty(sponsorId)){
            focus=editSponsorId;
            cancel=true;
            editSponsorId.setError("Please enter Sponsor ID");
        }

        if(TextUtils.isEmpty(transHash)){
            focus=editTransHash;
            cancel=true;
            editTransHash.setError("Please enter transaction hash");
        }

        if(TextUtils.isEmpty(password)){
            focus=editPassword;
            cancel=true;
            editPassword.setError(getResources().getString(R.string.password_required));
        }else if(!password.equals(confPassword)){
            focus=editPassword;
            cancel=true;
            editPassword.setError(getResources().getString(R.string.password_not_match));
        }

        if(TextUtils.isEmpty(mobile)){
            focus=editMobile;
            cancel=true;
            editMobile.setError(getResources().getString(R.string.mobile_required));
        }

        if(TextUtils.isEmpty(email)){
            focus=editEmail;
            cancel=true;
            editEmail.setError(getResources().getString(R.string.email_required));
        }

        if(TextUtils.isEmpty(fullName)){
            focus=editFullName;
            cancel=true;
            editFullName.setError(getResources().getString(R.string.full_name_required));
        }



        if(cancel){
            focus.requestFocus();
            return;
        }else {

            if(packageSelected.equals("")){
                DialogAndToast.showDialog("Please select Package",this);
                return;
            }

            if(countryID == 0){
                DialogAndToast.showDialog("Please select Country",this);
                return;
            }

            if(ConnectionDetector.isNetworkAvailable(this)) {
                progressDialog.setMessage(getResources().getString(R.string.creating_account));
                showProgress(true);
                Map<String,String> params=new HashMap<>();
                params.put("name",fullName);
                params.put("email",email);
                params.put("password",password);
                params.put("mobile_no",mobile);
                params.put("sponser",sponsorId);
                params.put("transaction_hash",transHash);
                params.put("country_id",""+countryID);
                params.put("package",packageSelected);
                String url=getResources().getString(R.string.url)+"/merchants/register";
                jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"SignUp");

            }else {
                DialogAndToast.showDialog(getResources().getString(R.string.no_internet),this);
            }

        }
    }

    public void getCountries(){
        showProgress(true);
        String url=getResources().getString(R.string.url)+"/merchants/appcountries";
        jsonObjectApiRequest(Request.Method.GET,url,new JSONObject(),"appcountries");
    }

    public void getPackages(){
        showProgress(true);
        String url=getResources().getString(R.string.url)+"/merchants/apppackages";
        jsonObjectApiRequest(Request.Method.GET,url,new JSONObject(),"apppackages");
    }

    public void volleyRequest(){
        Map<String,String> params=new HashMap<>();
        params.put("name",fullName);
        params.put("username",email.split("@")[0]);
        params.put("email",email);
        params.put("password",password);
        params.put("mobile",mobile);
        String url=getResources().getString(R.string.url)+"/Users/SignUp";
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"SignUp");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if(apiName.equals("SignUp")){
                if(response.getString("success").equals("true")){
                    showMyDialog(response.getString("message"));
                }else {
                    DialogAndToast.showDialog(response.getString("message"),RegisterActivity.this);
                }
            }else if(apiName.equals("appcountries")){
                 if(response.getString("success").equals("true")){
                     JSONArray jsonArray = response.getJSONArray("data");
                     int len = jsonArray.length();
                     Country country = null;
                     JSONObject dataObject = null;
                     countryList.add("Select Country");
                     countryObjectList.add(new Country());
                     for(int i=0; i<len; i++){
                         dataObject = jsonArray.getJSONObject(i);
                         country = new Country();
                         country.setId(dataObject.getInt("id"));
                         country.setCountry(dataObject.getString("country_name"));
                         countryObjectList.add(country);
                         countryList.add(dataObject.getString("country_name"));
                     }

                     countryAdapter.notifyDataSetChanged();

                 }else{

                 }
            }else if(apiName.equals("apppackages")){
                if(response.getString("success").equals("true")){
                    JSONArray jsonArray = response.getJSONArray("data");
                    int len = jsonArray.length();
                    JSONObject dataObject = null;
                    float totGoldAmt = 0f,totPlatAmt = 0f;
                    for(int i=0; i<len; i++) {
                        dataObject = jsonArray.getJSONObject(i);
                        if(i == 0){
                            tv_gold_joining_amount.setText("Joining Amount: "+dataObject.getString("joining_amount"));
                            tv_gold_provide_help_amount.setText("Provide Help: "+dataObject.getString("provide_help_amount"));
                            tv_first_get_help_days.setText(dataObject.getString("first_get_help_days"));
                            tv_first_get_help_amount.setText(dataObject.getString("first_get_help_amount"));
                            tv_second_get_help_days.setText(dataObject.getString("second_get_help_days"));
                            tv_second_get_help_amount.setText(dataObject.getString("second_get_help_amount"));
                            tv_third_get_help_days.setText(dataObject.getString("third_get_help_days"));
                            tv_third_get_help_amount.setText(dataObject.getString("third_get_help_amount"));
                            totGoldAmt = Float.parseFloat(dataObject.getString("first_get_help_amount")) +
                                    Float.parseFloat(dataObject.getString("second_get_help_amount"))+
                                    Float.parseFloat(dataObject.getString("third_get_help_amount"));

                            tv_gold_get_help_amount.setText("Get Help: "+String.format("%.00f",totGoldAmt));

                        }else{
                            tv_platinum_joining_amount.setText("Joining Amount: "+dataObject.getString("joining_amount"));
                            tv_platinum_provide_help_amount.setText("Provide Help: "+dataObject.getString("provide_help_amount"));
                            tv_plat_first_get_help_days.setText(dataObject.getString("first_get_help_days"));
                            tv_plat_first_get_help_amount.setText(dataObject.getString("first_get_help_amount"));
                            tv_plat_second_get_help_days.setText(dataObject.getString("second_get_help_days"));
                            tv_plat_second_get_help_amount.setText(dataObject.getString("second_get_help_amount"));
                            tv_plat_third_get_help_days.setText(dataObject.getString("third_get_help_days"));
                            tv_plat_third_get_help_amount.setText(dataObject.getString("third_get_help_amount"));
                            totPlatAmt = Float.parseFloat(dataObject.getString("first_get_help_amount")) +
                                    Float.parseFloat(dataObject.getString("second_get_help_amount"))+
                                    Float.parseFloat(dataObject.getString("third_get_help_amount"));

                            tv_platinum_get_help_amount.setText("Get Help: "+String.format("%.00f",totPlatAmt));
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),RegisterActivity.this);
        }
    }

    @Override
    public void onDialogPositiveClicked(){
      finish();
    }

}
