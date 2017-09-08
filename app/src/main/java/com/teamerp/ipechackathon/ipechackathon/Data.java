package com.teamerp.ipechackathon.ipechackathon;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by satyam on 7/9/17.
 */

public class Data {

    public RouteDetails parse(JSONObject jObject, int f, Context context){

       // Toast.makeText(context,"hi",Toast.LENGTH_SHORT).show();
        Log.e("op","hi");
        List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONObject jsonObject;
        RouteDetails routeDetails=null;
        List<String> summary=new ArrayList<String>();
        List<String> time=new ArrayList<String>();
        List<String> traffic=new ArrayList<String>();
        List<String> distance=new ArrayList<String>();

        try {

            jRoutes = jObject.getJSONArray("routes");
            for(int i=0;i<jRoutes.length();i++){

                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                for (int j = 0; j < jLegs.length(); j++) {
                    distance.add(jLegs.getJSONObject(j).getJSONObject("distance").getString("text"));
                    time.add(jLegs.getJSONObject(j).getJSONObject("duration").getString("text"));
                    traffic.add(jLegs.getJSONObject(j).getJSONObject("duration_in_traffic").getString("text"));
                }

                jsonObject= (JSONObject) jRoutes.get(i);
                    summary.add(jsonObject.getString("summary"));
                Log.e("op",jsonObject.getString("summary"));

                routeDetails=new RouteDetails(distance,time,summary,traffic);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }



        return routeDetails;
    }
}
