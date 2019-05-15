package com.rapidhelp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.rapidhelp.R;
import com.rapidhelp.adapters.MyItemAdapter;
import com.rapidhelp.adapters.ReportAdapter;
import com.rapidhelp.models.DirectUserReport;
import com.rapidhelp.models.JoiningReport;
import com.rapidhelp.models.LevelItemReport;
import com.rapidhelp.models.LevelReport;
import com.rapidhelp.models.ProvideHelp;
import com.rapidhelp.models.ProvideHelpItem;
import com.rapidhelp.models.SupportCenter;
import com.rapidhelp.models.TransactionReport;
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

public class ReportActivity extends NetworkBaseActivity {

    private RecyclerView recyclerView;
    private ReportAdapter myItemAdapter;
    private List<Object> itemList;
    private TextView textViewError;

    private Button btnCreateNewTicket;

    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        flag = getIntent().getStringExtra("flag");
        setTitle(flag);

        itemList = new ArrayList<>();
        textViewError = findViewById(R.id.text_error);
        btnCreateNewTicket = findViewById(R.id.btn_create_new_tocket);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerHomeMenu=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManagerHomeMenu);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new ReportAdapter(this,itemList,flag);
        recyclerView.setAdapter(myItemAdapter);

        if(flag.equals("Support Center")){
            btnCreateNewTicket.setVisibility(View.VISIBLE);
            btnCreateNewTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ReportActivity.this,CreateNewTicketActivity.class);
                    startActivityForResult(intent,2);
                }
            });
        }

        if(ConnectionDetector.isNetworkAvailable(this)){
            getReport();
        }else{
         DialogAndToast.showDialog(getResources().getString(R.string.no_internet),this);
        }

    }

    private void getReport(){
        showProgress(true);
        itemList.clear();
        Map<String,String> params=new HashMap<>();
        params.put("user_token",sharedPreferences.getString(Constants.TOKEN,""));
        params.put("login_name",sharedPreferences.getString(Constants.USER_ID,""));
        String url = "",api = "";

        if(flag.equals("Joining Report")){
            url=getResources().getString(R.string.url)+"/merchants/appjoiningreport";
            api = "appjoiningreport";
        }
        else if(flag.equals("Level Report")){
            url=getResources().getString(R.string.url)+"/merchants/applevelreport";
            api = "applevelreport";
        }else if(flag.equals("Provide Helps")){
            url=getResources().getString(R.string.url)+"/merchants/apphelpslist";
            api = "apphelpslist";
        }else if(flag.equals("Direct Users Report")){
            url=getResources().getString(R.string.url)+"/merchants/appdirectslist";
            api = "appdirectslist";
        }else if(flag.equals("Transaction Report")){
            url=getResources().getString(R.string.url)+"/merchants/apptransactions";
            api = "apptransactions";
        }else if(flag.equals("Support Center")){
            url=getResources().getString(R.string.url)+"/merchants/applistsupportrequests";
            api = "applistsupportrequests";
        }

        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),api);
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if(apiName.equals("appjoiningreport")){
                if(response.getString("success").equals("true")){
                    JSONArray jsonArray = response.getJSONArray("data");
                    JSONObject jsonObject =null;
                    JoiningReport item = null;
                    int len = jsonArray.length();
                    for(int i=0; i<len; i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        item = new JoiningReport();
                        item.setName(jsonObject.getString("name"));
                        item.setUserId(jsonObject.getString("login_name"));
                        item.setUserPackage(jsonObject.getString("packages_name"));
                        item.setRegDate(jsonObject.getString("created"));
                        item.setProvideHelpDate("");
                        int status = jsonObject.getInt("provide_help_complete");
                        if(status == 0){
                            item.setProvideHelpStatus("Not assigned");
                        }else if(status == 1){
                            item.setProvideHelpStatus("Assigned");
                        }else if(status == 2){
                            item.setProvideHelpDate(jsonObject.getString("provide_help_complete_date"));
                            item.setProvideHelpStatus("Completed");
                        }
                      //  item.setProvideHelpStatus(jsonObject.getString("Completed"));
                        itemList.add(item);
                    }

                    myItemAdapter.notifyDataSetChanged();

                }else {
                    DialogAndToast.showDialog(response.getString("message"),ReportActivity.this);
                }
            }else if(apiName.equals("applevelreport")){
                if(response.getString("success").equals("true")) {
                    JSONArray jsonArray = response.getJSONArray("data");
                    JSONObject jsonObject =null;
                    LevelReport item = null;
                    LevelItemReport levelItemReport = null;
                    List<Object> itemLevelList = null;
                    int len = jsonArray.length();
                    int tempLevel = 0;
                    for(int i=0; i<len; i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        if(tempLevel != jsonObject.getInt("level")){
                            item = new LevelReport();
                            itemLevelList = new ArrayList<>();
                            levelItemReport = new LevelItemReport();
                            item.setLevelNo(jsonObject.getInt("level"));
                            levelItemReport.setName(jsonObject.getString("name"));
                            levelItemReport.setUserId(jsonObject.getString("login_name"));
                            levelItemReport.setRegDate(jsonObject.getString("created"));
                            int status = jsonObject.getInt("provide_help_complete");
                            levelItemReport.setProvideHelpDate("");
                            if(status == 0){
                                levelItemReport.setProvideHelpStatus("Not assigned");
                            }else if(status == 1){
                                levelItemReport.setProvideHelpStatus("Assigned");
                            }else if(status == 2){
                                levelItemReport.setProvideHelpDate(jsonObject.getString("provide_help_complete_date"));
                                levelItemReport.setProvideHelpStatus("Completed");
                            }
                            itemLevelList.add(levelItemReport);
                            item.setItemList(itemLevelList);
                            itemList.add(item);
                            tempLevel = item.getLevelNo();
                        }else{
                            itemLevelList = getLevelReportList(jsonObject.getInt("level"));
                            levelItemReport = new LevelItemReport();
                            levelItemReport.setName(jsonObject.getString("name"));
                            levelItemReport.setUserId(jsonObject.getString("login_name"));
                            levelItemReport.setRegDate(jsonObject.getString("created"));
                            levelItemReport.setProvideHelpDate("");
                            int status = jsonObject.getInt("provide_help_complete");
                            if(status == 0){
                                levelItemReport.setProvideHelpStatus("Not assigned");
                            }else if(status == 1){
                                levelItemReport.setProvideHelpStatus("Assigned");
                            }else if(status == 2){
                                levelItemReport.setProvideHelpDate(jsonObject.getString("provide_help_complete_date"));
                                levelItemReport.setProvideHelpStatus("Completed");
                            }
                            itemLevelList.add(levelItemReport);

                        }
                    }

                    myItemAdapter.notifyDataSetChanged();
                }
            }else if(apiName.equals("apphelpslist")){
                if(response.getString("success").equals("true")) {
                    JSONObject dataObject =response.getJSONObject("data");
                    JSONArray provideHelpArray = dataObject.getJSONArray("activeprovidehelp");
                    JSONArray getHelpArray = dataObject.getJSONArray("activegethelp");
                    JSONObject jsonObject = null;
                    ProvideHelp item = null;
                    ProvideHelpItem provideHelpItem = null;
                    List<Object> provideHelpItemList = new ArrayList<>();
                    int len = provideHelpArray.length();
                    item = new ProvideHelp();
                    item.setHeader("Provide Helps");
                    for(int i=0; i<len; i++){
                        jsonObject = provideHelpArray.getJSONObject(i);
                        provideHelpItem = new ProvideHelpItem();
                        provideHelpItem.setId(jsonObject.getString("id"));
                        provideHelpItem.setStatus(jsonObject.getString("status"));
                        provideHelpItem.setCreationDate(jsonObject.getString("creation_date"));
                        provideHelpItem.setCompletionDate(jsonObject.getString("completion_date"));
                        provideHelpItemList.add(provideHelpItem);
                    }
                    item.setItemList(provideHelpItemList);
                    itemList.add(item);

                    provideHelpItemList = new ArrayList<>();
                    len = getHelpArray.length();
                    item = new ProvideHelp();
                    item.setHeader("Get Helps");
                    for(int i=0; i<len; i++){
                        jsonObject = getHelpArray.getJSONObject(i);
                        provideHelpItem = new ProvideHelpItem();
                        provideHelpItem.setId(jsonObject.getString("id"));
                        provideHelpItem.setStatus(jsonObject.getString("status"));
                        provideHelpItem.setCreationDate(jsonObject.getString("creation_date"));
                        provideHelpItem.setCompletionDate(jsonObject.getString("completion_date"));
                        provideHelpItemList.add(provideHelpItem);
                    }
                    item.setItemList(provideHelpItemList);
                    itemList.add(item);

                    myItemAdapter.notifyDataSetChanged();
                }
            }else if(apiName.equals("appdirectslist")){
                if(response.getString("success").equals("true")){
                    JSONArray jsonArray = response.getJSONArray("data");
                    JSONObject jsonObject =null;
                    DirectUserReport item = null;
                    int len = jsonArray.length();
                    for(int i=0; i<len; i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        item = new DirectUserReport();
                        item.setName(jsonObject.getString("name"));
                        item.setPhone(jsonObject.getString("mobile_no"));
                        item.setEmail(jsonObject.getString("email"));
                        item.setUserPackage(jsonObject.getString("packages_name"));
                        item.setRegDate(jsonObject.getString("created"));
                        item.setProvideHelpDate("");
                        int status = jsonObject.getInt("provide_help_complete");
                        if(status == 0){
                            item.setProvideHelpStatus("Not assigned");
                        }else if(status == 1){
                            item.setProvideHelpStatus("Assigned");
                        }else if(status == 2){
                            item.setProvideHelpDate(jsonObject.getString("provide_help_complete_date"));
                            item.setProvideHelpStatus("Completed");
                        }
                        //  item.setProvideHelpStatus(jsonObject.getString("Completed"));
                        itemList.add(item);
                    }

                    myItemAdapter.notifyDataSetChanged();

                }else {
                    DialogAndToast.showDialog(response.getString("message"),ReportActivity.this);
                }
            }else if(apiName.equals("apptransactions")){
                if(response.getString("success").equals("true")){
                    JSONArray jsonArray = response.getJSONArray("data");
                    JSONObject jsonObject =null;
                    TransactionReport item = null;
                    int len = jsonArray.length();
                    for(int i=0; i<len; i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        item = new TransactionReport();
                        item.setDate(jsonObject.getString("date"));
                        item.setTransactionType(jsonObject.getString("transaction_type"));
                        item.setAmountType(jsonObject.getString("wallet_type"));
                        item.setAmount(jsonObject.getString("amount"));
                        item.setComment(jsonObject.getString("description"));
                        item.setUser(jsonObject.getString("reference_user"));
                        itemList.add(item);
                    }

                    myItemAdapter.notifyDataSetChanged();

                }else {
                    DialogAndToast.showDialog(response.getString("message"),ReportActivity.this);
                }
            }else if(apiName.equals("applistsupportrequests")){
                if(response.getString("success").equals("true")){
                    JSONArray jsonArray = response.getJSONArray("data");
                    JSONObject jsonObject =null;
                    SupportCenter item = null;
                    int len = jsonArray.length();
                    for(int i=0; i<len; i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        item = new SupportCenter();
                        item.setDate(jsonObject.getString("date"));
                        item.setQuery(jsonObject.getString("query"));
                        item.setAction("");
                        itemList.add(item);
                    }

                    myItemAdapter.notifyDataSetChanged();

                }else {
                    DialogAndToast.showDialog(response.getString("message"),ReportActivity.this);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),ReportActivity.this);
        }
    }

    private List<Object> getLevelReportList(int levelNo){
        LevelReport item = null;
        List<Object> itemReportList = null;
        for(Object ob : itemList){
            item = (LevelReport) ob;
            if(item.getLevelNo() == levelNo){
                itemReportList = item.getItemList();
            }
        }

        return itemReportList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG,"Result received");

        if (requestCode == 2){
            if(data != null){
                getReport();
            }
        }
    }

    private void showNoData(boolean show){
        if(show){
            recyclerView.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
        }
    }

}
