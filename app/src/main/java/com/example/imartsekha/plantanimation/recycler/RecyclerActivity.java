package com.example.imartsekha.plantanimation.recycler;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;

import com.example.imartsekha.plantanimation.R;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imartsekha on 11/24/17.
 */

public class RecyclerActivity extends Activity {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.overlay_view) ViewGroup overlayView;
    @BindView(R.id.move_view) ViewGroup moveView;


    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_layout);
        ButterKnife.bind(this);

        SimpleAdapter simpleAdapter = new SimpleAdapter();


        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                            recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

//                        linearLayoutManager.scrollToPositionWithOffset(1, 0);
                        applyBubbles(1);
                        applyBubbles(0);
//
                        applyBubulesAtStart();
                    }
                });

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(simpleAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            float currentPosition = 0;

            AnimatorSet animatorSet = new AnimatorSet();

            float moveX;
            float moveY;

            float moveHeight;
            float moveWidth;


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Log.d("Scroll", "state= "+newState);
                if(newState == 1) {
//                    position = 0;
                } else if (newState == 2) {

                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(end_layout == null) {
                    applyBubbles(0);
                    if(end_layout == null) {
                        return;
                    } else {
//                        final ObjectAnimator animY = ObjectAnimator.ofFloat(moveView, View.TRANSLATION_Y, moveView.getY(), end_layout.getY());
//                        final ObjectAnimator animX = ObjectAnimator.ofFloat(moveView, View.TRANSLATION_X, moveView.getX(), end_layout.getX());
////
//                        animatorSet.setInterpolator(new LinearInterpolator());
//                        animatorSet.playTogether(animX, animY);
//                        animatorSet.setDuration(1000);

                        moveX = moveView.getX();
                        moveY = moveView.getY();

                        moveHeight = moveView.getHeight();
                        moveWidth = moveView.getWidth();
                    }
                }

                int recyclerHeight = recyclerView.getHeight();
//
                currentPosition = currentPosition+dy;
                int percentPosition = 0;

                if(currentPosition == 0) {
                    percentPosition = 0;
                } else {
                    percentPosition = (int) Math.abs((currentPosition/(float) recyclerHeight) * 100.0f);//(recyclerHeight/100);
                }
//                if(percentPosition > 100) {
//                    percentPosition = 100;
//                }


//                animatorSet.setCurrentPlayTime(percentPosition*10);

                float endY = end_layout.getY();
                float endX = end_layout.getX();


                float endHeight = end_layout.getHeight();
                float endWidth = end_layout.getWidth();



//                float newX=moveView.getX();
//                float newY=moveView.getY();

                if(percentPosition != 0) {
                    float newX = ((endX-moveX)/100.0f)*(float) percentPosition;
                    float newY = ((endY-moveY)/100.0f)*(float) percentPosition;

                    float newHeight = ((endHeight-moveHeight)/100.0f)*(float) percentPosition;
                    float newWidth = ((endWidth-moveWidth)/100.0f)*(float) percentPosition;

                    if(percentPosition == 100) {
                        newX = endX-moveX;
                        newY = endY-moveY;

                        newHeight = endHeight;
                        newWidth = endWidth;
                    }
                    moveView.setX(moveX+newX);
                    moveView.setY(moveY+newY);
                } else {
                    moveView.setX(moveX);
                    moveView.setY(moveY);
//                    moveView.setX(moveX);
//                    moveView.setY(moveY);
                }








//                int newMoveViewY = (int)moveView.getY()+dy;
//                if(newMoveViewY < 0) {newMoveViewY = 0;}
//                if(newMoveViewY > recyclerView.getHeight() - moveView.getHeight()) {newMoveViewY = recyclerView.getHeight() - moveView.getHeight();}
//                moveView.setY(newMoveViewY);


                int position = 0;
                Log.d("Scroll", "position= "+ percentPosition
                        +"; dx= "+dx
                        +"; dy= "+dy
                        +"; moveViewX="+moveView.getX()
                        +"; moveViewY="+moveView.getY()
                        +"; recyclerHeight="+recyclerView.getHeight()
                        +"; currentPosition="+currentPosition);

            }
        });

        linearLayoutManager.scrollToPositionWithOffset(1, 0);

    }

    View start_layout;
    View end_layout;

    @Override
    protected void onResume() {
        super.onResume();

    }

    void playAnimation() throws InterruptedException {
        AnimatorSet animatorSet = new AnimatorSet();
        final ObjectAnimator animY = ObjectAnimator.ofFloat(moveView, View.TRANSLATION_Y, moveView.getY(), 10);
        final ObjectAnimator animX = ObjectAnimator.ofFloat(moveView, View.TRANSLATION_X, moveView.getX(), 500);
//
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(animX, animY);
        animatorSet.setDuration(1000);

        for (int i=0; i < 100; i++) {
            animatorSet.setCurrentPlayTime(0*10);
            Thread.sleep(1000);
        }
    }

    void applyBubbles(int index) {
        RecyclerView.ViewHolder viewGroup = recyclerView.findViewHolderForLayoutPosition(index);

        RecyclerView.ViewHolder plantView = recyclerView.findViewHolderForAdapterPosition(index);
        RecyclerView.ViewHolder plantViewFirst = recyclerView.findViewHolderForLayoutPosition(index);

        if(viewGroup == null)
            return;


        View viewPlant = viewGroup.itemView;

        if(index == 1) {
            start_layout = viewPlant.findViewById(R.id.plant_branch_1);
        } else {
            end_layout = viewPlant.findViewById(R.id.plant_branch_end_1);
        }
//        float x = layout_branch_first.getX();
//        float y = layout_branch_first.getY();
//
//        float width = layout_branch_first.getWidth();
//        float height = layout_branch_first.getHeight();
//
//        float centerX = x + width/2;
//        float centerY = y + height/2;
//
//        moveView.setX(centerX- moveView.getWidth()/2);
//        moveView.setY(centerY- moveView.getHeight()/2);
//        RecyclerView.ViewHolder plantView = recyclerView.findViewHolderForAdapterPosition(index);
//        RecyclerView.ViewHolder plantViewFirst = recyclerView.findViewHolderForLayoutPosition(index);
//        View plantViewSecond = linearLayoutManager.getChildAt(index);
//        View plantViewFirst1 = recyclerView.getChildAt(index);
    }

    void applyBubulesAtStart() {
        float x = start_layout.getX();
        float y = start_layout.getY();

        float width = start_layout.getWidth();
        float height = start_layout.getHeight();

        float centerX = x + width/2;
        float centerY = y + height/2;

        moveView.setX(centerX- moveView.getWidth()/2);
        moveView.setY(centerY- moveView.getHeight()/2);

        try {
//            playAnimation();
        } catch (Exception e) {

        }
    }
}
