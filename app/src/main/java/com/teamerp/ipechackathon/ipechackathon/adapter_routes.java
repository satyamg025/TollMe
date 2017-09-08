package com.teamerp.ipechackathon.ipechackathon;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satyam on 7/9/17.
 */


public class adapter_routes extends RecyclerView.Adapter<adapter_routes.view_holder> {

    View view;
    Context context;
   public RouteDetails routeDetails;

    public adapter_routes(Context context, RouteDetails routeDetails) {
        this.context=context;
        this.routeDetails=routeDetails;
    }

    @Override
    public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.routes,parent,false);

        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(final adapter_routes.view_holder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.summary.setText(routeDetails.getSummary().get(position));
        holder.time.setText(routeDetails.getTime().get(position));
        holder.distance.setText(routeDetails.getDistance().get(position));
        holder.traffic.setText(routeDetails.getTraffic().get(position));
    }


    @Override
    public int getItemCount() {
        return routeDetails.getSummary().size();
    }
    public class view_holder extends RecyclerView.ViewHolder {

        public TextView summary,time,distance,traffic;
        public view_holder(View itemView) {
            super(itemView);

            summary=(TextView)itemView.findViewById(R.id.route);
            distance=(TextView)itemView.findViewById(R.id.distance);
            time=(TextView)itemView.findViewById(R.id.time);
            traffic=(TextView)itemView.findViewById(R.id.traffic);
        }

    }
}