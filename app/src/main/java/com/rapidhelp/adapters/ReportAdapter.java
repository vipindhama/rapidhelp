package com.rapidhelp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rapidhelp.R;
import com.rapidhelp.models.DirectUserReport;
import com.rapidhelp.models.JoiningReport;
import com.rapidhelp.models.LevelItemReport;
import com.rapidhelp.models.LevelReport;
import com.rapidhelp.models.ProvideHelp;
import com.rapidhelp.models.ProvideHelpItem;
import com.rapidhelp.models.SupportCenter;
import com.rapidhelp.models.TransactionReport;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> itemList;
    private Context context;
    private String type;

    public ReportAdapter(Context context, List<Object> itemList,String type) {
        this.itemList = itemList;
        this.context=context;
        this.type = type;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textName,textPackage,textUserId,textRegDate,textProvideHelpStatus,textProvideHelpDate;


        public MyViewHolder(View itemView) {
            super(itemView);
            textName=itemView.findViewById(R.id.text_name);
            textUserId=itemView.findViewById(R.id.text_user_id);
            if(!type.equals("Level Report")){
                textPackage=itemView.findViewById(R.id.text_package);
            }
            textRegDate=itemView.findViewById(R.id.text_reg_date);
            textProvideHelpDate=itemView.findViewById(R.id.text_ph_date);
            textProvideHelpStatus=itemView.findViewById(R.id.text_provide_help_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public class MyLevelReportHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textLevelNo;
        private RecyclerView recyclerView;


        public MyLevelReportHolder(View itemView) {
            super(itemView);
            textLevelNo=itemView.findViewById(R.id.text_level_no);
            recyclerView=itemView.findViewById(R.id.recycler_view);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public class MyLevelItemReportHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textName,textUserId,textRegDate,textProvideHelpStatus,textProvideHelpDate;


        public MyLevelItemReportHolder(View itemView) {
            super(itemView);
            textName=itemView.findViewById(R.id.text_name);
            textUserId=itemView.findViewById(R.id.text_user_id);
            textRegDate=itemView.findViewById(R.id.text_reg_date);
            textProvideHelpDate=itemView.findViewById(R.id.text_ph_date);
            textProvideHelpStatus=itemView.findViewById(R.id.text_provide_help_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public class MyHelpReportHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader;
        private RecyclerView recyclerView;


        public MyHelpReportHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_header);
            recyclerView=itemView.findViewById(R.id.recycler_view);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public class MyHelpItemReportHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textID,textStatus,textCreationDate,textCompletionDate;


        public MyHelpItemReportHolder(View itemView) {
            super(itemView);
            textID=itemView.findViewById(R.id.text_id);
            textStatus=itemView.findViewById(R.id.text_status);
            textCreationDate=itemView.findViewById(R.id.text_creation_date);
            textCompletionDate=itemView.findViewById(R.id.text_completion_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public class MyDirectUserReportHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textName,textPackage,textEmail,textPhone,textRegDate,textProvideHelpStatus,textProvideHelpDate;


        public MyDirectUserReportHolder(View itemView) {
            super(itemView);
            textName=itemView.findViewById(R.id.text_name);
            textEmail=itemView.findViewById(R.id.text_email);
            textPhone=itemView.findViewById(R.id.text_phone);
            textPackage=itemView.findViewById(R.id.text_package);
            textRegDate=itemView.findViewById(R.id.text_reg_date);
            textProvideHelpDate=itemView.findViewById(R.id.text_ph_date);
            textProvideHelpStatus=itemView.findViewById(R.id.text_provide_help_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public class MyTransactionReportHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textDate,textTransactionType,textAmountType,textAmount,textComment,textUser;


        public MyTransactionReportHolder(View itemView) {
            super(itemView);
            textDate=itemView.findViewById(R.id.text_date);
            textTransactionType=itemView.findViewById(R.id.text_transaction_type);
            textAmountType=itemView.findViewById(R.id.text_amount_type);
            textAmount=itemView.findViewById(R.id.text_amount);
            textComment=itemView.findViewById(R.id.text_comment);
            textUser=itemView.findViewById(R.id.text_user);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public class MySupportCenterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textQuery,textDate,textAction;


        public MySupportCenterHolder(View itemView) {
            super(itemView);
            textDate=itemView.findViewById(R.id.text_date);
            textQuery=itemView.findViewById(R.id.text_query);
            textAction=itemView.findViewById(R.id.text_action);
        }

        @Override
        public void onClick(View view) {
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case 0:
                View v0 = inflater.inflate(R.layout.joining_item_layout, parent, false);
                viewHolder = new MyViewHolder(v0);
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.level_report_item_layout, parent, false);
                viewHolder = new MyLevelReportHolder(v1);
                break;
            case 2:
                View v2 = inflater.inflate(R.layout.level_report_item_item_layout, parent, false);
                viewHolder = new MyLevelItemReportHolder(v2);
                break;
            case 3:
                View v3 = inflater.inflate(R.layout.provide_help_item_layout, parent, false);
                viewHolder = new MyHelpReportHolder(v3);
                break;
            case 4:
                View v4 = inflater.inflate(R.layout.provide_help_item_item_layout, parent, false);
                viewHolder = new MyHelpItemReportHolder(v4);
                break;
            case 5:
                View v5 = inflater.inflate(R.layout.direct_user_report_item_layout, parent, false);
                viewHolder = new MyDirectUserReportHolder(v5);
                break;
            case 6:
                View v6 = inflater.inflate(R.layout.transaction_report_item_layout, parent, false);
                viewHolder = new MyTransactionReportHolder(v6);
                break;
            case 7:
                View v7 = inflater.inflate(R.layout.support_center_item_layout, parent, false);
                viewHolder = new MySupportCenterHolder(v7);
                break;
            default:
                View v = inflater.inflate(R.layout.joining_item_layout, parent, false);
                viewHolder = new MyViewHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(type.equals("Joining Report")){
            return 0;
        }else if(type.equals("Level Report")){
            return 1;
        }else if(type.equals("Level Item Report")){
            return 2;
        }else if(type.equals("Provide Helps")){
            return 3;
        }else if(type.equals("Provide Item Helps")){
            return 4;
        }else if(type.equals("Direct Users Report")){
            return 5;
        }else if(type.equals("Transaction Report")){
            return 6;
        }else if(type.equals("Support Center")){
            return 7;
        } else{
            return 10;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            JoiningReport item = (JoiningReport) itemList.get(position);
            MyViewHolder myViewHolder = (MyViewHolder)holder;
            myViewHolder.textName.setText(item.getName());
            myViewHolder.textUserId.setText(item.getUserId());
            myViewHolder.textPackage.setText(item.getUserPackage());
            myViewHolder.textRegDate.setText(item.getRegDate());
            myViewHolder.textProvideHelpDate.setText(item.getProvideHelpDate());
            myViewHolder.textProvideHelpStatus.setText(item.getProvideHelpStatus());
        }else if(holder instanceof MyLevelReportHolder){
            LevelReport item = (LevelReport) itemList.get(position);
            MyLevelReportHolder myViewHolder = (MyLevelReportHolder)holder;
            myViewHolder.textLevelNo.setText("Level No: "+item.getLevelNo());
            myViewHolder.recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManagerHomeMenu=new LinearLayoutManager(context);
            myViewHolder.recyclerView.setLayoutManager(layoutManagerHomeMenu);
            myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            ReportAdapter myItemAdapter=new ReportAdapter(context,item.getItemList(),"Level Item Report");
            myViewHolder.recyclerView.setAdapter(myItemAdapter);
        }else if(holder instanceof MyLevelItemReportHolder){
            LevelItemReport item = (LevelItemReport) itemList.get(position);
            MyLevelItemReportHolder myViewHolder = (MyLevelItemReportHolder)holder;
            myViewHolder.textName.setText(item.getName());
            myViewHolder.textUserId.setText(item.getUserId());
            myViewHolder.textRegDate.setText(item.getRegDate());
            myViewHolder.textProvideHelpDate.setText(item.getProvideHelpDate());
            myViewHolder.textProvideHelpStatus.setText(item.getProvideHelpStatus());
        }else if(holder instanceof MyHelpReportHolder){
            ProvideHelp item = (ProvideHelp) itemList.get(position);
            MyHelpReportHolder myViewHolder = (MyHelpReportHolder)holder;
            myViewHolder.textHeader.setText(item.getHeader());
            myViewHolder.recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManagerHomeMenu=new LinearLayoutManager(context);
            myViewHolder.recyclerView.setLayoutManager(layoutManagerHomeMenu);
            myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            ReportAdapter myItemAdapter=new ReportAdapter(context,item.getItemList(),"Provide Item Helps");
            myViewHolder.recyclerView.setAdapter(myItemAdapter);
        }else if(holder instanceof MyHelpItemReportHolder){
            ProvideHelpItem item = (ProvideHelpItem) itemList.get(position);
            MyHelpItemReportHolder myViewHolder = (MyHelpItemReportHolder)holder;
            myViewHolder.textID.setText(item.getId());
            myViewHolder.textStatus.setText(item.getStatus());
            myViewHolder.textCreationDate.setText(item.getCreationDate());
            myViewHolder.textCompletionDate.setText(item.getCompletionDate());
        }else if(holder instanceof MyDirectUserReportHolder){
            DirectUserReport item = (DirectUserReport) itemList.get(position);
            MyDirectUserReportHolder myViewHolder = (MyDirectUserReportHolder)holder;
            myViewHolder.textName.setText(item.getName());
            myViewHolder.textEmail.setText(item.getEmail());
            myViewHolder.textPhone.setText(item.getPhone());
            myViewHolder.textPackage.setText(item.getUserPackage());
            myViewHolder.textRegDate.setText(item.getRegDate());
            myViewHolder.textProvideHelpDate.setText(item.getProvideHelpDate());
            myViewHolder.textProvideHelpStatus.setText(item.getProvideHelpStatus());
        }else if(holder instanceof MyTransactionReportHolder){
            TransactionReport item = (TransactionReport) itemList.get(position);
            MyTransactionReportHolder myViewHolder = (MyTransactionReportHolder)holder;
            myViewHolder.textDate.setText(item.getDate());
            myViewHolder.textTransactionType.setText(item.getTransactionType());
            myViewHolder.textAmountType.setText(item.getAmountType());
            myViewHolder.textAmount.setText(item.getAmount());
            myViewHolder.textComment.setText(item.getComment());
            myViewHolder.textUser.setText(item.getUser());
        }else if(holder instanceof MySupportCenterHolder){
            SupportCenter item = (SupportCenter) itemList.get(position);
            MySupportCenterHolder myViewHolder = (MySupportCenterHolder)holder;
            myViewHolder.textDate.setText(item.getDate());
            myViewHolder.textQuery.setText(item.getQuery());
            myViewHolder.textAction.setText(item.getAction());
        }
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
