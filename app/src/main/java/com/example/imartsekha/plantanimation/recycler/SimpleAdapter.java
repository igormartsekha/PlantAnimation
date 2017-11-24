package com.example.imartsekha.plantanimation.recycler;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.imartsekha.plantanimation.R;

/**
 * Created by imartsekha on 11/24/17.
 */

public class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_WEEK = 0;
    public static final int ITEM_TYPE_PLANT = 1;


    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if(viewType == ITEM_TYPE_WEEK) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weak_layout, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_layout, parent, false);
        }
        return new SimpleAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_WEEK;
        } else {
            return ITEM_TYPE_PLANT;
        }
    }


    @Override
    public int getItemCount() {
        return 2;
    }

    static class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {
        public SimpleAdapterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
