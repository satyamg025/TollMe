package com.teamerp.ipechackathon.ipechackathon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.teamerp.ipechackathon.ipechackathon.MainActivity.position;
import static com.teamerp.ipechackathon.ipechackathon.MainActivity.re;

/**
 * Created by satyam on 7/9/17.
 */


public class adapter_routes extends RecyclerView.Adapter<adapter_routes.view_holder> {

    View view;
    static Context context;
    public RouteDetails routeDetails;
    FragmentManager fragmentManager;
    public static List<LatLng> list=new ArrayList<LatLng>();
    static int pos;

    public adapter_routes(Context context, RouteDetails routeDetails, FragmentManager supportFragmentManager) {
        this.context=context;
        this.routeDetails=routeDetails;
        this.fragmentManager=supportFragmentManager;
    }

    @Override
    public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.routes,parent,false);

        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(final adapter_routes.view_holder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.summary.setText("via "+routeDetails.getSummary().get(position));
        holder.time.setText(routeDetails.getTime().get(position)+" without traffic");
        holder.distance.setText(routeDetails.getDistance().get(position));
        holder.traffic.setText(routeDetails.getTraffic().get(position));
       // holder.pay.setText("Rs." + (int)((Math.random()*10000)%250));
    }


    @Override
    public int getItemCount() {
        return routeDetails.getSummary().size();
    }
    public class view_holder extends RecyclerView.ViewHolder {

        public TextView summary,time,distance,traffic,route;
        public LinearLayout toll_details;
        public LinearLayout ll;
        public MapView mapView;
        public view_holder(View itemView) {
            super(itemView);

            summary=(TextView)itemView.findViewById(R.id.route);
            distance=(TextView)itemView.findViewById(R.id.distance);
            time=(TextView)itemView.findViewById(R.id.time);
            route=(TextView)itemView.findViewById(R.id.view_route);
            traffic=(TextView)itemView.findViewById(R.id.traffic);
            ll=(LinearLayout)itemView.findViewById(R.id.ll);
            toll_details=(LinearLayout) itemView.findViewById(R.id.toll_details);

            toll_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    pos=getAdapterPosition();
                    Log.e("listlatlng",String.valueOf(adapter_routes.list));
                    // TollTaxPOJO tollTaxPOJO=response.body();
                    Intent intent=new Intent(context,TollTaxActivity.class);
//                    intent.putStringArrayListExtra("tax", (ArrayList<String>) tollTaxPOJO.getTollTaxes());
//                    intent.putStringArrayListExtra("name", (ArrayList<String>) tollTaxPOJO.getTollName());
//                    intent.putStringArrayListExtra("toll_id", (ArrayList<String>) tollTaxPOJO.getToll_id());
                    intent.putExtra("index",getAdapterPosition());
                    intent.putExtra("route_name",routeDetails.getSummary().get(getAdapterPosition()));
                    context.startActivity(intent);

//                    ParserTask parserTask = new ParserTask();
//                    parserTask.execute(re);
//                    final ProgressDialog progressDialog=new ProgressDialog(context);
//                    progressDialog.setCancelable(false);
//                    progressDialog.setMessage("Loading...");
//                    progressDialog.show();
//
//                    TollTaxRequest tollTaxRequest=ServiceGenerator.createService(TollTaxRequest.class);
//                    Call<TollTaxPOJO> call=tollTaxRequest.call(MainActivity.source.getText().toString(),MainActivity.destination.getText().toString(),routeDetails.getSummary().get(getAdapterPosition()));
//                    call.enqueue(new Callback<TollTaxPOJO>() {
//                        @Override
//                        public void onResponse(Call<TollTaxPOJO> call, Response<TollTaxPOJO> response) {
//
//
//
//                            progressDialog.dismiss();
//                            Log.e("data",String.valueOf(response.body()));
//
//                            if(response.code()==200){
//                                TollTaxPOJO tollTaxPOJO=response.body();
//                                Intent intent=new Intent(context,TollTaxActivity.class);
//                                intent.putStringArrayListExtra("tax", (ArrayList<String>) tollTaxPOJO.getTollTaxes());
//                                intent.putStringArrayListExtra("name", (ArrayList<String>) tollTaxPOJO.getTollName());
//                                intent.putStringArrayListExtra("toll_id", (ArrayList<String>) tollTaxPOJO.getToll_id());
//                                intent.putExtra("index",String.valueOf(getAdapterPosition()));
//                                context.startActivity(intent);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<TollTaxPOJO> call, Throwable t) {
//                            progressDialog.dismiss();
//                            Log.e("rtyu" ,String.valueOf(t));
//                        }
//                    });
                }
            });

            route.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_route dialog_ = new dialog_route();
                    Bundle bundle=new Bundle();
                    bundle.putInt("index",getAdapterPosition());
                    dialog_.setArguments(bundle);
                    dialog_.show(fragmentManager,"Vehicle");
                }
            });
        }

    }


    private static class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();

                routes = parser.parse(jObject,pos);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            Log.e("latlng",String.valueOf(list));



//            TollTaxRequest tollTaxRequest=ServiceGenerator.createService(TollTaxRequest.class);
//            Call<TollTaxPOJO> call=tollTaxRequest.call(MainActivity.source.getText().toString(),MainActivity.destination.getText().toString());
//            call.enqueue(new Callback<TollTaxPOJO>() {
//                @Override
//                public void onResponse(Call<TollTaxPOJO> call, Response<TollTaxPOJO> response) {
//                    progressDialog.dismiss();
//                    if(response.code()==200){
//                        TollTaxPOJO tollTaxPOJO=response.body();
//                        Intent intent=new Intent(context,TollTaxActivity.class);
//                        intent.putStringArrayListExtra("tax", (ArrayList<String>) tollTaxPOJO.getTollTaxes());
//                        intent.putStringArrayListExtra("name", (ArrayList<String>) tollTaxPOJO.getTollName());
//                        intent.putStringArrayListExtra("toll_id", (ArrayList<String>) tollTaxPOJO.getToll_id());
//                        context.startActivity(intent);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<TollTaxPOJO> call, Throwable t) {
//                    progressDialog.dismiss();
//                    Log.e("rtyu" ,String.valueOf(t));
//                }
//            });

        }
    }
}
