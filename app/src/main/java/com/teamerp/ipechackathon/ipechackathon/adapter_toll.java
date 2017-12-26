package com.teamerp.ipechackathon.ipechackathon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by satyam on 7/9/17.
 */


public class adapter_toll extends RecyclerView.Adapter<adapter_toll.view_holder> {

    View view;
    Context context;
    List<String> tax=new ArrayList<String>(),name=new ArrayList<String>(),toll_id=new ArrayList<String>();
    double mul;
    double total=0;
    public String type,rname;

    public adapter_toll(double mul, List<String> tax, List<String> name, List<String> toll_id, Context context, String route_name, String type) {
        this.context=context;
        this.tax=tax;
        this.name=name;
        this.toll_id=toll_id;
        this.mul=mul;
        this.rname=route_name;
        this.type=type;
    }


    @Override
    public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_toll,parent,false);
        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(final adapter_toll.view_holder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.name.setText(name.get(position));
        holder.pay.setText(String.valueOf(Integer.valueOf(tax.get(position))*mul));

        total+=Integer.valueOf(tax.get(position))*mul;
        if(position==tax.size()-1){
            TollTaxActivity.setTotal(total);
        }
    }


    @Override
    public int getItemCount() {
        return name.size();
    }
    public class view_holder extends RecyclerView.ViewHolder {

        public TextView name;
        public Button pay;
        public view_holder(View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.tname);
            pay=(Button)itemView.findViewById(R.id.pay);
            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedpreferences = context.getSharedPreferences("DB_NAME", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("toll_id", toll_id.get(getAdapterPosition()));
                    editor.commit();
                    Intent intent=new Intent(context,PaymentActivity.class);
                    DbHandler.putString(context,"route",rname);
                    DbHandler.putString(context,"amount",String.valueOf(Integer.valueOf(tax.get(getAdapterPosition()))*mul));
                    DbHandler.putString(context,"tid",String.valueOf(toll_id.get(getAdapterPosition())));
                    DbHandler.putString(context,"type",type);

                    context.startActivity(intent);
                }
            });
        }

    }
}