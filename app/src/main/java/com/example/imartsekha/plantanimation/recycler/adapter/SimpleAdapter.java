package com.example.imartsekha.plantanimation.recycler.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.imartsekha.plantanimation.R;
import com.example.imartsekha.plantanimation.customviews.PlantView;
import com.example.imartsekha.plantanimation.recycler.IItemTypeProvider;
import com.example.imartsekha.plantanimation.recycler.Shape;
import com.example.imartsekha.plantanimation.recycler.helper.ShapePlantAnimatedHelper;

import java.util.List;

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

    ShapePlantAnimatedHelper shapePlantAnimatedHelper;

    public SimpleAdapter(ShapePlantAnimatedHelper shapePlantAnimatedHelper) {
        this.shapePlantAnimatedHelper = shapePlantAnimatedHelper;
    }

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
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_recycler_layout, parent, false);
                return new PlantRecyclerViewHolder(v);
            case ITEM_TYPE_TIME_LINE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_layout, parent, false);
                return new WeekViewHolder(v);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == ITEM_TYPE_PLANT) {
            PlantRecyclerViewHolder plantRecyclerViewHolder = (PlantRecyclerViewHolder)holder;
            plantRecyclerViewHolder.recyclerView.setAdapter(new DayAdapter(plantRecyclerViewHolder));
            shapePlantAnimatedHelper.attachToHorizontalRecyclerView(plantRecyclerViewHolder.recyclerView);
        }
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

    static public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder implements IItemTypeProvider {
        public SimpleAdapterViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public Shape provideShape(String circleId) {
            return null;
        }

        public List<BubbleCircle> provideBubbleCircles() {
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

//    static public class PlantViewHolder extends SimpleAdapterViewHolder implements IItemTypeProvider {
//        PlantView plantView;
//
//        public PlantViewHolder(View itemView) {
//            super(itemView);
//            this.plantView = itemView.findViewById(R.id.plant_tree);
//        }
//
//        @Override
//        public Shape provideShape(String circleId) {
//            int index = -1;
//
//            if(SHAPE_MOVE.equalsIgnoreCase(circleId)) {
//                index = 0;
//            } else if(SHAPE_EXERCISE.equalsIgnoreCase(circleId)) {
//                index = 1;
//            } else if(SHAPE_RELAX.equalsIgnoreCase(circleId)) {
//                index = 2;
//            } else if(SHAPE_SLEEP.equalsIgnoreCase(circleId)) {
//                index = 3;
//            }
//
//            if(index < 0)
//                throw new IllegalArgumentException();
//
//            PlantView.CircleInfo circleInfo = plantView.getCircleInfo(index);
//            return new Shape((int)plantView.getX()+circleInfo.getX()-circleInfo.getRadius(), (int)plantView.getY()+circleInfo.getY()-circleInfo.getRadius(), 2*circleInfo.getRadius(), 2*circleInfo.getRadius(), Shape.ShapeType.CIRCLE);
//        }
//    }


    static public class PlantRecyclerViewHolder extends SimpleAdapterViewHolder implements IItemTypeProvider {
        PlantView plantView;
        RecyclerView recyclerView;

        public PlantRecyclerViewHolder(View itemView) {
            super(itemView);
            this.plantView = itemView.findViewById(R.id.plant_tree);
            this.recyclerView = itemView.findViewById(R.id.recycler_view);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(this.recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, true));
            SnapHelper helper = new LinearSnapHelper();
            helper.attachToRecyclerView(recyclerView);
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

            List<BubbleCircle> circles = provideBubbleCircles();
            BubbleCircle circle = null;
            for(BubbleCircle bubbleCircle : circles) {
                if(bubbleCircle.bubleId.equalsIgnoreCase(circleId)) {
                    circle = bubbleCircle;
                    break;
                }
            }

            PlantView.CircleInfo circleInfo = plantView.getCircleInfo(index);


            int newRadius =  ((circleInfo.getRadius())/100)* circle.score; //calculateRadiusForScoreBubble(finishView.getWidth()/2, circle.score);
//            finishX = finishView.getX()+finishView.getWidth()/2 - newRadius;
//            finishY = finishView.getY()+finishView.getHeight()/2 - newRadius;

//            private int calculateRadiusForScoreBubble(int finalRadius, int score) {
//                return ((finalRadius)/100)*score;
//            }

            return new Shape((int)plantView.getX()+circleInfo.getX()-newRadius, (int)plantView.getY()+circleInfo.getY()-newRadius, 2*newRadius, 2*newRadius, Shape.ShapeType.CIRCLE);
//            return new Shape((int)plantView.getX()+circleInfo.getX()-circleInfo.getRadius(), (int)plantView.getY()+circleInfo.getY()-circleInfo.getRadius(), 2*circleInfo.getRadius(), 2*circleInfo.getRadius(), Shape.ShapeType.CIRCLE);
        }

        public List<BubbleCircle> provideBubbleCircles() {
            int currentPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            final DayAdapter.DayViewHolder viewGroup = (DayAdapter.DayViewHolder)recyclerView.findViewHolderForLayoutPosition(currentPosition);

            return viewGroup.getBubbleCircles();
        }
    }
}
