package com.teamerp.ipechackathon.ipechackathon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class OtpFragment extends Fragment {

    View mRootView;
    Button one,two,three,four,five,six,seven,eight,nine,zero,clear,ok;
    TextView tv1,tv2,tv3,tv4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv1=(TextView)mRootView.findViewById(R.id.num1);
        tv3=(TextView)mRootView.findViewById(R.id.num2);
        tv3=(TextView)mRootView.findViewById(R.id.num3);
        tv4=(TextView)mRootView.findViewById(R.id.num4);

        one=(Button) mRootView.findViewById(R.id.one);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = (View) inflater.inflate(R.layout.previous_order, container, false);
        return mRootView;
    }


}
