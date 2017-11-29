package com.example.imartsekha.plantanimation.recycler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.imartsekha.plantanimation.R;
import com.example.imartsekha.plantanimation.customviews.PlantView;
import com.example.imartsekha.plantanimation.recycler.IItemTypeProvider;
import com.example.imartsekha.plantanimation.recycler.Shape;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by imartsekha on 11/29/17.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
//    ViewGroup viewOverlay;
    SimpleAdapter.PlantRecyclerViewHolder simpleAdapterViewHolder;


    public DayAdapter(SimpleAdapter.PlantRecyclerViewHolder simpleAdapterViewHolder/*ViewGroup viewOverlay*/) {
//        this.viewOverlay = viewOverlay;
        this.simpleAdapterViewHolder = simpleAdapterViewHolder;
    }


    private List<BubbleCircle> generateData(Context context, int percent) {
        List<BubbleCircle> bubbleCircles = new ArrayList<>();

        float multiplier = percent/100.0f;
        List<BubbleCircle.BubbleDirection> directions = Arrays.asList(
                new BubbleCircle.BubbleDirection(SimpleAdapter.ITEM_TYPE_PLANT, SimpleAdapter.ITEM_TYPE_WEEK),
                new BubbleCircle.BubbleDirection(SimpleAdapter.ITEM_TYPE_PLANT, SimpleAdapter.ITEM_TYPE_TIME_LINE));

        bubbleCircles.add(new BubbleCircle.Builder()
                .color(context.getResources().getColor(R.color.colorMove))
                .score((int)(100*multiplier))
                .text(context.getString(R.string.move))
                .bubleId(SimpleAdapter.SHAPE_MOVE)
                .bubbleDirections(directions)
                .build());

        bubbleCircles.add(new BubbleCircle.Builder()
                .color(context.getResources().getColor(R.color.colorExercise))
                .score((int)(60*multiplier))
                .text(context.getString(R.string.exercise))
                .bubleId(SimpleAdapter.SHAPE_EXERCISE)
                .bubbleDirections(directions)
                .build());

        bubbleCircles.add(new BubbleCircle.Builder()
                .color(context.getResources().getColor(R.color.colorRelax))
                .score((int)(40*multiplier))
                .text(context.getString(R.string.relax))
                .bubleId(SimpleAdapter.SHAPE_RELAX)
                .bubbleDirections(directions)
                .build());

        bubbleCircles.add(new BubbleCircle.Builder()
                .color(context.getResources().getColor(R.color.colorSleep))
                .score((int)(10*multiplier))
                .text(context.getString(R.string.sleep))
                .bubleId(SimpleAdapter.SHAPE_SLEEP)
                .bubbleDirections(directions)
                .build());

        return bubbleCircles;
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, (-1)*position);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        holder.dayTitle.setText(dateFormat.format(calendar.getTime()));
        holder.setData(generateData(holder.itemView.getContext(),  100-position*10));
//        holder.verticalRecyclerView.setAdapter(new SimpleAdapter());
//        ((LinearLayoutManager)holder.verticalRecyclerView.getLayoutManager()).scrollToPositionWithOffset(SimpleAdapter.ITEM_TYPE_PLANT, 0);
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_recycler_item, parent, false);
        return new DayViewHolder(v, simpleAdapterViewHolder/*, this.viewOverlay*/);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder implements IItemTypeProvider{
        TextView dayTitle;
        private List<BubbleCircle> bubbleCircles = new ArrayList<>();

        SimpleAdapter.PlantRecyclerViewHolder simpleAdapterViewHolder;

        public void setData(List<BubbleCircle> bubbleCircles) {
            this.bubbleCircles = bubbleCircles;
        }

        public List<BubbleCircle> getBubbleCircles() {
            return bubbleCircles;
        }

        public DayViewHolder(View itemView, SimpleAdapter.PlantRecyclerViewHolder simpleAdapterViewHolder/*, ViewGroup viewOverlay*/) {
            super(itemView);
            dayTitle = itemView.findViewById(R.id.day_title);
            this.simpleAdapterViewHolder = simpleAdapterViewHolder;
        }

        public SimpleAdapter.SimpleAdapterViewHolder provideSimpleAdapterVH() {
            return simpleAdapterViewHolder;
        }

        @Override
        public Shape provideShape(String shapeId) {
            int index = -1;

            if(SimpleAdapter.SHAPE_MOVE.equalsIgnoreCase(shapeId)) {
                index = 0;
            } else if(SimpleAdapter.SHAPE_EXERCISE.equalsIgnoreCase(shapeId)) {
                index = 1;
            } else if(SimpleAdapter.SHAPE_RELAX.equalsIgnoreCase(shapeId)) {
                index = 2;
            } else if(SimpleAdapter.SHAPE_SLEEP.equalsIgnoreCase(shapeId)) {
                index = 3;
            }

            if(index < 0)
                throw new IllegalArgumentException();

            List<BubbleCircle> circles = getBubbleCircles();
            BubbleCircle circle = null;
            for(BubbleCircle bubbleCircle : circles) {
                if(bubbleCircle.bubleId.equalsIgnoreCase(shapeId)) {
                    circle = bubbleCircle;
                    break;
                }
            }

            PlantView.CircleInfo circleInfo = simpleAdapterViewHolder.plantView.getCircleInfo(index);


            int newRadius = ((circleInfo.getRadius())/100)* circle.score; //calculateRadiusForScoreBubble(finishView.getWidth()/2, circle.score);
//            finishX = finishView.getX()+finishView.getWidth()/2 - newRadius;
//            finishY = finishView.getY()+finishView.getHeight()/2 - newRadius;

//            private int calculateRadiusForScoreBubble(int finalRadius, int score) {
//                return ((finalRadius)/100)*score;
//            }

            return new Shape((int)simpleAdapterViewHolder.plantView.getX()+circleInfo.getX()-newRadius, (int)simpleAdapterViewHolder.plantView.getY()+circleInfo.getY()-newRadius, 2*newRadius, 2*newRadius, Shape.ShapeType.CIRCLE);
//            return new Shape((int)plantView.getX()+circleInfo.getX()-circleInfo.getRadius(), (int)plantView.getY()+circleInfo.getY()-circleInfo.getRadius(), 2*circleInfo.getRadius(), 2*circleInfo.getRadius(), Shape.ShapeType.CIRCLE);
        }
    }
}
