package com.teamerp.ipechackathon.ipechackathon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by satyam on 4/11/17.
 */

public class dialog_toll extends DialogFragment {

    View view;
    RecyclerView recyclerView;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_route, null);
        builder.setView(view);
//
//        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_toll);
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(adapter_routes.context);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        adapter_toll adapter_toll=new adapter_toll(getArguments().getStringArrayList("tax"),getArguments().getStringArrayList("tax"),getArguments().getStringArrayList("name"),this);
//        recyclerView.setAdapter(adapter_toll);

        return builder.create();
    }
}
