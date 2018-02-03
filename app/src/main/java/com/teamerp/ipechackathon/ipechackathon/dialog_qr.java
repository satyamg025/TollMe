package com.teamerp.ipechackathon.ipechackathon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by satyam on 8/12/17.
 */

public class dialog_qr extends DialogFragment {

    View view;
   // ImageView qr,close;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_qr, null);
        builder.setView(view);

       // qr=(ImageView) view.findViewById(R.id.qr);
       // close=(ImageView)view.findViewById(R.id.close);

//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });
        return builder.create();
    }
}

