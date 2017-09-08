package com.teamerp.ipechackathon.ipechackathon;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by satyam on 8/9/17.
 */

public class dialog extends DialogFragment{

    View view;
    RadioGroup radioGroup;
    String map_type="normal";
    RadioButton btn1,btn2,btn3,btn4,btn5;
    Button set;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_vehicle, null);
        builder.setView(view);

        radioGroup=(RadioGroup)view.findViewById(R.id.radio_group);
        btn1=(RadioButton)view.findViewById(R.id.two);
        btn2=(RadioButton)view.findViewById(R.id.three);
        btn3=(RadioButton)view.findViewById(R.id.more);

        set=(Button)view.findViewById(R.id.submit);

        btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    MainActivity.vehicle="two";
                    dismiss();
                }
            }
        });
        btn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    MainActivity.vehicle="three";
                    dismiss();
                }
            }
        });
        btn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    MainActivity.vehicle="more";
                    dismiss();
                }
            }
        });

        return builder.create();
    }
}