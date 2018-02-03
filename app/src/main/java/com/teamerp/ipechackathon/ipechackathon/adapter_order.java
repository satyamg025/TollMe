package com.teamerp.ipechackathon.ipechackathon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by satyam on 7/9/17.
 */


public class adapter_order extends RecyclerView.Adapter<adapter_order.view_holder> {

    View view;

    public FragmentManager fragment;

    public adapter_order(FragmentManager fragmentManager) {
        this.fragment = fragmentManager;
    }

    @Override
    public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_prev, parent, false);

        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(final adapter_order.view_holder holder, final int position) {
        holder.setIsRecyclable(false);

    }


    @Override
    public int getItemCount() {
        return 10;
    }

    public class view_holder extends RecyclerView.ViewHolder {
        public ImageView qr;

        public view_holder(final View itemView) {
            super(itemView);

            qr = (ImageView) itemView.findViewById(R.id.qr);
            qr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_qr dialog_ = new dialog_qr();
                    dialog_.show(fragment,"Vehicle");
                  //  ProfileActivity.zoomImageFromThumb(qr, R.drawable.download);
                }
            });

        }

    }

}