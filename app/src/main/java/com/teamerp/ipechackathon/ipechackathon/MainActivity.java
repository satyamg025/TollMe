package com.teamerp.ipechackathon.ipechackathon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.Settings.NameValueTable.VALUE;
import static android.provider.Telephony.Mms.Part.TEXT;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Toolbar search;
    static String vehicle;
    static int position=0;
    private static BottomSheetBehavior bottomSheetBehavior;
    static List<LatLng> MarkerPoints = new ArrayList<LatLng>();
    GPSTracker gpsTracker;
    static RecyclerView recyclerView;
    static String re;

    static MapView mapView;
    static GoogleMap googleMap = null;

    LinearLayout ll1,ll2;
    RouteDetails routeDetails=null;
    static TextView source,destination;
    static LinearLayoutManager linearLayoutManager;
    String type="";
    static LatLng src=null,dest=null;
    ProgressDialog progressDialog;
    //static ImageView no_route;
    static FloatingActionButton fab;
    static ImageView close,profile;
    static Toolbar toolbar;
   // static LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Dont Wait Just TollMe");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        setSupportActionBar(toolbar);

        ll1=(LinearLayout)findViewById(R.id.ll1);
        ll2=(LinearLayout)findViewById(R.id.ll2);

        close=(ImageView)findViewById(R.id.close);

        profile=(ImageView)findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));

            }
        });

        fab=(FloatingActionButton)findViewById(R.id.get_route);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (src==null || dest==null){
                    Toast.makeText(MainActivity.this,"Source and destination required",Toast.LENGTH_LONG).show();
                }
                else {
                    MainActivity.showBottomSheet(MainActivity.this, routeDetails, getSupportFragmentManager());
                }
            }
        });

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setVisibility(View.INVISIBLE);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        gpsTracker= new GPSTracker(MainActivity.this);
        source=(TextView) findViewById(R.id.et_source);
        destination=(TextView) findViewById(R.id.et_dest);

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="source";
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(MainActivity.this);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="destination";
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(MainActivity.this);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_layout));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        recyclerView=(RecyclerView)findViewById(R.id.recycler_bottom);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching current location");
        progressDialog.show();

        final Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

        if(gpsTracker.canGetLocation()){

            LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            src=latLng;
            List<Address> addresses;

            try {
                addresses = geocoder.getFromLocation(gpsTracker.getLatitude(),gpsTracker.getLongitude(), 1);
                if (addresses.size() != 0) {
                    source.setText(addresses.get(0).getLocality());
                    addMarker(src,addresses.get(0).getLocality());
                }
                else{
                }
            } catch (Exception e) {

            }

        }
        else{
            Toast.makeText(MainActivity.this,"Can't get current location",Toast.LENGTH_LONG).show();
        }
        progressDialog.dismiss();

    }


    public static void showBottomSheet(Context context,RouteDetails routeDetails,FragmentManager supportFragmentManager){

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        recyclerView.setVisibility(View.VISIBLE);

        fab.hide();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter_routes adapter_routes=new adapter_routes(context,routeDetails,supportFragmentManager);
        recyclerView.setAdapter(adapter_routes);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:

                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        fab.show();
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    default:
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                toolbar.setVisibility(View.VISIBLE);
                fab.show();
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    public static void setVehicle(String v){
        vehicle=v;
    }

    public static void showRoutes(Context context, RouteDetails routeDetails, FragmentManager supportFragmentManager){
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(MainActivity.this, data);

                if(type.equals("source")){
                    src=place.getLatLng();
                    source.setText(String.valueOf(place.getName()));
                    addMarker(src,String.valueOf(place.getName()));

                    if (src!=null && dest!=null){
                        String url=getUrl(src,dest);

                        progressDialog=new ProgressDialog(this);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Hold On!! Fetching your routes...");
                        progressDialog.show();

                        FetchUrl fetchUrl=new FetchUrl();
                        fetchUrl.execute(url);
                    }
                }
                else{
                    dest=place.getLatLng();
                    destination.setText(String.valueOf(place.getName()));
                    addMarker(dest,String.valueOf(place.getName()));

                    if (src!=null && dest!=null){
                        String url=getUrl(src,dest);

                        progressDialog=new ProgressDialog(this);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Hold On!! Fetching your routes...");
                        progressDialog.show();

                        FetchUrl fetchUrl=new FetchUrl();
                        fetchUrl.execute(url);
                    }
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(MainActivity.this, data);
                // TODO: Handle the error

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private String getUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyBSAie3Ux8f-f0RhIksE7vLDXXul685pls&alternatives=true&traffic_model=best_guess&departure_time=now&mode=driving";

        return url;
    }


    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            re=result;
            DbHandler.putString(MainActivity.this,"result",result);
            ParserTask2 parserTask2 = new ParserTask2();

            parserTask2.execute(result);

        }
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask2 extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        List<String> sumary=new ArrayList<String>();
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Data parser = new Data();

                routeDetails = parser.parse(jObject,0,MainActivity.this);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            progressDialog.dismiss();
        }
    }

    public void addMarker(LatLng latLng,String place) {
        googleMap.addMarker(new MarkerOptions().position(latLng).title(place));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;

        mapView.setVisibility(View.VISIBLE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(true);

        final Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        //googleMap.getUiSettings().setZoomControlsEnabled(true);

    }

}

