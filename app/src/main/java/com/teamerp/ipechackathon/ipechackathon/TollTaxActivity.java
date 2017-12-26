package com.teamerp.ipechackathon.ipechackathon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.teamerp.ipechackathon.ipechackathon.MainActivity.re;

public class TollTaxActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RadioGroup radioGroup;
    static RadioButton single;
    RadioButton round;
    RadioButton monthly;
    static Button total;
    static ProgressDialog progressDialog;
    static int index=0;
    static List<LatLng> l=new ArrayList<LatLng>();
    static List<String> name=new ArrayList<String>();
    static List<String> cost=new ArrayList<String>();
    static List<String> cost2=new ArrayList<String>();

    double w;
    static List<String> id=new ArrayList<String>();
    static String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toll_tax);

        progressDialog=new ProgressDialog(this);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Toll Tax");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioGroup=(RadioGroup)findViewById(R.id.radio_group);
        single=(RadioButton)findViewById(R.id.single);
        monthly=(RadioButton)findViewById(R.id.monthly);
        round=(RadioButton)findViewById(R.id.round);

        index=getIntent().getExtras().getInt("index");
        total=(Button)findViewById(R.id.pay);
        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<cost.size();i++){
                    cost2.add(String.valueOf(Integer.valueOf(cost.get(i))*w));
                }
                DbHandler.putString(TollTaxActivity.this,"route",getIntent().getExtras().getString("route_name"));
                DbHandler.putString(TollTaxActivity.this,"amount",String.valueOf(TextUtils.join(",",cost2)));
                DbHandler.putString(TollTaxActivity.this,"tid",String.valueOf(TextUtils.join(",",id)));

                startActivity(new Intent(TollTaxActivity.this,PaymentActivity.class));
            }
        });

        ParserTask parserTask = new ParserTask();
        parserTask.execute(DbHandler.getString(TollTaxActivity.this,"result","{}"));

        Log.e("result123",DbHandler.getString(TollTaxActivity.this,"result","{}"));
        single.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    set(1);
                    w=1;
                    type="1";
                    DbHandler.putString(TollTaxActivity.this,"type",type);
                }
            }
        });

        round.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    set(1.5);
                    w=1.5;
                    type="2";
                    DbHandler.putString(TollTaxActivity.this,"type",type);
                }
            }
        });

        monthly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    set(22);
                    w=22;
                    type="60";
                    DbHandler.putString(TollTaxActivity.this,"type",type);
                }
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recycler_toll);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    public static void tollData(){

        progressDialog.setCancelable(false);
        progressDialog.setMessage("Hold On!! Fetching your tollBooths...");
        progressDialog.show();

        TollRequest tollRequest=ServiceGenerator.createService(TollRequest.class);
        Call<TollPOJO> call=tollRequest.call();
        call.enqueue(new Callback<TollPOJO>() {
            @Override
            public void onResponse(Call<TollPOJO> call, Response<TollPOJO> response) {
                if(response.code()==200){
                    TollPOJO data=response.body();
                    //if(data.getError()){
                        for(int i=0;i<data.getAllTolls().size();i++){
                            if(PolyUtil.containsLocation((data.getAllTolls().get(i).getLatitude()),data.getAllTolls().get(i).getLongitude(),l,false)){
                                name.add(data.getAllTolls().get(i).getTollname());
                                id.add(String.valueOf(data.getAllTolls().get(i).getTollplazaid()));
                                cost.add(String.valueOf(data.getAllTolls().get(i).getCost()));
                            }
                        }
                        single.setChecked(true);
                        progressDialog.dismiss();

                }
                else{
                    progressDialog.dismiss();
                    //Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_INDEFINITE)
                            //.show();
                }
            }

            @Override
            public void onFailure(Call<TollPOJO> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

    public static void setTotal(double amount){
        total.setText(String.valueOf(amount));
    }

    public void set(double i){
        adapter_toll adapter_toll=new adapter_toll(i,cost,name,id,this,getIntent().getExtras().getString("route_name"),type);
        recyclerView.setAdapter(adapter_toll);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private static class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.e("json123",String.valueOf(jObject));
                DataParser parser = new DataParser();

                routes = parser.parse(jObject,index);
                Log.e("json345",String.valueOf(routes));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            Log.e("res345",String.valueOf(result));
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            if(lineOptions != null) {
               // googleMap.addPolyline(lineOptions);
            }
            else {

            }
            tollData();

        }
    }

    public static class DataParser {

        /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
        public List<List<HashMap<String,String>>> parse(JSONObject jObject,int f){

            List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
            JSONArray jRoutes;
            JSONArray jLegs;
            JSONArray jSteps;
            List<String> summary=new ArrayList<String>();
            // List<LatLng> l=new ArrayList<LatLng>();

            try {

                jRoutes = jObject.getJSONArray("routes");

                for(int i=0;i<jRoutes.length();i++){
                    if(i==f) {
                        Log.e("comes",String.valueOf(i));
                        jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                        List path = new ArrayList<>();

                        /** Traversing all legs */
                        for (int j = 0; j < jLegs.length(); j++) {
                            jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                            /** Traversing all steps */
                            for (int k = 0; k < jSteps.length(); k++) {
                                String polyline = "";
                                polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                                List<LatLng> list = decodePoly(polyline);
                                for(int o=0;o<list.size();o++){
                                    l.add(list.get(o));
                                }
                                Log.e("pnts",String.valueOf(l));
                                /** Traversing all points */
                                for (int li = 0; li < list.size(); li++) {
                                    HashMap<String, String> hm = new HashMap<>();

                                    hm.put("lat", Double.toString((list.get(li)).latitude));
                                    hm.put("lng", Double.toString((list.get(li)).longitude));

                                    path.add(hm);
                                }
                                Log.e("tyui",String.valueOf(path));
                            }
                            routes.add(path);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){
            }


            Log.e("oplk",String.valueOf(routes));
            return routes;
        }


        /**
         * Method to decode polyline points
         * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
         * */
        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
    }
}
