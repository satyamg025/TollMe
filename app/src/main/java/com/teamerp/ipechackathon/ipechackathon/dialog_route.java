package com.teamerp.ipechackathon.ipechackathon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.teamerp.ipechackathon.ipechackathon.MainActivity.re;

/**
 * Created by satyam on 4/11/17.
 */

public class dialog_route extends DialogFragment implements OnMapReadyCallback {

    View view;
    static MapView mapView;
    static GoogleMap googleMap = null;
    static LatLng src,dest;
    static int index;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_route, null);
        builder.setView(view);

        index=getArguments().getInt("index");
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.setVisibility(View.INVISIBLE);
        mapView.onCreate(savedInstanceState);

        src=MainActivity.src;
        dest=MainActivity.dest;

        mapView.getMapAsync(this);

        setRoute();
        return builder.create();
    }

    public void setRoute(){
        Log.e("res123",DbHandler.getString(getActivity(),"result",""));

        ParserTask parserTask = new ParserTask();
        parserTask.execute(DbHandler.getString(getActivity(),"result",""));
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
    public void onMapReady(final GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        mapView.setVisibility(View.VISIBLE);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        googleMap.setTrafficEnabled(true);
    }

    private static class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();

                routes = parser.parse(jObject,index);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            googleMap.clear();

            googleMap.addMarker(new MarkerOptions().position(src).title(""));

            googleMap.addMarker(new MarkerOptions().position(dest).title(""));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(src));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));

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
                googleMap.addPolyline(lineOptions);
            }
            else {

            }
        }
    }


}
