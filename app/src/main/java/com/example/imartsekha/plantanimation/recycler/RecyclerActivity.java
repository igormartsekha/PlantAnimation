package com.example.imartsekha.plantanimation.recycler;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.ViewGroup;

import com.example.imartsekha.plantanimation.R;
import com.example.imartsekha.plantanimation.recycler.adapter.SimpleAdapter;
import com.example.imartsekha.plantanimation.recycler.helper.ShapePlantAnimatedHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imartsekha on 11/24/17.
 */

public class RecyclerActivity extends Activity {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.overlay_view) ViewGroup overlayView;

    ShapePlantAnimatedHelper shapePlantAnimatedHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_layout);
        ButterKnife.bind(this);

//        DayAdapter dayAdapter = new DayAdapter(overlayView);
//        recyclerView.setAdapter(dayAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        this.shapePlantAnimatedHelper = new ShapePlantAnimatedHelper(overlayView);

        SimpleAdapter simpleAdapter = new SimpleAdapter(shapePlantAnimatedHelper);
        recyclerView.setAdapter(simpleAdapter);

        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        shapePlantAnimatedHelper.attachToVerticalRecyclerView(recyclerView);

//        List<BubbleCircle.BubbleDirection> directions = Arrays.asList(
//                new BubbleCircle.BubbleDirection(SimpleAdapter.ITEM_TYPE_PLANT, SimpleAdapter.ITEM_TYPE_WEEK),
//                new BubbleCircle.BubbleDirection(SimpleAdapter.ITEM_TYPE_PLANT, SimpleAdapter.ITEM_TYPE_TIME_LINE));
//
//        this.shapePlantAnimatedHelper.addAnimateBubble(new BubbleCircle.Builder()
//                .color(getResources().getColor(R.color.colorMove))
//                .score(100)
//                .text(getString(R.string.move))
//                .bubleId(SimpleAdapter.SHAPE_MOVE)
//                .bubbleDirections(directions)
//                .build());
//
//        this.shapePlantAnimatedHelper.addAnimateBubble(new BubbleCircle.Builder()
//                .color(getResources().getColor(R.color.colorExercise))
//                .score(60)
//                .text(getString(R.string.exercise))
//                .bubleId(SimpleAdapter.SHAPE_EXERCISE)
//                .bubbleDirections(directions)
//                .build());
//
//        this.shapePlantAnimatedHelper.addAnimateBubble(new BubbleCircle.Builder()
//                .color(getResources().getColor(R.color.colorRelax))
//                .score(40)
//                .text(getString(R.string.relax))
//                .bubleId(SimpleAdapter.SHAPE_RELAX)
//                .bubbleDirections(directions)
//                .build());
//
//        this.shapePlantAnimatedHelper.addAnimateBubble(new BubbleCircle.Builder()
//                .color(getResources().getColor(R.color.colorSleep))
//                .score(10)
//                .text(getString(R.string.sleep))
//                .bubleId(SimpleAdapter.SHAPE_SLEEP)
//                .bubbleDirections(directions)
//                .build());

        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(simpleAdapter.getPositionViewType(SimpleAdapter.ITEM_TYPE_PLANT), 0);

    }
}
