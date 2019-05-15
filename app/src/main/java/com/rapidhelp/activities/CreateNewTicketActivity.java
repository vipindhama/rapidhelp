package com.rapidhelp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.rapidhelp.R;
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

public class CreateNewTicketActivity extends NetworkBaseActivity {

    private EditText etQuery;
    private Spinner spinnerCategory;
    private Button btnCreate;
    private List<String> catList;
    private ArrayAdapter<String> catAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_ticket);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init(){
        etQuery = findViewById(R.id.edit_query);
        btnCreate = findViewById(R.id.btn_create);
        spinnerCategory = findViewById(R.id.spinner_category);
        catList = new ArrayList<>();

        catList.add("Select Category");
        catList.add("Get Help Related Queries");
        catList.add("Test Queries");
        catAdapter = new ArrayAdapter<String>(this, R.layout.simple_dropdown_list_item, catList) {
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

        spinnerCategory.setAdapter(catAdapter);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ConnectionDetector.isNetworkAvailable(CreateNewTicketActivity.this)){
                    createNewTicket();
                }else{
                    DialogAndToast.showDialog(getResources().getString(R.string.no_internet),CreateNewTicketActivity.this);
                }
            }
        });

    }

    private void createNewTicket(){
        String query = etQuery.getText().toString();
        String cat = spinnerCategory.getSelectedItem().toString();

        if(TextUtils.isEmpty(query)){
            DialogAndToast.showDialog("Please enter query",this);
            return;
        }

        if(cat.equals("Select Category")){
            DialogAndToast.showDialog("Please select category",this);
            return;
        }

        showProgress(true);
        Map<String,String> params=new HashMap<>();
        params.put("user_token",sharedPreferences.getString(Constants.TOKEN,""));
        params.put("login_name",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("category",cat);
        params.put("query",query);
        String url=getResources().getString(R.string.url)+"/merchants/appcreatesupportrequest";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"appcreatesupportrequest");
    }


    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if(apiName.equals("appcreatesupportrequest")){
                if(response.getString("success").equals("true")){
                    showMyDialog(response.getJSONArray("data").getJSONObject(0).getString("message"));
                }else {
                    DialogAndToast.showDialog(response.getString("message"),CreateNewTicketActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),CreateNewTicketActivity.this);
        }
    }

    @Override
    public void onDialogPositiveClicked(){
        spinnerCategory.setSelection(0);
        etQuery.setError(null);
        etQuery.setText("");
        Intent intent = new Intent();
        intent.putExtra("status","new ticket created");
        setResult(-1,intent);
        finish();
    }

}
