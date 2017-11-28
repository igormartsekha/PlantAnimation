package com.example.imartsekha.plantanimation.recycler;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.view.ViewGroup;

import com.example.imartsekha.plantanimation.R;
import com.example.imartsekha.plantanimation.recycler.helper.BubbleCircle;
import com.example.imartsekha.plantanimation.recycler.helper.ShapePlantAnimatedHelper;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imartsekha on 11/24/17.
 */

public class RecyclerActivity extends Activity {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.overlay_view) ViewGroup overlayView;
    @BindView(R.id.move_view) ViewGroup moveView;

    ShapePlantAnimatedHelper shapePlantAnimatedHelper;


    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_layout);
        ButterKnife.bind(this);

        SimpleAdapter simpleAdapter = new SimpleAdapter();



        recyclerView.setAdapter(simpleAdapter);

        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);



        this.shapePlantAnimatedHelper = new ShapePlantAnimatedHelper(overlayView);
        shapePlantAnimatedHelper.attachToRecyclerView(recyclerView);

        List<BubbleCircle.BubbleDirection> directions = Arrays.asList(
                new BubbleCircle.BubbleDirection(SimpleAdapter.ITEM_TYPE_PLANT, SimpleAdapter.ITEM_TYPE_WEEK),
                new BubbleCircle.BubbleDirection(SimpleAdapter.ITEM_TYPE_PLANT, SimpleAdapter.ITEM_TYPE_TIME_LINE));

        this.shapePlantAnimatedHelper.addAnimateBubble(new BubbleCircle.Builder()
                .color(getResources().getColor(R.color.colorMove))
                .score(100)
                .text(getString(R.string.move))
                .bubleId(SimpleAdapter.SHAPE_MOVE)
                .bubbleDirections(directions)
                .build());

        this.shapePlantAnimatedHelper.addAnimateBubble(new BubbleCircle.Builder()
                .color(getResources().getColor(R.color.colorExercise))
                .score(60)
                .text(getString(R.string.exercise))
                .bubleId(SimpleAdapter.SHAPE_EXERCISE)
                .bubbleDirections(directions)
                .build());

        this.shapePlantAnimatedHelper.addAnimateBubble(new BubbleCircle.Builder()
                .color(getResources().getColor(R.color.colorRelax))
                .score(40)
                .text(getString(R.string.relax))
                .bubleId(SimpleAdapter.SHAPE_RELAX)
                .bubbleDirections(directions)
                .build());

        this.shapePlantAnimatedHelper.addAnimateBubble(new BubbleCircle.Builder()
                .color(getResources().getColor(R.color.colorSleep))
                .score(10)
                .text(getString(R.string.sleep))
                .bubleId(SimpleAdapter.SHAPE_SLEEP)
                .bubbleDirections(directions)
                .build());

        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(simpleAdapter.getPositionViewType(simpleAdapter.ITEM_TYPE_PLANT), 0);

    }

    View start_layout;
    View end_layout;

    @Override
    protected void onResume() {
        super.onResume();

    }

//    void playAnimation() throws InterruptedException {
//        AnimatorSet animatorSet = new AnimatorSet();
//        final ObjectAnimator animY = ObjectAnimator.ofFloat(moveView, View.TRANSLATION_Y, moveView.getY(), 10);
//        final ObjectAnimator animX = ObjectAnimator.ofFloat(moveView, View.TRANSLATION_X, moveView.getX(), 500);
////
//        animatorSet.setInterpolator(new LinearInterpolator());
//        animatorSet.playTogether(animX, animY);
//        animatorSet.setDuration(1000);
//
////        for (int i=0; i < 100; i++) {
////            animatorSet.setCurrentPlayTime(0*10);
////            Thread.sleep(1000);
////        }
//    }
}
