package com.teamerp.ipechackathon.ipechackathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by satyam on 8/12/17.
 */

public class OrderFragment extends Fragment {
    private View mRootView;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (View) inflater.inflate(R.layout.previous_order, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView=(RecyclerView)mRootView.findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter_order adapter_order=new adapter_order(getFragmentManager());
        recyclerView.setAdapter(adapter_order);
    }

    public static Fragment newInstance() {
        return new OrderFragment();
    }

}