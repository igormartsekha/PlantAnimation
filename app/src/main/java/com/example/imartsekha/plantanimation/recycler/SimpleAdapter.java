package com.example.imartsekha.plantanimation.recycler;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.imartsekha.plantanimation.R;
import com.example.imartsekha.plantanimation.customviews.PlantView;

/**
 * Created by imartsekha on 11/24/17.
 */

public class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_MONTH = 0;
    public static final int ITEM_TYPE_WEEK = 1;
    public static final int ITEM_TYPE_PLANT = 2;
    public static final int ITEM_TYPE_TIME_LINE = 3;


    public static final String SHAPE_MOVE = "MOVE";
    public static final String SHAPE_EXERCISE = "EXCERCISE";
    public static final String SHAPE_RELAX = "RELAX";
    public static final String SHAPE_SLEEP = "SLEEP";

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case ITEM_TYPE_MONTH:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_layout, parent, false);
                return new SimpleAdapterViewHolder(v);
            case ITEM_TYPE_WEEK:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weak_layout, parent, false);
                return new WeekViewHolder(v);
            case ITEM_TYPE_PLANT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_layout, parent, false);
                return new PlantViewHolder(v);
            case ITEM_TYPE_TIME_LINE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_layout, parent, false);
                return new WeekViewHolder(v);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public int getPositionViewType(int viewType) {
        for(int i=0; i < getItemCount(); i++) {
            if(viewType == getItemViewType(i))
                return i;
        }
        throw new IllegalArgumentException();
    }

    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ITEM_TYPE_MONTH;
            case 1:
                return ITEM_TYPE_WEEK;
            case 2:
                return ITEM_TYPE_PLANT;
            case 3:
                return ITEM_TYPE_TIME_LINE;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    static public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder implements IItemTypeProvider  {
        public SimpleAdapterViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public Shape provideShape(String circleId) {
            return null;
        }

    }

    static public class WeekViewHolder extends SimpleAdapterViewHolder implements IItemTypeProvider {
        FrameLayout moveView;
        FrameLayout exerciseView;
        FrameLayout relaxView;
        FrameLayout sleepView;

        public WeekViewHolder(View itemView) {
            super(itemView);
            this.moveView = itemView.findViewById(R.id.shape_move);
            this.exerciseView = itemView.findViewById(R.id.shape_exercise);
            this.relaxView = itemView.findViewById(R.id.shape_relax);
            this.sleepView = itemView.findViewById(R.id.shape_sleep);
        }

        @Override
        public Shape provideShape(String circleId) {
            View selected = null;
            if(SHAPE_MOVE.equalsIgnoreCase(circleId)) {
                selected = moveView;
            } else if(SHAPE_EXERCISE.equalsIgnoreCase(circleId)) {
                selected = exerciseView;
            } else if(SHAPE_RELAX.equalsIgnoreCase(circleId)) {
                selected = relaxView;
            } else if(SHAPE_SLEEP.equalsIgnoreCase(circleId)) {
                selected = sleepView;
            }

            if(selected == null)
                throw new IllegalArgumentException();

            return new Shape((int)selected.getX(), (int)selected.getY(), selected.getWidth(), selected.getHeight(), Shape.ShapeType.RECTANGLE);
        }
    }

    static public class PlantViewHolder extends SimpleAdapterViewHolder implements IItemTypeProvider {
        PlantView plantView;

        public PlantViewHolder(View itemView) {
            super(itemView);
            this.plantView = itemView.findViewById(R.id.plant_tree);
        }

        @Override
        public Shape provideShape(String circleId) {
            int index = -1;

            if(SHAPE_MOVE.equalsIgnoreCase(circleId)) {
                index = 0;
            } else if(SHAPE_EXERCISE.equalsIgnoreCase(circleId)) {
                index = 1;
            } else if(SHAPE_RELAX.equalsIgnoreCase(circleId)) {
                index = 2;
            } else if(SHAPE_SLEEP.equalsIgnoreCase(circleId)) {
                index = 3;
            }

            if(index < 0)
                throw new IllegalArgumentException();

            PlantView.CircleInfo circleInfo = plantView.getCircleInfo(index);
            return new Shape((int)plantView.getX()+circleInfo.getX()-circleInfo.getRadius(), (int)plantView.getY()+circleInfo.getY()-circleInfo.getRadius(), 2*circleInfo.getRadius(), 2*circleInfo.getRadius(), Shape.ShapeType.CIRCLE);
        }
    }
}
