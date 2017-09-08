package com.teamerp.ipechackathon.ipechackathon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
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

import static android.provider.Settings.NameValueTable.VALUE;
import static android.provider.Telephony.Mms.Part.TEXT;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    Toolbar search;
    static MapView mapView;
    static GoogleMap googleMap = null;
    private static BottomSheetBehavior bottomSheetBehavior;
    List<LatLng> MarkerPoints = new ArrayList<LatLng>();
    GPSTracker gpsTracker;
    static RecyclerView recyclerView;
    String re;
    RouteDetails routeDetails=null;
    static LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ll=(LinearLayout)findViewById(R.id.ll);
        gpsTracker= new GPSTracker(MainActivity.this);
        search = (Toolbar) findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setVisibility(View.INVISIBLE);
        mapView.onCreate(savedInstanceState);

        //mMapView.onDestroy();
        // needed to get the map to display immediately

        mapView.getMapAsync(this);
        setSupportActionBar(search);
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_layout));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_bottom);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);


    }


    public static void showBottomSheet(Context context,RouteDetails routeDetails){

        Log.e("summ",String.valueOf(routeDetails));
//        Toast.makeText(context,String.valueOf(summary),Toast.LENGTH_LONG).show();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter_routes adapter_routes=new adapter_routes(context,routeDetails);
        recyclerView.setAdapter(adapter_routes);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        //toolbar.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                       // toolbar.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
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

//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                toolbar.setVisibility(View.VISIBLE);
//            }
//        });



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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(MainActivity.this, data);
                // l = place.getLatLng();
                MarkerPoints.add(place.getLatLng());
                googleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(String.valueOf(place.getName())));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                addMarker(place.getLatLng(),"destination");

                if(MarkerPoints.size()>1) {
                    LatLng origin = MarkerPoints.get(0);
                    LatLng dest = MarkerPoints.get(1);

                    String url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    FetchUrl FetchUrl = new FetchUrl();

                    FetchUrl.execute(url);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                }

                Toast.makeText(MainActivity.this, place.getName(), Toast.LENGTH_LONG).show();

                //location.setText(place.getName());

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

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyBSAie3Ux8f-f0RhIksE7vLDXXul685pls&alternatives=true&traffic_model=best_guess&departure_time=now&mode=driving";


        return url;
    }


    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            re=result;
            ParserTask2 parserTask2 = new ParserTask2();

            // Invokes the thread for parsing the JSON data
            parserTask2.execute(result);
            Log.e("drt",String.valueOf(result));

        }
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void addMarker(LatLng latLng,String place) {
        if(MarkerPoints.size()==0) {
            MarkerPoints.add(latLng);
            googleMap.addMarker(new MarkerOptions().position(latLng).title(""));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        else{
            if(place.equals("origin")){
                MarkerPoints.add(0,latLng);
                googleMap.addMarker(new MarkerOptions().position(latLng).title(""));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
            else{
                if(MarkerPoints.size()==1){
                    MarkerPoints.add(latLng);
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(""));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
                else{
                    MarkerPoints.add(1,latLng);
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(""));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;

        mapView.setVisibility(View.VISIBLE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(true);

        final Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if(gpsTracker.canGetLocation()){

            LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            addMarker(latLng,"origin");
        }
        else{
            Toast.makeText(MainActivity.this,"Can't get current location",Toast.LENGTH_LONG).show();
        }

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Toast.makeText(MainActivity.this,"location",Toast.LENGTH_LONG).show();
                if(gpsTracker.canGetLocation()){

                    LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                    addMarker(latLng,"origin");
                }
                else{
                    Toast.makeText(MainActivity.this,"Can't get current location",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

       // LatLng sydney2 = new LatLng(28.6329, 77.2195);



       // googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));


    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                Log.e("ddd",String.valueOf(jsonData));
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject,0);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask2",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                googleMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }


    private class ParserTask2 extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        List<String> sumary=new ArrayList<String>();
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                Log.e("ddd",String.valueOf(jsonData));
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                Data parser = new Data();
                Log.d("ParserTask", parser.toString());

                routeDetails = parser.parse(jObject,0,MainActivity.this);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask2",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            MainActivity.showBottomSheet(MainActivity.this,routeDetails);
        }
    }
}

