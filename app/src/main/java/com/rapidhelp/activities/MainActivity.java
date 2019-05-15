package com.rapidhelp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.rapidhelp.R;
import com.rapidhelp.adapters.MyItemAdapter;
import com.rapidhelp.utilities.ConnectionDetector;
import com.rapidhelp.utilities.Constants;
import com.rapidhelp.utilities.DialogAndToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends NetworkBaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerView;
    private MyItemAdapter myItemAdapter;
    private List<Object> itemList;
    private TextView textViewError;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemList = new ArrayList<>();
        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        textViewError = findViewById(R.id.text_error);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerHomeMenu=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManagerHomeMenu);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
      /*  int resId = R.anim.layout_animation_slide_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);*/
        myItemAdapter=new MyItemAdapter(this,itemList,"homeList");
        recyclerView.setAdapter(myItemAdapter);

        if(itemList.size() == 0){
            showNoData(true);
        }

        if(ConnectionDetector.isNetworkAvailable(MainActivity.this)){
            getDasboardData();
        }else{
            DialogAndToast.showDialog(getResources().getString(R.string.no_internet),MainActivity.this);
        }

        intDrawer();

    }

    private void getDasboardData(){
        Map<String,String> params=new HashMap<>();
        params.put("user_token",sharedPreferences.getString(Constants.TOKEN,""));
        params.put("login_name",sharedPreferences.getString(Constants.USER_ID,""));
        String url=getResources().getString(R.string.url)+"/merchants/appdashboard";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"appdashboard");
    }

    private void runLayoutAnimation() {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void intDrawer(){

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        TextView textViewName = headerLayout.findViewById(R.id.text_name);
        TextView textViewEmail = headerLayout.findViewById(R.id.text_email);
        textViewName.setText(sharedPreferences.getString(Constants.FULL_NAME,""));
        textViewEmail.setText(sharedPreferences.getString(Constants.EMAIL,""));
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        navItemSelected(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void navItemSelected(int id){
        if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_joining_report) {
            Intent intent = new Intent(MainActivity.this,ReportActivity.class);
            intent.putExtra("flag","Joining Report");
            startActivity(intent);
        }else if (id == R.id.nav_level_report) {
            Intent intent = new Intent(MainActivity.this,ReportActivity.class);
            intent.putExtra("flag","Level Report");
            startActivity(intent);
        }else if (id == R.id.nav_provide_help) {
            Intent intent = new Intent(MainActivity.this,ReportActivity.class);
            intent.putExtra("flag","Provide Helps");
            startActivity(intent);
        }else if (id == R.id.nav_direct_user_report) {
            Intent intent = new Intent(MainActivity.this,ReportActivity.class);
            intent.putExtra("flag","Direct Users Report");
            startActivity(intent);
        }else if (id == R.id.nav_transaction_report) {
            Intent intent = new Intent(MainActivity.this,ReportActivity.class);
            intent.putExtra("flag","Transaction Report");
            startActivity(intent);
        }else if (id == R.id.nav_support_center) {
            Intent intent = new Intent(MainActivity.this,ReportActivity.class);
            intent.putExtra("flag","Support Center");
            startActivity(intent);
        }else if (id == R.id.nav_change_password) {
            Intent intent = new Intent(MainActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_request_registration_pin) {
            Intent intent = new Intent(MainActivity.this,RegsiterPinActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_transfer_funds_to_team) {
            Intent intent = new Intent(MainActivity.this,TransferFundActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lagout) {
           logout();
        }
    }

    private void logout(){
        Map<String,String> params=new HashMap<>();
        params.put("user_token",sharedPreferences.getString(Constants.TOKEN,""));
        params.put("login_name",sharedPreferences.getString(Constants.USER_ID,""));
        String url=getResources().getString(R.string.url)+"/merchants/applogout";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"applogout");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if(apiName.equals("applogout")){
                if(response.getString("success").equals("true")){
                    editor.clear();
                    editor.commit();
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    DialogAndToast.showDialog(response.getString("message"),MainActivity.this);
                }
            }else if(apiName.equals("appdashboard")){
                if(response.getString("success").equals("true")){

                }else {
                    DialogAndToast.showDialog(response.getString("message"),MainActivity.this);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),MainActivity.this);
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
